package com.mgr.api.repository;

import com.mgr.api.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long>, JpaSpecificationExecutor<Seller> {
}
