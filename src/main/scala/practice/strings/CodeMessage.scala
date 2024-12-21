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
        if (vowels.contains(c) && prev != c) {
          tailrec(remaining.tail, c, res + "av" + c)
        } else {
          tailrec(remaining.tail, c, res + c)
        }
      }
    }

    tailrec(str, '1', "")
  }

  val text = "hello world"
  println("original: " + text)
  println(translate("coded: " + text))
}
