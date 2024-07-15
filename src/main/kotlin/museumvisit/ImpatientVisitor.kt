package museumvisit

import java.io.PrintStream

class ImpatientVisitor (
    val name: String,
    val printStream: PrintStream,
    val museum: Museum
): Runnable {

    override fun run() {
        var entered = museum.enterIfPossible()
        while (entered == null){
            printStream.println("$name could not get into " +
                    "${museum.name} but will try again soon.")
            Thread.sleep(10)
            printStream.println("$name is ready to try again.")
            entered = museum.enterIfPossible()
        }
        printStream.println("$name has entered ${museum.name}")

        var room = museum.entrance
        printStream.println("$name has entered ${room.name}")
        while (room != museum.outside){
            Thread.sleep((1..200).shuffled().first().toLong())
            printStream.println("$name wants to leave ${room.name}")
            val temp = room
            room = museum.passTurnstile(
                museum
                .turnstiles
                .filter { it.r1 == room }
                .shuffled()
                .first()
            )
            if (temp == room) {
                printStream.println("$name failed to leave " +
                        "${room.name} but will try again soon.")
            } else {
                printStream.println("$name has left ${temp.name}")
                printStream.println("$name has entered ${room.name}")
            }
        }
        printStream.println("$name has left ${museum.name}")
    }
}


fun main(){
    val arr: MutableList<Int> = mutableListOf(0, 1, 2, 3)
    println(arr.subList(0, 3).sum())
}