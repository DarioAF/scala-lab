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
  class ListNode(_x: Int = 0, _next: ListNode = null) {
    var next: ListNode = _next
    var x: Int = _x
  }

  def reverseListNode(lnode: ListNode): ListNode = {
    @tailrec def aux(remaining: ListNode, result: ListNode = null): ListNode = {
      if (remaining == null) result
      else aux(remaining.next, ListNode(_x = remaining.x, _next = result))
    }
    aux(lnode)
  }

  def addTwoNumbers(l1: ListNode, l2: ListNode): ListNode = {
    @tailrec def aux(rem1: ListNode, rem2: ListNode, rest: Int, res: ListNode): ListNode = {
      val exists1 = rem1 != null
      val exists2 = rem2 != null
      if (!exists1 && !exists2) res
      else if (!exists1 && !exists2) ListNode(_x = rest, _next = res)
      else {
        val r = (if (exists1) rem1.x else 0) + (if (exists2) rem2.x else 0)
        val hasPending = r > 9

        aux(
          if (exists1) rem1.next else rem1,
          if (exists2) rem2.next else rem2,
          if (hasPending) 1 else 0,
          if (hasPending) ListNode(_x = r - 10, _next = res) else ListNode(_x = r, _next = res)
        )
      }
    }

    reverseListNode(aux(l1, l2, 0, null))
  }
}

object LinkedList extends App {
  import AddTwoNumbers._

  val a = ListNode(_x = 2, _next = ListNode(_x = 4, _next = ListNode(_x = 3, _next = null)))
  val b = ListNode(_x = 5, _next = ListNode(_x = 6, _next = ListNode(_x = 4, _next = null)))

  println(addTwoNumbers(a, b))
}
