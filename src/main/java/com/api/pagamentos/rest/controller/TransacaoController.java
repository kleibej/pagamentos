package com.api.pagamentos.rest.controller;

import com.api.pagamentos.domain.entity.Transacao;
import com.api.pagamentos.domain.service.ModelMapperService;
import com.api.pagamentos.domain.service.TransacaoService;
import com.api.pagamentos.rest.TransacaoWrapper;
import com.api.pagamentos.rest.dto.transacao.ConsultaTransacaoDto;
import com.api.pagamentos.rest.dto.transacao.CriarTransacaoDto;
import com.api.pagamentos.rest.dto.transacao.TransacaoDetalheDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<TransacaoDetalheDto> pagar(@RequestBody TransacaoWrapper wrapper) {
        //fixme avaliar se precisa de tanta transformacao
        CriarTransacaoDto transacaoDto = modelMapperService.toObject(CriarTransacaoDto.class, wrapper.getTransacao());
        Transacao pagamento = this.transacaoService.pagamento(modelMapperService.toObject(Transacao.class, transacaoDto));
        return ResponseEntity.ok(modelMapperService.toObject(TransacaoDetalheDto.class, pagamento));
    }

    @GetMapping("/estorno/{id}")
    public ResponseEntity<Map<String, TransacaoDetalheDto>> estornar(@PathVariable Long id) {
        Map<String, TransacaoDetalheDto> respostaPadrao = new HashMap<>();
        respostaPadrao.put("transacao", modelMapperService.toObject(TransacaoDetalheDto.class, this.transacaoService.estorno(id)));
        return ResponseEntity.ok(respostaPadrao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, ConsultaTransacaoDto>> consultarPorId(@PathVariable Long id){
        Map<String, ConsultaTransacaoDto> retornoPadrao = new HashMap<>();
        retornoPadrao.put("transacao", modelMapperService.toObject(ConsultaTransacaoDto.class, this.transacaoService.consultaPorId(id)));
        return ResponseEntity.ok(retornoPadrao);
    }

    @GetMapping
    public ResponseEntity<List<Map<String, ConsultaTransacaoDto>>> listarTransacoes(){
        List<Map<String, ConsultaTransacaoDto>> retornoPadrao = new ArrayList<>();
        modelMapperService.toList(ConsultaTransacaoDto.class, this.transacaoService.listarTransacoes()).forEach(t -> {
            Map<String, ConsultaTransacaoDto> atributoTransacao = new HashMap<>();
            atributoTransacao.put("transacao", t);
            retornoPadrao.add(atributoTransacao);
        });
        return ResponseEntity.ok(retornoPadrao);
    }

}
