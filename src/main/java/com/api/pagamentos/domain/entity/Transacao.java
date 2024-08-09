package com.api.pagamentos.domain.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_transacao")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Transacao {
    @Id
    private Long id;
    private String cartao;
    @OneToOne(cascade = CascadeType.ALL)
    private Descricao descricao;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "forma_pagamento_id")
    private FormaPagamento formaPagamento;
}
