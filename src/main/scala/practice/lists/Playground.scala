package practice.lists

object Playground extends App {
  def basicOperations(): Unit = {
    val nilList = Nil
    println("# basic operations")
    println("## Nil operations")
    println(s"created: ${nilList.toString}")
    println(s"isEmpty: ${nilList.isEmpty}")

    val myList = "A" :: "B"  :: "C" :: nilList
    println("## MyList operations")
    println(s"created: ${myList.toString}")
    println(s"isEmpty: ${myList.isEmpty}")
    println(s"head: ${myList.head}")
    println(s"tail: ${myList.tail}")

    println("## Append")
    val otherList = "D" :: "E"  :: "F" :: nilList
    val appended = myList ++ otherList
    println(s"$myList ++ $otherList = $appended")
  }
  basicOperations()
}
