package practice.lists

object Playground extends App {
  val myABCList = "A" :: "B"  :: "C" :: Nil
  val my123List = 1 :: 2 :: 3 :: Nil

  def basicOperations(): Unit = {
    val nilList = Nil
    println("# basic operations")
    println("## Nil operations")
    println(s"created: ${nilList.toString}")
    println(s"isEmpty: ${nilList.isEmpty}")

    println("## MyList operations")
    println(s"created: ${myABCList.toString}")
    println(s"isEmpty: ${myABCList.isEmpty}")
    println(s"head: ${myABCList.head}")
    println(s"tail: ${myABCList.tail}")

    println("## Append")
    val otherList = "D" :: "E"  :: "F" :: nilList
    val appended = myABCList ++ otherList
    println(s"$myABCList ++ $otherList = $appended")

    println("## Apply")
    println(s"take $appended (3) -> ${appended(3)}")

    println("## Length")
    println(s"$appended .length -> ${appended.length}")

  }
  basicOperations()

  def mapFilterFlatMapOperations(): Unit = {
    println("# map f(x)")
    println(s"$my123List -> f(_ * 2) -> ${my123List.map(_ * 2)}")
    println("# filter")
    println(s"$my123List -> (_ % 2 == 0) -> ${my123List.filter(_ % 2 == 0)}")
    println("# flatmap")
    println(s"$my123List -> f(_ -> _, _*2) -> ${my123List.flatMap(x => x :: x * 2 :: Nil)}")
  }
//  mapFilterFlatMapOperations()
}
