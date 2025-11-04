package practice.arrays

import scala.annotation.tailrec

/**
 * 3318. Find X-Sum of All K-Long Subarrays I
 * https://leetcode.com/problems/find-x-sum-of-all-k-long-subarrays-i
 */
object FindXSumAllKLongSubArraysI extends App {
  private def calcN(window: Array[Int], x: Int): Int = window
    .groupBy(identity)
    .view
    .mapValues(_.length)
    .toSeq
    .sortBy { case (num, count) => (-count, -num) }  // sort descending by count, then number
    .take(x)
    .map { case (num, count) => num * count }
    .sum

  def findXSum(nums: Array[Int], k: Int, x: Int): Array[Int] = {
    @tailrec
    def loop(i: Int = 0, j: Int = k, res: Array[Int] = Array.empty): Array[Int] = {
      if (j >= nums.length) {
        res :+ calcN(nums.slice(i, j), x)
      } else {
        loop(i + 1, j + 1, res :+ calcN(nums.slice(i, j), x))
      }
    }
    loop()
  }

  Seq(
    ((Array(1, 1, 2, 2, 3, 4, 2, 3), 6, 2), Array(6, 10, 12)),
    ((Array(3,8,7,8,7,5), 2, 2), Array(11,15,15,15,12)),
  ).foreach { case ((nums, k, j), expectedOutput) =>
    val output = findXSum(nums, k, j)
    if (output sameElements expectedOutput) {
      println(Console.GREEN + "[OK] " + Console.RESET +
        s"for ${nums.mkString("Array(", ", ", ")")}, k = $k, j = $j -> ${output.mkString("Array(", ", ", ")")}")
    } else {
      println(Console.RED + "[ERROR] " + Console.RESET +
        s"for ${nums.mkString("Array(", ", ", ")")}, k = $k, j = $j " +
        s"expected ${expectedOutput.mkString("Array(", ", ", ")")} " +
        s"but got ${output.mkString("Array(", ", ", ")")}")
    }
  }
}
