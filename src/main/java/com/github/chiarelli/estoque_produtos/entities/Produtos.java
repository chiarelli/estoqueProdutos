package com.github.chiarelli.estoque_produtos.entities;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.validator.internal.util.stereotypes.Immutable;

import com.github.slugify.Slugify;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
  name = "tb_produtos",
  uniqueConstraints = {
    @UniqueConstraint(name = "uk_produtos_nome_slug", columnNames = "nome_slug"),
    @UniqueConstraint(name = "uk_produtos_nome", columnNames = "nome"),
  }
)
@NoArgsConstructor
@Getter
public class Produtos {

  @Id
  @Immutable
  private UUID id;

  @Column(name = "nome", nullable = false, unique = true, length = 100)
  @Size(min = 3, max = 100)
  private String nome;
  
  @Column(name = "nome_slug", nullable = false, unique = true)
  private String nomeSlug;
  
  @Column(name = "preco", nullable = false)
  @Min(0)
  private BigDecimal preco;
  
  @Column(name = "quantidade", nullable = false)
  @Min(0)
  private Integer quantidade;

  @Column(name = "created_at", nullable = false)
  private Date createdAt;
  
  @Column(name = "updated_at", nullable = false)
  private Date updatedAt;
  
  @ManyToOne
  @JoinColumn(name = "categoria_id", nullable = false)
  private Categorias categoria;

  @Transient
  final Slugify slg = Slugify.builder().build();

  public Produtos(
    UUID id, 
    @Size(min = 3, max = 100) String nome, 
    @Min(0) BigDecimal preco, 
    @Min(0) Integer quantidade, 
    Date createdAt,
    Date updatedAt,
    Categorias categoria
  ) {
    this.id = id;
    setNome(nome);
    this.preco = preco;
    this.quantidade = quantidade;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.categoria = categoria;
  }

  public Produtos(
    UUID id, 
    @Size(min = 3, max = 100) String nome, 
    @Min(0) BigDecimal preco, 
    @Min(0) Integer quantidade, 
    Categorias categoria
  ) {
    this.id = id;
    setNome(nome);
    this.preco = preco;
    this.quantidade = quantidade;
    this.categoria = categoria;
  }

  public void setNome(@Size(min = 3, max = 100) String nome) {
    this.nome = nome;
    generateSlug();
  }

  public void setPreco(@Min(0) BigDecimal preco) {
    this.preco = preco;
  }

  public void setQuantidade(@Min(0) Integer quantidade) {
    this.quantidade = quantidade;
  }

  public void setCategoria(Categorias categoria) {
    if (Objects.nonNull(categoria)) {
      this.categoria = categoria;
    }
  }

  @PrePersist
  void OnCreate() {
    generateSlug();  
    defineCreatedAt();
    defineUpdatedAt();
  }
  
  @PreUpdate
  void OnUpdate() {
    generateSlug(); 
    defineUpdatedAt();
  }

  private void generateSlug() {
    if (nome != null) {
      this.nomeSlug = slg.slugify(nome).replace("-", "");
    }
  }

  private void defineCreatedAt() {
    if (Objects.isNull(createdAt)) {
      this.createdAt = new Date();
    }
  }

  private void defineUpdatedAt() {
    this.updatedAt = new Date();
  }

}
