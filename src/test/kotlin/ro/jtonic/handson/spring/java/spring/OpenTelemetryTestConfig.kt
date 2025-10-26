/*
package ro.jtonic.handson.spring.java.spring

import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.testing.exporter.InMemorySpanExporter
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor
import io.opentelemetry.semconv.ServiceAttributes.SERVICE_NAME
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Configuration
@Profile("test")
class OpenTelemetryTestConfig {

    @Value("\${spring.application.name}")
    private lateinit var applicationName: String

    @Bean
    @Primary
    fun inMemorySpanExporter(): InMemorySpanExporter {
        return InMemorySpanExporter.create()
    }

    @Bean
    @Primary
    fun testOpenTelemetry(inMemorySpanExporter: InMemorySpanExporter): OpenTelemetry {
        val resource = Resource.getDefault()
            .merge(
                Resource.create(
                    Attributes.of(
                        SERVICE_NAME, applicationName
                    )
                )
            )

        val sdkTracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(SimpleSpanProcessor.create(inMemorySpanExporter))
            .setResource(resource)
            .build()

        return OpenTelemetrySdk.builder()
            .setTracerProvider(sdkTracerProvider)
            .setPropagators(ContextPropagators.noop())
            .build()
    }

    @Bean
    @Primary
    fun testTracer(openTelemetry: OpenTelemetry): Tracer {
        return openTelemetry.getTracer(applicationName)
    }
}
*/
