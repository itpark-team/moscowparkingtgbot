package org.example.service.logic;

import org.example.db.entities.ParkingPlace;
import org.example.db.repositories.ParkingPlacesRepository;
import org.example.statemachine.State;
import org.example.statemachine.TransmittedData;
import org.example.util.NumberUtil;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParkCarLogic {

    private ParkingPlacesRepository parkingPlacesRepository;

    public ParkCarLogic(ParkingPlacesRepository parkingPlacesRepository) {
        this.parkingPlacesRepository = parkingPlacesRepository;
    }

    public SendMessage processInputManName(String textFromUser, TransmittedData transmittedData) throws Exception {
        SendMessage messageToUser = new SendMessage();
        messageToUser.setChatId(transmittedData.getChatId());

        if (textFromUser.length() > 50) {
            messageToUser.setText("Ошибка ввода имени владельца автомобиля. Длина имени должна быть от 1 до 50 символов. Повторите ввод имени");
            return messageToUser;
        }

        transmittedData.getDataStorage().addOrUpdate("ManName", textFromUser);

        messageToUser.setText("Имя владельца автомобиля успешно записано. Теперь введите мобильный телефон владельца автомобиля");
        transmittedData.setState(State.WaitingInputMobTel);

        return messageToUser;
    }

    public SendMessage processInputMobTel(String textFromUser, TransmittedData transmittedData) throws Exception {
        SendMessage messageToUser = new SendMessage();
        messageToUser.setChatId(transmittedData.getChatId());

        if (textFromUser.length() > 30) {
            messageToUser.setText("Ошибка ввода телефона владельца автомобиля. Длина имени должна быть от 1 до 30 символов. Повторите ввод телефона");
            return messageToUser;
        }

        transmittedData.getDataStorage().addOrUpdate("MobTel", textFromUser);

        messageToUser.setText("Телефон владельца автомобиля успешно записан. Теперь введите номер автомобиля");
        transmittedData.setState(State.WaitingInputCarNumber);

        return messageToUser;
    }

    public SendMessage processInputCarNumber(String textFromUser, TransmittedData transmittedData) throws Exception {
        SendMessage messageToUser = new SendMessage();
        messageToUser.setChatId(transmittedData.getChatId());

        if (textFromUser.length() != 6) {
            messageToUser.setText("Ошибка ввода номера автомобиля. Длина номера автомобиля должна быть 6 символов. Повторите ввод номера");
            return messageToUser;
        }

        Pattern pattern = Pattern.compile("[а-я]{1}[0-9]{3}[а-я]{2}");

        Matcher matcher = pattern.matcher(textFromUser);

        if (matcher.find() == false) {
            messageToUser.setText("Ошибка ввода номера автомобиля. Шаблон номера должен быть я909ая. Повторите ввод номера");
            return messageToUser;
        }

        transmittedData.getDataStorage().addOrUpdate("CarNumber", textFromUser);

        messageToUser.setText("Номер автомобиля успешно записан. Теперь введите номер парковочного места от 1 до 1000");
        transmittedData.setState(State.WaitingInputPlaceNumber);

        return messageToUser;
    }

    public SendMessage processInputPlaceNumber(String textFromUser, TransmittedData transmittedData) throws Exception {
        SendMessage messageToUser = new SendMessage();
        messageToUser.setChatId(transmittedData.getChatId());

        if (NumberUtil.isIntNumber(textFromUser) == false) {
            messageToUser.setText("Ошибка ввода парковочного места. Вы ввели не число");
            return messageToUser;
        }

        int placeNumber = NumberUtil.stringToInt(textFromUser);

        if (NumberUtil.isNumberInRange(placeNumber, 1, 1000) == false) {
            messageToUser.setText("Ошибка ввода парковочного места. Парковочное место должно быть от 1 до 1000");
            return messageToUser;
        }

        if (parkingPlacesRepository.isFreeParkingPlaceByNumber(placeNumber) == false) {
            messageToUser.setText("Ошибка ввода парковочного места. Парковочное место занято, пожалуйста введите другое парковочное место");
            return messageToUser;
        }

        String manName = (String) transmittedData.getDataStorage().get("ManName");
        String mobTel = (String) transmittedData.getDataStorage().get("MobTel");
        String carNumber = (String) transmittedData.getDataStorage().get("CarNumber");

        ParkingPlace parkingPlace = new ParkingPlace(manName, mobTel, carNumber, placeNumber);

        parkingPlacesRepository.addNewParkingRecord(parkingPlace);

        messageToUser.setText("Ваш автомобиль успешно запаркован. Для возврата в главное меню введите /start");
        transmittedData.setState(State.WaitingCommandStart);

        return messageToUser;
    }
}
