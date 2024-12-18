package practice.arrays

import scala.annotation.tailrec

/** 1475. Final Prices With a Special Discount in a Shop
 * (https://leetcode.com/problems/final-prices-with-a-special-discount-in-a-shop)
 *
 * You are given an integer array prices where prices[i] is the price of the ith item in a shop.
 * There is a special discount for items in the shop. If you buy the ith item, then you will receive a discount equivalent to prices[j] where j is the minimum index such that j > i and prices[j] <= prices[i]. Otherwise, you will not receive any discount at all.
 * Return an integer array answer where answer[i] is the final price you will pay for the ith item of the shop, considering the special discount.
 */
object Solution {
  def finalPrices(prices: Array[Int]): Array[Int] = {

    @tailrec
    def findDiscount(prices: Array[Int], price: Int): Int = {
      if (prices.isEmpty) 0
      else if (prices.head <= price) prices.head
      else findDiscount(prices.tail, price)
    }

    @tailrec
    def iteratePrices(prices: Array[Int], res: Array[Int]): Array[Int] = {
      if (prices.isEmpty) res
      else {
        val price = prices.head
        val discount = findDiscount(prices.tail, price)
        iteratePrices(prices.tail, res :+ price - discount)
      }
    }

    iteratePrices(prices, Array.empty)
  }
}

object FinalPricesWDiscounts extends App {
  import Solution._
  println(finalPrices(Array(8, 4, 6, 2, 3)).mkString("Array(", ", ", ")"))
  println(finalPrices(Array(1, 2, 3, 4, 5)).mkString("Array(", ", ", ")"))
  println(finalPrices(Array(10, 1, 1, 6)).mkString("Array(", ", ", ")"))
}
