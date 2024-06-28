package org.example.service.logic;

import org.example.statemachine.State;
import org.example.statemachine.TransmittedData;
import org.example.util.buttons.InlineButtonsStorage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.example.util.buttons.InlineKeyboardsStorage;

import java.util.ArrayList;


public class MainMenuLogic {
    public SendMessage processCommandStart(String textFromUser, TransmittedData transmittedData) throws Exception {
        SendMessage messageToUser = new SendMessage();
        messageToUser.setChatId(transmittedData.getChatId());

        if (textFromUser.equals("/start") == false) {
            messageToUser.setText("Нераспознанное сообщение. Для начала работы с ботом введите /start");
            return messageToUser;
        }

        messageToUser.setText("Добро пожаловать в московский паркинг! Пожалуйста выберите нужное вам действие.");
        messageToUser.setReplyMarkup(InlineKeyboardsStorage.getMainMenuKeyboard());

        transmittedData.setState(State.WaitingClickOnMainMenuButton);

        return messageToUser;
    }

    public SendMessage processClickOnMainMenuButton(String textFromUser, TransmittedData transmittedData) throws Exception {
        SendMessage messageToUser = new SendMessage();
        messageToUser.setChatId(transmittedData.getChatId());

        if (textFromUser.equals(InlineButtonsStorage.ParkCar.getCallBackData()) == false && textFromUser.equals(InlineButtonsStorage.LeaveParking.getCallBackData()) == false) {
            messageToUser.setText("Ошибка. Нажмите на кнопку.");
            return messageToUser;
        }

        if (textFromUser.equals(InlineButtonsStorage.ParkCar.getCallBackData()) == true) {
            messageToUser.setText("Начало добавления машины на стоянку. Пожалуйста введите имя владельца автомобиля от 1 до 50 символов");

            transmittedData.setState(State.WaitingInputManName);

        } else if (textFromUser.equals(InlineButtonsStorage.LeaveParking.getCallBackData()) == true) {
            //todo доделать выезд с парковки
        }

        return messageToUser;
    }


}
