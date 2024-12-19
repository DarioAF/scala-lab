package practice.design

/**
 * Goals: Design a parking lot using object-oriented principles
 *
 * Here are a few methods that you should be able to run:
 *
 * Tell us how many spots are remaining
 * Tell us how many total spots are in the parking lot
 * Tell us when the parking lot is full
 * Tell us when the parking lot is empty
 * Tell us when certain spots are full e.g. when all motorcycle spots are taken
 * Tell us how many spots vans are taking up
 *
 * Assumptions:
 *
 * The parking lot can hold motorcycles, cars and vans
 * The parking lot has compact spots, regular spots and large spots
 * A motorcycle can park in any spot
 * A car can park in a regular spot, or in a large spot
 * A van can park in a single large spot or take up 3 regular spots
 * These are just a few assumptions. Feel free to ask your interviewer about more assumptions as needed
 */

// Enum to represent spot types
object ParkingSpotType extends Enumeration {
  type ParkingSpotType = Value
  val Compact: Value = Value("C")
  val Regular: Value = Value("R")
  val Large: Value = Value("L")
}

// Vehicle base trait
trait Vehicle {
  var parkedInSpots: List[String] = List.empty
  val vehicleType: String
}

// Specific vehicle types
class Motorcycle extends Vehicle {
  val vehicleType: String = "Motorcycle"
}

class Car extends Vehicle {
  val vehicleType: String = "Car"
}

class Van extends Vehicle {
  val vehicleType: String = "Van"
}

// A parking spot class
case class Spot(id: Int, spotType: ParkingSpotType.ParkingSpotType) {
  import Spot._

  private var occupied: Boolean = false

  val spotId: String = id.toString + spotType.toString
  def isOccupied: Boolean = occupied

  def modifyState(action: Action): Unit = action match {
    case Occupy if !isOccupied => occupied = true
    case Free if isOccupied => occupied = false

    case Occupy => throw new IllegalStateException("Spot is already occupied")
    case Free => throw new IllegalStateException("Spot is already free")
    case _ => throw new IllegalStateException("Invalid action")
  }

  override def toString: String =
    "[" + (if (isOccupied) Console.RED else Console.GREEN) + spotId + Console.RESET + "]"

}
object Spot {
  abstract class Action
  case object Occupy extends Action
  case object Free extends Action
}

// Parking lot class
class ParkingLot(val compactSpots: Int, val regularSpots: Int, val largeSpots: Int) {
  import Spot._
  import ParkingSpotType._

  val totalSpots: Int = compactSpots + regularSpots + largeSpots

  private val compactSpotsList = (1 to compactSpots).map(id => id.toString + Compact.toString -> Spot(id, Compact)).toMap
  private val regularSpotsList = (1 to regularSpots).map(id => id.toString + Regular.toString -> Spot(id, Regular)).toMap
  private val largeSpotsList = (1 to largeSpots).map(id => id.toString + Large.toString -> Spot(id, Large)).toMap
  val spots: List[Spot] = compactSpotsList.values.toList ++ regularSpotsList.values.toList ++ largeSpotsList.values.toList

  // Helper function to get all available spots of a particular type
  private def availableSpots(spotType: ParkingSpotType.ParkingSpotType): Int = {
    spots.count(s => s.spotType == spotType && !s.isOccupied)
  }

  // Park a vehicle
  def park(vehicle: Vehicle): List[String] = vehicle match {
    case _: Motorcycle =>
      compactSpotsList.find((_, s) => !s.isOccupied) match {
        case Some(spotId, spot) =>
          spot.modifyState(Occupy)
          vehicle.parkedInSpots = List(spotId)
          vehicle.parkedInSpots
        case None => regularSpotsList.find((_, s) => !s.isOccupied) match {
          case Some(spotId, spot) =>
            spot.modifyState(Occupy)
            vehicle.parkedInSpots = List(spotId)
            vehicle.parkedInSpots
          case None => largeSpotsList.find((_, s) => !s.isOccupied) match {
            case Some(spotId, spot) =>
              spot.modifyState(Occupy)
              vehicle.parkedInSpots = List(spotId)
              vehicle.parkedInSpots
            case None => List.empty
          }
        }
      }

    case _: Car =>
      regularSpotsList.find((_, s) => !s.isOccupied) match {
        case Some(spotId, spot) =>
          spot.modifyState(Occupy)
          vehicle.parkedInSpots = List(spotId)
          vehicle.parkedInSpots
        case None => largeSpotsList.find((_, s) => !s.isOccupied) match {
          case Some(spotId, spot) =>
            spot.modifyState(Occupy)
            vehicle.parkedInSpots = List(spotId)
            vehicle.parkedInSpots
          case None => List.empty
        }
      }

    case _: Van =>
      largeSpotsList.find((_, s) => !s.isOccupied) match {
        case Some(spotId, spot) =>
          spot.modifyState(Occupy)
          vehicle.parkedInSpots = List(spotId)
          vehicle.parkedInSpots
        case None =>
          val consecutiveRegularSpots = regularSpotsList.filter((_, s) => !s.isOccupied).take(3)
          if (consecutiveRegularSpots.size == 3) {
            consecutiveRegularSpots.foreach((_, s) => s.modifyState(Occupy))
            vehicle.parkedInSpots = consecutiveRegularSpots.map((_, s) => s.spotId).toList
            vehicle.parkedInSpots
          } else List.empty
      }
  }

