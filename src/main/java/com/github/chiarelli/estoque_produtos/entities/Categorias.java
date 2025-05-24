package com.github.chiarelli.estoque_produtos.entities;

import java.util.List;
import java.util.UUID;

import org.hibernate.validator.internal.util.stereotypes.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_categorias")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Categorias {

  @Id
  @Immutable
  private UUID id;
  
  @Column(name = "nome", nullable = false, unique = true, length = 100)
  @Size(min = 3, max = 50)
  private String nome;

  @OneToMany(mappedBy = "categoria", targetEntity = Produtos.class)
  private List<Produtos> produtos;

}
