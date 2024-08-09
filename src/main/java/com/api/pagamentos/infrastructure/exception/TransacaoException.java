package com.api.pagamentos.infrastructure.exception;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
public class TransacaoException extends RuntimeException implements Serializable {
    private static final long serialVersionUID = 1L;

    private String mensagem;
    @Builder.Default
    private final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
}
