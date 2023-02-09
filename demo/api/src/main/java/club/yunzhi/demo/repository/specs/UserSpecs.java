package club.yunzhi.demo.repository.specs;

import club.yunzhi.demo.entity.User;
import org.springframework.data.jpa.domain.Specification;

/**
 * 用户
 */
public class UserSpecs {
  public static Specification<User> containingName(String name) {
    if (name != null) {
      return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("name").as(String.class), String.format("%%%s%%", name));
    } else {
      return Specification.where(null);
    }
  }

  public static Specification<User> equalUsername(String username) {
    if (username == null) {
      return Specification.where(null);
    }
    return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("username").as(String.class), String.format("%s%%", username));
  }
}
