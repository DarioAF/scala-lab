package practice.lists

import scala.annotation.tailrec

sealed abstract class MyList[+T] {
  def head: T
  def tail: MyList[T]
  def isEmpty: Boolean

  def ::[S >: T](elem: S): MyList[S] = new::(elem, this)
  def ++[S >: T](list: MyList[S]): MyList[S]

  def reverse: MyList[T]
}

case object Nil extends MyList[Nothing] {
  override def head: Nothing = throw new NoSuchElementException
  override def tail: MyList[Nothing] = throw new NoSuchElementException
  override def isEmpty: Boolean = true
  override def toString: String = "[]"

  override def ++[S >: Nothing](list: MyList[S]): MyList[S] = list

  override def reverse: MyList[Nothing] = Nil
}

case class ::[+T](override val head: T, override val tail: MyList[T]) extends MyList[T] {
  override def isEmpty: Boolean = false
  override def toString: String = {
    // complexity: O(N)
    @tailrec def toStringTailrec(remaining: MyList[T], result: String = ""): String = {
      if (remaining.isEmpty) result
      else if (remaining.tail.isEmpty) s"$result${remaining.head}"
      else toStringTailrec(remaining.tail, s"$result${remaining.head}, ")
    }

    "[" + toStringTailrec(this) + "]"
  }

  override def ++[S >: T](list: MyList[S]): MyList[S] = {
    // complexity: O(N+M)
    @tailrec def recAux(remaining: MyList[S], result: MyList[S]): MyList[S] = {
      if (remaining.isEmpty) result
      else recAux(remaining.tail, remaining.head :: result)
    }
    recAux(this.reverse, list)
  }

  override def reverse: MyList[T] = {
    // complexity: O(N)
    @tailrec def recAux(remaining: MyList[T], result: MyList[T]): MyList[T] = {
      if (remaining.isEmpty) result
      else recAux(remaining.tail, remaining.head :: result)
    }
    recAux(this, Nil)
  }
}