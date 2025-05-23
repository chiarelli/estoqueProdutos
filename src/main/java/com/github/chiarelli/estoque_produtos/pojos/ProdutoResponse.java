package com.github.chiarelli.estoque_produtos.pojos;

import java.math.BigDecimal;
import java.util.UUID;

import com.github.chiarelli.estoque_produtos.entities.Produtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProdutoResponse {
  
  private UUID id;

  private String nome;
  
  private BigDecimal preco;
  
  private Integer quantidade;

  private CategoriaResponse categoria;

  public static ProdutoResponse fromEntity(Produtos produto) {
    return new ProdutoResponse(produto.getId(), produto.getNome(), produto.getPreco(), produto.getQuantidade(), CategoriaResponse.fromEntity(produto.getCategoria()));
  }
  
}
