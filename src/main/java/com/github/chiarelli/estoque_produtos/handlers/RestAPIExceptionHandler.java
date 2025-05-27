package com.github.chiarelli.estoque_produtos.handlers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.github.chiarelli.estoque_produtos.exceptions.UIException;

@ControllerAdvice
public class RestAPIExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, String> erros = new HashMap<>();
    ex.getBindingResult().getFieldErrors().forEach(error -> erros.put(error.getField(), error.getDefaultMessage()));
    return new ResponseEntity<>(erros, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(UIException.class)
  public ResponseEntity<Map<String, Object>> handleUIException(UIException ex) {
    return new ResponseEntity<>(ex.getMessages(), HttpStatus.BAD_REQUEST);
  }

}
