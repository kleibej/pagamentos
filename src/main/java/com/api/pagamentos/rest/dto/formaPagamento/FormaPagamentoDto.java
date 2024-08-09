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
    private Integer parcelas;
    private TipoPagamento tipo;
}
