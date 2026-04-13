package com.mgr.api.model.criteria;

import com.mgr.api.model.Category;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryCriteria {
    private Long id;
    private String name;
    private Long parentId;
    private Integer status;

    public Specification<Category> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (id != null) predicates.add(cb.equal(root.get("id"), id));
            if (status != null) predicates.add(cb.equal(root.get("status"), status));
            if (parentId != null) predicates.add(cb.equal(root.get("parent").get("id"), parentId));
            if (StringUtils.hasText(name)) {
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
