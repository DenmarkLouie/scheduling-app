package com.example.creoteccalendarapplication;

public class user {

    String ClientName;
    String ClientSchool;
    String ClientNumber;
    String ProductType;
    String ZoomLink;
    String ID;
    String ScheduleTime;
    String IsDone;


    public user(){}

    public user(String clientName, String clientSchool, String clientNumber, String productType, String scheduleTime, String DocID, String isDone) {
        ClientName = clientName;
        ClientSchool = clientSchool;
        ClientNumber = clientNumber;
        ProductType = productType;
        ID = DocID;
        ScheduleTime = scheduleTime;
        IsDone = isDone;

    }



    public String getIsDone() {
        return IsDone;
    }

    public void setIsDone(String isDone) {
        this.IsDone = isDone;
    }

    public String getID() {return ID;}

    public void setID(String ID) {this.ID = ID;}

    public String getZoomLink() { return ZoomLink;}

    public void setZoomLink(String zoomLink) {ZoomLink = zoomLink;}

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getClientSchool() {
        return ClientSchool;
    }

    public void setClientSchool(String clientSchool) {
        ClientSchool = clientSchool;
    }

    public String getClientNumber() {
        return ClientNumber;
    }

    public void setClientNumber(String clientNumber) {
        ClientNumber = clientNumber;
    }

    public String getProductType() {
        return ProductType;
    }

    public void setProductType(String productType) {
        ProductType = productType;
    }

    public String getScheduleTime() {
        return ScheduleTime;
    }

    public void setScheduleTime(String scheduleTime) {
        ScheduleTime = scheduleTime;
    }
}
