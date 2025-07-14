package com.kast.optimQuery.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kast.optimQuery.dto.ResultadosResumenDto;
import com.kast.optimQuery.repository.ResultadosListasRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResultadosService {

    private final ResultadosListasRepository repository;
    
    @Cacheable(value = "resultados", key = "#campana + '-' + #dia + '-' + #mes + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<ResultadosResumenDto> obtenerResultadosOptimizados(String campana, String dia, String mes, Pageable pageable) {
        return repository.findResultadosOptimizados(campana, dia, mes, pageable);
    }
}
