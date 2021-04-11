package org.cafe.management.repository;

import org.cafe.management.domain.OrderEntity;
import org.cafe.management.enums.OrderStatusType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ali Karimizandi
 * @since 2021
 */
@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Page<OrderEntity> findByTableNameAndTableUserId(String tableName, Long tableUserId, Pageable pageable);

    boolean existsByTableNameAndStatus(String tableName, OrderStatusType status);
}
