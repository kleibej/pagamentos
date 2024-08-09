package com.api.pagamentos.transacao;

import com.api.pagamentos.domain.entity.Descricao;
import com.api.pagamentos.domain.entity.FormaPagamento;
import com.api.pagamentos.domain.entity.Transacao;
import com.api.pagamentos.domain.enums.StatusTransacao;
import com.api.pagamentos.domain.enums.TipoPagamento;
import com.api.pagamentos.domain.repository.TransacaoRepository;
import com.api.pagamentos.rest.TransacaoWrapper;
import com.api.pagamentos.rest.dto.descricao.CriarDescricaoDto;
import com.api.pagamentos.rest.dto.formaPagamento.FormaPagamentoDto;
import com.api.pagamentos.rest.dto.transacao.CriarTransacaoDto;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.math.BigDecimal;
import java.time.LocalDateTime;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc

//fixme teste com erro por setar o nsu manualmente e ele ser chave primária
public class TransacaoPagamentoTest {


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
        //id deve ser único
        mockMvc.perform(post("/transacoes")
                .content(mapper.writeValueAsString(new TransacaoWrapper(CriarTransacaoDto
                        .builder()
                        .id(123L)
                        .cartao("4444********1234")
                        .descricao(CriarDescricaoDto
                                .builder()
                                .valor(BigDecimal.valueOf(500.5))
                                .estabelecimento("PetShop Mundo cão")
                                .build())
                        .formaPagamento(FormaPagamentoDto
                                .builder()
                                .tipo(TipoPagamento.AVISTA)
                                .parcelas(1)
                                .build())
                        .build())))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("Já existe uma transação com esse identificador.")));

        //Cartão é obrigatório
        mockMvc.perform(post("/transacoes")
                        .content(mapper.writeValueAsString(new TransacaoWrapper(CriarTransacaoDto
                                .builder()
                                .id(1234L)
                                .descricao(CriarDescricaoDto
                                        .builder()
                                        .valor(BigDecimal.valueOf(500.5))
                                        .estabelecimento("PetShop Mundo cão")
                                        .build())
                                .formaPagamento(FormaPagamentoDto
                                        .builder()
                                        .tipo(TipoPagamento.AVISTA)
                                        .parcelas(1)
                                        .build())
                                .build())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("O cartão não foi informado.")));

        //Descrição da transação é obrigatória
        mockMvc.perform(post("/transacoes")
                        .content(mapper.writeValueAsString(new TransacaoWrapper(CriarTransacaoDto
                                .builder()
                                .id(1234L)
                                .cartao("4444********1234")
                                .formaPagamento(FormaPagamentoDto
                                        .builder()
                                        .tipo(TipoPagamento.AVISTA)
                                        .parcelas(1)
                                        .build())
                                .build())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("A descrição da transação não foi informada.")));

        //A forma de pagamento da transação é obrigatória
        mockMvc.perform(post("/transacoes")
                        .content(mapper.writeValueAsString(new TransacaoWrapper(CriarTransacaoDto
                                .builder()
                                .id(1234L)
                                .cartao("4444********1234")
                                .descricao(CriarDescricaoDto
                                        .builder()
                                        .valor(BigDecimal.valueOf(500.5))
                                        .estabelecimento("PetShop Mundo cão")
                                        .build())
                                .build())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("A forma de pagamento não foi informada.")));

        mockMvc.perform(post("/transacoes")
                        .content(mapper.writeValueAsString(new TransacaoWrapper(CriarTransacaoDto
                                .builder()
                                .id(1234L)
                                .cartao("4444********1234")
                                .descricao(CriarDescricaoDto
                                        .builder()
                                        .valor(BigDecimal.valueOf(500.5))
                                        .estabelecimento("PetShop Mundo cão")
                                        .build())
                                .formaPagamento(FormaPagamentoDto
                                        .builder()
                                        .tipo(TipoPagamento.AVISTA)
                                        .parcelas(1)
                                        .build())
                                .build())))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
    }
}
