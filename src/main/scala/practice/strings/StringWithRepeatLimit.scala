package practice.strings

import scala.annotation.tailrec

/** 2182. Construct String With Repeat Limit
 * (https://leetcode.com/problems/construct-string-with-repeat-limit)
 *
 * You are given a string s and an integer repeatLimit. Construct a new string repeatLimitedString using the characters of s such that no letter appears more than repeatLimit times in a row. You do not have to use all characters from s.
 * Return the lexicographically largest repeatLimitedString possible.
 * A string a is lexicographically larger than a string b if in the first position where a and b differ, string a has a letter that appears later in the alphabet than the corresponding letter in b. If the first min(a.length, b.length) characters do not differ, then the longer string is the lexicographically larger one.
 */
object Solution {
  def toOrderedTuple(s: String): List[(Char, Int)] = {
    s.groupBy(c => c).map((k,v) => (k, v.length)).toList.sortBy(_._1).reverse
  }

  def repeatLimitedString(s: String, repeatLimit: Int): String = {
    @tailrec
    def auxTailRec(rem: List[(Char, Int)], prev: Char, count: Int, res: String): String = {
      if (rem.isEmpty) res
      else if (rem.head._1 == prev && count >= repeatLimit) {
        if (rem.tail.isEmpty) res
        else {
          println(s"#1 $rem, prev: $prev, count: $count, res: $res")
          val next = if (rem.tail.head._2 > 1) (rem.tail.head._1, rem.tail.head._2 - 1) :: rem.tail.tail else rem.tail.tail
          auxTailRec((rem.tail.head._1, 1) :: rem.head :: next, prev, count, res)
        }
      } else {
        val currentChar = rem.head._1
        val currentAvail = rem.head._2
        val newCount = if (currentChar == prev) count + 1 else 1
        if (currentAvail > 1) {
          println(s"#2 $rem, prev: $prev, count: $count, res: $res")
          auxTailRec((currentChar, currentAvail - 1) :: rem.tail, currentChar, newCount, res + currentChar)
        } else {
          println(s"#3 $rem, prev: $prev, count: $count, res: $res")
          auxTailRec(rem.tail, currentChar, newCount, res + currentChar)
        }
      }
    }
    auxTailRec(toOrderedTuple(s), '?', 0, "")
  }
}

object StringWithRepeatLimit extends App {
  import Solution._

  println(repeatLimitedString("cczazcc", 3))
}

