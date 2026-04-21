package com.mgr.api.model.criteria;

import com.mgr.api.model.Product;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Data
public class ProductCriteria {
    private Long id;
    private String name;
    private Long categoryId;
    private Long sellerId;
    private String brand;
    private Integer status;
    private Double minPrice;
    private Double maxPrice;

    public Specification<Product> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (id != null) predicates.add(cb.equal(root.get("id"), id));
            if (status != null) predicates.add(cb.equal(root.get("status"), status));
            if (categoryId != null) predicates.add(cb.equal(root.get("category").get("id"), categoryId));
            if (sellerId != null) predicates.add(cb.equal(root.get("seller").get("id"), sellerId));
            if (StringUtils.hasText(name)) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(brand)) {
                predicates.add(cb.equal(cb.lower(root.get("brand")), brand.toLowerCase()));
            }
            if (minPrice != null) predicates.add(cb.ge(root.get("price"), minPrice));
            if (maxPrice != null) predicates.add(cb.le(root.get("price"), maxPrice));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}