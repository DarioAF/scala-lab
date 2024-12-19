package practice.design

import practice.design.ParkingSpotType.ParkingSpotType
import practice.design.SpotState.SpotState

import scala.collection.mutable

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


/* Model

     ┌────────────────────────────────────────────┐
     │ParkingLotService                           │
     ├────────────────────────────────────────────┤
     │-spotsQuantity: Map[ParkingSpotType, Int]   │
 ┌───┤-spots                                      │
 │ ┌─┤-cache                                      │
 │ │ ├────────────────────────────────────────────┤
 │ │ │+CheckIn(Vehicle, ParkingSpotType, Int)     │
 │ │ │+CheckOut(Vehicle)                          │
 │ │ └────────────────────────────────────────────┘
 │ │ ┌────────────────────────────────────────────┐
 │ └─┤Cache                                       │
 │   ├────────────────────────────────────────────┤
 │   │+spotAvailability: Map[ParkingSpotType, Int]│
 │   │+parkedVehicles: Map[Vehicle.id, Vehicle]   │
 │   └────────────────────────────────────────────┘
 │   ┌────────────────────────────────────────────────┐
 └───┤ParkingLotSpots                                 │
     ├────────────────────────────────────────────────┤
   ┌─┤-spots: Map[ParkingSpotType, Map[Spot.id, Spot]]│
   │ ├────────────────────────────────────────────────┤
   │ │+Availability(ParkingSpotType): List[Spot.id]   │
   │ │+Allocate(List[Spot.id])                        │
   │ │+Deallocate(List[Spot.id])                      │
   │ └────────────────────────────────────────────────┘
   │ ┌──────────────────────────────────┐
   └─┤Spot                              │
     ├──────────────────────────────────┤
     │-id:       Int                    │
     │-state:    SpotState              │
     │+spotType: ParkingSpotType        │
     │+spotId:   String                 │
     ├──────────────────────────────────┤
     │+isOccupied: Boolean              │
     │+modifyState(action: Action): Unit│
     └──────────────────────────────────┘

  ┌─────────────────────────────────────────────────────┐
┌►│Vehicle                                              │
│ ├─────────────────────────────────────────────────────┤
│ │+id:              String                             │
│ │+type:            String                             │
│ │+spotConsumption: List[(ParkingSpotType, Int)]       │
│ │+parkedInSpots:   Map[ParkingSpotType, List[Spot.id]]│
│ └─────────────────────────────────────────────────────┘
│ ┌─────────────┐
├─┤Car          │
│ ├─────────────┤
│ │+type = "Car"│
│ └─────────────┘
│ ┌─────────────┐
├─┤Van          │
│ ├─────────────┤
│ │+type = "Van"│
│ └─────────────┘
│ ┌────────────────────┐
└─┤Motorcycle          │
  ├────────────────────┤
  │+type = "Motorcycle"│
  └────────────────────┘


 ParkingSpotType = (Compact|Regular|Large)
 SpotState       = (Free|Occupied)
 */

/* CheckIn Flow
             ┌─────────────────┐                                  ┌─────┐            ┌───────────────┐                         ┌─────┐
             │ParkingLotService│                                  │Cache│            │ParkingLotSpots│                         │Spots│
             └────────┬────────┘                                  └──┬──┘            └───────┬───────┘                         └──┬──┘
                      │                                              │                       │                                    │
                      │                                              │                       │                                    │
CheckIn(Vehicle)─────►├──────────────find Vehicle.spotConsumption    │                       │                                    │
                      │              (type, capacity) =>             │                       │                                    │
                      │                spotAvailability(type)───────►│                       │                                    │
◄──false──noCapacity──┼───────┬────────availability >= capacity◄─────┤                       │                                    │
                      │       └─true─────────────────────────────────┼───Allocate(spotIds)──►├─────foreach isOccupied(spot)──────►│
                      │                                              │◄──false:─update─┬─────┼──┬─────────────────────────────────┤
◄──false──noCapacity──┼──────────────────────────────────────────────┼─────────────────┘     │  └──true:modifyState(Spot.Occupy)─►│
                      │                                              │                       │                                    │
                      │                                              └◄──update────────┬─────┴────────────────────────────────────┘
◄──true───────────────┴────────────────────────────────────────────────────────────────┘
*/

/* CheckOut Flow
              ┌─────────────────┐      ┌───────────────┐               ┌─────┐        ┌─────┐
              │ParkingLotService│      │ParkingLotSpots│               │Spots│        │Cache│
              └────────┬────────┘      └───────┬───────┘               └──┬──┘        └──┬──┘
                       │                       │                          │              │   
                       │                       │                          │              │   
CheckOut(Vehicle)─────►├────Deallocate(_)─────►├─modifyState(Spot.Free)──►│              │   
                       │                       ├──────────────────────────┼───update────►│   
◄──────────────────────┴───────────────────────┴──────────────────────────┴──────────────┘   
 */

