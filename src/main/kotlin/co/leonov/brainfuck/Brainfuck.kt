package co.leonov.brainfuck

import java.util.*

data class Program(
        val instructionPointer: Int,
        val commands: String,
        val loopStack: IntArray,
        val dataPointer: Int,
        val data: ByteArray
)

fun increaseByte(b: Byte) = if (b == Byte.MAX_VALUE) Byte.MIN_VALUE else b.inc()
fun decreaseByte(b: Byte) = if (b == Byte.MIN_VALUE) Byte.MAX_VALUE else b.dec()

fun increaseWrappingPointer(currentIndex: Int, length: Int) = if (currentIndex + 1 >= length) 0 else currentIndex + 1
fun decreaseWrappingPointer(currentIndex: Int, length: Int) = if (currentIndex - 1 < 0) length - 1 else currentIndex - 1

fun incrementDataPointerCommand() = State<Unit, Program>  {
    Pair(Unit, Program(it.instructionPointer + 1, it.commands, it.loopStack, increaseWrappingPointer(it.dataPointer, it.data.size), it.data) )
}

fun decrementDataPointerCommand() = State<Unit, Program>  {
    Pair(Unit, Program(it.instructionPointer + 1, it.commands, it.loopStack, decreaseWrappingPointer(it.dataPointer, it.data.size), it.data) )
}

fun incrementByteCommand() = State<Unit, Program>  {
    val modifiedData = it.data
    modifiedData[it.dataPointer] = increaseByte(it.data[it.dataPointer])
    Pair(Unit, Program(it.instructionPointer + 1, it.commands, it.loopStack, it.dataPointer, modifiedData))
}

fun decrementByteCommand() = State<Unit, Program>  {
    val modifiedData = it.data
    modifiedData[it.dataPointer] = decreaseByte(it.data[it.dataPointer])
    Pair(Unit, Program(it.instructionPointer + 1, it.commands, it.loopStack, it.dataPointer, modifiedData))
}

fun outputByteCommand() = State<Unit, Program> {
    print(it.data[it.dataPointer].toChar())
    Pair(Unit, Program(it.instructionPointer + 1, it.commands, it.loopStack, it.dataPointer, it.data))
}

fun inputByteCommand(b: Byte) = State<Unit, Program> {
    val modifiedData = it.data
    modifiedData[it.dataPointer] = b
    Pair(Unit, Program(it.instructionPointer + 1, it.commands, it.loopStack, it.dataPointer, modifiedData))
}


tailrec fun advanceToMatchingBracket(pointer: Int, bracketsToMatch: Int, commands: String): Int =
        if (bracketsToMatch == 0) pointer-1 else
            when (commands[pointer]) {
                '[' -> advanceToMatchingBracket(pointer+1, bracketsToMatch+1, commands)
                ']' -> advanceToMatchingBracket(pointer+1, bracketsToMatch-1, commands)
                else -> advanceToMatchingBracket(pointer+1, bracketsToMatch, commands)
            }

fun skipCommand() = State<Unit, Program> {
    Pair(Unit, Program(it.instructionPointer + 1, it.commands, it.loopStack, it.dataPointer, it.data))
}

fun startLoopCommand() = State<Unit, Program> {
    val instructionPointer = if (it.data[it.dataPointer] == 0.toByte()) { advanceToMatchingBracket(it.instructionPointer+1, 1, it.commands) } else { it.instructionPointer + 1 }
    Pair(Unit, Program(instructionPointer, it.commands, it.loopStack.plus(it.instructionPointer), it.dataPointer, it.data))
}

fun endLoopCommand() = State<Unit, Program> {
    val instructionPointer = if (it.data[it.dataPointer] != 0.toByte()) { it.loopStack.last() } else { it.instructionPointer + 1 }
    Pair(Unit, Program(instructionPointer, it.commands, it.loopStack.dropLast(1).toIntArray(), it.dataPointer, it.data))
}

fun command(c: Char): State<Unit, Program> = when(c) {
    '>' -> incrementDataPointerCommand()
    '<' -> decrementDataPointerCommand()
    '+' -> incrementByteCommand()
    '-' -> decrementByteCommand()
    '.' -> outputByteCommand()
    '[' -> startLoopCommand()
    ']' -> endLoopCommand()
    else -> skipCommand()
}

fun currentRawCommand() = State<Char?, Program> {
    Pair(it.commands.getOrNull(it.instructionPointer), it)
}

fun evaluateSingleCommand(): State<Unit?, Program> = currentRawCommand()
        .flatMap<Unit?> {
            if (it == null)
                State<Unit?, Program>(null)
            else
                command(it).map { it }
        }

fun nonTailRecursiveEvaluate(): State<Unit, Program> = evaluateSingleCommand()
        .flatMap {
            if (it == null)
                State(Unit)
            else
                nonTailRecursiveEvaluate()
        }

tailrec fun tailRecursiveEvaluate(program: Program): Program {
    val rawCommand = evaluateSingleCommand().runState(program)
    return if (rawCommand.first == null)
        program
    else {
        tailRecursiveEvaluate(rawCommand.second)
    }
}