package com.github.chiarelli.estoque_produtos.exceptions;

import java.util.Map;

public class NotFoundException extends UIException {

  public NotFoundException(Map<String, Object> userMessages) {
    super(userMessages);
  }

  public NotFoundException(String userMessage) {
    super(userMessage);
  }

  public NotFoundException(String userMessage, String message, Throwable cause) {
    super(userMessage, message, cause);
  }

  public NotFoundException(Map<String, Object> userMessages, String message, Throwable cause) {
    super(userMessages, message, cause);
  }

}
