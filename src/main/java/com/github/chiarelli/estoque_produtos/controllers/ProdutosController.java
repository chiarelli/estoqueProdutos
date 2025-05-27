package com.github.chiarelli.estoque_produtos.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.chiarelli.estoque_produtos.pojos.CriarProdutoRequest;
import com.github.chiarelli.estoque_produtos.pojos.ProdutoRequest;
import com.github.chiarelli.estoque_produtos.pojos.ProdutoResponse;
import com.github.chiarelli.estoque_produtos.usercases.ProdutoUsercase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/produtos")
public class ProdutosController {

  @Autowired ProdutoUsercase pUsercase;

  @PostMapping
  public ResponseEntity<ProdutoResponse> register(@RequestBody @Valid CriarProdutoRequest produto) throws Exception {
    var resp = pUsercase.registrarProduto(produto);
    return ResponseEntity.status(HttpStatus.CREATED).body(resp);
  }

  @GetMapping
  public ProdutoResponse getAll() {
    throw new UnsupportedOperationException("Not yet implemented method getAll");
  }

  @GetMapping("{id}")
  public ProdutoResponse getById(@RequestParam UUID id) {
    throw new UnsupportedOperationException("Not yet implemented method getById");
  }
  
  @PatchMapping("{id}")
  public ProdutoResponse update(@RequestParam UUID id, @RequestBody @Valid ProdutoRequest request) {
    throw new UnsupportedOperationException("Not yet implemented method update");
  }

  @DeleteMapping("{id}")
  public void delete(@RequestParam UUID id) {
    throw new UnsupportedOperationException("Not yet implemented method delete");
  }

}
