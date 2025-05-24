package com.github.chiarelli.estoque_produtos.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.chiarelli.estoque_produtos.pojos.CategoriaResponse;
import com.github.chiarelli.estoque_produtos.repositories.CategoriasRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("api/v1/categorias")
@Tag(name = "Categorias", description = "Recupera todas as categorias pré cadastradas.")
public class CategoriasController {

  @Autowired CategoriasRepository repository;

  @GetMapping
  @Operation(
    summary = "Listar categorias", 
    description = "Lista todas as categorias cadastradas em ordem alfabetica."
  ) 
  public List<CategoriaResponse> getAll() {
    var categorias = repository.findAll();
    if (Objects.isNull(categorias)) {
      return new ArrayList<CategoriaResponse>();
    }
    return categorias.stream()
      .map(cat -> CategoriaResponse.fromEntity(cat))
      .toList();
  }

  @GetMapping("{id}")
  @Operation(
    summary = "Retornar uma categoria", 
    description = "Retorna uma categoria específica pelo seu uuid.",
    responses = {
      @ApiResponse(
        responseCode = "200", 
        description = "Categoria encontrada"
      ),
      @ApiResponse(
        responseCode = "404", 
        description = "Categoria não encontrada"
      )
    }
  )
  public ResponseEntity<CategoriaResponse> getById(@RequestParam UUID id) {
    var categoria = repository.findById(id);
    if (categoria.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    var catResp = CategoriaResponse.fromEntity(categoria.get());
    return ResponseEntity.ok(catResp);
  }

}
