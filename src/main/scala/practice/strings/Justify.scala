package practice.strings

import scala.annotation.tailrec

object Justify extends App {

  def justify(text: String, width: Int): String = {
    def createSpaces(n: Int): String = (1 to n).map(_ => " ").mkString("")

    @tailrec def pack(words: List[String], c: Int, buff: List[String], res: List[List[String]]): List[List[String]] = {
      if (words.isEmpty && buff.isEmpty) res
      else if (words.isEmpty) res :+ buff
      else if (buff.isEmpty && words.head.length > width) {
        val (thiRow, nextRow) = words.head.splitAt(width - 2)
        pack(nextRow :: words.tail, 0, List.empty, res :+ List(thiRow + "-"))
      } else if (words.head.length + c > width) {
        pack(words, 0, List.empty, res :+ buff)
      } else {
        pack(words.tail, c + 1 + words.head.length, buff :+ words.head, res)
      }
    }

    def justifyRow(row: List[String]): String = {
      if (row.length == 1) row.head
      else {
        val availableSpaces = width - row.map(_.length).sum
        val intervals = row.length - 1
        val spaces = availableSpaces / intervals
        val extras = availableSpaces % intervals
        val regularSpace = createSpaces(spaces + 1)
        val biggerSpace = createSpaces(spaces + 2)

        if (extras == 0) row.mkString(regularSpace)
        else {
          val wordsWithBiggerIntervals = row.take(extras + 1).mkString(biggerSpace)
          val restOfTheWords = row.drop(extras + 1).mkString(regularSpace)
          wordsWithBiggerIntervals + regularSpace + restOfTheWords
        }
      }
    }

    assert(width > 2)
    // split text into words
    val words = text.split(" ").toList
    // pack the words into rows
    val unjustifiedRows = pack(words, 0, List.empty, List.empty)
    // justify the rows
    val justifiedRows = unjustifiedRows.map(justifyRow)
    // rebuild the justified text
    justifiedRows.mkString("\n")
  }

  val text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
  println(justify(text, 60))
}
