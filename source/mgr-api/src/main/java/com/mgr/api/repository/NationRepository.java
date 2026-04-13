package com.mgr.api.repository;

import com.mgr.api.model.Nation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.validation.constraints.NotEmpty;
import java.util.Optional;

public interface NationRepository extends JpaRepository<Nation, Long>, JpaSpecificationExecutor<Nation> {
    Optional<Object> findByName(@NotEmpty(message = "Name cannot be empty") String name);
}
