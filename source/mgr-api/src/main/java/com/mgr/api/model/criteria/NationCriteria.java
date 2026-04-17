package com.mgr.api.model.criteria;

import com.mgr.api.model.Nation;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Predicate;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Data
public class NationCriteria implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private Integer kind;
    private Long parentId;

    public Specification<Nation> getSpecification() {
        return new Specification<Nation>() {
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(Root<Nation> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if (getId() != null) {
                    predicates.add(cb.equal(root.get("id"), getId()));
                }
                if (getKind() != null) {
                    predicates.add(cb.equal(root.get("kind"), getKind()));
                }
                if (getParentId() != null) {
                    predicates.add(cb.equal(root.get("parent").get("id"), getParentId()));
                }
                if (!StringUtils.isEmpty(getName())) {
                    predicates.add(cb.like(cb.lower(root.get("name")), "%" + getName().toLowerCase() + "%"));
                }
                query.orderBy(cb.asc(root.get("kind")), cb.asc(root.get("name")));
                return cb.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
    }
}