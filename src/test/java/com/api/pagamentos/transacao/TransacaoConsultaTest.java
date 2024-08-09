package com.api.pagamentos.transacao;

import com.api.pagamentos.domain.entity.Descricao;
import com.api.pagamentos.domain.entity.FormaPagamento;
import com.api.pagamentos.domain.entity.Transacao;
import com.api.pagamentos.domain.enums.StatusTransacao;
import com.api.pagamentos.domain.enums.TipoPagamento;
import com.api.pagamentos.domain.repository.TransacaoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc
public class TransacaoConsultaTest {

    ObjectMapper mapper = new ObjectMapper();
    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    void setup() {
        mapper.registerModule(new JavaTimeModule());
        transacaoRepository.save(Transacao
                .builder()
                .id(123L)
                .cartao("4444********1234")
                .descricao(Descricao
                        .builder()
                        .nsu(1234567890L)
                        .codigoAutorizacao(147258369L)
                        .status(StatusTransacao.AUTORIZADO)
                        .valor(BigDecimal.valueOf(500.5))
                        .dataHora(LocalDateTime.now())
                        .estabelecimento("PetShop Mundo cão")
                        .build())
                .formaPagamento(FormaPagamento
                        .builder()
                        .tipo(TipoPagamento.AVISTA)
                        .parcelas(1)
                        .build())
                .build());

        transacaoRepository.save(Transacao
                .builder()
                .id(12345L)
                .cartao("4444********5555")
                .descricao(Descricao
                        .builder()
                        .nsu(4434567833L)
                        .codigoAutorizacao(337258369L)
                        .status(StatusTransacao.NEGADO)
                        .valor(BigDecimal.valueOf(600.5))
                        .dataHora(LocalDateTime.now())
                        .estabelecimento("Uma Loja ai")
                        .build())
                .formaPagamento(FormaPagamento
                        .builder()
                        .tipo(TipoPagamento.AVISTA)
                        .parcelas(1)
                        .build())
                .build());
    }

    @Test
    void it_ok() throws Exception {

        //transação por id não existe
        mockMvc.perform(get("/transacoes/124")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Transação não encontrada.")));

        // Transação por id existente
        mockMvc.perform(get("/transacoes/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao.descricao.status", is("AUTORIZADO")))
                .andExpect(jsonPath("$.transacao.cartao", is("4444********1234")))
                .andExpect(jsonPath("$.transacao.descricao.estabelecimento", is("PetShop Mundo cão")))
                .andExpect(jsonPath("$.transacao.formaPagamento.tipo", is("AVISTA")));

        // Transações existentes
        mockMvc.perform(get("/transacoes")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].transacao.id", is("123")))
                .andExpect(jsonPath("$[0].transacao.cartao", is("4444********1234")))
                .andExpect(jsonPath("$[0].transacao.descricao.nsu", is("1234567890")))
                .andExpect(jsonPath("$[0].transacao.descricao.valor", is("500.50")))
                .andExpect(jsonPath("$[0].transacao.descricao.estabelecimento", is("PetShop Mundo cão")))
                .andExpect(jsonPath("$[0].transacao.descricao.codigoAutorizacao", is("147258369")))
                .andExpect(jsonPath("$[0].transacao.descricao.status", is("AUTORIZADO")))
                .andExpect(jsonPath("$[0].transacao.formaPagamento.parcelas", is("1")))
                .andExpect(jsonPath("$[0].transacao.formaPagamento.tipo", is("AVISTA")));
    }
}
