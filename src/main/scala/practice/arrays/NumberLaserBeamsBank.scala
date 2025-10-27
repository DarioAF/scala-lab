package practice.arrays

import scala.annotation.tailrec

/*
2125. Number of Laser Beams in a Bank
https://leetcode.com/problems/number-of-laser-beams-in-a-bank
*/
object NumberLaserBeamsBank extends App {
  @tailrec
  private def loop(rem: Array[String], prev: Int = 0, res: Int = 0): Int = {
    if (rem.isEmpty) res
    else {
      val actual = rem.head.count(_ == '1')
      if (actual == 0) loop(rem.tail, prev, res)
      else {
        loop(
          rem.tail,
          actual,
          if (prev != 0) res + (prev * actual) else res,
        )
      }
    }
  }

  def numberOfBeams(bank: Array[String]): Int = {
    loop(bank)
  }


  println(numberOfBeams(Array("011001", "000000", "010100", "001000")))
  println(numberOfBeams(Array("011001")))
}
