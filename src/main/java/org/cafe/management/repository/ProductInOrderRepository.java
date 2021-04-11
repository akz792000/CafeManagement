package org.cafe.management.repository;

import org.cafe.management.domain.ProductEntity;
import org.cafe.management.domain.ProductInOrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * @author Ali Karimizandi
 * @since 2021
 */
@Repository
public interface ProductInOrderRepository extends JpaRepository<ProductInOrderEntity, Long> {

    @Query("select U from org.cafe.management.domain.ProductInOrderEntity as U left join U.order as O where O.id = :orderId")
    Page<ProductInOrderEntity> findAllByOrderId(Long orderId, Pageable pageable);

}
