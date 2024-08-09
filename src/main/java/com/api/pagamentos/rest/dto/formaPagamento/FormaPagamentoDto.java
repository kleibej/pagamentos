package com.api.pagamentos.rest.dto.formaPagamento;

import com.api.pagamentos.domain.enums.TipoPagamento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormaPagamentoDto {
    private String parcelas;
    private TipoPagamento tipo;
}
