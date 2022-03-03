package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InviteRepository extends JpaRepository<Invite, Integer> {
    List<Invite> getAllByCreator(User creator);

    Invite findFirstByCode(String code);
}
