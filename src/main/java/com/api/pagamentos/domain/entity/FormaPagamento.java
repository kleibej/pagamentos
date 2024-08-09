package com.api.pagamentos.domain.entity;

import com.api.pagamentos.domain.enums.TipoPagamento;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_forma_pagamento")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormaPagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer parcelas;
    private TipoPagamento tipo;
}
