package co.leonov.brainfuck

import org.junit.Test
import org.junit.Assert.*

class BrainfuckTest {

    @Test
    fun moveValueFromCell0ToCell2() {
        val result = tailRecursiveEvaluate(Program(0, ">>[-]<<[->>+<<]", intArrayOf(), 0, byteArrayOf(30, 0, 0)))
        assertArrayEquals(byteArrayOf(0, 0, 30), result.data)
    }

    @Test
    fun hello1() {
        val result = tailRecursiveEvaluate(Program(0, "++++++++++[>+++++++>+++a+++++++>+++>+<<<<-]>++.>+.+++++++..+++.>++.<<+++++++++++++++.>.+++.------.--------.>+.>.", intArrayOf(), 0, ByteArray(7)))
        assertArrayEquals(byteArrayOf(0, 87, 100, 33, 10, 0, 0), result.data)
    }

    @Test
    fun hello2() {
        val result = tailRecursiveEvaluate(Program(0, ">++++++++[-<+++++++++>]<.>>+>-[+]++>++>+++[>[->+++<<+++>]<<]>-----.>->+++..+++.>-.<<+[>[+>+]>>]<--------------.>>.+++.------.--------.>+.>+.", intArrayOf(), 0, ByteArray(7)))
        assertArrayEquals(byteArrayOf(72, 0, 87, 0, 100, 33, 10), result.data)
    }

    @Test
    fun hello3() {
        val result = tailRecursiveEvaluate(Program(0, "++++++++++[>+>+++>+++++++>++++++++++<<<<-]>>>++.>+.+++++++..+++.<<++.>+++++++++++++++.>.+++.------.--------.<<+.<.", intArrayOf(), 0, ByteArray(10)))
        assertArrayEquals(byteArrayOf(0, 10, 33, 87, 100, 0, 0, 0, 0, 0), result.data)
    }

    @Test
    fun hello4() {
        val result = tailRecursiveEvaluate(Program(0, "+[-->-[>>+>-----<<]<--<---]>-.>>>+.>>..+++[.>]<<<<.+++.------.<<-.>>>>+.", intArrayOf(), 0, ByteArray(10)))
        assertArrayEquals(byteArrayOf(-84, 108, 44, 33, 87, 0, 72, 0, -103, 100), result.data)
    }

    @Test
    fun hello5() {
        val result = tailRecursiveEvaluate(Program(0, "+[>>>->-[>->----<<<]>>]>.---.>+..+++.>>.<.>>---.<<<.+++.------.<-.>>+.", intArrayOf(), 0, ByteArray(23)))
        assertArrayEquals(byteArrayOf(-55, 0, 0, -1, 50, -1, 0, 55, 3, 0, -51, -92, 0, 52, 60, 0, 41, 0, 100, 108, 33, 44, 119), result.data)
    }

    @Test
    fun squaresFrom0to100() {
        val result = tailRecursiveEvaluate(Program(0,
                "++++[>+++++<-]>[<+++++>-]+<+[" +
                "    >[>+>+<<-]++>>[<<+>>-]>>>[-]++>[-]+" +
                "    >>>+[[-]++++++>>>]<<<[[<++++++++<++>>-]+<.<[>----<-]<]" +
                "    <<[>>>>>[>>>[-]+++++++++<[>-<-]+++++++++>[-[<->-]+[<<<]]<[>+<-]>]<<-]<<-" +
                "]", intArrayOf(), 0, ByteArray(26)))
        assertArrayEquals(byteArrayOf(0, -53, 0, 0, 0, 0, 2, 1, 0, 1, 1, 0, 0, 9, 0, 2, 1, 0, 0, 1, 0, 1, 1, 0, 0, 0), result.data)
    }
}
