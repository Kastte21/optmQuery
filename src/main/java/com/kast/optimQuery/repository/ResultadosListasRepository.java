package com.kast.optimQuery.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.kast.optimQuery.dto.ResultadosResumenDto;
import com.kast.optimQuery.model.ResultadosListas;

public interface ResultadosListasRepository extends Repository<ResultadosListas, Long> {
    @Query("""
        SELECT new com.kast.optimQuery.dto.ResultadosResumenDto(
            r.documento,
            r.telefono,
            r.start,
            r.estado,
            r.obs,
            r.dia,
            r.mes
        )
        FROM ResultadosListas r
        WHERE (:campana IS NULL OR r.campana <> :campana OR r.campana = 'nan')
          AND (:dia IS NULL OR r.dia = :dia)
          AND (:mes IS NULL OR r.mes = :mes)
    """)
    Page<ResultadosResumenDto> findResultadosOptimizados(
        @Param("campana") String campana,
        @Param("dia") String dia,
        @Param("mes") String mes,
        Pageable pageable
    );
}
