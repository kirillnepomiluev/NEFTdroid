package com.example.android.project1;

import java.util.HashMap;
import java.util.Map;

public class User {



    private String login;
    private String firstname;
    private String lastname;
    private String datebirth;
    private String passportnumber;
    private String country;
    private String city;
    private String street;
    private String address;
    private String sponsorlogin;
    private String phone;
    private String email;
    private String photoUrl;
    private String selfiePhotoUrl;
    private String useruid;
    private boolean male;
    private int money;
    private boolean status;
    private long statusend;
    private String sponsorid;
    private String sponsor2id;
    private String sponsor3id;
    private String sponsor4id;
    private String sponsor5id;
    private String password;
    private String dateOfReg;
    private boolean isProfileFilled;
    private boolean verifyEnd;

    public User () {

    }

    public User (String useruid, String phone) {
        this.useruid = useruid;
        this.phone = phone;
        this.male=true;
        this.status = false;
        this.login = phone;
        this.money=0;



    }
//    public User (Invite invite) {
//        this.useruid = invite.getUser2uid();
//        this.login=invite.getUsername();
//
//    }

    public User (Chat chat) {
        this.useruid = chat.getUser2uid();
        this.login=chat.getUsername();
        this.photoUrl=chat.getUserPhotourl();

    }

    public User (String firstname, String lastname, String sponsorlogin, String password, String phone, String useruid) {

        this.sponsorlogin=sponsorlogin;
        this.firstname=firstname;
        this.lastname =lastname;
        this.password = password;
        this.phone=phone;
        this.useruid=useruid;
        this.male=true;


    }


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("useruid", useruid);
        result.put("firstname", firstname);
        result.put("lastname", lastname);
        result.put("datebirth", datebirth);
        result.put("passportnumber", passportnumber);
        result.put("address", address);
        result.put("country", country);
        result.put("city", city);
        result.put("street", street);
        result.put("sponsorlogin", sponsorlogin);
        result.put("sponsorid", sponsorid);
        result.put("phone", phone);
        result.put("email", email);
        result.put("photoUrl", photoUrl);
        result.put("selfiePhotoUrl", selfiePhotoUrl);
        result.put("money", money);
        result.put("status", status);
        result.put("male", male);
        result.put("sponsor2id", sponsor2id);
        result.put("sponsor3id", sponsor3id);
        result.put("sponsor4id", sponsor4id);
        result.put("sponsor5id", sponsor5id);
        result.put("statusend", statusend);
        result.put("password", password);
        result.put("dateOfReg", dateOfReg);
        result.put("isProfileFilled", isProfileFilled);
        result.put("verifyEnd", verifyEnd);
        return result;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getDatebirth() {
        return datebirth;
    }

    public void setDatebirth(String datebirth) {
        this.datebirth = datebirth;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }


    public String getPassportnumber() {
        return passportnumber;
    }

    public void setPassportnumber(String passportnumber) {
        this.passportnumber = passportnumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }



    public String getSponsorlogin() {
        return sponsorlogin;
    }

    public void setSponsorlogin(String sponsorlogin) {
        this.sponsorlogin = sponsorlogin;
    }





    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getUseruid() {
        return useruid;
    }

    public void setUseruid(String useruid) {this.useruid = useruid;}

    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public void setMoney(int money) {
        this.money = money;
    }
    public int getMoney() {
        return money;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getSponsorid() {
        return sponsorid;
    }

    public void setSponsorid(String sponsorid) {
        this.sponsorid = sponsorid;
    }

    public String getSponsor2id() {
        return sponsor2id;
    }

    public void setSponsor2id(String sponsor2id) {
        this.sponsor2id = sponsor2id;
    }

    public String getSponsor3id() {
        return sponsor3id;
    }

    public void setSponsor3id(String sponsor3id) {
        this.sponsor3id = sponsor3id;
    }

    public String getSponsor4id() {
        return sponsor4id;
    }

    public void setSponsor4id(String sponsor4id) {
        this.sponsor4id = sponsor4id;
    }

    public String getSponsor5id() {
        return sponsor5id;
    }

    public void setSponsor5id(String sponsor5id) {
        this.sponsor5id = sponsor5id;
    }

    public long getStatusend() {
        return statusend;
    }

    public void setStatusend(long statusend) {
        this.statusend = statusend;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateOfReg() {
        return dateOfReg;
    }

    public void setDateOfReg(String dateOfReg) {
        this.dateOfReg = dateOfReg;
    }

    public boolean getIsProfileFilled() {
        return isProfileFilled;
    }

    public void setProfileFilled(boolean profileFilled) {
        this.isProfileFilled = profileFilled;
    }

    public boolean getVerifyEnd() {
        return verifyEnd;
    }

    public void setVerifyEnd(boolean verifyEnd) {
        this.verifyEnd = verifyEnd;
    }

    public String getSelfiePhotoUrl() {
        return selfiePhotoUrl;
    }

    public void setSelfiePhotoUrl(String selfiePhotoUrl) {
        this.selfiePhotoUrl = selfiePhotoUrl;
    }
}
