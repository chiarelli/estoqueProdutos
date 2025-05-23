package com.github.chiarelli.estoque_produtos.pojos;

import java.math.BigDecimal;
import java.util.UUID;

public class CriarProdutoRequest extends ProdutoRequest {

  public CriarProdutoRequest(String nome, BigDecimal preco, Integer quantidade, UUID categoria_id) {
    super(nome, preco, quantidade, categoria_id);
  }
  
}
