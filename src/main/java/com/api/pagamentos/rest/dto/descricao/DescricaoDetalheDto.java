package com.api.pagamentos.rest.dto.descricao;

import com.api.pagamentos.domain.enums.StatusTransacao;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DescricaoDetalheDto {
    private String nsu;
    private String valor;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataHora;
    private String estabelecimento;
    private String codigoAutorizacao;
    private StatusTransacao status;
}
