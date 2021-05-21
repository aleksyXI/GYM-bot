package com.aleksy.gymbot.model;

import com.aleksy.gymbot.bot.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "chat_id", name = "users_unique_chatid_idx")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstractBaseEntity {
    @Column(name = "chat_id", unique = true, nullable = false)
    @NotNull
    private Integer chatId;

    @Column(name = "name", nullable = false)
    @NotBlank
    private String name;

    @Column(name = "birth_day", nullable = false)
    @NotBlank
    private String birthday;

    @Column(name = "abon_type", nullable = false)
    @NotNull
    private Integer abontype;

    @Column(name = "bot_state", nullable = false)
    @NotBlank
    private State  botState;


    public User(int chatId) {
        this.chatId = chatId;
        this.name = String.valueOf(chatId);
        this.birthday = String.valueOf(chatId);
        this.abontype = 0;
        this.botState = State.START;
    }
}
