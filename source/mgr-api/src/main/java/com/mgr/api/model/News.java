package com.mgr.api.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Entity
@Table(name = TablePrefix.PREFIX_TABLE + "news")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class News extends Auditable<String> {
    private String title;

    @Column(columnDefinition = "LONGTEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    // account

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id") // Tên cột trong bảng news ở database sẽ là account_id
    private Account account;
}
