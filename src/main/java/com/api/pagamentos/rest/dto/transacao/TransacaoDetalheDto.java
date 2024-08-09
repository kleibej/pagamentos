package com.api.pagamentos.rest.dto.transacao;

import com.api.pagamentos.rest.dto.descricao.DescricaoDetalheDto;
import com.api.pagamentos.rest.dto.formaPagamento.FormaPagamentoDto;
import lombok.Data;

@Data
public class TransacaoDetalheDto {
    private Long id;
    private String cartao;
    private DescricaoDetalheDto descricao;
    private FormaPagamentoDto formaPagamento;
}
