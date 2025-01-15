package practice.arrays

/**
 2070. Most Beautiful Item for Each Query
 https://leetcode.com/problems/most-beautiful-item-for-each-query/description/
 */
object MaximumBeauty extends App {
  def maximumBeauty(items: Array[Array[Int]], queries: Array[Int]): Array[Int] = {
    val itemMap = items.foldLeft[Map[Int, Int]](Map.empty)((acc, item) => {
      val price = item(0)
      val beauty = item(1)

      if (acc.isEmpty) acc + (price -> beauty)
      else {
        if (acc.exists((k, v) => k < price && v >= beauty)) acc
        else {
          val facc = acc.filterNot((k, v) => k > price && v < beauty)
          facc.get(price) match {
            case Some(b) if b < beauty => facc.updated(price, beauty)
            case _ => facc + (price -> beauty)
          }
        }
      }
    })

    val keys = itemMap.keys.toList.sortWith(_ > _)
    queries.map { q =>
      itemMap.get(q).orElse(keys.find(_ < q).map(itemMap)).getOrElse(0)
    }
  }

  println(maximumBeauty(
    Array(Array(1, 2), Array(3, 2), Array(2, 4), Array(5, 6), Array(3, 5)),
    Array(1, 2, 3, 4, 5, 6)
  ).mkString(", "))

  println(maximumBeauty(
    Array(Array(2, 20), Array(5, 10), Array(1, 50), Array(10, 100)),
    Array(1, 5)
  ).mkString(", "))
}
