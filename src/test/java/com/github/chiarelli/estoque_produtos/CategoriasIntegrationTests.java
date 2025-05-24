package com.github.chiarelli.estoque_produtos;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;

import com.github.chiarelli.estoque_produtos.controllers.CategoriasController;

@SpringBootTest
public class CategoriasIntegrationTests {

  @Autowired CategoriasController catCrl;

  @Test
  @SuppressWarnings("null")
	void listingPreinsertedCategories() {
    var cats = catCrl.getAll();
    assertTrue(cats.size() == 10, "Expected 10 categorias, but got " + cats.size());

    var verifyCategory = new HashMap<UUID, String>();
      verifyCategory.put(UUID.fromString("85aec76d-b899-4eb3-af61-01f4d9120f9b"), "Hortifruti");
      verifyCategory.put(UUID.fromString("022b570e-bf1b-4d90-b18a-4d75dfe291d1"), "Laticínios");
      verifyCategory.put(UUID.fromString("ab882be1-c518-4227-bca8-d5126b361b6a"), "Padaria");
      verifyCategory.put(UUID.fromString("48f9ab24-b953-4d75-b236-3c166f85c574"), "Mercearia");
    
    verifyCategory.forEach((catid, catName) -> {
      var resp = catCrl.getById(catid);
      
      var statusCode = resp.getStatusCode();
      assertTrue(statusCode.equals(HttpStatusCode.valueOf(200)), "Expected 200, but got " + statusCode);

      var cat = resp.getBody();
      assertFalse(Objects.isNull(cat), "Expected a category, but got null");

      if(Objects.isNull(cat)) {
        throw new RuntimeException("Expected a category, but got null");
      }
      assertTrue(cat.getId().equals(catid), "Expected " + catid + ", but got " + cat.getId());
      assertTrue(cat.getNome().equals(catName), "Expected " + catName + ", but got " + cat.getNome());
    });

	}

  @Test
  void retriveCategoryByInvalidId() {
    var id = UUID.randomUUID();
    var resp = catCrl.getById(id);

    HttpStatusCode status = resp.getStatusCode();
    assertTrue(status.equals(HttpStatusCode.valueOf(404)), "Expected 404, but got " + status);
  }
}
