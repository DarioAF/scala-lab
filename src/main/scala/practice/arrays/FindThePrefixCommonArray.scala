package practice.arrays

/**
 * 2657. Find the Prefix Common Array of Two Arrays
 * https://leetcode.com/problems/find-the-prefix-common-array-of-two-arrays/
 */
object FindThePrefixCommonArray extends App {
  def findThePrefixCommonArray(A: Array[Int], B: Array[Int]): Array[Int] = {
    val seen: collection.mutable.Set[Int] = collection.mutable.Set.empty
    var count = 0

    A.indices.map { i =>
      val (a, b) = (A(i), B(i))
      count = count + (if (a == b) 1 else 0)
      count = count + (if (seen.contains(a)) 1 else 0)
      count = count + (if (seen.contains(b)) 1 else 0)

      seen.add(a)
      seen.add(b)
      count
    }.toArray
  }

  def findThePrefixCommonArray2(A: Array[Int], B: Array[Int]): Array[Int] = {
    A.indices.foldLeft[(Array[Int], Set[Int])]((Array.empty, Set.empty))((acc, i) => {
      val (a, b) = (A(i), B(i))
      val count = List[Boolean](
        a == b, acc._2.contains(a), acc._2.contains(b)
      ).count(b => b) + (if (acc._1.nonEmpty) acc._1.last else 0)
      (acc._1 :+ count, acc._2 ++ Set(a, b))
    })._1
  }

  println(findThePrefixCommonArray(Array(1,3,2,4), Array(3,1,2,4)).mkString(", "))
  println(findThePrefixCommonArray2(Array(1,3,2,4), Array(3,1,2,4)).mkString(", "))
}
