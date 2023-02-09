package club.yunzhi.demo.entity;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * 软删除
 */
@MappedSuperclass
public abstract class SoftDeleteEntity {
  @JsonView(BaseEntity.DeleteAtJsonView.class)
  @Column(nullable = false)
  protected Long deleteAt = 0L;

  @JsonView(BaseEntity.DeletedJsonView.class)
  protected Boolean deleted = false;

  public Long getDeleteAt() {
    return deleteAt;
  }

  public void setDeleteAt(Long deleteAt) {
    this.deleteAt = deleteAt;
  }

  public Boolean getDeleted() {
    return deleted;
  }

  public void setDeleted(Boolean deleted) {
    this.deleted = deleted;
  }
}
