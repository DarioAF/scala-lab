package utils

object Quicksort extends App {

  /*
┌───────────────┬────────────┬────────────────┐
│Variation Time │ Complexity │ Auxiliary Space│
├───────────────┼────────────┼────────────────┤
│Best Case      │ O(n log n) │        O(log n)│
├───────────────┼────────────┼────────────────┤
│Average Case   │ O(n log n) │        O(log n)│
├───────────────┼────────────┼────────────────┤
│Worst Case     │   O(n^2)   │            O(n)│
└───────────────┴────────────┴────────────────┘
  */
  def qsort[T](list: List[T])(implicit ord: Ordering[T]): List[T] =
    list match {
      case Nil => Nil
      case x :: xs =>
        val (before, after) = xs.partition(ord.lt(_, x))
        qsort(before) ++ (x :: qsort(after))
    }

  val a: List[Int] = List(3, 1, 4, 1, 5, 9, 2, 6, 5)
  val b: List[String] = List("b", "d", "a", "e", "c")
  println(qsort(a).mkString(", "))
  println(qsort(b).mkString(", "))
}