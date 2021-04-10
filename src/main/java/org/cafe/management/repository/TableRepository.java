package org.cafe.management.repository;

import org.cafe.management.domain.TableEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Long> {

    TableEntity findByName(String name);

    Page<TableEntity> findByUserId(Long id, Pageable pageable);

}
