package com.github.chiarelli.estoque_produtos.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.chiarelli.estoque_produtos.pojos.CriarProdutoRequest;
import com.github.chiarelli.estoque_produtos.pojos.ProdutoRequest;
import com.github.chiarelli.estoque_produtos.pojos.ProdutoResponse;
import com.github.chiarelli.estoque_produtos.usercases.ProdutoUsercase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
  public List<ProdutoResponse> getAll() {
    var resp = pUsercase.listarProdutos();
    return resp;
  }

  @GetMapping("{id}")
  public ProdutoResponse getById(@PathVariable UUID id) {
    var resp = pUsercase.retornarPorId(id);
    return resp;
  }
  
  @PutMapping("{id}")
  public ProdutoResponse update(@PathVariable UUID id, @RequestBody @Valid ProdutoRequest request) {
    var resp = pUsercase.atualizarProduto(id, request);
    return resp;
  }

  @DeleteMapping("{id}")
  @Operation(
    summary = "Excluir um produto", 
    description = "Exclui um produto pelo seu uuid.",
    responses = {
      @ApiResponse(
        responseCode = "204", 
        description = "Produto excluído"
      ),
      @ApiResponse(
        responseCode = "400", 
        description = "Erro se tentar excluir produto com quantidade maior que zero"
      )
    }
  )
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    pUsercase.excluirProduto(id);
    return ResponseEntity.noContent().build();
  }

}
