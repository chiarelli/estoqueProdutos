package com.github.chiarelli.estoque_produtos.usercases;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.github.chiarelli.estoque_produtos.entities.Categorias;
import com.github.chiarelli.estoque_produtos.entities.Produtos;
import com.github.chiarelli.estoque_produtos.exceptions.UIException;
import com.github.chiarelli.estoque_produtos.pojos.CriarProdutoRequest;
import com.github.chiarelli.estoque_produtos.pojos.ProdutoResponse;
import com.github.chiarelli.estoque_produtos.repositories.CategoriasRepository;
import com.github.chiarelli.estoque_produtos.repositories.ProdutosRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Component
public class ProdutoUsercase {

  @Autowired
  ProdutosRepository repository;
  @Autowired
  CategoriasRepository catRepository;
  @Autowired
  Validator validator;

  public ProdutoResponse registrarProduto(CriarProdutoRequest produto) {
    Optional<Categorias> opCat = catRepository.findById(produto.getCategoria_id());
    if (opCat.isEmpty()) {
      throw new UIException("categoria id " + produto.getCategoria_id().toString() + " not exists.", "");
    }
    var entity = new Produtos(
        UUID.randomUUID(),
        produto.getNome(),
        produto.getPreco(),
        produto.getQuantidade(),
        opCat.get());

    validateEntity(entity);

    try {
      repository.save(entity);

    } catch (DataIntegrityViolationException ex) {
      if (ex.getMessage().contains("duplicate key value violates unique constraint \"uk_produtos_nome")) {
        throw new UIException(
            "Produto com o nome semelhante a \"%s\" já existe.".formatted(produto.getNome()),
            ex.getMessage(),
            ex);
      }
      throw ex;
    }

    return ProdutoResponse.fromEntity(entity);
  }

  private void validateEntity(Produtos entity) {
    Set<ConstraintViolation<Produtos>> violations = validator.validate(entity);

    if (!violations.isEmpty()) {
      Map<String, Object> userMessages = new LinkedHashMap<>();
      StringBuilder devMessage = new StringBuilder();

      for (ConstraintViolation<Produtos> violation : violations) {
        String campo = violation.getPropertyPath().toString();
        String msg = violation.getMessage();

        // Preenche map com mensagens para o usuário
        userMessages.put(campo, msg);

        // Mensagem técnica (opcional, útil para logs)
        devMessage.append(campo)
            .append(": ")
            .append(msg)
            .append(" | ");
      }

      throw new UIException(userMessages, devMessage.toString());
    }
  }

}
