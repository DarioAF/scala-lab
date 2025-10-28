package practice.arrays

import scala.annotation.tailrec

/*
3354. Make Array Elements Equal to Zero
https://leetcode.com/problems/make-array-elements-equal-to-zero
*/
case object MakeArrayElementsEqualZero extends App {
  @tailrec
  private def loop(ns: Array[Int], dir: Int, pos: Int): Int = {
    if (pos < 0 || pos >= ns.length) 0
    else if (ns.forall(_ == 0)) 1
    else {
      if (ns(pos) > 0) {
        loop(ns.updated(pos, ns(pos) - 1), dir * (-1), pos + dir * (-1))
      } else {
        loop(ns, dir, pos + dir)
      }
    }
  }

  def countValidSelections(nums: Array[Int]): Int = {
    val zeroPos = nums.zipWithIndex.foldLeft(List.empty[Int]) {
      case (acc, (p, i)) => if (p == 0) acc :+ i else acc
    }

    Seq(-1, 1).flatMap(dir =>
      zeroPos.map(pos => loop(nums, dir, pos))
    ).sum
  }

  Seq(
    (Array(1, 0, 2, 0, 3), 2),
    (Array(2,3,4,0,4,1,0), 0),
  ).foreach((input, expectedOutput) => {
    val output = countValidSelections(input)
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
