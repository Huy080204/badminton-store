package com.mgr.api.repository;

import com.mgr.api.model.Nation;
import com.mgr.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NationRepository extends JpaRepository<Nation, Long>, JpaSpecificationExecutor<Nation> {
    boolean existsByNameAndParent(String name, Nation parent);
    boolean existsByNameAndParentAndIdNot(String name, Nation parent, Long id);
    boolean existsByParent(Nation parent);
    List<Nation> findByParent(Nation parent);
    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.data.jpa.repository.Query("DELETE FROM Nation n WHERE n.id = :id")
    void hardDeleteById(@org.springframework.data.repository.query.Param("id") Long id);
}