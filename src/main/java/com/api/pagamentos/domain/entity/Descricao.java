package com.api.pagamentos.domain.entity;

import com.api.pagamentos.domain.enums.StatusTransacao;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_descricao")
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Descricao {
    @Id
    @Column(name = "id")
    private Long nsu;
    private BigDecimal valor;
    private LocalDateTime dataHora;
    private String estabelecimento;
    private Long codigoAutorizacao;
    private StatusTransacao status;

    public Descricao() {

    }
}
