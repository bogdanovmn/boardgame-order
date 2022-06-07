package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import com.github.bogdanovmn.common.spring.jpa.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Date;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor

@Entity
@Table(
    name = "user_order_item", // TODO remane to cart_item
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"user_id", "item_id"}
        )
    }
)
public class CartItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false)
    private Integer count = 1;

    @Column(nullable = false)
    private Date updated = new Date();

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CartItem that = (CartItem) o;
        return Objects.equals(user, that.user)
            && Objects.equals(item, that.item)
            && Objects.equals(count, that.count)
            && Objects.equals(updated, that.updated);
    }

    @Override
    public int hashCode() {

        return Objects.hash(user, item, count, updated);
    }

    public CartItem incCount(final int increment) {
        count += increment;
        return this;
    }
}
