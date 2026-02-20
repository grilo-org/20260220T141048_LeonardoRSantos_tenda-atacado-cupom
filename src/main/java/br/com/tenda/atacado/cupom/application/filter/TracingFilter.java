package br.com.tenda.atacado.cupom.application.filter;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TracingFilter extends OncePerRequestFilter {

    public static final String HEADER_X_TRACE_ID = "X-Trace-Id";
    public static final String HEADER_TRACE_ID = "traceId";
    private final Tracer tracer;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Span span = this.tracer.nextSpan();
        try (Tracer.SpanInScope ignored = this.tracer.withSpan(span.start())) {
            String traceId = span.context().traceId();
            MDC.put(HEADER_TRACE_ID, traceId);
            response.addHeader(HEADER_X_TRACE_ID, traceId);

            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(HEADER_TRACE_ID);
            span.end();
        }
    }
}
