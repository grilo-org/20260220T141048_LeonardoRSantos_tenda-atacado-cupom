package br.com.tenda.atacado.cupom.application.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String dateString = p.getText();
        
        if (dateString == null || dateString.isEmpty()) {
            return null;
        }
        
        try {
            if (dateString.contains("T")) {
                Instant instant = Instant.parse(dateString);
                return instant.atZone(ZoneId.of("UTC")).toLocalDate();
            } else {
                return LocalDate.parse(dateString);
            }
        } catch (Exception e) {
            throw new IOException("Erro ao deserializar data: " + dateString, e);
        }
    }
}
