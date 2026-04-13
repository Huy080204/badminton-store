package com.mgr.api.model.criteria;

import com.mgr.api.model.Nation;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Data
public class NationCriteria {
    private Long id;
    private String name;
    private Integer kind;
    private Long parentId;

    public Specification<Nation> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null) {
                predicates.add(cb.equal(root.get("id"), id));
            }
            if (StringUtils.hasText(name)) {
                // Tìm kiếm theo tên (LIKE %name%)
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }
            if (kind != null) {
                predicates.add(cb.equal(root.get("kind"), kind));
            }
            if (parentId != null) {
                // Lọc theo id của parent
                predicates.add(cb.equal(root.get("parent").get("id"), parentId));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}