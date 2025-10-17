package practice.numbers

import scala.annotation.tailrec

object IntegerToRoman extends App {

  /*
  12. Integer to Roman
  https://leetcode.com/problems/integer-to-roman/submissions/1804505139

  Symbol, Value
  I -> 1
  V -> 5
  X -> 10
  L -> 50
  C -> 100
  D -> 500
  M -> 1000
  */

  private def calcOnes(c: Char): String = c match {
    case '9' => "IX"
    case '5' => "V"
    case '4' => "IV"
    case cs =>
      val n = cs.toString.toInt
      if (n > 4) "V" + Seq.fill(n - 5)('I').mkString
      else Seq.fill(n)('I').mkString
  }

  private def calcTens(c: Char): String = c match {
    case '9' => "XC"
    case '5' => "L"
    case '4' => "XL"
    case cs =>
      val n = cs.toString.toInt
      if (n > 4) "L" + Seq.fill(n - 5)('X').mkString
      else Seq.fill(n)('X').mkString
  }

  private def calcHundreds(c: Char): String = c match {
    case '9' => "CM"
    case '5' => "D"
    case '4' => "CD"
    case cs =>
      val n = cs.toString.toInt
      if (n > 4) "D" + Seq.fill(n - 5)('C').mkString
      else Seq.fill(n)('C').mkString
  }

  private def calcThousands(c: Char): String = Seq.fill(c.toString.toInt)('M').mkString

  def intToRoman(num: Int): String = {
    assert(1 <= num && num <= 3999)

    val decimalCalc: Map[Int, Char => String] = Map(
      0 -> calcOnes,
      1 -> calcTens,
      2 -> calcHundreds,
      3 -> calcThousands,
    )

    @tailrec
    def auxRec(rem: String, numeration: Int = 0, res: String = ""): String = {
      if (rem.isEmpty || rem == "0") res
      else auxRec(
        rem.tail,
        numeration - 1,
        res + decimalCalc(numeration)(rem.head),
      )
    }
    auxRec(num.toString, num.toString.length - 1)
  }

  Seq(
    (3749, "MMMDCCXLIX"),
    (58, "LVIII"),
    (1994, "MCMXCIV"),
  ).foreach { case (n, expected) =>
    val res = intToRoman(n)
    if (res == expected) {
      println(Console.GREEN + s"$n -> $expected ✅" + Console.RESET)
    } else {
      println(Console.RED + s"$n -> expected $expected but got $res ❌" + Console.RESET)
    }
  }

}
