package com.mgr.api.model.criteria;

import com.mgr.api.model.Order;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderCriteria {
    private Long id;
    private Long accountId;
    private Integer status;

    public Specification<Order> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (id != null) predicates.add(cb.equal(root.get("id"), id));
            if (status != null) predicates.add(cb.equal(root.get("status"), status));
            if (accountId != null) predicates.add(cb.equal(root.get("account").get("id"), accountId));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}