package com.api.pagamentos.rest.controller;

import com.api.pagamentos.domain.entity.Transacao;
import com.api.pagamentos.domain.service.ModelMapperService;
import com.api.pagamentos.domain.service.TransacaoService;
import com.api.pagamentos.rest.TransacaoWrapper;
import com.api.pagamentos.rest.dto.transacao.ConsultaTransacaoDto;
import com.api.pagamentos.rest.dto.transacao.CriarTransacaoDto;
import com.api.pagamentos.rest.dto.transacao.TransacaoDetalheDto;
import com.api.pagamentos.utils.RespostaUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transacoes")
public class TransacaoController {
    private final ModelMapperService modelMapperService;
    private TransacaoService transacaoService;

    public TransacaoController(TransacaoService transacaoService, ModelMapperService modelMapperService) {
        this.transacaoService = transacaoService;
        this.modelMapperService = modelMapperService;
    }

    @PostMapping
    public ResponseEntity<Map<String, TransacaoDetalheDto>> pagar(@RequestBody TransacaoWrapper wrapper) {
        CriarTransacaoDto transacaoDto = modelMapperService.toObject(CriarTransacaoDto.class, wrapper.getTransacao());
        Transacao pagamento = this.transacaoService.pagamento(modelMapperService.toObject(Transacao.class, transacaoDto));
        return ResponseEntity.ok(RespostaUtils.padronizaRespostas(
                "transacao",
                modelMapperService.toObject(TransacaoDetalheDto.class, pagamento))
        );
    }

    @GetMapping("/estorno/{id}")
    public ResponseEntity<Map<String, TransacaoDetalheDto>> estornar(@PathVariable Long id) {
        return ResponseEntity.ok(RespostaUtils.padronizaRespostas(
                "transacao",
                modelMapperService.toObject(TransacaoDetalheDto.class, this.transacaoService.estorno(id)))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, ConsultaTransacaoDto>> consultarPorId(@PathVariable Long id){
        return ResponseEntity.ok(RespostaUtils.padronizaRespostas(
                "transacao",
                modelMapperService.toObject(ConsultaTransacaoDto.class, this.transacaoService.consultaPorId(id)))
        );
    }

    @GetMapping
    public ResponseEntity<List<Map<String, ConsultaTransacaoDto>>> listarTransacoes() {
        List<ConsultaTransacaoDto> transacoes = modelMapperService.toList(ConsultaTransacaoDto.class, this.transacaoService.listarTransacoes());

        List<Map<String, ConsultaTransacaoDto>> retornoPadrao = transacoes.stream()
                .map(transacao -> RespostaUtils.padronizaRespostas("transacao", transacao))
                .collect(Collectors.toList());

        return ResponseEntity.ok(retornoPadrao);
    }

}
