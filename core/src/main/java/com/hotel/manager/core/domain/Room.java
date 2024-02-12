package com.hotel.manager.core.domain;

public class Room {

    private Long id;
    private String number;
    private RoomStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public void setAvailable() {
        status = RoomStatus.AVAILABLE;
    }

    public void setOccupied() {
        status = RoomStatus.OCCUPIED;
    }

    public boolean isOccupied() {
        return status == RoomStatus.OCCUPIED;
    }

    public boolean isAvailable() {
        return status == RoomStatus.AVAILABLE;
    }
}
