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

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.is;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc
public class TransacaoEstornoTest {
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
    }

    @Test
    void it_ok() throws Exception {

        //transação não existe
        mockMvc.perform(get("/transacoes/estorno/124")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Transação não encontrada.")));

        // Verificando estorno de transação existente
        mockMvc.perform(get("/transacoes/estorno/123")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transacao.descricao.status", is("NEGADO")))
                .andExpect(jsonPath("$.transacao.cartao", is("4444********1234")))
                .andExpect(jsonPath("$.transacao.descricao.estabelecimento", is("PetShop Mundo cão")))
                .andExpect(jsonPath("$.transacao.formaPagamento.tipo", is("AVISTA")));
    }
}
