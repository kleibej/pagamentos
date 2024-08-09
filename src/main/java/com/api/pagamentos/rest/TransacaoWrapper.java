package com.api.pagamentos.rest;

import com.api.pagamentos.rest.dto.transacao.CriarTransacaoDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransacaoWrapper {
    private CriarTransacaoDto transacao;
}