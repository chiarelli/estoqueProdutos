package com.github.chiarelli.estoque_produtos.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.github.chiarelli.estoque_produtos.entities.Categorias;

@Repository
public interface CategoriasRepository extends JpaRepository<Categorias, UUID> {

  @SuppressWarnings("null")
  @Override
  @Query("SELECT c FROM Categorias c ORDER BY c.nome ASC")
  List<Categorias> findAll();

}
