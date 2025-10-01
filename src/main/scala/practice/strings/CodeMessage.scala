package practice.strings

import scala.annotation.tailrec

/**
 * Insert 'av' before any vowel ('a', 'e', 'i', 'o', 'u') character
 * unless it is followed by another vowel
 */

object CodeMessage extends App {
  val vowels: List[Char] = List('a', 'e', 'i', 'o', 'u')

  def translate(str: String): String = {
    @tailrec
    def tailrec(remaining: String, prev: Char, res: String): String = {
      if (remaining.isEmpty) res
      else {
        val c = remaining.head
        if (vowels.contains(c) && !vowels.contains(prev)) {
          tailrec(remaining.tail, c, res + "av" + c)
        } else {
          tailrec(remaining.tail, c, res + c)
        }
      }
    }

    tailrec(str, '1', "")
  }

  Seq(
    ("", ""),
    ("hello", "havellavo"),
    ("world", "wavorld"),
    ("abaalonii", "avabavaalavonavii"),
    ("aeiou", "avaeiou"),
  ).foreach { (input, expected) =>
    val actual = translate(input)
    if (actual == expected) {
      println(s"$input -> $actual ✅")
    } else {
      println(s"expected: $expected got: $actual ❌")
    }
  }
}
