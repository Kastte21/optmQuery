package com.kast.optimQuery.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "resultados_listas",
    indexes = {
        @Index(name = "idx_resultados_listas_documento", columnList = "documento"),
        @Index(name = "idx_resultados_listas_telefono", columnList = "telefono"),
        @Index(name = "idx_resultados_listas_start", columnList = "start"),
        @Index(name = "idx_resultados_listas_estado", columnList = "estado"),
        @Index(name = "idx_resultados_listas_campana_start", columnList = "campana, start"),
        @Index(name = "idx_resultados_listas_campana_estado", columnList = "campana, estado"),
        @Index(name = "idx_resultados_listas_start_doc_telf_estado", columnList = "documento, telefono,start, estado") 
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResultadosListas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String documento;

    @Column(length = 50)
    private String telefono;

    @Column(length = 255)
    private String campana;

    @Column(length = 50)
    private String canal;

    @Column(length = 50)
    private String cartera;

    @Column(length = 50)
    private String idcliente;

    @Column(length = 50)
    private String start;

    @Column(length = 50)
    private String subcartera;

    @Column(length = 50)
    private String supervisor;

    @Column(length = 50)
    private String anexo;

    @Column(length = 50)
    private String causa;

    @Column(length = 50)
    private String corta;

    @Column(length = 50)
    private String disposition;

    @Column(length = 50)
    private String duration;

    @Column(length = 50)
    private String estado;

    @Column(length = 50)
    private String gestion;

    @Column(length = 4)
    private String dia;

    @Column(length = 4)
    private String mes;

    @Column(length = 13)
    private String obs;
}
