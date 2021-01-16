// Created by huub
// Creation date 2020-12-14

package sofa.internetbankieren.backing_bean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginFormBackingBean {

  private Logger logger = LoggerFactory.getLogger(LoginFormBackingBean.class);

  private String userName;
  private String password;

  public LoginFormBackingBean(String userName, String password) {
    super();
    this.userName = userName;
    this.password = password;
  }

  public Logger getLogger() {
    return logger;
  }

  public void setLogger(Logger logger) {
    this.logger = logger;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
}
