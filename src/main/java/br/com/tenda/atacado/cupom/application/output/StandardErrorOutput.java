package br.com.tenda.atacado.cupom.application.output;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class StandardErrorOutput implements Serializable {

    private Long timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;

}