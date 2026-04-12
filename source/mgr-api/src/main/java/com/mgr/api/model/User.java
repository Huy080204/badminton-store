package com.mgr.api.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE + "user")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class User extends Auditable<String> {
    @OneToOne
    @MapsId // Mapping id from account
    @JoinColumn(name = "id")
    private Account account;

    private Integer gender;
    private Date dateOfBirth;
}
