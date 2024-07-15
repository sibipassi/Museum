package museumvisit

class UnreachableRoomsException(val rooms: Set<MuseumRoom>): Exception() {

    override fun toString(): String {
        return "Unreachable rooms: ".plus(sortAndJoin(rooms))
    }
}

class CannotExitMuseumException(val rooms: Set<MuseumRoom>): Exception() {

    override fun toString(): String {
        return "Impossible to leave museum from: ".plus(sortAndJoin(rooms))
    }
}

fun sortAndJoin(rooms: Set<MuseumRoom>): String = rooms.map{it.toString()}.sorted().joinToString { ", " }

