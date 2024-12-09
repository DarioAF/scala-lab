package practice.lists

import scala.annotation.tailrec

sealed abstract class MyList[+T] {
  def head: T
  def tail: MyList[T]
  def isEmpty: Boolean

  def ::[S >: T](elem: S): MyList[S] = new::(elem, this)
  def ++[S >: T](list: MyList[S]): MyList[S]

  def map[S](f:T => S): MyList[S]
  def flatMap[S](f: T => MyList[S]): MyList[S]
  def filter(f: T => Boolean): MyList[T]
  def reverse: MyList[T]
}

case object Nil extends MyList[Nothing] {
  override def head: Nothing = throw new NoSuchElementException
  override def tail: MyList[Nothing] = throw new NoSuchElementException
  override def isEmpty: Boolean = true
  override def toString: String = "[]"

  override def ++[S >: Nothing](list: MyList[S]): MyList[S] = list

  override def reverse: MyList[Nothing] = Nil

  override def map[S](f: Nothing => S): MyList[S] = Nil
  override def flatMap[S](f: Nothing => MyList[S]): MyList[S] = Nil
  override def filter(f: Nothing => Boolean): MyList[Nothing] = Nil
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

  override def map[S](f: T => S): MyList[S] = {
    // complexity: O(N)
    @tailrec def mapTailRec(remaining: MyList[T], result: MyList[S] = Nil): MyList[S] = {
      if (remaining.isEmpty) result
      else mapTailRec(remaining.tail, f(remaining.head) :: result)
    }
    mapTailRec(this).reverse
  }

  override def flatMap[S](f: T => MyList[S]): MyList[S] = {
    @tailrec def flattenTailRec(remaining: MyList[MyList[S]], intermediate: MyList[S], res: MyList[S]): MyList[S] = {
      if (remaining.isEmpty && intermediate.isEmpty) res
      else if (!intermediate.isEmpty) flattenTailRec(remaining, intermediate.tail, intermediate.head :: res)
      else flattenTailRec(remaining.tail, remaining.head, res)
    }
    
    @tailrec def toListsTailrec(remaining: MyList[T], acc: MyList[MyList[S]] = Nil): MyList[S] = {
      if (remaining.isEmpty) flattenTailRec(acc, Nil, Nil)
      else toListsTailrec(remaining.tail, f(remaining.head).reverse :: acc)
    }
    
    // complexity: sum of all lengths of f(x) = Z => O(N + Z)
    toListsTailrec(this)
  }

  override def filter(f: T => Boolean): MyList[T] = {
    // complexity: O(N)
    @tailrec def filterTailRec(remaining: MyList[T], acc: MyList[T] = Nil): MyList[T] = {
      if (remaining.isEmpty) acc.reverse
      else if (f(remaining.head)) filterTailRec(remaining.tail, remaining.head :: acc)
      else filterTailRec(remaining.tail, acc)
    }
    filterTailRec(this)
  }
}