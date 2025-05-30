package com.github.chiarelli.estoque_produtos;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
import com.github.chiarelli.estoque_produtos.pojos.ProdutoRequest;
import com.github.chiarelli.estoque_produtos.pojos.ProdutoResponse;
import com.github.chiarelli.estoque_produtos.repositories.ProdutosRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class ProdutosIntegrationTests {

  private static final String CAT_DEFAULT_ID = "022b570e-bf1b-4d90-b18a-4d75dfe291d1";

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
        UUID.fromString(CAT_DEFAULT_ID));

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
        UUID.fromString(CAT_DEFAULT_ID));

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
        UUID.fromString(CAT_DEFAULT_ID));

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
        UUID.fromString(CAT_DEFAULT_ID));

    mockMvc.perform(post("/api/v1/produtos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request5))).andExpect(status().isCreated());

    var request6 = new CriarProdutoRequest("pROdutO Da  moca ", BigDecimal.valueOf(85.55), 52,
        UUID.fromString(CAT_DEFAULT_ID));

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
  void retornarProdutoPorId() throws JsonProcessingException, Exception {
    // Prepare
    produtosRepository.deleteAll();
    ProdutoResponse produto1;
    ProdutoResponse produto2;
    UUID productInvalidId = UUID.randomUUID();

    var request1 = new CriarProdutoRequest("Meu primeiro produto", BigDecimal.valueOf(70.89), 10,
        UUID.fromString(CAT_DEFAULT_ID));

    var result1 = mockMvc.perform(post("/api/v1/produtos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request1)))
        .andExpect(status().isCreated())
        .andReturn();

    var content = result1.getResponse().getContentAsString();
    produto1 = mapper.readValue(content, ProdutoResponse.class);
        
    var request2 = new CriarProdutoRequest("Produto da moça", BigDecimal.valueOf(85.55), 52,
        UUID.fromString(CAT_DEFAULT_ID));

    var result2 = mockMvc.perform(post("/api/v1/produtos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request2)))
        .andExpect(status().isCreated())
        .andReturn();

    var content2 = result2.getResponse().getContentAsString();
    produto2 = mapper.readValue(content2, ProdutoResponse.class);

    // Ack
    mockMvc.perform(get("/api/v1/produtos/{id}", produto1.getId().toString()))
      .andExpect(status().isOk())
      .andDo(result -> {
        var contents = result.getResponse().getContentAsString();
        var resp = mapper.readValue(contents, ProdutoResponse.class);
        assertEquals(produto1.getId(), resp.getId());
        assertEquals(produto1.getNome(), resp.getNome());
        assertEquals(produto1.getPreco(), resp.getPreco());
        assertEquals(produto1.getQuantidade(), resp.getQuantidade());
        assertEquals(produto1.getCategoria().getId(), resp.getCategoria().getId());
        assertEquals(produto1.getCategoria().getNome(), resp.getCategoria().getNome());
      });

    mockMvc.perform(get("/api/v1/produtos/{id}", produto2.getId().toString()))
      .andExpect(status().isOk())
      .andDo(result -> {
        var contents = result.getResponse().getContentAsString();
        var resp = mapper.readValue(contents, ProdutoResponse.class);
        assertEquals(produto2.getId(), resp.getId());
        assertEquals(produto2.getNome(), resp.getNome());
        assertEquals(produto2.getPreco(), resp.getPreco());
        assertEquals(produto2.getQuantidade(), resp.getQuantidade());
        assertEquals(produto2.getCategoria().getId(), resp.getCategoria().getId());
        assertEquals(produto2.getCategoria().getNome(), resp.getCategoria().getNome());
      });

    mockMvc.perform(get("/api/v1/produtos/{id}", productInvalidId.toString()))
      .andExpect(status().isNotFound());
  }

  @Test
  void atualizarProduto() throws JsonProcessingException, Exception {
    // prepare
    produtosRepository.deleteAll();

    var request1 = new CriarProdutoRequest("Produto para ser atualizado", BigDecimal.valueOf(41.99), 90,
        UUID.fromString(CAT_DEFAULT_ID));

    var result1 = mockMvc.perform(post("/api/v1/produtos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request1)))
        .andExpect(status().isCreated())
        .andReturn();

    var content = result1.getResponse().getContentAsString();
    var product1 = mapper.readValue(content, ProdutoResponse.class);

    // Act
    // Atualizar produto com nome, preco e quantidade invalidos
    var request2 = new ProdutoRequest("Produto para ser atualizado", BigDecimal.valueOf(-41.99), -90, UUID.fromString(CAT_DEFAULT_ID));

    mockMvc.perform(put("/api/v1/produtos/{id}", product1.getId().toString())
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request2)))
        .andExpect(status().isBadRequest());

    // Atualizar produto com uma categoria inexistente
    var catInvalidId = UUID.randomUUID();
    var request3 = new ProdutoRequest("Produto para ser atualizado", BigDecimal.valueOf(41.99), 90, catInvalidId);

    mockMvc.perform(put("/api/v1/produtos/{id}", product1.getId().toString())
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request3)))
        .andExpect(status().isBadRequest());

    // Atualizar produto com valores validos
    var request4 = new ProdutoRequest("Produto para ser atualizado", BigDecimal.valueOf(0), 0, UUID.fromString("ab882be1-c518-4227-bca8-d5126b361b6a"));

    var result4 = mockMvc.perform(put("/api/v1/produtos/{id}", product1.getId().toString())
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request4)))
        .andExpect(status().isOk())
        .andReturn();

    var content4 = result4.getResponse().getContentAsString();
    var product4 = mapper.readValue(content4, ProdutoResponse.class);

    assertEquals(product4.getPreco(), BigDecimal.valueOf(0));
    assertEquals(product4.getPreco(), BigDecimal.valueOf(0));
    assertEquals(product4.getCategoria().getId(), UUID.fromString("ab882be1-c518-4227-bca8-d5126b361b6a"));
    assertEquals(product4.getNome(), "Produto para ser atualizado");
    assertTrue(product4.getUpdatedAt().after(product1.getUpdatedAt()));

  }

  @Test
  void excluirProduto() throws Exception {
    // prepare
    produtosRepository.deleteAll();

    // Excluir um produto com que não existe
    mockMvc.perform(delete("/api/v1/produtos/{id}", UUID.randomUUID().toString()))
        .andExpect(status().isNoContent());

    // Criar produto com estoque maior que zero
    var request1 = new CriarProdutoRequest("Produto para ser excluido", BigDecimal.valueOf(41.99), 90,
        UUID.fromString(CAT_DEFAULT_ID));

    var result1 = mockMvc.perform(post("/api/v1/produtos")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request1)))
        .andExpect(status().isCreated())
        .andReturn();

    var content = result1.getResponse().getContentAsString();
    var product1 = mapper.readValue(content, ProdutoResponse.class);

    // Excluir produto com estoque maior que zero
    mockMvc.perform(delete("/api/v1/produtos/{id}", product1.getId().toString()))
        .andExpect(status().isBadRequest());

    // Atualizar produto com estoque igual a zero
    var request2 = new ProdutoRequest(product1.getNome(), product1.getPreco(), 0, product1.getCategoria().getId());

    mockMvc.perform(put("/api/v1/produtos/{id}", product1.getId().toString())
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(request2)))
        .andExpect(status().isOk());

    // Excluir produto com estoque igual a zero
    mockMvc.perform(delete("/api/v1/produtos/{id}", product1.getId().toString()))
        .andExpect(status().isNoContent());
  }
}
