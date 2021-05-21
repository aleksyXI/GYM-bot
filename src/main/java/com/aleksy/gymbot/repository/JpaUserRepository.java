package com.aleksy.gymbot.repository;

import com.aleksy.gymbot.model.Abonement;
import com.aleksy.gymbot.model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface JpaUserRepository extends JpaRepository<User, Integer> {
// По названию метода Spring сам поймет, что мы хотим получить пользователя по переданному chatId
        Optional<User> getByChatId(int chatId);

}