object ParkingSpotType extends Enumeration {
  type ParkingSpotType = Value
  val Compact: Value = Value("C")
  val Regular: Value = Value("R")
  val Large: Value = Value("L")
}

object SpotState extends Enumeration {
  type SpotState = Value
  val Free, Occupied = Value
}

trait Vehicle {
  val id: String
  val vehicleType: String
  val spotConsumption: List[(ParkingSpotType,  Int)]
  var parkedInSpots: Option[(ParkingSpotType, List[String])] = None
}

class Motorcycle(override val id: String, override val spotConsumption: List[(ParkingSpotType,  Int)]) extends Vehicle {
  val vehicleType: String = "Motorcycle"
}
object Motorcycle {
  private val consumption = List[(ParkingSpotType,  Int)](
    (ParkingSpotType.Compact, 1),(ParkingSpotType.Regular, 1),(ParkingSpotType.Large, 1)
  )
  def apply(id: String): Motorcycle = {
    new Motorcycle(id, consumption)
  }
}

class Car(override val id: String, override val spotConsumption: List[(ParkingSpotType,  Int)]) extends Vehicle {
  val vehicleType: String = "Car"
}
object Car {
  private val consumption = List[(ParkingSpotType, Int)](
    (ParkingSpotType.Regular, 1), (ParkingSpotType.Large, 1)
  )
  def apply(id: String): Car = {
    new Car(id, consumption)
  }
}

class Van(override val id: String, override val spotConsumption: List[(ParkingSpotType,  Int)]) extends Vehicle {
  val vehicleType: String = "Van"
}
object Van {
  private val consumption = List[(ParkingSpotType, Int)](
    (ParkingSpotType.Large, 1), (ParkingSpotType.Regular, 3)
  )
  def apply(id: String): Van = {
    new Van(id, consumption)
  }
}

case class Spot(private val position: Int, spotType: ParkingSpotType) {
  import Spot._

  val id: String = position.toString + spotType.toString
  private var state: SpotState = SpotState.Free

  def isOccupied: Boolean = state.eq(SpotState.Occupied)

  def modifyState(action: Action): Unit = action match {
    case Occupy if !isOccupied => state = SpotState.Occupied
    case Free if isOccupied => state = SpotState.Free

    case Occupy => throw new IllegalStateException("Spot is already occupied")
    case Free => throw new IllegalStateException("Spot is already free")
    case _ => throw new IllegalStateException("Invalid action")
  }

  override def toString: String =
    "[" + (if (isOccupied) Console.RED else Console.GREEN) + id + Console.RESET + "]"
}
object Spot {
  abstract class Action
  case object Occupy extends Action
  case object Free extends Action
}

class ParkingLotSpots(private val spots: Map[ParkingSpotType, Map[String, Spot]]) {
  import ParkingLotSpots._

  def availability(spotType: ParkingSpotType): List[String] = spots.getOrElse(spotType, List.empty).flatMap{
    case (id, spot) if !spot.isOccupied => Some(id)
    case _ => None
  }.toList

  def receive(action: Action): Boolean = {
    val targetSpots = getSpotsById(action.spotIds)
    val (invalidAction, modifyState) = action match {
      case Allocate(_) => ((s: Spot) => s.isOccupied, (spot: Spot) => spot.modifyState(Spot.Occupy) )
      case Deallocate(_) => ((s: Spot) => !s.isOccupied, (spot: Spot) => spot.modifyState(Spot.Free))
    }

    if (targetSpots.exists(invalidAction)) return false
    targetSpots.foreach(modifyState)
    action match {
      case Allocate(_) => targetSpots.forall(_.isOccupied)
      case Deallocate(_) => targetSpots.forall(!_.isOccupied)
      case _ => false
    }
  }

  def getAll: List[Spot] = spots.values.flatten.toList.map(_._2)

  private def getSpotsById(spotIds: List[String]) =
    spotIds.map(spots.values.flatten.toMap)
}
object ParkingLotSpots {
  abstract class Action { val spotIds: List[String] }
  case class Allocate(override val spotIds: List[String]) extends Action
  case class Deallocate(override val spotIds: List[String]) extends Action

  def apply(spotsQuantity: Map[ParkingSpotType, Int]): ParkingLotSpots = {
    new ParkingLotSpots(spotsQuantity.map { (spotType, quantity) =>
      spotType -> (1 to quantity).map(id => id.toString + spotType.toString -> Spot(id, spotType)).toMap
    })
  }
}

