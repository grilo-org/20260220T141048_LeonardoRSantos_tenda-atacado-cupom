package br.com.tenda.atacado.cupom.application.output;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
public class FieldMessageOutput implements Serializable {

    private String fieldName;
    private String message;
}
