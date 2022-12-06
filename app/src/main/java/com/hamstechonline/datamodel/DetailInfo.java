package com.hamstechonline.datamodel;

public class DetailInfo {

    private String sequence = "";
    private String name = "";
    private String type="";
    private String time="";
    private String trip_id = "";
    private String Datee = "";
    private String Ttype = "";
    private String Imagee = "";
    private boolean status = false;
    private String TripTime = "";

    public String getSequence() {
        return sequence;
    }
    public void setSequence(String sequence) {
        this.sequence = sequence;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean getStatus() {return status;}
    public boolean setStatus(boolean status) {this.status = status;
        return status;
    }

    public String getTrip() {
        return trip_id;
    }
    public void setTrip(String trip_id) {
        this.trip_id = trip_id;
    }

    public String getDatee() {
        return Datee;
    }
    public void setDatee(String Datee) {
        this.Datee = Datee;
    }

    public String getTtype() {
        return Ttype;
    }
    public void setTtype(String Ttype) {
        this.Ttype = Ttype;
    }

    public String getImage() {
        return Imagee;
    }

    public void setImage(String Imagee) {
        this.Imagee = Imagee;
    }

    public String getTripTime() {
        return TripTime;
    }

    public void setTripTime(String TripTime) {
        this.TripTime = TripTime;
    }


}