  // Free a spot based on the vehicle
  def freeSpot(vehicle: Vehicle): Unit = vehicle match {
    case _: Motorcycle =>
      vehicle.parkedInSpots.foreach(id => compactSpotsList.get(id).foreach(_.modifyState(Free)))
      vehicle.parkedInSpots = List.empty

    case _: Car =>
      vehicle.parkedInSpots.foreach { id =>
        regularSpotsList.get(id) match {
          case Some(s) => s.modifyState(Free)
          case None => largeSpotsList.get(id).foreach(_.modifyState(Free))
        }
      }
      vehicle.parkedInSpots = List.empty

    case _: Van =>
      if (vehicle.parkedInSpots.size == 1) vehicle.parkedInSpots.foreach(id => largeSpotsList.get(id).foreach(_.modifyState(Free)))
      else vehicle.parkedInSpots.foreach(id => regularSpotsList.get(id).foreach(_.modifyState(Free)))
      vehicle.parkedInSpots = List.empty
  }

  // Query the number of remaining spots
  def remainingSpots: Int = spots.count(s => !s.isOccupied)

  // Query if the parking lot is full
  def isFull: Boolean = remainingSpots == 0

  // Query if the parking lot is empty
  def isEmpty: Boolean = remainingSpots == totalSpots

}
object ParkingLot {
  implicit class Printable(parkingLot: ParkingLot) {
    def printCurrentState(): Unit = {
      println("== Parking Lot State ==")
      println(s"Capacity: ${parkingLot.totalSpots}, Available: ${parkingLot.remainingSpots}")
      println("Distribution:")
      parkingLot.spots.foreach(s => print(s.toString))
      println("\n=======================")
    }
  }
}

// Driver code to demonstrate usage
object ParkingLotApp extends App {
  import ParkingLot._
  // Create a parking lot with 10 motorcycle spots, 5 regular spots, and 2 large spots
  val compactSpots = 1
  val regularSpots = 5
  val largeSpots = 1
  val parkingLot = new ParkingLot(compactSpots, regularSpots, largeSpots)

  parkingLot.printCurrentState()

  val motorcycle1 = new Motorcycle
  val motorcycle2 = new Motorcycle
  val car1 = new Car
  val car2 = new Car
  val car3 = new Car
  val van1 = new Van
  val van2 = new Van

  // Park vehicles
  println(s"Parking motorcycle1: ${parkingLot.park(motorcycle1).mkString(",")}")
  println(s"Parking motorcycle2: ${parkingLot.park(motorcycle2).mkString(",")}")
  println(s"Parking car1: ${parkingLot.park(car1).mkString(",")}")
  println(s"Parking car2: ${parkingLot.park(car2).mkString(",")}")
  println(s"Parking van1: ${parkingLot.park(van1).mkString(",")}")
  println(s"Parking car3: ${parkingLot.park(car3).mkString(",")}")

  parkingLot.printCurrentState()

  // Car 1 leaves
  parkingLot.freeSpot(car1)
  println("car1 leaves")

  // Car 3 leaves
  parkingLot.freeSpot(car3)
  println("car3 leaves")

  parkingLot.printCurrentState()

  // Van 2 parks
  println(s"Parking van2: ${parkingLot.park(van2).mkString(",")}")

  parkingLot.printCurrentState()

  // Check remaining spots and parking lot status
  println(s"Remaining spots: ${parkingLot.remainingSpots}")
  println(s"Is the lot full? ${parkingLot.isFull}")
  println(s"Is the lot empty? ${parkingLot.isEmpty}")
}
