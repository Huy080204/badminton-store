package com.mgr.api.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE + "nation")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Nation extends Auditable<String> {
    private Integer kind;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId")
    //if parent null, it is province
    private Nation parent;
}
