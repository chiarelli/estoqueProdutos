package com.github.chiarelli.estoque_produtos.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** 
 * Exceptions to display to the REST API client
*/
public class UIException extends RuntimeException {

  protected Map<String, Object> userMessages = new HashMap<>(); 

  public UIException(String userMessage, String message, Throwable cause) {
    super(message, cause);
    userMessages.put("error", userMessage);
  }

  public UIException(Map<String, Object> userMessages, String message, Throwable cause) {
    super(message, cause);
    if(Objects.nonNull(userMessages)) {
      this.userMessages = userMessages;
    }
  }

  public UIException(String userMessage) {
    super(userMessage);
    userMessages.put("error", userMessage);
  }

  public UIException(Map<String, Object> userMessages) {
    super();
    if(Objects.nonNull(userMessages)) {
      this.userMessages = userMessages;
    }
  }

  public Map<String, Object> getUserMessages() {
    return userMessages;
  }

}
