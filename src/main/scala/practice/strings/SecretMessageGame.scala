package practice.strings

import scala.annotation.tailrec

/*
  Given two strings (Secret and Guess String) of the same length that may contain repeated characters,
  determine the number of characters in the correct position and the number of characters in the wrong position.
  Example:
    Secret String: A B C
    Guess String: C B A
    1 in the correct position (B)
    2 in the wrong position (A and C)
* */
object SecretMessageGame extends App {

  def buildPositionMap(secret: String): Map[Char, Seq[Int]] = {
    secret.zipWithIndex.foldLeft(Map.empty[Char, Seq[Int]]){
      case (acc, (c, p)) if acc.contains(c) => acc.updated(c, acc(c) :+ p)
      case (acc, (c, p)) => acc.updated(c, Seq(p))
    }
  }

  def play(guess: String, secret: String): (Int, Int, Int) = {
    assert(guess.length == secret.length)
    val positionMap = buildPositionMap(secret)

    @tailrec
    def calcGuesses(rem: String, corrects: Int = 0, wrongs: Int = 0, fails: Int = 0): (Int, Int, Int) = {
      if (rem.isEmpty) (corrects, wrongs, fails)
      else {
        val currentPosition = corrects + wrongs + fails
        positionMap.get(rem.head) match {
          case Some(positionsForChar) if positionsForChar.contains(currentPosition) =>
            calcGuesses(rem.tail, corrects + 1, wrongs, fails)
          case Some(positionsForChar) => calcGuesses(rem.tail, corrects, wrongs + 1, fails)
          case _ => calcGuesses(rem.tail, corrects, wrongs, fails + 1)
        }
      }
    }

    calcGuesses(guess)
  }

  Seq(
    ("CBA", "ABC", (1, 2, 0)),
    ("ABC", "ABC", (3, 0, 0)),
    ("CAB", "ABC", (0, 3, 0)),
    ("ABC", "123", (0, 0, 3)),
    ("CBA", "AB1", (1, 1, 1)),
    ("", "", (0, 0, 0)),
  ).foreach { case (guess, secret, (expectedCorrects, expectedWrongs, expectedFails)) =>
    val (actualCorrects, actualWrongs, actualFails) = play(guess, secret)

    if (expectedCorrects == actualCorrects
      && expectedWrongs == actualWrongs
      && expectedFails == actualFails) {
      println(Console.GREEN
        + s"For $secret, the guess: $guess has: $actualCorrects Correct, $actualWrongs Wrong, $actualFails fails ✅"
        + Console.RESET)
    } else {
      println(Console.RED
        + s"For $secret, the guess: $guess has: $actualCorrects Correct, $actualWrongs Wrong, $actualFails fails ❌"
        + Console.RESET)
    }
  }
}
