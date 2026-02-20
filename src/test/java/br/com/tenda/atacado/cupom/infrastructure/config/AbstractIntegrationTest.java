package br.com.tenda.atacado.cupom.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class AbstractIntegrationTest {

    protected static final ObjectMapper OBJECT_MAPPER = new ObjectMapper().findAndRegisterModules();
}

