package com.mgr.api.repository;

import com.mgr.api.model.Nation;
import com.mgr.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NationRepository extends JpaRepository<Nation, Long>, JpaSpecificationExecutor<Nation> {
    boolean existsByNameAndParent(String name, Nation parent);
    boolean existsByNameAndParentAndIdNot(String name, Nation parent, Long id);
    boolean existsByParent(Nation parent);
}