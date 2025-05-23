package com.github.chiarelli.estoque_produtos.pojos;

import java.util.UUID;

import com.github.chiarelli.estoque_produtos.entities.Categorias;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CategoriaResponse {
  
  private UUID id;
  
  private String nome;

  public static CategoriaResponse fromEntity(Categorias categoria) {
    return new CategoriaResponse(categoria.getId(), categoria.getNome());
  }
  
}
