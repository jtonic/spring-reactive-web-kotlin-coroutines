package ro.jtonic.handson.spring.java.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ro.jtonic.handson.spring.java.aspect.annotation.Header;
import ro.jtonic.handson.spring.java.aspect.annotation.KafkaListener;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;

import static java.util.stream.IntStream.range;

@Aspect
@Component
public class TracingAspect {

    private static final Logger logger = LoggerFactory.getLogger(TracingAspect.class);
    private static final String UBER_TRACE_ID = "uber-trace-id";

    @Pointcut("@annotation(ro.jtonic.handson.spring.java.aspect.EnableTracing)")
    public void enableTracingPointcut() {
    }

    @Around("enableTracingPointcut()")
    public Object traceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        EnableTracing annotation = method.getAnnotation(EnableTracing.class);

        String methodName = method.getName();
        String className = method.getDeclaringClass().getSimpleName();

        if (!annotation.enabled()) {
            logger.debug("Tracing disabled for {}.{}", className, methodName);
            return joinPoint.proceed();
        }

        String level = annotation.level();

        if (method.isAnnotationPresent(KafkaListener.class)) {
            processKafkaListenerTracing(method, joinPoint.getArgs());
        }

        Instant start = Instant.now();

        Object[] args = joinPoint.getArgs();
        logMethodEntry(className, methodName, args, level);

        try {
            Object result = joinPoint.proceed();

            Instant end = Instant.now();
            Duration duration = Duration.between(start, end);

            logMethodExit(className, methodName, result, duration, level);

            return result;
        } catch (Throwable throwable) {
            logMethodException(className, methodName, throwable, level);
            throw throwable;
        }
    }

    private void logMethodEntry(String className, String methodName, Object[] args, String level) {
        String message = String.format("Entering method %s.%s with parameters: %s",
                className, methodName, Arrays.toString(args));
        logByLevel(message, level);
    }

    private void logMethodExit(String className, String methodName, Object result, Duration duration, String level) {
        String resultStr = (result != null) ? result.toString() : "null";
        String message = String.format("Exiting method %s.%s with result: %s (executed in %d ms)",
                className, methodName, resultStr, duration.toMillis());
        logByLevel(message, level);
    }

    private void logMethodException(String className, String methodName, Throwable throwable, String level) {
        String message = String.format("Exception in method %s.%s: %s",
                className, methodName, throwable.getMessage());
        logByLevel(message, level);
    }

    private void logByLevel(String message, String level) {
        switch (level.toUpperCase()) {
            case "TRACE":
                logger.trace(message);
                break;
            case "DEBUG":
                logger.debug(message);
                break;
            case "INFO":
                logger.info(message);
                break;
            case "WARN":
                logger.warn(message);
                break;
            case "ERROR":
                logger.error(message);
                break;
            default:
                logger.info(message);
        }
    }

    private void processKafkaListenerTracing(Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();

        // Look for the Header parameter with name="uber-trace-id"
        range(0, parameters.length)
                .filter(i -> {
                    Header headerAnnotation = parameters[i].getAnnotation(Header.class);
                    return headerAnnotation != null && UBER_TRACE_ID.equals(headerAnnotation.name());
                })
                .findFirst()
                .ifPresent(i -> {
                    if (args[i] != null) {
                        String uberTraceId = args[i].toString();
                        parseAndLogTraceId(uberTraceId);
                    }
                });
    }

    private void parseAndLogTraceId(String uberTraceId) {
        String[] traceParts = uberTraceId.split(":");

        if (traceParts.length >= 4) {
            String traceId = traceParts[0];
            String spanId = traceParts[1];
            String parentSpanId = traceParts[2];
            String ext = traceParts[3];

            logger.debug("In KafkaListener processing message with traceId: {}, spanId: {}, parentSpanId: {}, ext: {}",
                    traceId, spanId, parentSpanId, ext);
        } else {
            logger.debug("In KafkaListener with malformed uber-trace-id: {}", uberTraceId);
        }
    }
}
