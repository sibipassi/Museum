package museumvisit

import java.lang.UnsupportedOperationException
import kotlin.concurrent.withLock

class Museum (val name: String, val entrance: MuseumRoom){
    private var admitted: Int = 0

    //implement museum sites
    val outside:MuseumRoom = MuseumRoom("Outside", 100)
    private var rooms: Set<MuseumRoom> = setOf(entrance)

    class Turnstile(val r1: MuseumRoom, val r2: MuseumRoom)
    var turnstiles: Set<Turnstile> = setOf()

    fun entranceHasCapacity() = entrance.hasCapacity()
    fun enter(){
        if (entranceHasCapacity()) {
            admitted += 1
            entrance.enter()
        } else {
            throw UnsupportedOperationException()
        }
    }
    fun addRoom(room: MuseumRoom){
        if (room in rooms) {
            throw IllegalArgumentException()
        } else {
            rooms = rooms.plus(room)
        }
    }

    //order rooms for turnstiles
    fun connectRoomTo(roomOne: MuseumRoom, roomTwo: MuseumRoom){
        val greater = roomTwo
        val smaller = roomOne
        if (roomOne.name >= roomTwo.name) {
            val greater = roomOne
            val smaller = roomTwo
        }
        if (rooms.containsAll(listOf(roomOne, roomTwo)) &&
            Turnstile(smaller, greater) !in turnstiles){
            turnstiles = turnstiles.plus(Turnstile(smaller, greater))
        } else {
            throw IllegalArgumentException()
        }
    }
    fun connectRoomToExit(room: MuseumRoom){
        if (room in rooms && Turnstile(room, outside) !in turnstiles){
            turnstiles = turnstiles.plus(Turnstile(room, outside))
        } else {
            throw IllegalArgumentException()
        }
    }

    fun checkWellFormed(){
        val connectedRooms: MutableSet<MuseumRoom> = mutableSetOf()
        for (t in turnstiles) {
            connectedRooms.addAll(setOf(t.r1, t.r2))
        }
        val unreachableRooms = rooms - connectedRooms
        if (unreachableRooms.isNotEmpty()) {
            throw UnreachableRoomsException(unreachableRooms)
        }
        val connectedToExit: MutableSet<MuseumRoom> = mutableSetOf()
        for(t in turnstiles) {
            if (t.r2 == outside){
                connectedToExit.add(t.r1)
            }
        }
        val cannotExit = rooms - connectedToExit
        if (cannotExit.isNotEmpty()) {
            throw CannotExitMuseumException(unreachableRooms)
        }
        return
    }
    override fun toString(): String {
        val str: StringBuilder = StringBuilder()
        str.append(this.name, "\n")
        for (room in rooms) {
            var connectedRooms: List<MuseumRoom> = turnstiles
                .filter { it.r1 == room }
                .map { it.r2 }
            str.append("Room ${room.name} leads to: ", connectedRooms)
        }
        return str.toString()
    }

    fun enterIfPossible(): MuseumRoom? {
        entrance.lock.withLock {
            if (entranceHasCapacity()){
                enter()
                return entrance
            }
            return null
        }
    }

    fun passTurnstile(t: Turnstile): MuseumRoom {
        t.r1.lock.lock()
        t.r2.lock.lock()
        if (t.r2.hasCapacity()) {
            t.r1.exit()
            t.r2.enter()
            t.r1.lock.unlock()
            t.r2.lock.unlock()
            return t.r2
        }
        t.r1.lock.unlock()
        t.r2.lock.unlock()
        return t.r1
    }
}

