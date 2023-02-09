package club.yunzhi.demo.repository;

import club.yunzhi.demo.entity.SoftDeleteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * 软删除仓库工厂
 * 1. 继承了SoftDelete的，返回自定义
 * 1. 没有继承的，返回原SPRING JPA DATA Bean
 */
public class SoftDeleteRepositoryFactoryBean<R extends JpaRepository<T, Serializable>, T> extends JpaRepositoryFactoryBean<R, T, Serializable> {
  public SoftDeleteRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
    super(repositoryInterface);
  }

  @Override
  protected RepositoryFactorySupport createRepositoryFactory(final EntityManager entityManager) {
    return new JpaRepositoryFactory(entityManager) {
      protected SimpleJpaRepository<T, Serializable> getTargetRepository(
          RepositoryInformation information, EntityManager entityManager) {
        Class<T> domainClass = (Class<T>) information.getDomainType();
        if(SoftDeleteEntity.class.isAssignableFrom(domainClass)) {
          return new SoftDeleteRepositoryImpl(domainClass, entityManager);
        } else {
          return new SimpleJpaRepository(domainClass, entityManager);
        }
      }

      @Override
      protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
        return metadata.getDomainType().isAssignableFrom(SoftDeleteEntity.class) ? SoftDeleteRepositoryImpl.class : SimpleJpaRepository.class;
      }
    };
  }
}
