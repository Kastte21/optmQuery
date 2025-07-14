package com.kast.optimQuery.service;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kast.optimQuery.dto.ResultadosExportDto;
import com.kast.optimQuery.dto.ResultadosResumenDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ExportService {
    @Autowired
    private ResultadosService resultadosService;

    @Cacheable(value = "exportOptimizados", key = "#campana + '_' + #dia + '_' + #mes")
    public String exportOptimizadosToTxt(String campana, String dia, String mes) {
        log.info("Iniciando exportacion de resultados listas - Campaña: {}, Dia: {}, Mes: {}", campana, dia, mes);

        try {
            // Crear directorio de exports si no existe
            Path exportDir = Paths.get("exports");
            if (!Files.exists(exportDir)) {
                Files.createDirectories(exportDir);
            }

            // Generar nombre de archivos con timestamp
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String filename = String.format("export_%s_%s_%s_%s.txt", 
                    campana != null ? campana : "all", 
                    dia != null ? dia : "all", 
                    mes != null ? mes : "all", 
                    timestamp);
            Path filePath = exportDir.resolve(filename);

            // Obtener todos los datos paginafos
            List<ResultadosResumenDto> allData = getAllDataOptimizados(campana, dia, mes);

            // Convierte a DTOs de exportacion
            List<ResultadosExportDto> exportDtos = allData.stream()
                .map(this::convertToExportDto)
                .collect(Collectors.toList());

            // Escribir archivo
            try (FileWriter writer = new FileWriter(filePath.toFile())) {
                writer.write(ResultadosExportDto.getHeader() + "\n");
                for (ResultadosExportDto dto : exportDtos) {
                    writer.write(dto.toTxtFormat() + "\n");
                }
            }

            log.info("Exportacion completada: {} registros exportados a {}", exportDtos.size(), filePath.toString());

            return filePath.toString();
        } catch (IOException e) {
            log.error("Error durante la exportacion: {}", e.getMessage(), e);
            throw new RuntimeException("Error al exportra datos: " + e.getMessage(), e);
        }
    }

    // Obtener todos los datos paginados
    private List<ResultadosResumenDto> getAllDataOptimizados(String campana, String dia, String mes) {
        List<ResultadosResumenDto> allData = new java.util.ArrayList<>();
        int page = 0;
        int size = 1000;

        while (true) {
            Pageable pageable = PageRequest.of(page, size);
            Page<ResultadosResumenDto> pageResult = resultadosService.obtenerResultadosOptimizados(campana, dia, mes, pageable);
            allData.addAll(pageResult.getContent());

            if (pageResult.isLast()) break;
            page++;
        }
        return allData;
    }

    // Convertir ResultadosResumenDto a ResultadosExportDto
    private ResultadosExportDto convertToExportDto(ResultadosResumenDto resumenDto) {
        return ResultadosExportDto.builder()
            .documento(resumenDto.getDocumento())
            .telefono(resumenDto.getTelefono())
            .start(resumenDto.getStart())
            .estado(resumenDto.getEstado())
            .obs(resumenDto.getObs())
            .dia(resumenDto.getDia())
            .mes(resumenDto.getMes())
            .build();
    }

    // Obtener estadisticas de exportacion
    public String getExportStats(String campana, String dia, String mes) {
        try {
            List<ResultadosResumenDto> data = getAllDataOptimizados(campana, dia, mes);

            return String.format("""
                    Estadisticas de exportacion:
                    - Total de registros: %d
                    - Campaña: %s
                    - Dia: %s
                    - Mes: %s
                    """, data.size(),
                    campana != null ? campana : "Todas",
                    dia != null ? dia : "Todos",
                    mes != null ? mes : "Todos");
        } catch (Exception e) {
            log.error("Error obteniendo estadisticas: {}", e.getMessage(), e);
            return "Error obteniendo estadisticas: " + e.getMessage();
        }
    }
}
