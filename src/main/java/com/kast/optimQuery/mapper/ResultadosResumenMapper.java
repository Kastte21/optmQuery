package com.kast.optimQuery.mapper;

import org.mapstruct.*;

import com.kast.optimQuery.dto.ResultadosResumenDto;
import com.kast.optimQuery.model.ResultadosListas;

@Mapper(componentModel = "spring")
public interface ResultadosResumenMapper {
    @Mapping(target = "obs", source = "telefono", qualifiedByName = "processTelefono")
    @Mapping(target = "dia", source = "start", qualifiedByName = "extractDia")
    @Mapping(target = "mes", source = "start", qualifiedByName = "extractMes")
    ResultadosResumenDto toDto(ResultadosListas entity);

    @Named("processTelefono")
    default String processTelefono(String telefono) {
        if (telefono == null) return "DESCONOCIDO";
        if (telefono.length() == 7) return "TELEFONO";
        if (telefono.length() == 9 && 
            telefono.startsWith("9") && 
            !telefono.equals("999999999") && 
            !telefono.equals("900000000")) return "CELULAR";
        return "DESCONOCIDO";
    }

    @Named("extractDia")
    default String extractDia(String start) {
        if (start != null && start.length() >= 10) return start.substring(8, 10); // SUBSTRING(start, 9, 2) 
        return ""; 
    }

    @Named("extractMes")
    default String extractMes(String start) {
        if (start != null && start.length() >= 7) return start.substring(5, 7); // SUBSTRING(start, 6, 2)
        return "";
    }
}
