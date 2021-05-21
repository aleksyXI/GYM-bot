package com.aleksy.gymbot.bot.handler;


import com.aleksy.gymbot.bot.State;
import com.aleksy.gymbot.bot.keyboard.ReplyKeyboardMarkupBuilder;
import com.aleksy.gymbot.model.User;
import com.aleksy.gymbot.repository.JpaUserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.Serializable;
import java.util.List;

import static com.aleksy.gymbot.util.TelegramUtil.createInlineKeyboardButton;
import static com.aleksy.gymbot.util.TelegramUtil.createMessageTemplate;

@Component
public class AddingBirthHandler implements Handler {
    public static final String DATE_ACCEPT = "/enter_date_accept";
    public static final String DATE_CHANGE = "/enter_date";
    public static final String DATE_CHANGE_CANCEL = "/enter_date_cancel";
    public static final String MAIN_MENU = "/main_menu";

    private final JpaUserRepository userRepository;

    public AddingBirthHandler(JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        // Checking type of input message
        if (message.equalsIgnoreCase(DATE_ACCEPT) || message.equalsIgnoreCase(DATE_CHANGE_CANCEL)) {
            return accept(user);
        } else if (message.equalsIgnoreCase(DATE_CHANGE)) {
            return changeDate(user);
        }
        return checkDate(user, message);

    }

    private List<PartialBotApiMethod<? extends Serializable>> accept(User user) {
        // If user accepted the change - update bot state and save user
        user.setBotState(State.NONE);
        userRepository.save(user);

        return List.of(createMessageTemplate(user).setText("Хотите ли Вы перейти в главное меню?")
                .setReplyMarkup(ReplyKeyboardMarkupBuilder.create()
                        .row()
                        .button("В гланое меню")
                        .endRow()
                        .getKeyboard()));
    }

    private List<PartialBotApiMethod<? extends Serializable>> checkDate(User user, String message) {
        // When we check user name we store it in database immediately
        // refactoring idea: temporal storage
        user.setBirthday(message);
        userRepository.save(user);

        // Creating button to accept changes
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("Да", DATE_ACCEPT));

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        return List.of(createMessageTemplate(user)
                .setText(String.format("Вы ввели: %s%nЕсли все правильно, нажмите кнопку", user.getBirthday()))
                .setReplyMarkup(inlineKeyboardMarkup));
    }

    private List<PartialBotApiMethod<? extends Serializable>> changeDate(User user) {
        // When name change request is received - bot state changes
        user.setBotState(State.ENTER_BIRTH);
        userRepository.save(user);

        // Cancel button creation
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("Отмена", DATE_CHANGE_CANCEL));

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        return List.of(createMessageTemplate(user).setText(String.format(
                "Ваша текущая дата рождения: %s%nВведите новую дату", user.getBirthday()))
                .setReplyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public State operatedBotState() {
        return State.ENTER_BIRTH;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(DATE_ACCEPT, DATE_CHANGE, DATE_CHANGE_CANCEL,MAIN_MENU);
    }
}
