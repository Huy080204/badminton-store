package com.mgr.api.model.criteria;

import com.mgr.api.model.Category;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryCriteria {
    private Long id;
    private String name;
    private Integer status;

    public Specification<Category> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null) {
                predicates.add(cb.equal(root.get("id"), id));
            }

            if (StringUtils.isNoneBlank(name)) {
                // Tìm kiếm theo tên (Like %name%) - không phân biệt hoa thường
                predicates.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            // Mặc định sắp xếp theo ngày tạo giảm dần được xử lý ở Pageable trong Controller
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}