package com.github.chiarelli.estoque_produtos.pojos;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProdutoRequest {

  @NotBlank
  @Size(min = 3, max = 100)
  private String nome;
  
  @Min(0)
  private BigDecimal preco;
  
  @Min(0)
  private Integer quantidade;

  @Nonnull
  private UUID categoria_id;

}
