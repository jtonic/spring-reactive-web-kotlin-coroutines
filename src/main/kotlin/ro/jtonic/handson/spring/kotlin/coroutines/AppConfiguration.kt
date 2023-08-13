package ro.jtonic.handson.spring.kotlin.coroutines

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class AppConfiguration {

    @Bean
    fun prepare(): UseCase<Input, Input> =
        Prepare()

    @Bean
    fun execute(): UseCase<Input, Input> =
        Execute()

    @Bean
    fun postExecute(): UseCase<Input, Unit> =
        PostExecute()

    @Bean
    fun runFlow(
        prepare: UseCase<Input, Input>,
        execute: UseCase<Input, Input>,
        postExecute: UseCase<Input, Unit>,
    ): UseCase<Input, Unit> =
        RunFlow(prepare, execute, postExecute)
}