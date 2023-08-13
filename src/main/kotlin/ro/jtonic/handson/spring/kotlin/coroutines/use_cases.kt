package ro.jtonic.handson.spring.kotlin.coroutines

import arrow.core.andThen


@JvmInline
value class Input(val value: Int)

typealias UseCase<I, R> = (I) -> R

class Prepare : UseCase<Input, Input> {
    override fun invoke(p1: Input) = run {
        println("Prepare ${p1.value}")
        p1
    }
}

class Execute : UseCase<Input, Input> {
    override fun invoke(p1: Input): Input = run {
        println("Executing using the input $p1 ...")
        Input(p1.value * 2)
    }
}

class PostExecute : UseCase<Input, Unit> {
    override fun invoke(p1: Input) = run {
        println("Post execution. Result: ${p1.value}")
    }
}

class RunFlow(
    private val prepare: UseCase<Input, Input>,
    private val execute: UseCase<Input, Input>,
    private val postExecute: UseCase<Input, Unit>,
) : UseCase<Input, Unit> {
    override fun invoke(p1: Input) =
        prepare andThen execute andThen postExecute applyOn p1
}

infix fun <P1, R> ((P1) -> R).applyOn(p1: P1): R = this(p1)
