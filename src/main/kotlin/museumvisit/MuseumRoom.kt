package museumvisit

import java.lang.UnsupportedOperationException
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class MuseumRoom(val name: String, val capacity: Int) {
    init {
        if (capacity < 1) {
            throw IllegalArgumentException()
        }
    }

    val lock: Lock = ReentrantLock()

    var occupancy = 0
        private set

    fun hasCapacity() = (occupancy < capacity)

    fun enter() {
        if (hasCapacity()) {
            occupancy += 1
        } else {
            throw UnsupportedOperationException()
        }
    }
    fun exit() {
        if (occupancy == 0){
            throw UnsupportedOperationException()
        } else {
            occupancy -= 1
        }
    }

    override fun toString(): String {
        return name
    }
}