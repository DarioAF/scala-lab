package practice.lists

import scala.annotation.tailrec

/*
  Given an array of decimal numbers, containing measurements, return the "peaks" in these values.
  - When a value is at least 5 units higher than its two neighbors, its a "top peak"
  - When a value is at least 5 units lower than its two neighbors, its a "bottom peak"
  - Return an int: the total number of top peaks and bottom peaks.
  The first and last elements of the array values can never be peaks.

  Example:
  given the values (8, 10.7, 17.1, 11.2, 13.5, 9.9, 15.9, 9.4, 9.4, 3.1, 12.7)
  - The value at index 2 is 17.1. The value just before is 10.7 and the value just after is 11.2.
    The value at index 2 is more than 5 units higher than its two neighbors. So, its a "top peak"
  - When the difference is exactly 5, it can validate a peak.
    For example, the value at index 6 is 14.9. The value before is 9.9 and the one after is 9.4. It is another top peak.
  - The value at index 9 is 3.1, its neighbors are 9.4 and 12.7. So, the value at index 9 is a "bottom peak".
  With this example, there are 3 peaks.
*/
object MeasurementPeaks extends App {

  private def isPeak(prev: Double, value: Double, next: Double): Boolean =
    math.abs(value - prev) >= 5 && math.abs(value - next) >= 5

  private def isSequence(prev: Double, value: Double, next: Double): Boolean =
    ((math.abs(prev) <= math.abs(value)) && (math.abs(value) <= math.abs(next)))
    || ((math.abs(prev) >= math.abs(value)) && (math.abs(value) >= math.abs(next)))

  @tailrec
  private def getPeaks(prev: Double, value: Double, rem: Seq[Double], res: Seq[Double]): Seq[Double] = {
    if (rem.isEmpty) res
    else getPeaks(
      prev = value,
      value = rem.head,
      rem = rem.tail,
      res = if (isPeak(prev, value, rem.head) && !isSequence(prev, value, rem.head)) {
        res :+ value
      } else res
    )
  }

  private def countPeaks(values: Seq[Double]): Int = {
    if (values.isEmpty || values.size < 3) 0
    else {
      val (prev, value, rem) = (values.head, values.tail.head, values.tail.tail)
      getPeaks(prev, value, rem, Seq.empty).size
    }
  }

  Seq(
    (Seq(8, 10.7, 17.1, 11.2, 13.5, 9.9, 15.9, 9.4, 9.4, 3.1, 12.7), 3),
    (Seq(81.3, 80.3, 75.3, 80.3, 85.2, 90.2, 95.2, 90.2, 89.1, 88.8), 2),
    (Seq(8, 8, 2.1, 20.7, 8, 8, 8, 2.1, 20.7, 8), 4),
    (Seq(0.0, 10, 0, 10, 0, 10, 0, 10, 0, 10), 8),
    (Seq(0, 20, 40, 60, 80, 99.9, 80, 60, 40, 20, 0), 1),
    (Seq(51.8, 42.8), 0),
    (Seq.empty, 0),
  ).foreach { (values: Seq[Double], expectedPeaks: Int) =>
    val actualPeaks = countPeaks(values)
    if (actualPeaks == expectedPeaks) {
      println(Console.GREEN + s"${values.mkString(",")} -> $expectedPeaks ✅" + Console.RESET)
    } else {
      println(Console.RED + s"${values.mkString(",")} -> expected: $expectedPeaks got: $actualPeaks ❌" + Console.RESET)
    }
  }
}
