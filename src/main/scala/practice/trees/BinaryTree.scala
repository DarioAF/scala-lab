package practice.trees

import scala.annotation.tailrec
import scala.collection.immutable.Queue

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
  def sameShapeAs[S >: T](that: BTree[S]): Boolean
  def isSymmetrical: Boolean
  def toList(order: BTree.ListOrder): List[T]
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
  override def sameShapeAs[S >: Nothing](that: BTree[S]): Boolean = that.isEmpty
  override def isSymmetrical: Boolean = true
  override def toList(order: BTree.ListOrder): List[Nothing] = List.empty
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

  override def sameShapeAs[S >: T](that: BTree[S]): Boolean = {
    @tailrec def tailrec(a: List[BTree[S]], b: List[BTree[S]]): Boolean = {
      if (a.isEmpty && b.isEmpty) true
      else if ((a.isEmpty && b.nonEmpty) || (b.isEmpty && a.nonEmpty)) false
      else if ((a.head.isEmpty && !b.head.isEmpty) || (b.head.isEmpty && !a.head.isEmpty)) false
      else if (a.head.isEmpty && b.head.isEmpty) tailrec(a.tail, b.tail)
      else tailrec(a.head.right :: a.head.left :: a.tail, b.head.right :: b.head.left :: b.tail)
    }

    tailrec(List(this), List(that))
  }

  override def isSymmetrical: Boolean = this.sameShapeAs(this.mirror)

  override def toList(order: BTree.ListOrder): List[T] = order match {
      case BTree.ListOrder.PreOrder => preOrder(List(this))
      case BTree.ListOrder.InOrder => inOrder(List(this))
      case BTree.ListOrder.PostOrder => postOrder(List(this))
      case BTree.ListOrder.PerLevel => perLevel(List(this))
  }

  @tailrec private def preOrder[S >: T](todo: List[BTree[S]], res: List[S] = List.empty): List[S] = {
    if (todo.isEmpty) res.reverse
    else if (todo.head.isEmpty) preOrder(todo.tail, res)
    else preOrder(todo.head.left :: todo.head.right :: todo.tail, todo.head.value :: res)
  }
  @tailrec private def inOrder[S >: T](todo: List[BTree[S]], visited: Set[BTree[S]] = Set.empty,
                                       acc: Queue[S] = Queue.empty): List[S] = {
    if (todo.isEmpty) acc.toList
    else {
      val node = todo.head
      if (node.isEmpty) inOrder(todo.tail, visited, acc)
      else if (node.isLeaf || visited.contains(node)) inOrder(todo.tail, visited, acc :+ node.value)
      else inOrder(node.left :: node :: node.right :: todo.tail, visited + node, acc)
    }
  }
  @tailrec private def postOrder[S >: T](todo: List[BTree[S]], visited: Set[BTree[S]] = Set.empty,
                                 res: Queue[S] = Queue.empty): List[S] = {
    if (todo.isEmpty) res.toList
    else if (todo.head.isEmpty) postOrder(todo.tail, visited, res)
    else if (todo.head.isLeaf || visited.contains(todo.head)) postOrder(todo.tail, visited, res :+ todo.head.value)
    else postOrder(todo.head.left :: todo.head.right :: todo.head :: todo.tail, visited + todo.head, res)
  }
  @tailrec private def perLevel[S >: T](todo: List[BTree[S]], res: List[S] = List.empty): List[S] = {
    if (todo.isEmpty) res
    else {
      val nextLevel = todo.flatMap(n => List(n.left, n.right)).filter(!_.isEmpty)
      val levelValues = todo.map(_.value)
      perLevel(nextLevel, res ++ levelValues)
    }
  }

}

object BTree {
  enum ListOrder {
    case PreOrder, InOrder, PostOrder, PerLevel
  }
}