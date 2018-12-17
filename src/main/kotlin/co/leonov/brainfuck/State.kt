package co.leonov.brainfuck

class State<A, S> {
    val runState: (S) -> Pair<A, S>

    constructor(a: A) {
        runState = { Pair(a, it) }
    }

    constructor(runState: (S) -> Pair<A, S>) {
        this.runState = runState
    }

    fun <B> map(f: (A) -> B) = State<B, S> {
        runState(it).run { (Pair(f(first), second)) }
    }

    fun <B> flatMap(f: (A) -> State<B, S>) = State<B, S> {
        runState(it).run {
            f(first).runState(second)
        }
    }

    fun <B> then(bs: State<B, S>) = flatMap { bs }

    fun evalState(s: S) = runState(s).first
}