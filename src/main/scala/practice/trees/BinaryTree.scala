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
  def collectNodes(level: Int): List[BTree[T]]

  def mirror: BTree[T]
}

case object BEnd extends BTree[Nothing] {
  override def value: Nothing = throw new NoSuchElementException
  override def left: BTree[Nothing] = throw new NoSuchElementException
  override def right: BTree[Nothing] = throw new NoSuchElementException
  override def isEmpty: Boolean = true

  override def isLeaf: Boolean = false
  override def leafCount: Int = 0
  override val size: Int = 0

  override def collectLeaves: List[BTree[Nothing]] = List.empty
  override def collectNodes(level: Int): List[BTree[Nothing]] = List.empty

  override def mirror: BTree[Nothing] = this
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

  override def collectNodes(level: Int): List[BTree[T]] = {
    @tailrec def tailrec(currentLevel: Int, nodes: List[BTree[T]]): List[BTree[T]] = {
      if (nodes.isEmpty) List.empty
      else if (currentLevel == level) nodes
      else {
        val expandedNodes = for {
          node <- nodes
          child <- List(node.left, node.right) if !child.isEmpty
        } yield child

        tailrec(currentLevel + 1, expandedNodes)
      }
    }

    if (level < 0) List.empty
    else tailrec(0, List(this))
  }

  override def mirror: BTree[T] = {
    @tailrec
    def tailrec(todo: List[BTree[T]], expanded: Set[BTree[T]], done: List[BTree[T]]): BTree[T] = {
      if (todo.isEmpty) done.head
      else if (todo.head.isEmpty || todo.head.isLeaf) tailrec(todo.tail, expanded, todo.head :: done)
      else if (!expanded.contains(todo.head)) tailrec(todo.head.left :: todo.head.right :: todo, expanded + todo.head, done)
      else {
        val newLeft = done.head
        val newRight = done.tail.head
        val newNode = BNode(todo.head.value, newLeft, newRight)
        tailrec(todo.tail, expanded, newNode :: done.drop(2))
      }
    }

    tailrec(List(this), Set.empty, List.empty)
  }
}