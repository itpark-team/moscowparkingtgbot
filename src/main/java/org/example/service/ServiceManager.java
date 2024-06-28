package org.example.service;

import org.example.db.repositories.ParkingPlacesRepository;
import org.example.service.logic.MainMenuLogic;
import org.example.service.logic.ParkCarLogic;
import org.example.statemachine.State;
import org.example.statemachine.TransmittedData;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.Map;

public class ServiceManager {
    private Map<String, Service> methods;
    private MainMenuLogic mainMenuLogic;
    private ParkCarLogic parkCarLogic;
    private ParkingPlacesRepository parkingPlacesRepository;

    public ServiceManager() {

        parkingPlacesRepository = new ParkingPlacesRepository();

        methods = new HashMap<>();

        mainMenuLogic = new MainMenuLogic();
        parkCarLogic = new ParkCarLogic(parkingPlacesRepository);

        methods.put(State.WaitingCommandStart, mainMenuLogic::processCommandStart);

        methods.put(State.WaitingClickOnMainMenuButton, mainMenuLogic::processClickOnMainMenuButton);


        methods.put(State.WaitingInputManName, parkCarLogic::processInputManName);
        methods.put(State.WaitingInputMobTel, parkCarLogic::processInputMobTel);
        methods.put(State.WaitingInputCarNumber, parkCarLogic::processInputCarNumber);
        methods.put(State.WaitingInputPlaceNumber, parkCarLogic::processInputPlaceNumber);
    }

    public SendMessage callLogicMethod(String textFromUser, TransmittedData transmittedData) throws Exception {
        String state = transmittedData.getState();
        return methods.get(state).process(textFromUser, transmittedData);
    }

}
