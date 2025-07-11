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
            CASE
                WHEN LENGTH(r.telefono) = 7 THEN 'TELEFONO'
                WHEN LENGTH(r.telefono) = 9 AND r.telefono LIKE '9%' AND r.telefono NOT IN ('999999999', '900000000') THEN 'CELULAR'
                ELSE 'DESCONOCIDO'
            END,
            SUBSTRING(r.start, 9, 2),
            SUBSTRING(r.start, 6, 2)
        )
        FROM ResultadosListas r
        WHERE (:campana IS NULL OR r.campana <> :campana OR r.campana = 'nan')
          AND (:dia IS NULL OR SUBSTRING(r.start,9,2) = :dia)
          AND (:mes IS NULL OR SUBSTRING(r.start,6,2) = :mes)
    """)
    Page<ResultadosResumenDto> findResultadosOptimizados(
        @Param("campana") String campana,
        @Param("dia") String dia,
        @Param("mes") String mes,
        Pageable pageable
    );
}
