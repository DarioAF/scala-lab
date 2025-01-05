package practice.design

import scala.util.boundary
import boundary.break
import scala.annotation.tailrec
import scala.util.Random

val emptySpace: Char = '.'
val connectNumber: Int = 4

case class Player(name: String) {
  def codeName: Char = name.charAt(0)
}

case class Box(value: Char, pos: (Int, Int))

class Board(private val cols: Int, private val rows: Int) {
  private val board: Array[Array[Char]] = Array.fill(cols, rows)(emptySpace)
  private def isFull(col: Int): Boolean = getVal(col, rows - 1) != emptySpace
  private def getVal(col: Int, row: Int): Char = board(col)(row)

  def update(col: Int, v: Char): Either[String, (Int, Int)] = {
    if (col >= cols) {
      return Left(s"Invalid position: $col")
    }
    if (isFull(col)) {
      return Left(s"Column $col is full")
    }
    boundary {
      for (row <- board(col).indices) if (getVal(col, row) == emptySpace) {
        board(col)(row) = v
        break(Right(col, row))
      }
      Left(s"Unexpected error updating $col with $v")
    }
  }

  def getStar(col: Int, row: Int, long: Int = connectNumber): Seq[Seq[Box]] = {
    // ← → // (1, 1) => (0, 1) (1, 1) (2, 1)
    val horizontal = (col - (long - 1) until col + long).filter(c => c >= 0 && c < cols).map(c =>
      Box(getVal(c, row), (c, row))
    )

    // ↑ ↓ // (1, 1) => (1, 0) (1, 1) (1, 2)
    val vertical = (row - (long - 1) until row + long).filter(r => r >= 0 && r < rows).map(r =>
      Box(getVal(col, r), (col, r))
    )

    // ↙ ↗ // (1, 1) => (0, 0) (1, 1) (2, 2)
    val diagonal1 =
      ((col - (long - 1) until col) ++ (col until col + long))
        .zip((row - (long - 1) until row) ++ (row until row + long))
        .filter((c, r) => c >= 0 && c < cols && r >= 0 && r < rows)
        .map((c, r) => Box(getVal(c, r), (c, r)))

    // ↖ ↘ // (1, 1) => (0, 2) (1, 1) (2, 0)
    val diagonal2 =
      ((col - (long - 1) until col) ++ (col until col + long))
        .zip((row + (long - 1) until row by -1) ++ (row to row - (long - 1) by -1))
        .filter((c, r) => c >= 0 && c < cols && r >= 0 && r < rows)
        .map((c, r) => Box(getVal(c, r), (c, r)))

    Seq(horizontal, vertical, diagonal1, diagonal2)
  }

  def toString(highlighted: Seq[(Int, Int)]): String = {
    /*
      (0, 0) (0, 1) (0, 2)  <- 0
      (1, 0) (1, 1) (1, 2)  <- 1
      (2, 0) (2, 1) (2, 2)  <- 2
    */
    def original(sb: StringBuilder)(p: (c: Int, r: Int) => String): Unit =
      (0 until cols).foreach { c =>
        (0 until rows).foreach { r =>
          sb ++= p(c, r)
        }
        sb ++= s" <- $c\n"
      }

    /* ↓      ↓      ↓
      (0, 2) (1, 2) (2, 2)
      (0, 1) (1, 1) (2, 1)
      (0, 0) (1, 0) (2, 0)
    */
    def rotated(sb: StringBuilder)(p: (c: Int, r: Int) => String): Unit =
      (rows - 1 to 0 by -1).foreach { r =>
        (0 until cols).foreach { c =>
          sb ++= p(c, r)
        }
        sb ++= "\n"
      }

    def highlight(c: Int, r: Int): String = {
      val v = board(c)(r)
      val isHighlighted: Boolean = highlighted.contains((c, r))
      if (isHighlighted) Console.GREEN + s"$v " + Console.RESET else s"$v "
    }

    val sb = new StringBuilder()
    sb ++= "---\n"
    rotated(sb)(highlight)
//    original(sb)((c: Int, r: Int) =>
////      s"($c, $r) "
//      s"${board(c)(r)} "
//    )
    sb ++= "---\n"
    sb.toString()
  }

  override def toString: String = toString(Seq.empty)
}

class Game(cols: Int, rows: Int) {
  private val board = new Board(cols, rows)

  def play(player: Player, pos: Int): Boolean = {
    board.update(pos, player.codeName) match {
      case Left(e) =>
        println(s"${player.name} -> $e")
        printBoard()
        true
      case Right(c, r) =>
        val connected = connectedWinPos(board.getStar(c, r), player.codeName)
        println(s"${player.name} -> ($c, $r) ${if (connected.nonEmpty) "Winner!" else ""}")
        if (connected.nonEmpty) {
          printBoard(connected)
          true
        } else false
    }
  }

  private def connectedWinPos(star: Seq[Seq[Box]], code: Char): Seq[(Int, Int)] = {
    @tailrec def tailrec(rem: Seq[Box], acc: List[Box]): Seq[Box] = {
      if (acc.size == connectNumber) acc
      else if (rem.isEmpty) List.empty
      else if (rem.head.value == code) tailrec(rem.tail, rem.head :: acc)
      else tailrec(rem.tail, List.empty)
    }
    val winPos = star.map(l => tailrec(l, List.empty)).filter(_.nonEmpty)
    if (winPos.isEmpty) Seq.empty
    else winPos.head.map(b => (b.pos._1, b.pos._2))
  }

  private def printBoard(h: Seq[(Int, Int)] = Seq.empty): Unit = println(board.toString(h))
}

object ConnectFour extends App {
  val (cols, rows) = (7, 6)
  val game = new Game(cols, rows)
  val playerA = Player("A")
  val playerB = Player("B")

  @tailrec private def randomPlay(turn: Player, ended: Boolean): Unit = {
    if (ended) return
    val rndCol = Random.nextInt(cols)
    val next = if (turn == playerA) playerB else playerA
    randomPlay(next, game.play(turn, rndCol))
  }

  randomPlay(playerA, false)
}
