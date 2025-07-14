package com.kast.optimQuery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultadosExportDto {
    private String documento;
    private String telefono;
    private String start;
    private String estado;
    private String obs;
    private String dia;
    private String mes;

    // Convierte el DTO a TXT separado por |
    public String toTxtFormat() {
        return String.join("|",
        documento != null ? documento :"",
        telefono != null ? telefono : "",
        start != null ? start : "",
        estado != null ? estado : "",
        obs != null ? obs : "",
        dia != null ? dia : "",
        mes != null ? mes : ""
        );
    }

    // Header del TXT
    public static String getHeader() {
        return "DOCUMENTO|TELEFONO|START|ESTADO|OBS|DIA|MES";
    }
}
