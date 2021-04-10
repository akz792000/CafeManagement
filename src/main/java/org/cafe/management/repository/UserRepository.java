package org.cafe.management.repository;

import org.cafe.management.domain.UserEntity;
import org.cafe.management.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Ali Karimizandi
 * @since 2021
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByUsername(String username);

    List<UserEntity> findByRoleType(RoleType roleType);

}
