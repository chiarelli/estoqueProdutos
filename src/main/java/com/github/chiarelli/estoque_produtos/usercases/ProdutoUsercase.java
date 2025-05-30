package com.github.chiarelli.estoque_produtos.usercases;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.github.chiarelli.estoque_produtos.entities.Categorias;
import com.github.chiarelli.estoque_produtos.entities.Produtos;
import com.github.chiarelli.estoque_produtos.exceptions.NotFoundException;
import com.github.chiarelli.estoque_produtos.exceptions.UIException;
import com.github.chiarelli.estoque_produtos.pojos.CriarProdutoRequest;
import com.github.chiarelli.estoque_produtos.pojos.ProdutoRequest;
import com.github.chiarelli.estoque_produtos.pojos.ProdutoResponse;
import com.github.chiarelli.estoque_produtos.repositories.CategoriasRepository;
import com.github.chiarelli.estoque_produtos.repositories.ProdutosRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Component
public class ProdutoUsercase {

  @Autowired ProdutosRepository repository;
  @Autowired CategoriasRepository catRepository;
  @Autowired Validator validator;

  /**
   * Registers a new product in the system.
   *
   * <p>This method validates and saves a new product entity based on the given
   * {@link CriarProdutoRequest}. It checks for the existence of the product's
   * category and throws a {@link UIException} if the category is not found.
   * After ensuring the product entity is valid, it attempts to save it to the
   * database. If a product with a similar name already exists, a
   * {@link UIException} is thrown.
   *
   * @param produto the product creation request containing details such as name,
   *                price, quantity, and category ID
   * @return a {@link ProdutoResponse} representing the successfully created product
   * @throws UIException if the category does not exist or if a product with a
   *                     similar name already exists
   * @throws DataIntegrityViolationException if a database integrity violation
   *                                         occurs during the save operation
   */

  public ProdutoResponse registrarProduto(CriarProdutoRequest produto) {
    Optional<Categorias> opCat = catRepository.findById(produto.getCategoria_id());
    if (opCat.isEmpty()) {
      // Categoria nao encontrada, lança uma exception
      throw new UIException("categoria id " + produto.getCategoria_id().toString() + " not exists.");
    }
    var entity = new Produtos(
        UUID.randomUUID(), // gera um uuid randomico para o novo produto
        produto.getNome(),
        produto.getPreco(),
        produto.getQuantidade(),
        opCat.get());

    // Valida a entidade antes de salvar no banco
    validateEntity(entity);

    try {
      // Salva a entidade no banco
      entity = repository.save(entity);

    } catch (DataIntegrityViolationException ex) {
      // Caso o nome do produto esteja duplicado, isso é verificado quando message do erro contém
      // a constraint "uk_produtos_nome", é lançada uma UIException
      if (ex.getMessage().contains("duplicate key value violates unique constraint \"uk_produtos_nome")) {
        throw new UIException(
            "Produto com o nome semelhante a \"%s\" já existe.".formatted(produto.getNome()),
            ex.getMessage(),
            ex);
      }
      // Caso seja outro erro, lança a exception
      throw ex;
    }

    

    return ProdutoResponse.fromEntity(entity);
  }

  public List<ProdutoResponse> listarProdutos() {
    var produtos = repository.findAll();

    return produtos.stream()
      .map(produto -> ProdutoResponse.fromEntity(produto))
      .toList();
  }
  
  public ProdutoResponse retornarPorId(UUID id) {
    var produto = repository.findById(id);
    if (produto.isEmpty()) {
      throw new NotFoundException("produto id " + id.toString() + " not exists.");
    }
    return ProdutoResponse.fromEntity(produto.get());
  }
  public void excluirProduto(UUID id) {
    var produto = repository.findById(id);
    if (produto.isEmpty()) {
      return; // Se o produto nao existir, nao faz nada
    }

    if(produto.get().getQuantidade() > 0) {
      Map<String, Object> userMessages = new LinkedHashMap<>();
        userMessages.put("erro", "Produto com quantidade maior que zero não pode ser excluído");
        userMessages.put("quantidade", "Quantidade atual: " + produto.get().getQuantidade());
      throw new UIException(userMessages);
    }

    repository.delete(produto.get());
  }

  /**
   * Updates a product with a given id, using the provided request.
   * 
   * @param id      the id of the product to be updated
   * @param produto the request with the new product data
   * @return a response with the updated product data
   * 
   * @throws UIException if the category does not exist
   * @throws DataIntegrityViolationException if a database integrity violation
   *                                         occurs during the save operation
   * @throws NotFoundException if the product id does not exist
   */
  public ProdutoResponse atualizarProduto(UUID id, ProdutoRequest produto) {
    Optional<Categorias> opCat = catRepository.findById(produto.getCategoria_id());
    if (opCat.isEmpty()) {
      // Categoria nao encontrada, lança uma exception
      throw new UIException("categoria id " + produto.getCategoria_id().toString() + " not exists.");
    }

    var opcional = repository.findById(id);
    if (opcional.isEmpty()) {
      throw new NotFoundException("produto id " + id.toString() + " not exists.");
    }
    var entity = opcional.get();
      entity.setNome(produto.getNome());
      entity.setPreco(produto.getPreco());
      entity.setQuantidade(produto.getQuantidade());
      entity.setCategoria(opCat.get());

    validateEntity(entity);

    entity = repository.save(entity);
    return ProdutoResponse.fromEntity(entity);
  }  
  
  /**
   * Validates the given {@link Produtos} entity against the constraints defined in the class.
   * If validation errors are found, a {@link UIException} is thrown containing user-friendly
   * error messages and an optional developer message for logging or debugging.
   *
   * <p>The user messages are organized in a map where the key is the field name and the value
   * is the error message. The developer message is a concatenation of all violations, providing
   * a more detailed context for debugging purposes.
   *
   * @param entity the {@link Produtos} entity to be validated
   * @throws UIException if validation errors are present
   */
   public void validateEntity(Produtos entity) {
    Set<ConstraintViolation<Produtos>> violations = validator.validate(entity);

    if (violations.isEmpty()) {
      return; // Nenhuma violação encontrada
    }

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

    // Lançamento da exception
    var cause = new RuntimeException(devMessage.toString());
    throw new UIException(userMessages, cause.getMessage(), cause);
  }

}