class Cache(private val spotAvailability: mutable.Map[ParkingSpotType, Int]) {
  private val parkedVehicles: mutable.Map[String, Vehicle] = mutable.Map.empty

  def incAvailability(spotType: ParkingSpotType, quantity: Int): Unit =
    spotAvailability.put(spotType, spotAvailability(spotType) + quantity)
  def decAvailability(spotType: ParkingSpotType, quantity: Int): Unit =
    spotAvailability.put(spotType, spotAvailability(spotType) - quantity)
  def getSpotAvailability(spotType: ParkingSpotType): Int = spotAvailability(spotType)

  def addVehicle(v: Vehicle): Unit = parkedVehicles.put(v.id, v)
  def removeVehicle(v: Vehicle): Unit = parkedVehicles.remove(v.id)
  def getParkedVehicles: List[String] = parkedVehicles.keys.toList
}
object Cache {
  def apply(spotsQuantity: Map[ParkingSpotType, Int]): Cache = {
    new Cache(spotsQuantity.to(mutable.Map))
  }
}

class ParkingLot(private val spotsQuantity: Map[ParkingSpotType, Int]) {
  import ParkingLotSpots._
  private val spots = ParkingLotSpots(spotsQuantity)
  private val cache = Cache(spotsQuantity)

  def CheckIn(vehicle: Vehicle): List[String] = {
    vehicle.spotConsumption.find((spotType, capacity) => cache.getSpotAvailability(spotType) >= capacity) match {
      case Some((spotType, quantity)) =>
        val availableSpotIds = spots.availability(spotType)
        if (availableSpotIds.length >= quantity) {
          val spotsToAllocate = availableSpotIds.take(quantity)
          spots.receive(Allocate(spotsToAllocate))
          vehicle.parkedInSpots = Some(spotType, spotsToAllocate)
          cache.decAvailability(spotType, spotsToAllocate.length)
          cache.addVehicle(vehicle)
          spotsToAllocate
        } else List.empty
      case _ => List.empty
    }
  }
  def CheckOut(vehicle: Vehicle): Unit = {
    spots.receive(Deallocate(vehicle.parkedInSpots.get._2))
    cache.incAvailability(vehicle.parkedInSpots.get._1, vehicle.parkedInSpots.get._2.length)
    cache.removeVehicle(vehicle)
    vehicle.parkedInSpots = None
  }

  def printCurrentState(): Unit = {
    println("== Parking Lot State ==")
    println(s"Capacity: ${spotsQuantity.values.sum}, Available: ${spots.getAll.count(!_.isOccupied)}")
    println(s"Parked vehicles: ${cache.getParkedVehicles.length} -> ${cache.getParkedVehicles.mkString(",")}")
    println("Distribution:")
    spots.getAll.foreach(s => print(s.toString))
    println("\n=======================")
  }
}

object ParkingLotApp extends App {

  val spotsQuantity: Map[ParkingSpotType, Int] = Map(
    ParkingSpotType.Compact -> 1,
    ParkingSpotType.Regular -> 5,
    ParkingSpotType.Large -> 1
  )
  val parkingLot = new ParkingLot(spotsQuantity)

  parkingLot.printCurrentState()
  
  val motorcycle1 = Motorcycle("moto1")
  val motorcycle2 = Motorcycle("moto2")
  val car1 = Car("car1")
  val car2 = Car("car2")
  val car3 = Car("car3")
  val van1 = Van("van1")
  val van2 = Van("van2")

  // Park vehicles
  println(s"Parking motorcycle1: ${parkingLot.CheckIn(motorcycle1).mkString(",")}")
  println(s"Parking motorcycle2: ${parkingLot.CheckIn(motorcycle2).mkString(",")}")
  println(s"Parking car1: ${parkingLot.CheckIn(car1).mkString(",")}")
  println(s"Parking car2: ${parkingLot.CheckIn(car2).mkString(",")}")
  println(s"Parking van1: ${parkingLot.CheckIn(van1).mkString(",")}")
  println(s"Parking car3: ${parkingLot.CheckIn(car3).mkString(",")}")

  parkingLot.printCurrentState()

  // Car 1 leaves
  parkingLot.CheckOut(car1)
  println("car1 leaves")

  // Car 3 leaves
  parkingLot.CheckOut(car3)
  println("car3 leaves")

  parkingLot.printCurrentState()

  // Van 2 parks
  println(s"Parking van2: ${parkingLot.CheckIn(van2).mkString(",")}")

  parkingLot.printCurrentState()
}
