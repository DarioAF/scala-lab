package practice.graphs

/**
 * From the following friend list, create a relationship map, where you can assume
 *  - there are always two people from element on the friends list
 *  - each relationship is bidirectional, ex: 1 -> 2 and 2 -> 1 for the first case
 */
object BiDirFriendship extends App {

  val friends: List[String] = List(
    "1,2",
    "1,3",
    "1,4",
    "2,6",
  )

  val res = friends.map(_.split(",")).foldLeft[Map[String, List[String]]](Map.empty) {
    (acc, xs) => {
      val k = xs.head
      val f = xs(1)

      val acc1 = acc.get(k) match {
        case Some(lf) => acc + (k -> (f :: lf))
        case None => acc + (k -> (f :: List.empty))
      }

      val acc2 = acc1.get(f) match {
        case Some(lf) => acc1 + (f -> (k :: lf))
        case None => acc1 + (f -> (k :: List.empty))
      }

      acc2
    }
  }

  println(res)
}
