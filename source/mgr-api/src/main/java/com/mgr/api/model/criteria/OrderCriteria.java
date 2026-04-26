package com.mgr.api.model.criteria;

import com.mgr.api.model.Account;
import com.mgr.api.model.Order;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OrderCriteria {
    private Long id;
    private Long accountId;
    private Integer status;

    // Lọc theo tên khách hàng (fullName trên Account)
    private String accountName;

    // Lọc theo số điện thoại khách hàng
    private String phone;

    // Lọc theo khoảng ngày đặt hàng (createdDate từ Auditable, kiểu Date)
    private Date fromDate;

    private Date toDate;

    public Specification<Order> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (id != null) {
                predicates.add(cb.equal(root.get("id"), id));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (accountId != null) {
                predicates.add(cb.equal(root.get("account").get("id"), accountId));
            }
            if (accountName != null && !accountName.isEmpty()) {
                Join<Order, Account> accountJoin = root.join("account");
                predicates.add(cb.like(cb.lower(accountJoin.get("fullName")),
                        "%" + accountName.toLowerCase() + "%"));
            }
            if (phone != null && !phone.isEmpty()) {
                Join<Order, Account> accountJoin = root.join("account");
                predicates.add(cb.like(accountJoin.get("phone"), "%" + phone + "%"));
            }
            if (fromDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdDate"), fromDate));
            }
            if (toDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdDate"), toDate));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}