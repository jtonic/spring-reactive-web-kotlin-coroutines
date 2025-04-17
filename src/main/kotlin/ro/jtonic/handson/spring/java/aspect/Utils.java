package ro.jtonic.handson.spring.java.aspect;

public final class Utils {

    private static final int TRACE_ID_LENGTH = 8;
    private static final int SPAN_ID_LENGTH = 4;

    private Utils() {
    }

    public static boolean validateTraceId(String traceId) {
        if (traceId == null) {
            return false;
        }

        return traceId.length() == TRACE_ID_LENGTH;
    }

    public static boolean validateSpanId(String spanId) {
        if (spanId == null) {
            return false;
        }

        return spanId.length() == SPAN_ID_LENGTH;
    }
}
