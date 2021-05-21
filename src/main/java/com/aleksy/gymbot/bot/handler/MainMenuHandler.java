package com.aleksy.gymbot.bot.handler;

import com.aleksy.gymbot.bot.State;
import com.aleksy.gymbot.bot.keyboard.ReplyKeyboardMarkupBuilder;
import com.aleksy.gymbot.model.User;
import com.aleksy.gymbot.repository.JpaUserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.aleksy.gymbot.util.TelegramUtil.createInlineKeyboardButton;
import static com.aleksy.gymbot.util.TelegramUtil.createMessageTemplate;

@Component
public class MainMenuHandler implements Handler {

    public static final String HELP = "/help";

    private final List<KeyboardRow> keyboard = new ArrayList<>();
    private KeyboardRow row;

    private final JpaUserRepository userRepository;

    public MainMenuHandler(JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        // Checking type of input message
        if (message.equalsIgnoreCase("Купить абонемент")) {
            return buyAbon(user);
        } else if (message.equalsIgnoreCase("Просмотреть график тренировок")){
            return calendar(user);
        } else if (message.equalsIgnoreCase("Помощь")){
            return help(user);
        }
        return mainMenu(user);

    }

    private List<PartialBotApiMethod<? extends Serializable>> calendar(User user) {
        // If user accepted the change - update bot state and save user
        user.setBotState(State.NONE);
        userRepository.save(user);

        // Creating button to start show all commands

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(false)
                .setKeyboard(keyboard);

        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        KeyboardButton shareNumBtn = new KeyboardButton("Назад");
        keyboardSecondRow.add(shareNumBtn);
        keyboard.add(keyboardSecondRow);
        keyboardMarkup.setKeyboard(keyboard);

        return List.of(createMessageTemplate(user).setText("График тренировок \n    1 неделя: 11:00-14:00\n    2 неделя: 15:00-18:00\n    3 неделя: 8:00-10:00\n    4 неделя: 11:00-14:00")
                .setReplyMarkup(keyboardMarkup));
    }

    private List<PartialBotApiMethod<? extends Serializable>> help(User user) {
        // If user accepted the change - update bot state and save user
        user.setBotState(State.NONE);
        userRepository.save(user);

        // Creating button to start show all commands

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true)
                .setResizeKeyboard(true)
                .setOneTimeKeyboard(false)
                .setKeyboard(keyboard);

        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        KeyboardButton shareNumBtn = new KeyboardButton("Назад");
        keyboardSecondRow.add(shareNumBtn);
        keyboard.add(keyboardSecondRow);
        keyboardMarkup.setKeyboard(keyboard);

        return List.of(createMessageTemplate(user).setText("Правила таковы:")
                .setReplyMarkup(keyboardMarkup));
    }

    private List<PartialBotApiMethod<? extends Serializable>> mainMenu(User user) {

        return List.of(createMessageTemplate(user).setText("Главное меню").setReplyMarkup(ReplyKeyboardMarkupBuilder.create(user.getChatId().longValue())
                .setText("Главное меню")
                .row()
                .button("Купить абонемент")
                .endRow()
                .row()
                .button("Просмотреть график тренировок")
                .endRow()
                .row()
                .button("Помощь")
                .endRow()
                .getKeyboard()));

    }

    private List<PartialBotApiMethod<? extends Serializable>> buyAbon(User user) {
        user.setBotState(State.BUYING_ABON);
        userRepository.save(user);

        return List.of(createMessageTemplate(user).setText("Выберите тип абонемента").setReplyMarkup(ReplyKeyboardMarkupBuilder.create(user.getChatId().longValue())
                .setText("Главное меню")
                .row()
                .button("1 месяц")
                .button("2 месяца")
                .button("3 месяца")
                .endRow()
                .row()
                .button("Статус абонемента")
                .endRow()
                .getKeyboard()));

    }


    @Override
    public State operatedBotState() {
        return State.NONE;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of(HELP,"/main_menu","Назад","В гланое меню");
    }

}
