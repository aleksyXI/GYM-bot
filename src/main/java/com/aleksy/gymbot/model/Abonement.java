package com.aleksy.gymbot.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Set;

import static javax.persistence.FetchType.EAGER;

@Entity

@Table(name = "abon_type", uniqueConstraints = {@UniqueConstraint(columnNames = "user_id", name = "users_unique_userid_idx")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Abonement extends AbstractBaseEntity {


    @Column(name = "user_id", unique = true, nullable = false)
    @NotNull
    private Integer userId;

    @Column(name = "type",nullable = false)
    @NotNull
    private Integer type;

    @Column(name = "date_start",nullable = false)
    @NotNull
    private Date date;

}
