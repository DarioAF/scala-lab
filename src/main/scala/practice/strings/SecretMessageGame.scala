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

  @tailrec
  def calcGuesses(
    rem: String, corrects: Int = 0, wrongs: Int = 0, fails: Int = 0
  )(implicit positionMap: Map[Char, Seq[Int]]): (Int, Int, Int) = {
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

  def validate(guess: String, secret: String): Option[String] = {
    if (guess.isEmpty || secret.isEmpty) {
      Some("Both strings must be defined")
    } else if (guess.length != secret.length) {
      Some("Guess and Secret must have the same length")
    } else None
  }

  def play(guess: String, secret: String): Either[String, (Int, Int, Int)] = {
    validate(guess, secret).fold {
      assert(guess.length == secret.length)
      Right(calcGuesses(guess)(buildPositionMap(secret)))
    }(Left(_))
  }

  Seq(
    ("CBA", "ABC", Right((1, 2, 0))),
    ("ABC", "ABC", Right((3, 0, 0))),
    ("ABCDEFG", "ABCDEFG", Right((7, 0, 0))),
    ("CAB", "ABC", Right((0, 3, 0))),
    ("ABC", "123", Right((0, 0, 3))),
    ("CBA", "AB1", Right((1, 1, 1))),
    ("ACCDE1G", "ABCDEFG", Right((5, 1, 1))),
    ("", "", Left("Both strings must be defined")),
    ("A", "", Left("Both strings must be defined")),
    ("", "A", Left("Both strings must be defined")),
    ("11", "1111", Left("Guess and Secret must have the same length")),
  ).foreach {
    case (guess, secret, Right((expectedCorrects, expectedWrongs, expectedFails))) =>
      play(guess, secret) match {
        case Right((actualCorrects, actualWrongs, actualFails)) =>
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
        case Left(validationError) => println(Console.RED + s"Expected result, found $validationError" + Console.RESET)
      }

    case (guess, secret, Left(expectedValidationError)) =>
      play(guess, secret) match {
        case Left(actualValidationError) if (expectedValidationError == actualValidationError) =>
          println(Console.GREEN + s"For $secret, the guess: $guess result in: $actualValidationError ✅" + Console.RESET)

        case Left(actualValidationError) =>
          println(Console.RED + s"Expected $expectedValidationError, found $actualValidationError" + Console.RESET)

        case Right((actualCorrects, actualWrongs, actualFails)) =>
          println(Console.RED + s"Expected validation error, found ${(actualCorrects, actualWrongs, actualFails)}" + Console.RESET)
      }
  }
}
