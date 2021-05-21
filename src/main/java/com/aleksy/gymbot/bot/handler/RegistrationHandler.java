package com.aleksy.gymbot.bot.handler;

import com.aleksy.gymbot.bot.State;
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
public class RegistrationHandler implements Handler {
    // Supported CallBackQueries are stored as constants
    public static final String NAME_ACCEPT = "/enter_name_accept";
    public static final String NAME_CHANGE = "/enter_name";
    public static final String NAME_CHANGE_CANCEL = "/enter_name_cancel";

    private final JpaUserRepository userRepository;

    public RegistrationHandler(JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        // Checking type of input message
        if (message.equalsIgnoreCase(NAME_ACCEPT) || message.equalsIgnoreCase(NAME_CHANGE_CANCEL)) {
            return accept(user);
        } else if (message.equalsIgnoreCase(NAME_CHANGE)) {
            return changeName(user);
        }
        return checkName(user, message);

    }

    private List<PartialBotApiMethod<? extends Serializable>> accept(User user) {
        // If user accepted the change - update bot state and save user
        user.setBotState(State.ENTER_BIRTH);
        userRepository.save(user);
        return List.of(createMessageTemplate(user).setText(String.format(
                "Ваше имя сохранено как: %s \nВведите Вашу дату рождения", user.getName())));
    }

    private List<PartialBotApiMethod<? extends Serializable>> checkName(User user, String message) {
        // When we check user name we store it in database immediately
        // refactoring idea: temporal storage
        user.setName(message);
        userRepository.save(user);

        // Creating button to accept changes
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("Да", NAME_ACCEPT));

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        return List.of(createMessageTemplate(user)
                .setText(String.format("Вы ввели: %s%nЕсли все верно, нажмите кнопку", user.getName()))
                .setReplyMarkup(inlineKeyboardMarkup));
    }

    private List<PartialBotApiMethod<? extends Serializable>> changeName(User user) {
        // When name change request is received - bot state changes
        user.setBotState(State.ENTER_NAME);
        userRepository.save(user);

        // Cancel button creation
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<InlineKeyboardButton> inlineKeyboardButtonsRowOne = List.of(
                createInlineKeyboardButton("Отмена", NAME_CHANGE_CANCEL));

        inlineKeyboardMarkup.setKeyboard(List.of(inlineKeyboardButtonsRowOne));

        return List.of(createMessageTemplate(user).setText(String.format(
                "Ваше текущее ФИО: %s%nВведите заново  и нажмите кнопку", user.getName()))
                .setReplyMarkup(inlineKeyboardMarkup));
    }

    @Override
    public State operatedBotState() {
        return State.ENTER_NAME;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(NAME_ACCEPT, NAME_CHANGE, NAME_CHANGE_CANCEL);
    }
}
