package com.mgr.api.model.criteria;

import com.mgr.api.model.Seller;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Data
public class SellerCriteria {
    private Long id;
    private Integer status;
    private String shopName;
    private String email;

    public Specification<Seller> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (id != null) {
                predicates.add(cb.equal(root.get("id"), id));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (StringUtils.hasText(shopName)) {
                predicates.add(cb.like(cb.lower(root.get("shopName")), "%" + shopName.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(email)) {
                predicates.add(cb.like(cb.lower(root.get("account").get("email")), "%" + email.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
