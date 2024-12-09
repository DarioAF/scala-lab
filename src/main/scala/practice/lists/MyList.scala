package practice.lists

import scala.annotation.tailrec
import scala.util.Random

trait RND {
  val rnd = new Random(System.currentTimeMillis())
}

sealed abstract class MyList[+T] extends RND {
  def head: T
  def tail: MyList[T]
  def isEmpty: Boolean

  def ::[S >: T](elem: S): MyList[S] = new::(elem, this)
  def ++[S >: T](list: MyList[S]): MyList[S]

  def reverse: MyList[T]
  def apply(index: Int): T
  def length: Int
  def removeAt(index: Int): MyList[T]

  def map[S](f:T => S): MyList[S]
  def flatMap[S](f: T => MyList[S]): MyList[S]
  def filter(f: T => Boolean): MyList[T]

  /*
  # run-length encoding
  Count consecutive duplicates, and return them in a list of tuples
  Example: [1,1,2,3,3,3,3,3,4,4,4,5,6].rle = [(1, 2), (2, 1), (3, 5), (4, 3), (5, 1), (6, 1)]
   */
  def rle: MyList[(T, Int)]
  def duplicateEach(k: Int): MyList[T]
  def rotate(k: Int): MyList[T]
  def sample(k: Int): MyList[T]
}

case object Nil extends MyList[Nothing] {
  override def head: Nothing = throw new NoSuchElementException
  override def tail: MyList[Nothing] = throw new NoSuchElementException
  override def isEmpty: Boolean = true
  override def toString: String = "[]"

  override def ++[S >: Nothing](list: MyList[S]): MyList[S] = list

  override def reverse: MyList[Nothing] = Nil
  override def apply(index: Int): Nothing = throw new NoSuchElementException
  override def length: Int = 0
  override def removeAt(index: Int): MyList[Nothing] = Nil

  override def map[S](f: Nothing => S): MyList[S] = Nil
  override def flatMap[S](f: Nothing => MyList[S]): MyList[S] = Nil
  override def filter(f: Nothing => Boolean): MyList[Nothing] = Nil

  override def rle: MyList[(Nothing, Int)] = Nil
  override def duplicateEach(k: Int): MyList[Nothing] = Nil
  override def rotate(k: Int): MyList[Nothing] = Nil
  override def sample(k: Int): MyList[Nothing] = Nil
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

  override def apply(index: Int): T = {
    // complexity: O(min(N, index))
    @tailrec def iteratorTailRec(remaining: MyList[T], acc: Int): T = {
      if (index == acc) remaining.head
      else iteratorTailRec(remaining.tail, acc + 1)
    }

    if (index < 0) throw new NoSuchElementException
    else iteratorTailRec(this, 0)
  }

  override def length: Int = {
    // complexity: O(N)
    @tailrec def iteratorTailRec(remaining: MyList[T], acc: Int): Int = {
      if (remaining.isEmpty) acc
      else iteratorTailRec(remaining.tail, acc + 1)
    }

    iteratorTailRec(this, 0)
  }

  override def removeAt(index: Int): MyList[T] = {
    // complexity: O(N)
    @tailrec def removeTailRec(remaining: MyList[T], pos: Int = 0, acc: MyList[T] = Nil): MyList[T] = {
      if (remaining.isEmpty) acc
      else if (index == pos) acc.reverse ++ remaining.tail
      else removeTailRec(remaining.tail, pos + 1, remaining.head :: acc)
    }
    if (index < 0) this
    else removeTailRec(this)
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

  override def rle: MyList[(T, Int)] = {
    // complexity: O(N)
    def rleTailRec(remaining: MyList[T], currentTuple: (T, Int), acc: MyList[(T, Int)]): MyList[(T, Int)] = {
      if (remaining.isEmpty && currentTuple._2 == 0) acc
      else if (remaining.isEmpty) currentTuple :: acc
      else if (remaining.head == currentTuple._1) rleTailRec(remaining.tail, currentTuple.copy(_2 = currentTuple._2 + 1), acc)
      else rleTailRec(remaining.tail, (remaining.head, 1), currentTuple :: acc)
    }
    rleTailRec(this.tail, (this.head, 1), Nil).reverse
  }

  override def duplicateEach(k: Int): MyList[T] = {
    // complexity: O(N * K)
    @tailrec def recAux(remaining: MyList[T], dtimes: Int, result: MyList[T]): MyList[T] = {
      if (remaining.isEmpty && dtimes <= 1) result
      else if (dtimes > 1) recAux(remaining, dtimes - 1, result.head :: result)
      else recAux(remaining.tail, k, remaining.head :: result)
    }

    recAux(this.tail, k, this.head :: Nil).reverse
  }

  override def rotate(k: Int): MyList[T] = {
    @tailrec def tailrec(remaining: MyList[T], i: Int, rlist: MyList[T]): MyList[T] = {
      if (remaining.isEmpty && i == 0) this
      else if (remaining.isEmpty) tailrec(this, i, Nil)
      else if (i == 0) remaining ++ rlist.reverse
      else tailrec(remaining.tail, i - 1, remaining.head :: rlist)
    }
    assert(k > 0)
    tailrec(this, k, Nil)
  }

  override def sample(k: Int): MyList[T] = {
    @tailrec def tailrec(remaining: MyList[T], n: Int): T = {
      if (n == 0) remaining.head
      else tailrec(remaining.tail, n - 1)
    }

    val l = this.length
    (1 to k).map(_ => tailrec(this, rnd.nextInt(l)))
      .foldLeft[MyList[T]](Nil)((acc, n) => n :: acc)
  }
}

object MyList {
  def from[T](iterable: Iterable[T]): MyList[T] = {
    @tailrec def convertToRListRec(remaining: Iterable[T], acc: MyList[T]): MyList[T] = {
      if (remaining.isEmpty) acc
      else convertToRListRec(remaining.tail, remaining.head :: acc)
    }
    convertToRListRec(iterable, Nil).reverse
  }
}