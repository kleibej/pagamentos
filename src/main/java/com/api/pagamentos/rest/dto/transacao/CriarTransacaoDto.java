package com.api.pagamentos.rest.dto.transacao;

import com.api.pagamentos.rest.dto.descricao.CriarDescricaoDto;
import com.api.pagamentos.rest.dto.formaPagamento.FormaPagamentoDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CriarTransacaoDto {
    private Long id;
    private String cartao;
    private CriarDescricaoDto descricao;
    private FormaPagamentoDto formaPagamento;
}
