package org.cafe.management.repository;

import org.cafe.management.domain.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Long> {

    TableEntity findByName(String name);

}
