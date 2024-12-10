package practice.trees

object Playground extends App {
  val btreeStr ="""
      |       ______1_______
      |      /              \
      |   __2__            __6__
      |  /     \          /     \
      | 3       4        7       8
      |          \
      |           5
      |""".stripMargin
  val btree = BNode(1,
    BNode(2,
      BNode(3, BEnd, BEnd),
      BNode(4,
        BEnd,
        BNode(5, BEnd, BEnd),
      )
    ),
    BNode(6,
      BNode(7, BEnd, BEnd),
      BNode(8, BEnd, BEnd)
    )
  )

  def basicOperations(): Unit = {
    println("# basic operations")
    println(btreeStr)
    println(s"size: ${btree.size}")
    println(s"leaves: ${btree.collectLeaves}")
    ((lvl: Int) => println(s"nodes (lvl $lvl): ${btree.collectNodes(lvl)}"))(2)
  }
//  basicOperations()

  def mirror(): Unit = {
    println("# mirror")
    println(btreeStr)
    println(btree.mirror.toString)
  }
  mirror()
}
