package com.mgr.api.repository;

import com.mgr.api.model.Nation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NationRepository extends JpaRepository<Nation, Long>, JpaSpecificationExecutor<Nation> {
    boolean existsByNameAndParent(String name, Nation parent);

    @Modifying
    @Query("DELETE FROM Nation n WHERE n.parent.id IN (SELECT d.id FROM Nation d WHERE d.parent.id = :provinceId)")
    void deleteGrandchildrenByProvinceId(@Param("provinceId") Long provinceId);

    @Modifying
    @Query("DELETE FROM Nation n WHERE n.parent.id = :parentId")
    void deleteByParentId(@Param("parentId") Long parentId);
}
