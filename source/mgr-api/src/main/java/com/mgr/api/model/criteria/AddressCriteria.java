package com.mgr.api.model.criteria;

import com.mgr.api.model.Address;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Data
public class AddressCriteria {
    private Long id;
    private Long userId;
    private String receiverName;
    private String phone;
    private Boolean isDefault;

    public Specification<Address> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null) {
                predicates.add(cb.equal(root.get("id"), id));
            }
            if (userId != null) {
                // Lọc địa chỉ theo User ID
                predicates.add(cb.equal(root.get("user").get("id"), userId));
            }
            if (StringUtils.hasText(receiverName)) {
                // Tìm kiếm tên người nhận (LIKE %name%)
                predicates.add(cb.like(cb.lower(root.get("receiverName")), "%" + receiverName.toLowerCase() + "%"));
            }
            if (StringUtils.hasText(phone)) {
                // Tìm kiếm theo số điện thoại
                predicates.add(cb.like(root.get("phone"), "%" + phone + "%"));
            }
            if (isDefault != null) {
                predicates.add(cb.equal(root.get("isDefault"), isDefault));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}