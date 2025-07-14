package com.kast.optimQuery.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kast.optimQuery.dto.ResultadosResumenDto;
import com.kast.optimQuery.service.ResultadosService;

import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/resultados")
@RequiredArgsConstructor
public class ResultadosController {
    private final ResultadosService resultadosService;

    @GetMapping("/resumen")
    @Timed(value = "resultados.resumen", description = "Tiempo de ejecucion de la consulta de resultados optimizados")
    /*
        Si no colocas nada en dia o mes, se trae todos los datos.
        Si solo colocas el dia, se trae todos los datos de todos los meses en ese dia.
        Si solo colocas el mes, se trae todos los datos de todos los dias en ese mes.
     */ 
    public Page<ResultadosResumenDto> getResultadosOptimizados(
        @RequestParam(defaultValue = "nan") String campana,
        @RequestParam(required = false) String dia,
        @RequestParam(required = false) String mes,
        Pageable pageable
    ) {
        if (dia != null && dia.isBlank()) dia = null;
        if (mes != null && mes.isBlank()) mes = null;
        return resultadosService.obtenerResultadosOptimizados(campana, dia, mes,pageable);
    }
}
