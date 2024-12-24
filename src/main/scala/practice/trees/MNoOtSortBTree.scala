package practice.trees

import scala.annotation.tailrec

// 2471. Minimum Number of Operations to Sort a Binary Tree by Level
// https://leetcode.com/problems/minimum-number-of-operations-to-sort-a-binary-tree-by-level

// Provided definition for a binary tree node.
class TreeNode(_value: Int = 0, _left: TreeNode = null, _right: TreeNode = null) {
  var value: Int = _value
  var left: TreeNode = _left
  var right: TreeNode = _right
}

object extras {
  implicit class TreeNodeExtras(root: TreeNode) {
    def getValuesByLevel: List[List[Int]] = {
      @tailrec
      def tailrec(rem: List[TreeNode], currentLevel: Int, levelValues: List[TreeNode], res: List[List[Int]]): List[List[Int]] = {
        if (rem.isEmpty) res
        else if (levelValues.nonEmpty) {
          val vals = levelValues.filter(_ != null).map(_.value)
          tailrec(rem, currentLevel + 1, List.empty, res :+ vals)
        }
        else {
          if (currentLevel == 0) {
            tailrec(rem.head.left :: rem.head.right :: rem.tail, currentLevel, List(rem.head), res)
          } else {
            val next: List[TreeNode] = rem.foldLeft(List.empty)((acc, t) => {
              if (t != null) acc ++ (t.left :: t.right :: List.empty) else acc
            })
            tailrec(next, currentLevel, rem, res)
          }
        }
      }
      tailrec(List(root), 0, List.empty, List.empty)
    }
  }
}

object Solution extends App {
  import extras._

  private def minSwapsToSort(list: List[Int]): Int = {
    def swapWithMin(n: Int, xs: List[Int]): Option[List[Int]] = {
      if (xs.isEmpty) None
      else (0 to xs.size).zip(xs).foldLeft[Option[(Int, Int)]](None)((acc, n) => {
        if (acc.isEmpty || acc.isDefined && acc.get._2 > n._2) Some(n) else acc
      }) match {
        case None => None
        case Some(_, minVal) if minVal > n => None
        case Some(minPos, minVal) =>
          val (before, after) = xs.splitAt(minPos)
          Some(before ++ (n :: after.tail))
      }
    }
    @tailrec def tailrec(rem: List[Int], swaps: Int = 0): Int = rem match {
      case Nil => swaps
      case x :: xs => swapWithMin(x, xs) match {
        case None => tailrec(xs, swaps)
        case Some(tail) => tailrec(tail, swaps + 1)
      }
    }
    tailrec(list)
  }

  def minimumOperations(root: TreeNode): Int = {
    root.getValuesByLevel match {
      case Nil | _ :: Nil => 0
      case _ :: xs => xs.map(minSwapsToSort).sum
    }
  }

  /*
      _ 1 _
     /     \
    4       3
   / \     / \
  7   6   8   5
         /   /
        9   10
  */
  println(minimumOperations(
    TreeNode(1, TreeNode(4, TreeNode(7,null,null), TreeNode(6,null,null)),TreeNode(3, TreeNode(8, TreeNode(9, null, null),null), TreeNode(5,TreeNode(10, null, null),null)))
  ))

  /*
      _ 1 _
     /     \
    3       2
   / \     / \
  7   6   5   4
  */
  println(minimumOperations(
    TreeNode(1, TreeNode(3, TreeNode(7, null, null), TreeNode(6, null, null)), TreeNode(2, TreeNode(5, null, null), TreeNode(4, null, null)))
  ))

  /*
      _ 1 _
     /     \
    2       3
   / \     /
  4   5   6
  */
  println(minimumOperations(
    TreeNode(1, TreeNode(2, TreeNode(4, null, null), TreeNode(5, null, null)), TreeNode(3, TreeNode(6, null, null), null))
  ))
}