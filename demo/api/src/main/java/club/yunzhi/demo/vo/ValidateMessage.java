package club.yunzhi.demo.vo;

/**
 * 验证码和对应用户名实体
 * @author jincheng
 */
public class ValidateMessage {
  private String username;

  private String verificationCode;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getVerificationCode() {
    return verificationCode;
  }

  public void setVerificationCode(String verificationCode) {
    this.verificationCode = verificationCode;
  }
}
