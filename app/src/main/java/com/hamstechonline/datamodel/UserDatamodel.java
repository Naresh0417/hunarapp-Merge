package com.hamstechonline.datamodel;

public class UserDatamodel {

    public UserDatamodel(String prospectname,String phone){
        this.prospectname = prospectname;
        this.phone = phone;

    }


    public String getProspectname() {
        return prospectname;
    }

    public void setProspectname(String prospectname) {
        this.prospectname = prospectname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    String prospectname;
    String phone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    String userName;
}
