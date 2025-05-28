package com.github.chiarelli.estoque_produtos;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chiarelli.estoque_produtos.pojos.CriarProdutoRequest;
import com.github.chiarelli.estoque_produtos.repositories.ProdutosRepository;

import lombok.experimental.var;

@SpringBootTest
@AutoConfigureMockMvc
public class ProdutosIntegrationTests {

  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper mapper;
  @Autowired ProdutosRepository produtosRepository;

  @Test
  void criarNovoProdutoValoresInvalidos() throws JsonProcessingException, Exception {
    // Prepare
    produtosRepository.deleteAll();
    var request1 = new CriarProdutoRequest("Me", BigDecimal.valueOf(-70.89), -52, UUID.randomUUID());

    // Act
    mockMvc.perform(post("/api/v1/produtos")
    .contentType(MediaType.APPLICATION_JSON)
    .content(mapper.writeValueAsString(request1))
    ).andExpect(status().isBadRequest())
    // Assert
    .andDo(result -> {
      var content = result.getResponse().getContentAsString();
      
      assertTrue(content.contains("\"preco\":\"must be greater than or equal to 0\""), "deveria apresentar mensagem \"preco deve ser maior que ou igual à 0\"");
      assertTrue(content.contains("\"nome\":\"size must be between 3 and 100\""), "deveria apresentar mensagem \"tamanho deve ser entre 3 e 100\"");
      assertTrue(content.contains("\"quantidade\":\"must be greater than or equal to 0\""), "deveria apresentar mensagem \"quantidade deve ser maior que ou igual à 0\"");
    });

    // Tentar criar produto com categoria inexistente

    // Prepare
    var catInvalidId = UUID.randomUUID();
    var request2 = new CriarProdutoRequest("Produto de Teste", BigDecimal.valueOf(500.99), 10, catInvalidId);
    
    // Act
    mockMvc.perform(post("/api/v1/produtos")
    .contentType(MediaType.APPLICATION_JSON)
    .content(mapper.writeValueAsString(request2))
    ).andExpect(status().isBadRequest())
    // Assert
    .andDo(result -> {
      var content = result.getResponse().getContentAsString();
      
      assertTrue(content.contains("{\"error\":\"categoria id "+catInvalidId.toString()+" not exists.\"}"), "deveria apresentar mensagem que categoria não existe");
    });
  }

  @Test
  void criarNovoProdutoMesmoNome() throws JsonProcessingException, Exception {
    // Prepare
    produtosRepository.deleteAll();

    // Stage 1
    var request1 = new CriarProdutoRequest("Meu primeiro produto", BigDecimal.valueOf(70.89), 52,
        UUID.fromString("022b570e-bf1b-4d90-b18a-4d75dfe291d1"));

    mockMvc.perform(post("/api/v1/produtos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request1))).andExpect(status().isCreated());

    // Act
    var request2 = new CriarProdutoRequest("meu  PrimEirO  pRoduTo ", BigDecimal.valueOf(20.00), 99,
        UUID.fromString("ab882be1-c518-4227-bca8-d5126b361b6a"));

    mockMvc.perform(post("/api/v1/produtos")
        .contentType(MediaType.APPLICATION_JSON)
        // Assert
        .content(mapper.writeValueAsString(request2))).andExpect(status().isBadRequest()).andDo(result -> {
          var content = result.getResponse().getContentAsString();

          assertTrue(
              content.contains(
                  "\"error\":\"Produto com o nome semelhante a \\\"%s\\\" já existe.\"".formatted(request2.getNome())),
              "deveria apresentar mensagem \"nome deve ser unico\"");
        });

    var request3 = new CriarProdutoRequest("Meuprimeiroproduto", BigDecimal.valueOf(85.55), 10,
        UUID.fromString("022b570e-bf1b-4d90-b18a-4d75dfe291d1"));

    mockMvc.perform(post("/api/v1/produtos")
        .contentType(MediaType.APPLICATION_JSON)
        // Assert
        .content(mapper.writeValueAsString(request3))).andExpect(status().isBadRequest()).andDo(result -> {
          var content = result.getResponse().getContentAsString();

          assertTrue(
              content.contains(
                  "\"error\":\"Produto com o nome semelhante a \\\"%s\\\" já existe.\"".formatted(request3.getNome())),
              "deveria apresentar mensagem \"nome deve ser unico\"");
        });

    var request4 = new CriarProdutoRequest("Meu primeiro produto", BigDecimal.valueOf(70.89), 52,
        UUID.fromString("022b570e-bf1b-4d90-b18a-4d75dfe291d1"));

    mockMvc.perform(post("/api/v1/produtos")
        .contentType(MediaType.APPLICATION_JSON)
        // Assert
        .content(mapper.writeValueAsString(request4))).andExpect(status().isBadRequest()).andDo(result -> {
          var content = result.getResponse().getContentAsString();

          assertTrue(
              content.contains(
                  "\"error\":\"Produto com o nome semelhante a \\\"%s\\\" já existe.\"".formatted(request4.getNome())),
              "deveria apresentar mensagem \"nome deve ser unico\"");
        });
    
    // Stage 2
    // Prepare
    var request5 = new CriarProdutoRequest("Produto da moça", BigDecimal.valueOf(85.55), 52,
        UUID.fromString("022b570e-bf1b-4d90-b18a-4d75dfe291d1"));

    mockMvc.perform(post("/api/v1/produtos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request5))).andExpect(status().isCreated());

    var request6 = new CriarProdutoRequest("pROdutO Da  moca ", BigDecimal.valueOf(85.55), 52,
        UUID.fromString("022b570e-bf1b-4d90-b18a-4d75dfe291d1"));

    mockMvc.perform(post("/api/v1/produtos")
        .contentType(MediaType.APPLICATION_JSON)
        // Assert
        .content(mapper.writeValueAsString(request6))).andExpect(status().isBadRequest()).andDo(result -> {
          var content = result.getResponse().getContentAsString();

          assertTrue(
              content.contains(
                  "\"error\":\"Produto com o nome semelhante a \\\"%s\\\" já existe.\"".formatted(request6.getNome())),
              "deveria apresentar mensagem \"nome deve ser unico\"");
        });

  }

  @Test
  void tentarExcluirProdutoComEstoque() {
    // TODO falta implementar
  }
}
