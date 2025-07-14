package com.kast.optimQuery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kast.optimQuery.service.ExportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/export")
@Tag(name = "Exportación", description = "Endpoints para exportar datos optimizados en formato TXT")
@Slf4j
public class ExportController {
    @Autowired
    private ExportService exportService;

    @GetMapping("/optimizados")
    @Operation(summary = "Exportar resultados optimizados",
            description = "Exporta los datos optimizados a archivo TXT con separador |")
    public ResponseEntity<String> exportOptimizados(
        @Parameter(description = "Campaña a filtrar (default: 'nan')")
        @RequestParam(defaultValue = "nan") String campana,
        @Parameter(description = "Dia (opcional)")
        @RequestParam(required = false) String dia,
        @Parameter(description = "Mes (opcional)")
        @RequestParam(required = false) String mes
    ) {
        try {
            // Limpiar parametros vacios
            if (dia != null && dia.isBlank()) dia = null;
            if (mes != null && mes.isBlank()) mes = null;

            String filePath = exportService.exportOptimizadosToTxt(campana, dia, mes);
            return ResponseEntity.ok(filePath);
        } catch (Exception e) {
            log.error("Error en exportación: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body("Error en exportación: " + e.getMessage());
        }
    }

    @GetMapping("/optimizados/stats")
    @Operation(summary = "Obtener estadisticas de exportacion",
            description = "Retorna estadisticas sobre los datos disponibles para exportacion")
    public ResponseEntity<String> getExportStats(
        @Parameter(description = "Campaña a filtrar (default: 'nan')")
        @RequestParam(defaultValue = "nan") String campana,
        @Parameter(description = "Dia (opcional)")
        @RequestParam(required = false) String dia,
        @Parameter(description = "Mes (opcional)")
        @RequestParam(required = false) String mes
    ) {
        try {
            // Limpiar parameteos vacios
            if (dia != null && dia.isBlank()) dia = null;
            if (mes != null && mes.isBlank()) mes = null;

            String stats = exportService.getExportStats(campana, dia, mes);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            log.error("Error obteniendo estadisticas: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError()
                .body("Error obteniendo estadisticas: " + e.getMessage());
        }
    }
}
