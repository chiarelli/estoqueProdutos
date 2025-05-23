package com.github.chiarelli.estoque_produtos.entities;

import java.math.BigDecimal;
import java.util.UUID;

import org.hibernate.validator.internal.util.stereotypes.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Entity
@Table(name = "tb_produtos")
@AllArgsConstructor
@Getter
public class Produtos {

  @Id
  @Immutable
  private UUID id;

  @Column(name = "nome", nullable = false, unique = true, length = 100)
  private String nome;
  
  @Column(name = "preco", nullable = false)
  @Min(0)
  private BigDecimal preco;
  
  @Column(name = "quantidade", nullable = false)
  @Min(0)
  private Integer quantidade;
  
  @ManyToOne
  @JoinColumn(name = "categoria_id", nullable = false)
  private Categorias categoria;

}
