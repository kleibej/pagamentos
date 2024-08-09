package com.api.pagamentos.domain.service;

import com.api.pagamentos.domain.entity.Descricao;
import com.api.pagamentos.domain.entity.Transacao;
import com.api.pagamentos.domain.enums.StatusTransacao;
import com.api.pagamentos.domain.repository.TransacaoRepository;
import com.api.pagamentos.infrastructure.exception.TransacaoException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransacaoService {
    private TransacaoRepository transacaoRepository;

    public TransacaoService(TransacaoRepository transacaoRepository) {
        this.transacaoRepository = transacaoRepository;
    }

    void preProcessamento(Transacao transacao){
        transacao.getDescricao().setNsu(1234567890L);
        transacao.getDescricao().setCodigoAutorizacao(147258369L);
        transacao.getDescricao().setStatus(StatusTransacao.AUTORIZADO);
        transacao.setDescricao(transacao.getDescricao());
    }

    void validaTransacaoAntesDeSalvar(Transacao transacao){
        boolean existeTransacaoPorId = transacaoRepository.existsById(transacao.getId());
        if(existeTransacaoPorId){
            throw TransacaoException
                    .builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .mensagem("Já existe uma transação com esse identificador.")
                    .build();
        }

        if(transacao.getCartao() == null){
            throw TransacaoException
                    .builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .mensagem("O cartão não foi informado.")
                    .build();
        }

        if(transacao.getDescricao() == null){
            throw TransacaoException
                    .builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .mensagem("A descrição da transação não foi informada.")
                    .build();
        }

        if(transacao.getFormaPagamento() == null){
            throw TransacaoException
                    .builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .mensagem("A forma de pagamento não foi informada.")
                    .build();
        }
    }

    public Transacao pagamento(Transacao transacao) {
        validaTransacaoAntesDeSalvar(transacao);
        preProcessamento(transacao);
        return transacaoRepository.save(transacao);
    }

    public Transacao estorno(Long id) {
        Optional<Transacao> transacao = transacaoRepository.findById(id);
        if(transacao.isEmpty()){
            throw TransacaoException
                    .builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .mensagem("Transação não encontrada.")
                    .build();
        }

        transacao.get()
                .getDescricao()
                .setStatus(StatusTransacao.NEGADO);

        return transacao.get();
    }

    public Transacao consultaPorId(Long id) {
        Optional<Transacao> transacao = transacaoRepository.findById(id);
        if(transacao.isEmpty()){
            throw TransacaoException
                    .builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .mensagem("Transação não encontrada.")
                    .build();
        }

        return transacao.get();
    }

    public List<Transacao> listarTransacoes() {
        return transacaoRepository.findAll();
    }
}
