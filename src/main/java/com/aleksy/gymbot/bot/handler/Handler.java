package com.aleksy.gymbot.bot.handler;

import java.io.Serializable;

public interface Handler {

    // основной метод, который будет обрабатывать действия пользователя
    List<Partialbotapimethod<? extends Serializable>> handle(User user, String message);
    // метод, который позволяет узнать, можем ли мы обработать текущий State у пользователя
    State operatedBotState();
    // метод, который позволяет узнать, какие команды CallBackQuery мы можем обработать в этом классе
    List<string> operatedCallBackQuery();
}
