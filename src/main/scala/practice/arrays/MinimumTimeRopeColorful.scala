package practice.arrays

import scala.annotation.tailrec

/** 1578. Minimum Time to Make Rope Colorful
 * https://leetcode.com/problems/minimum-time-to-make-rope-colorful
 */
object MinimumTimeRopeColorful extends App {
  private def getMinTime(balloons: List[(Char, Int)]): Int =
    balloons.map(_._2).sorted(Ordering[Int].reverse).tail.sum

  def minCost(colors: String, neededTime: Array[Int]): Int = {
    @tailrec
    def loop(rem: Seq[(Char, Int)], prevs: List[(Char, Int)] = List.empty[(Char, Int)], accTime: Int = 0): Int = {
      if (rem.isEmpty) if (prevs.size > 1) accTime + getMinTime(prevs) else accTime
      else {
        if (prevs.isEmpty) {
          loop(rem.tail, List(rem.head), accTime)
        } else {
          val prevColor = prevs.head._1
          val (currentColor, currentTimeNeeded) = rem.head
          if (currentColor == prevColor) {
            loop(rem.tail, rem.head :: prevs, accTime)
          } else {
            if (prevs.size > 1) {
              loop(rem.tail, List(rem.head), accTime + getMinTime(prevs))
            } else {
              loop(rem.tail, List(rem.head), accTime)
            }
          }
        }
      }
    }

    loop(colors.zip(neededTime))
  }

  Seq(
    (("abaac", Array(1, 2, 3, 4, 5)), 3),
    (("abc", Array(1, 2, 3)), 0),
    (("aabaa", Array(1, 2, 3, 4, 1)), 2),
    (("aaabbbabbbb", Array(3, 5, 10, 7, 5, 3, 5, 5, 4, 8, 1)), 26),
  ).foreach { case ((colors, neededTime), expectedOutput) =>
    val output = minCost(colors, neededTime)
    if (output == expectedOutput) {
      println(Console.GREEN + "[OK] " + Console.RESET + s"($colors, ${neededTime.mkString("[", ",", "]")}) -> $output")
    } else {
      println(Console.RED + "[ERROR] " + Console.RESET + s"($colors, ${neededTime.mkString("[", ",", "]")}) -> Got $output, expected: $expectedOutput")
    }
  }
}

