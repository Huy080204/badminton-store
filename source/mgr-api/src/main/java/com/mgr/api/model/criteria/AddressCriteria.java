package com.mgr.api.model.criteria;

import com.mgr.api.model.Address;
import com.mgr.api.model.User;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Data
public class AddressCriteria {
    private Long id;
    private Long userId;
    private Integer status;

    public Specification<Address> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (id != null) {
                predicates.add(cb.equal(root.get("id"), id));
            }
            if (userId != null) {
                Join<Address, User> userJoin = root.join("user");
                predicates.add(cb.equal(userJoin.get("id"), userId));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
