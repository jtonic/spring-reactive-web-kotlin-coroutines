package ro.jtonic.handson.spring.kotlin.coroutines.kotest

import io.kotest.core.spec.style.FreeSpec
import ro.jtonic.handson.spring.kotlin.coroutines.*
import kotlin.system.measureTimeMillis

class UseCaseTest : FreeSpec({

    fun latency(input: Input, body: UseCase<Input, Unit>) {
        measureTimeMillis {
            body(input)
        }
    }

    "test composed Use Cases" {

        val prepare = Prepare()
        val execution = Execute()
        val postExecution = PostExecute()

        val flow = RunFlow(
            prepare,
            execution,
            postExecution
        )

        latency(Input(20), flow)
    }
})