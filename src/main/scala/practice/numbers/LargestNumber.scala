package practice.numbers

object LargestNumber extends App {
  /*
  Given a list of non-negative integers, arrange them such that they form the largest number.
  The result might be huge so return a string

  List(10, 2) => "210¨
  List(3, 30, 5, 9, 34) => "9534330"
   */

  def largestNumber(numbers: List[Int]): String = {
    /*
    * Note: This is not a proper sorting method for .sorted
    * For a comparison method to be effective as sorted, it has to fulfill the following properties:
    * - reflexive: a <= a
    * - transitive: if a <= b AND b <= c then a <= c
    * - anti-symmetrical: if a <= b AND b <= a then a == b
    *   -> This is not the case
    *   -> counterexample: List(1010, 10)
    *
    * for this problem it does not matter
    * */
    implicit val newOrdering: Ordering[Int] = Ordering.fromLessThan { (a, b) =>
      val as = a.toString
      val bs = b.toString
      (as + bs).compareTo(bs + as) >= 0
    }
    val largest = numbers.sorted.mkString("")
    if (numbers.isEmpty || largest.charAt(0) == '0') "0"
    else largest
  }

  println(largestNumber(List(10, 2))) //210
  println(largestNumber(List(3, 30, 5, 9, 34))) //9534330
  println(largestNumber(List(2020, 20, 1010, 10, 2, 22))) //222202020101010
  println(largestNumber(List(1))) //1
  println(largestNumber(List.empty)) //0
  println(largestNumber(List(0, 0, 0, 0))) //0
}
