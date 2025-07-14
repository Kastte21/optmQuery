package com.kast.optimQuery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadosResumenDto {
    private String documento;
    private String telefono;
    private String start;
    private String estado;
    private String obs;
    private String dia;
    private String mes;
}
