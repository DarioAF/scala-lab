package utils

import scala.annotation.tailrec

object BinarySearch extends App {

  /*
┌───────────────┬────────────┬────────────────┐
│Variation Time │ Complexity │ Auxiliary Space│
├───────────────┼────────────┼────────────────┤
│Best Case      │    O(1)    │        O(log n)│
├───────────────┼────────────┼────────────────┤
│Average Case   │  O(log n)  │        O(log n)│
├───────────────┼────────────┼────────────────┤
│Worst Case     │  O(log n)  │        O(log n)│
└───────────────┴────────────┴────────────────┘
  */
  def binarySearch[T](xs: List[T], target: T)(implicit ord: Ordering[T]): Option[Int] = {
    @tailrec
    def tailRecSearch(low: Int, high: Int): Option[Int] = {
      if (low > high) return None
      val mid = low + (high - low) / 2
      if (xs(mid) == target) Some(mid)
      else if (ord.lt(xs(mid), target)) tailRecSearch(mid + 1, high)
      else tailRecSearch(low, mid - 1)
    }
    tailRecSearch(0, xs.length - 1)
  }


  val (a, at) = (List(1, 2, 3, 4, 5, 6, 7, 8, 9), 5)
  binarySearch(a, at) match {
    case Some(i) => println(s"Element found at index: $i")
    case None => println("Element not found")
  }

  val (b, bt) = (List("a", "b", "c", "d", "e", "f"), "f")
  binarySearch(b, bt) match {
    case Some(i) => println(s"Element found at index: $i")
    case None => println("Element not found")
  }
}