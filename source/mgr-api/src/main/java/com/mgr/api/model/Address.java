package com.mgr.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE + "address")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address extends Auditable<String> {
    @Id
    @GenericGenerator(name = "idGenerator", strategy = "com.mgr.api.service.id.IdGenerator")
    @GeneratedValue(generator = "idGenerator")
    private Long id;
    private String street;
    private String zipCode;
    private Boolean isDefault;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province")
    private Nation province;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district")
    private Nation district;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commune")
    private Nation commune;
}
