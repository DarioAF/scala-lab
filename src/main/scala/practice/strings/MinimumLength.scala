package practice.strings

/**
 3223. Minimum Length of String After Operations
 https://leetcode.com/problems/minimum-length-of-string-after-operations/description/
 */
object MinimumLength extends App {
  def minimumLength(s: String): Int = {
    def tailrec(rem: String, ind: Int = 0, acc: Map[Char, List[Int]] = Map.empty): Map[Char, List[Int]] = {
      if (rem.isEmpty) acc
      else {
        val c = rem.head
        val newacc = acc.get(c) match {
          case Some(list) => acc + (c -> (list :+ ind))
          case None => acc + (c -> List(ind))
        }
        tailrec(rem.tail, ind + 1, newacc)
      }
    }
    val m = tailrec(s)
    val rest = m.values.map {
      case l :: v :: r :: tail => 2
      case _ => 0
    }.sum

    s.length - rest
  }

  def minimumLength2(s: String): Int = {
    s.length - s.foldLeft[(Int, Map[Char, Int])]((0, Map.empty))((acc, v) => {
      acc._2.get(v) match {
        case Some(c) if c % 2 == 0 => (acc._1 + 2, acc._2 + (v -> (c + 1)))
        case Some(c) => (acc._1, acc._2 + (v -> (c + 1)))
        case None => (acc._1, acc._2 + (v -> 1))
      }
    })._1
  }

  List(
    ("abaacbcbb", 5),
    ("aa", 2),
    ("ucvbutgkohgbcobqeyqwppbxqoynxeuuzouyvmydfhrprdbuzwqebwuiejoxsxdhbmuaiscalnteocghnlisxxawxgcjloevrdcj", 38),
  ).foreach(e => println(s"${e._1} -> ${minimumLength2(e._1)} | expected: ${e._2}"))

}
