package practice.trees

import scala.annotation.tailrec

sealed abstract class BTree[+T] {
  def value: T
  def left: BTree[T]
  def right: BTree[T]
  def isEmpty: Boolean

  def isLeaf: Boolean
  def leafCount: Int
  def size: Int

  def collectLeaves: List[BTree[T]]
}

case object BEnd extends BTree[Nothing] {
  override def value: Nothing = throw new NoSuchElementException
  override def left: BTree[Nothing] = throw new NoSuchElementException
  override def right: BTree[Nothing] = throw new NoSuchElementException
  override def isEmpty: Boolean = true

  override def isLeaf: Boolean = false
  override def leafCount: Int = 0
  override val size: Int = 0

  override def collectLeaves: List[BTree[Nothing]] = List()
}

case class BNode[+T](override val value: T, override val left: BTree[T], override val right: BTree[T]) extends BTree[T] {
  override def isEmpty: Boolean = false

  override def isLeaf: Boolean = left.isEmpty && right.isEmpty
  override def leafCount: Int = collectLeaves.length
  override val size: Int = 1 + this.left.size + this.right.size
  
  override def collectLeaves: List[BTree[T]] = {
    @tailrec def tailRec(todo: List[BTree[T]], acc: List[BTree[T]]): List[BTree[T]] = {
      if (todo.isEmpty) acc
      else if (todo.head.isEmpty) tailRec(todo.tail, acc)
      else if (todo.head.isLeaf) tailRec(todo.tail, todo.head :: acc)
      else {
        val node = todo.head
        tailRec(node.left :: node.right :: todo.tail, acc)
      }
    }

    tailRec(List(this), List.empty)
  }
}