package practice.arrays

import scala.annotation.tailrec

/** 2257. Count Unguarded Cells in the Grid
 * https://leetcode.com/problems/count-unguarded-cells-in-the-grid
 *
 * You are given two integers m and n representing a 0-indexed m x n grid. You are also given two 2D integer arrays guards and walls where guards[i] = [rowi, coli] and walls[j] = [rowj, colj] represent the positions of the ith guard and jth wall respectively.
 * A guard can see every cell in the four cardinal directions (north, east, south, or west) starting from their position unless obstructed by a wall or another guard. A cell is guarded if there is at least one guard that can see it.
 * Return the number of unoccupied cells that are not guarded.
 */
object CountUnguardedCellsInGrid extends App {

  @tailrec
  private def scan(row: Int, col: Int, deltaRow: Int, deltaCol: Int, accumulatedGuarded: Set[(Int, Int)])
                  (implicit m: Int, n: Int, occupied: Set[(Int, Int)]): Set[(Int, Int)] = {
    val nextRow = row + deltaRow
    val nextCol = col + deltaCol
    if (nextRow < 0 || nextRow >= m || nextCol < 0 || nextCol >= n || occupied.contains((nextRow, nextCol))) accumulatedGuarded
    else scan(nextRow, nextCol, deltaRow, deltaCol, accumulatedGuarded + ((nextRow, nextCol)))(m, n, occupied)
  }

  private def getGuardedPositions(guards: Array[Array[Int]])
                                 (implicit m: Int, n: Int, occupied: Set[(Int, Int)]): Set[(Int, Int)] = {
    val directions = Seq((-1, 0), (1, 0), (0, -1), (0, 1))
    guards.foldLeft(Set.empty[(Int, Int)]) { (accumulator, currentGuard) =>
      val row = currentGuard(0)
      val col = currentGuard(1)
      directions.foldLeft(accumulator) { (innerAccumulator, direction) =>
        val (deltaRow, deltaCol) = direction
        scan(row, col, deltaRow, deltaCol, innerAccumulator)(m, n, occupied)
      }
    }
  }

  private def getOccupiedPositions(guards: Array[Array[Int]], walls: Array[Array[Int]]) =
    (guards ++ walls).map(position => (position(0), position(1))).toSet

  def countUnguarded(m: Int, n: Int, guards: Array[Array[Int]], walls: Array[Array[Int]]): Int = {
    val occupied = getOccupiedPositions(guards, walls)
    val total = m * n
    total - occupied.size - getGuardedPositions(guards)(m, n, occupied).size
  }


  Seq(
    Scenario(m = 4, n = 6,
      guards = Array(Array(0,0),Array(1,1),Array(2,3)),
      walls = Array(Array(0,1),Array(2,2),Array(1,4)),
      expectedOutput = 7),
    Scenario(m = 3, n = 3,
      guards = Array(Array(1,1)),
      walls = Array(Array(0,1),Array(1,0),Array(2,1),Array(1,2)),
      expectedOutput = 4),
  ).foreach { scenario =>
    val output = countUnguarded(scenario.m, scenario.n, scenario.guards, scenario.walls)
    if (output == scenario.expectedOutput) {
      println(Console.GREEN + "[OK] " + Console.RESET + s"Got $output Unguarded cells in grid")
    } else {
      println(Console.RED + "[ERROR] " + Console.RESET + s"Got $output Unguarded cells in grid, expected ${scenario.expectedOutput}")
    }
    val occupied = getOccupiedPositions(scenario.guards, scenario.walls)
    val guarded = getGuardedPositions(scenario.guards)(scenario.m, scenario.n, occupied)
    for (row <- 0 until scenario.m) {
      for (col <- 0 until scenario.n) {
        val pos = (row, col)
        if (scenario.guards.exists(g => g(0) == row && g(1) == col)) print("G")
        else if (scenario.walls.exists(w => w(0) == row && w(1) == col)) print("W")
        else if (guarded.contains(pos)) print(Console.RED + "1" + Console.RESET)
        else print(Console.GREEN + "0" + Console.RESET)
        if (col < scenario.n - 1) print(", ")
      }
      println()
    }
  }
}

case class Scenario(m: Int, n: Int, guards: Array[Array[Int]], walls: Array[Array[Int]], expectedOutput: Int)

