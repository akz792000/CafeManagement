package org.cafe.management.repository;

import org.cafe.management.domain.ProductInOrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Ali Karimizandi
 * @since 2021
 */
@Repository
public interface ProductInOrderRepository extends JpaRepository<ProductInOrderEntity, Long> {

}
