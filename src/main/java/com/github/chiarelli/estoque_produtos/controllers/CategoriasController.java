package com.github.chiarelli.estoque_produtos.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.chiarelli.estoque_produtos.pojos.CategoriaResponse;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("api/v1/categorias")
public class CategoriasController {

  @GetMapping
  public List<CategoriaResponse> getAll() {
    throw new UnsupportedOperationException("Not yet implemented method getAll");
  }

  @GetMapping("{id}")
  public CategoriaResponse getById(@RequestParam UUID id) {
    throw new UnsupportedOperationException("Not yet implemented method getById");
  }

}
