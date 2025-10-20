package practice.strings

import scala.annotation.tailrec

/*
5. Longest Palindromic Substring
https://leetcode.com/problems/longest-palindromic-substring
*/
object LongestPalindromicSubstring extends App {

@tailrec
private def findLongestOddSubPal(leftIdx: Int, rightIdx: Int, res: String)(implicit s: String): String = {
  if (leftIdx < 0 || rightIdx >= s.length || s.charAt(leftIdx) != s.charAt(rightIdx)) res
  else if (rightIdx - leftIdx + 1 > res.length) {
    findLongestOddSubPal(leftIdx - 1, rightIdx + 1, s.substring(leftIdx, rightIdx + 1))
  } else findLongestOddSubPal(leftIdx - 1, rightIdx + 1, res)
}

@tailrec
private def findLongestEvenSubPal(leftIdx: Int, rightIdx: Int, res: String)(implicit s: String): String = {
  if (leftIdx < 0 || rightIdx >= s.length || s.charAt(leftIdx) != s.charAt(rightIdx)) res
  else if (rightIdx - leftIdx + 1 > res.length) {
    findLongestEvenSubPal(leftIdx - 1, rightIdx + 1, s.substring(leftIdx, rightIdx + 1))
  } else findLongestEvenSubPal(leftIdx - 1, rightIdx + 1, res)
}

def longestPalindrome(s: String): String = s.zipWithIndex.foldLeft("") { case (acc, (_, i)) =>
  findLongestEvenSubPal(i, i+1,
    findLongestOddSubPal(i, i, acc)(s)
  )(s)
}

Seq(
  ("babad", "bab"),
  ("cbbd", "bb"),
  ("ac", "a"),
  ("abb", "bb"),
  ("ccc", "ccc"),
  ("abcda", "a"),
  ("aacdefcaa", "aa"),
  ("acdefcaa", "aa"),
  ("acdefcca", "cc"),
  ("babaddtattarrattatddetartrateedredividerb", "ddtattarrattatdd"),
).foreach((input, expectedOutput) => {
  val output = longestPalindrome(input)
  if (output == expectedOutput) {
    println(Console.GREEN
      + s"$input -> $output ✅"
      + Console.RESET)
  } else {
    println(Console.RED
      + s"$input -> expected $expectedOutput but had: $output ❌"
      + Console.RESET)
  }
})
}
