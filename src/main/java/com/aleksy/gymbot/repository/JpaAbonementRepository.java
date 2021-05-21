package com.aleksy.gymbot.repository;

import com.aleksy.gymbot.model.Abonement;
import com.aleksy.gymbot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface JpaAbonementRepository  extends JpaRepository<Abonement, Integer> {
    Optional<Abonement> getByUserId (int chatId);
}
