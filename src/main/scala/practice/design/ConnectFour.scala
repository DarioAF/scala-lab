package practice.design

import scala.util.boundary, boundary.break

val emptySpace: Char = '.'
val connectNumber: Int = 4

case class Player(name: String) {
  def codeName: Char = name.charAt(0)
}

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

  def getStar(row: Int, col: Int, long: Int = connectNumber): Seq[Seq[Char]] = {
    // ← → // (1, 1) => (0, 1) (1, 1) (2, 1)
    val horizontal = (row - (long - 1) until row + long).filter(r => r >= 0 && r < cols).map(r => getVal(r, col))

    // ↑ ↓ // (1, 1) => (1, 0) (1, 1) (1, 2)
    val vertical = (col - (long - 1) until col + long).filter(c => c >= 0 && c < rows).map(c => getVal(row, c))

    // ↙ ↗ // (1, 1) => (0, 0) (1, 1) (2, 2)
    val diagonal1 =
      ((row - (long - 1) until row) ++ (row until row + long))
        .zip((col - (long - 1) until col) ++ (col until col + long))
        .filter((r, c) => r >= 0 && r < cols && c >= 0 && c < rows)
        .map((r, c) => getVal(r, c))

    // ↖ ↘ // (1, 1) => (0, 2) (1, 1) (2, 0)
    val diagonal2 =
      ((row - (long - 1) until row) ++ (row until row + long))
        .zip((col + (long - 1) until col by -1) ++ (col to col - (long - 1) by -1))
        .filter((r, c) => r >= 0 && r < cols && c >= 0 && c < rows)
        .map((r, c) => getVal(r, c))

    Seq(horizontal, vertical, diagonal1, diagonal2)
  }

  override def toString: String = {
    /*
      (0, 0) (0, 1) (0, 2)  <- 0
      (1, 0) (1, 1) (1, 2)  <- 1
      (2, 0) (2, 1) (2, 2)  <- 2
    */
    lazy val original = (sb: StringBuilder) =>
      (0 until cols).foreach { r =>
        (0 until rows).foreach { c =>
          //sb ++= s"${board(r)(c)} "
          sb ++= s"($r, $c) "
        }
        sb ++= s" <- $r\n"
      }

    /* ↓      ↓      ↓
      (0, 2) (1, 2) (2, 2)
      (0, 1) (1, 1) (2, 1)
      (0, 0) (1, 0) (2, 0)
    */
    lazy val rotated = (sb: StringBuilder) =>
      (rows - 1 to 0 by -1).foreach { r =>
        (0 until cols).foreach { c =>
          sb ++= s"${board(c)(r)} "
          //sb ++= s"($c, $r) "
        }
        sb ++= "\n"
      }

    val sb = new StringBuilder()
    sb ++= "---\n"
    rotated(sb)
//  original(sb)
    sb ++= "---\n"
    sb.toString()
  }
}

class Game {
  private val (cols, rows) = (7, 6)
  private val board = new Board(cols, rows)

  def play(player: Player, pos: Int): Unit = {
    board.update(pos, player.codeName) match {
      case Left(e) => println(s"$player -> $e")
      case Right(c, r) =>
        val winner: Boolean = isWinner(board.getStar(c, r), player.codeName)
        println(s"${player.name} -> ($c, $r) ${if (winner) "Winner!" else ""}")
    }
  }

  def isWinner(star: Seq[Seq[Char]], code: Char): Boolean = {
    def tailrec(rem: Seq[Char], acc: Int): Boolean = {
      if (acc == connectNumber) true
      else if (rem.isEmpty) false
      else if (rem.head == code) tailrec(rem.tail, acc + 1)
      else tailrec(rem.tail, 0)
    }

    star.exists(l => tailrec(l, 0))
  }

  def printBoard(): Unit = println(board.toString)
}

object ConnectFour extends App {
  val playerA = Player("A")
  val playerB = Player("B")

  val game = new Game

  game.play(playerA, 0)
  game.play(playerB, 1)
  game.play(playerA, 1)
  game.play(playerB, 2)
  game.play(playerB, 2)
  game.play(playerA, 2)
  game.play(playerB, 3)
  game.play(playerB, 3)
  game.play(playerB, 3)
  game.play(playerA, 3)

  game.printBoard()
}
