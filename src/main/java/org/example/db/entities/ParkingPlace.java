package org.example.db.entities;

import java.time.LocalDateTime;

public class ParkingPlace {
    private long id;
    private String manName;
    private String mobTel;
    private String carNumber;
    private int placeNumber;
    private LocalDateTime parkingDatetime;
    private boolean isFree;

    public ParkingPlace(long id, String manName, String mobTel, String carNumber, int placeNumber, LocalDateTime parkingDatetime, boolean isFree) {
        this.id = id;
        this.manName = manName;
        this.mobTel = mobTel;
        this.carNumber = carNumber;
        this.placeNumber = placeNumber;
        this.parkingDatetime = parkingDatetime;
        this.isFree = isFree;
    }

    public ParkingPlace(String manName, String mobTel, String carNumber, int placeNumber) {
        this.manName = manName;
        this.mobTel = mobTel;
        this.carNumber = carNumber;
        this.placeNumber = placeNumber;

        this.parkingDatetime = LocalDateTime.now();
        this.isFree = false;
    }

    public long getId() {
        return id;
    }

    public String getManName() {
        return manName;
    }

    public String getMobTel() {
        return mobTel;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public int getPlaceNumber() {
        return placeNumber;
    }

    public LocalDateTime getParkingDatetime() {
        return parkingDatetime;
    }

    public boolean isFree() {
        return isFree;
    }
}
