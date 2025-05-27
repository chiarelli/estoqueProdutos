package com.github.chiarelli.estoque_produtos.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.chiarelli.estoque_produtos.entities.Produtos;

@Repository
public interface ProdutosRepository extends JpaRepository<Produtos, UUID> {

}
