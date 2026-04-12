package com.mgr.api.model.criteria;


import com.mgr.api.model.Account;
import com.mgr.api.model.User;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Data
public class UserCriteria {
    private String username;
    private String fullName;
    private Integer status;
    private Integer gender;
    private Integer kind;

    public Specification<User> getSpecification() {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<User, Account> accountJoin = null; // Khai báo nhưng chưa join

            // Kiểm tra các trường cần Join bảng Account
            // Thêm check 'kind' vào đây để kích hoạt Join
            if (StringUtils.isNoneBlank(username) || StringUtils.isNoneBlank(fullName) || kind != null) {
                accountJoin = root.join("account");

                if (StringUtils.isNoneBlank(username)) {
                    predicates.add(cb.like(cb.lower(accountJoin.get("username")), "%" + username.toLowerCase() + "%"));
                }
                if (StringUtils.isNoneBlank(fullName)) {
                    predicates.add(cb.like(cb.lower(accountJoin.get("fullName")), "%" + fullName.toLowerCase() + "%"));
                }

                // 2. Sửa logic lọc kind: Ưu tiên theo tham số truyền lên, nếu không có mới mặc định là 2
                if (kind != null) {
                    predicates.add(cb.equal(accountJoin.get("kind"), kind));
                } else {
                    // Nếu bạn vẫn muốn mặc định chỉ ra User khi admin không truyền kind
                    predicates.add(cb.equal(accountJoin.get("kind"), 2));
                }
            } else {
                // Trường hợp không truyền gì cả, vẫn nên mặc định kind=2 để tránh ra data rác
                accountJoin = root.join("account");
                predicates.add(cb.equal(accountJoin.get("kind"), 2));
            }

            // Logic cho các trường thuộc bảng User
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (gender != null) {
                predicates.add(cb.equal(root.get("gender"), gender));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
