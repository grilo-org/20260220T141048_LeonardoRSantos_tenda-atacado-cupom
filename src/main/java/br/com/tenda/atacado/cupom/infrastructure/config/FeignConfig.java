package br.com.tenda.atacado.cupom.infrastructure.config;

import io.micrometer.tracing.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    private final Tracer tracer;

    public FeignConfig(Tracer tracer) {
        this.tracer = tracer;
    }

    @Bean
    public io.micrometer.tracing.Tracer feignTracer() {
        return tracer;
    }
}
