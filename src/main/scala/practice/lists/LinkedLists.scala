package practice.lists

import scala.annotation.tailrec

/** Add Two Numbers
 * (https://leetcode.com/problems/add-two-numbers)
 *
 * You are given two non-empty linked lists representing two non-negative integers. The digits are stored in reverse order, and each of their nodes contains a single digit. Add the two numbers and return the sum as a linked list.
 * You may assume the two numbers do not contain any leading zero, except the number 0 itself.
 *
 * Example:
 * Input: l1 = [2,4,3], l2 = [5,6,4]
 * Output: [7,0,8]
 * Explanation: 342 + 465 = 807.
 */
object AddTwoNumbers {
  def addTwoNumbers(l1: MyList[Int], l2: MyList[Int]): MyList[Int] = {
    @tailrec def aux(l1: MyList[Int], l2: MyList[Int], carry: Int, res: MyList[Int]): MyList[Int] = {
      val exists1 = !l1.isEmpty
      val exists2 = !l2.isEmpty
      if (!exists1 && !exists2) {
        if (carry > 0) carry :: res else res
      } else {
        val r = (if (exists1) l1.head else 0) + (if (exists2) l2.head else 0) + carry
        val digit = r % 10
        val newCarry = r / 10

        aux(
          if (exists1) l1.tail else l1,
          if (exists2) l2.tail else l2,
          newCarry,
          digit :: res
        )
      }
    }

    aux(l1, l2, 0, Nil).reverse
  }
}

/** 3217. Delete Nodes From Linked List Present in Array
https://leetcode.com/problems/delete-nodes-from-linked-list-present-in-array
*/
object DeleteNodesFromLinkedList {
  def modifiedList(nums: Array[Int], head: MyList[Int]): MyList[Int] = {
    val numsSet = nums.toSet

    @tailrec def loop(current: MyList[Int], acc: MyList[Int] = Nil): MyList[Int] = {
      if (current.isEmpty) acc
      else if (numsSet.contains(current.head)) {
        loop(current.tail, acc)
      } else {
        loop(current.tail, current.head :: acc)
      }
    }

    // loop builds the list in reverse, so reverse it again
    loop(head).reverse
  }
}

object LinkedList extends App {
  case class Exercise(name: String, enabled: Boolean, testSuite: () => Unit)

  Seq(
    Exercise("AddTwoNumbers", true, () => {
      Seq(
        ((2 :: 4 :: 3 :: Nil, 5 :: 6 :: 4 :: Nil), 7 :: 0 :: 8 :: Nil),
      ).foreach { case ((l1, l2), expectedOutput) =>
        val output = AddTwoNumbers.addTwoNumbers(l1, l2)
        if (output == expectedOutput) {
          println(Console.GREEN + s"$l1 + $l2 = $output ✅" + Console.RESET)
        } else {
          println(Console.RED + s"for: $l1 + $l2 was expected $expectedOutput, but got: $output ❌" + Console.RESET)
        }
      }
    }),
    Exercise("DeleteNodesFromLinkedList", true, () => {
      Seq(
        ((Array(1,2,3), 1 :: 2 :: 3 :: 4 :: 5 :: Nil), 4 :: 5 :: Nil),
        ((Array(1), 1 :: 2 :: 1 :: 2 :: 1 :: 2 :: Nil), 2 :: 2 :: 2 :: Nil),
        ((Array(5), 1 :: 2 :: 3 :: 4 :: Nil), 1 :: 2 :: 3 :: 4 :: Nil),
      ).foreach { case ((nums, head), expectedOutput) =>
        val output = DeleteNodesFromLinkedList.modifiedList(nums, head)
        if (output == expectedOutput) {
          println(Console.GREEN + s"$head - ${nums.mkString("[", ",", "]")} = $output ✅" + Console.RESET)
        } else {
          println(Console.RED + s"for: $head - ${nums.mkString("[", ",", "]")} was expected $expectedOutput, but got: $output ❌" + Console.RESET)
        }
      }
    }),
  ).foreach(exercise => {
    if (exercise.enabled) {
      println(s"Executing test suite for ${exercise.name} ...")
      exercise.testSuite()
    }
  })
}
