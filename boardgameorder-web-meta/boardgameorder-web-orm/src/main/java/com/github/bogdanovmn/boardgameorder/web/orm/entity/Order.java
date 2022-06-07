package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import com.github.bogdanovmn.common.spring.jpa.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

import static javax.persistence.FetchType.EAGER;

@Setter
@Getter
@NoArgsConstructor

@Entity
@Table(name = "user_order")
public class Order extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Integer estimatedCost;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "order", fetch = EAGER)
    private List<OrderItem> items;

    @Enumerated(EnumType.STRING)
    private Status status = Status.NEW;

    @Column(nullable = false)
    private Date created = new Date();

    public enum Status {
        NEW, IN_PROGRESS, COMPLETED, CANCELED
    }
}
