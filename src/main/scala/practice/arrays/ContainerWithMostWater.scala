package practice.arrays

import scala.annotation.tailrec

/*
11. Container With Most Water
https://leetcode.com/problems/container-with-most-water
*/
object ContainerWithMostWater extends App {

  @tailrec
  private def moveI(i: Int, j: Int, min: Int)(implicit height: Array[Int]): Int = {
    if (i == j || height(i) > min) i
    else moveI(i + 1, j, min)
  }

  @tailrec
  private def moveJ(i: Int, j: Int, min: Int)(implicit height: Array[Int]): Int = {
    if (i == j || height(j) > min) j
    else moveJ(i, j - 1, min)
  }

  @tailrec
  private def loop(max: Int, i: Int, j: Int)(implicit height: Array[Int]): Int = {
    if (i == j) max
    else {
      val min = Math.min(height(i), height(j))
      val newMax = Math.max(max, (j - i) * min)
      val newI = moveI(i, j, min)
      val newJ = moveJ(newI, j, min)
      loop(newMax, newI, newJ)
    }
  }

  def maxArea(height: Array[Int]): Int = {
    loop(0, 0, height.length - 1)(height)
  }

  Seq(
    (Array(1,8,6,2,5,4,8,3,7), 49),
    (Array(1,1), 1),
  ).foreach((input, expectedOutput) => {
    val output = maxArea(input)
    if (output == expectedOutput) {
      println(Console.GREEN
        + s"${input.mkString("Array(", ", ", ")")} -> $output ✅"
        + Console.RESET)
    } else {
      println(Console.RED
        + s"${input.mkString("Array(", ", ", ")")} -> expected $expectedOutput but had: $output ❌"
        + Console.RESET)
    }
  })
}
