package org.example.db.repositories;

import org.example.db.DbConnector;
import org.example.db.entities.ParkingPlace;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;

public class ParkingPlacesRepository {
    private Connection connection = null;

    public ParkingPlacesRepository() {
        connection = DbConnector.getInstance().getConnection();
    }

    public void addNewParkingRecord(ParkingPlace place) throws Exception {
        Statement statement = connection.createStatement();

        DateTimeFormatter CUSTOM_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String parkingDatetime = place.getParkingDatetime().format(CUSTOM_FORMATTER);

        String insertQuery = String.format("INSERT INTO parking_places (man_name, mob_tel, car_number, place_number, parking_datetime, is_free) VALUES ('%s','%s','%s', %d, '%s', %s);", place.getManName(), place.getMobTel(), place.getCarNumber(), place.getPlaceNumber(), parkingDatetime, place.isFree());

        statement.executeUpdate(insertQuery);

        statement.close();
    }

    public boolean isFreeParkingPlaceByNumber(int placeNumber) throws Exception {
        Statement statement = connection.createStatement();

        String selectQuery = String.format("select is_free from parking_places WHERE place_number = %d", placeNumber);

        ResultSet resultSet = statement.executeQuery(selectQuery);

        if (resultSet.next() == false) {
            resultSet.close();
            statement.close();

            return true;
        }

        boolean isFree = resultSet.getBoolean("is_free");

        resultSet.close();
        statement.close();

        return isFree;
    }
}
