package com.aleksy.gymbot.bot.handler;

import com.aleksy.gymbot.bot.State;
import com.aleksy.gymbot.bot.keyboard.ReplyKeyboardMarkupBuilder;
import com.aleksy.gymbot.model.Abonement;
import com.aleksy.gymbot.model.User;
import com.aleksy.gymbot.repository.JpaAbonementRepository;
import com.aleksy.gymbot.repository.JpaUserRepository;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.aleksy.gymbot.util.TelegramUtil.createMessageTemplate;

@Component
public class BuyAbonementHandler implements Handler {

    private final List<KeyboardRow> keyboard = new ArrayList<>();
    private KeyboardRow row;

    private final JpaUserRepository userRepository;
    private final JpaAbonementRepository abonementRepository;

    public BuyAbonementHandler(JpaUserRepository userRepository,JpaAbonementRepository abonementRepository) {
        this.abonementRepository=abonementRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message) {
        // Checking type of input message
        if (message.equalsIgnoreCase("1 месяц")) {
            return buy(user,1);
        } else if (message.equalsIgnoreCase("2 месяца")){
            return buy(user,2);
        }else if (message.equalsIgnoreCase("3 месяца")){
            return buy(user,3);
        }else if (message.equalsIgnoreCase("Статус абонемента")){
            return checkAbonement(user);
        }
        else return buy(user,3);

    }

    private List<PartialBotApiMethod<? extends Serializable>> buy(User user, Integer type) {
        // If user accepted the change - update bot state and save user
        Abonement abonement=new Abonement();
        user.setBotState(State.NONE);
        abonement.setUserId(user.getChatId());
        abonement.setType(type);
        abonement.setDate(new Date());
        abonementRepository.save(abonement);
        user.setAbontype(abonementRepository.getByUserId(user.getChatId()).get().getId());
        userRepository.save(user);

        return List.of(createMessageTemplate(user).setText("Вы успешно приобрели абонемент.")
                .setReplyMarkup(ReplyKeyboardMarkupBuilder.create()
                        .row()
                        .button("Назад")
                        .endRow()
                        .getKeyboard()));
    }

    private List<PartialBotApiMethod<? extends Serializable>> checkAbonement(User user) {
        // If user accepted the change - update bot state and save user
        user.setBotState(State.NONE);
        userRepository.save(user);
        Abonement abonement=new Abonement();
        DateTime startDate= new DateTime(abonementRepository.getByUserId(user.getChatId()).get().getDate());
        DateTime endDate=new DateTime(startDate);
        Integer type = new Integer(abonementRepository.getByUserId(user.getChatId()).get().getType().intValue());
        switch (type)
        {
            case 1: endDate=startDate.plusDays(30); break;
            case 2: endDate=startDate.plusDays(60); break;
            case 3: endDate=startDate.plusDays(90); break;
        }

        return List.of(createMessageTemplate(user).setText("Ваш абонемент истекает: " + endDate.toString(DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss")))
                .setReplyMarkup(ReplyKeyboardMarkupBuilder.create()
                        .row()
                        .button("Назад")
                        .endRow()
                        .getKeyboard()));
    }

    @Override
    public State operatedBotState() {
        return State.BUYING_ABON;
    }

    @Override
    public List<String> operatedCallBackQuery() {
        return List.of("1 месяц","2 месяца", "3 месяца");
    }


}
