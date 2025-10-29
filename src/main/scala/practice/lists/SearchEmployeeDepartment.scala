package practice.lists

import scala.annotation.tailrec

object SearchEmployeeDepartment extends App {
  case class Employee(id: Int,
                      name: String,
                      department: Option[String],
                      managerId: Option[Int]
                     )

  val employees = List(
    Employee(101, "Alice", Some("HR"), None),
    Employee(102, "Bob", None, Some(101)),
    Employee(103, "Charlie", Some("IT"), None),
    Employee(104, "David", None, Some(103)),
    Employee(105, "Eve", None, None),
    Employee(106, "Frank", None, Some(107)),
    Employee(107, "Grace", None, Some(106)),
  )

  def get_final_department(id: Int): Option[String] = {
    @tailrec
    def loop(rem: List[Employee], query_id: Int, visited_ids: List[Int] = List.empty): Option[String] = {
      if (rem.isEmpty) None
      else {
        rem.find(e => e.id == query_id) match
          case Some(employee) =>
            if (employee.department.isDefined) {
              employee.department
            } else {
              employee.managerId match
                case Some(managerId) if !visited_ids.contains(managerId) =>
                  loop(rem, managerId, visited_ids :+ query_id)
                case _ => None
            }
          case _ => None
      }
    }

    loop(employees, id)
  }

  Seq(
    (104, Some("IT")),
    (101, Some("HR")),
    (102, Some("HR")),
    (105, None),
    (107, None),
  ).foreach { (input, expected) =>
    val actual = get_final_department(input)
    if (actual == expected) {
      println(Console.GREEN + s"$input -> $actual ✅"+ Console.RESET)
    } else {
      println(Console.RED + s"$input -> expected: $expected got: $actual ❌"+ Console.RESET)
    }
  }
}
