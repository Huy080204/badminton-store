package com.mgr.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE + "seller")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
public class Seller extends Auditable<String> {

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Account account;

    @Column(name = "shop_name")
    private String shopName;

    @Column(name = "shop_description")
    private String shopDescription;

}
