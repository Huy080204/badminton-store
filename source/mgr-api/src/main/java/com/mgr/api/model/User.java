package com.mgr.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE + "user")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends Auditable<String>{

    @Id
    private Long id;
    private Integer gender;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Address> address;

    @OneToOne
    @MapsId
    @JoinColumn(name = "account_id")
    private Account account;
}

