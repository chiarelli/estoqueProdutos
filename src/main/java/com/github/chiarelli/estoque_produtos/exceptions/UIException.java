package com.github.chiarelli.estoque_produtos.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** 
 * Exceptions to display to the REST API client
*/
public class UIException extends RuntimeException {
  protected Map<String, Object> messages = new HashMap<>(); 

  public UIException(String userMessage, String message, Throwable cause) {
    super(message, cause);
    messages.put("error", userMessage);
  }

  public UIException(Map<String, Object> userMessages, String message, Throwable cause) {
    super(message, cause);
    if(Objects.nonNull(userMessages)) {
      messages = userMessages;
    }
  }
  public UIException(String userMessage, String message) {
    super(message);
    messages.put("error", userMessage);
  }

  public UIException(Map<String, Object> userMessages, String message) {
    super(message);
    if(Objects.nonNull(userMessages)) {
      messages = userMessages;
    }
  }

  public Map<String, Object> getMessages() {
    return messages;
  }

}
