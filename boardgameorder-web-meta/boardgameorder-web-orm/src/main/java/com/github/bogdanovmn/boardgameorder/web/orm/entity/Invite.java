package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import com.github.bogdanovmn.common.spring.jpa.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.DigestUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Setter
@Getter

@Entity
public class Invite extends BaseEntity {

    private static final int SECONDS_TO_EXPIRE = 3 * 86400;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator_user_id")
    private User creator;

    @Column(unique = true, length = 32)
    private String code;

    @Column(nullable = false)
    private LocalDateTime createDate = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime expireDate = LocalDateTime.now().plusSeconds(SECONDS_TO_EXPIRE);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invited_user_id")
    private User invited;

    public Invite setCreator(User creator) {
        this.creator = creator;
        this.code = DigestUtils.md5DigestAsHex(
            (creator.getId().toString() + createDate.toString()).getBytes()
        );
        return this;
    }
}
