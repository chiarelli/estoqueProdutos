package com.github.chiarelli.estoque_produtos.handlers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.core.JsonParseException;
import com.github.chiarelli.estoque_produtos.exceptions.NotFoundException;
import com.github.chiarelli.estoque_produtos.exceptions.UIException;

@ControllerAdvice
public class RestAPIExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, String> erros = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error -> erros.put(error.getField(), error.getDefaultMessage()));
    return new ResponseEntity<>(erros, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<Map<String, Object>> handleUIException(NotFoundException ex) {
    return new ResponseEntity<>(ex.getUserMessages(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(UIException.class)
  public ResponseEntity<Map<String, Object>> handleUIException(UIException ex) {
    return new ResponseEntity<>(ex.getUserMessages(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
    var cause = ex.getCause();
    if (cause instanceof JsonParseException) {
      return handleJsonParseException((JsonParseException) cause);
    }
    return handleThrowable(cause);
  }

  @ExceptionHandler(JsonParseException.class)
  public ResponseEntity<Map<String, Object>> handleJsonParseException(JsonParseException ex) {
    return new ResponseEntity<>(Map.of("error", "JSON inválido"), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Throwable.class)
  public ResponseEntity<Map<String, Object>> handleThrowable(Throwable ex) {
    // Logue para investigação (log completo no back-end)
    ex.printStackTrace();

    return ResponseEntity
      .status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(Map.of(
        "error", "Erro interno no servidor. Tente novamente mais tarde."
      ));
  }

}
