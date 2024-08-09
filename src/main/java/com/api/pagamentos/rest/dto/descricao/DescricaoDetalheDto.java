package com.api.pagamentos.rest.dto.descricao;

import com.api.pagamentos.domain.enums.StatusTransacao;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DescricaoDetalheDto {
    private Long nsu;
    private BigDecimal valor;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataHora;
    private String estabelecimento;
    private Long codigoAutorizacao;
    private StatusTransacao status;
}
