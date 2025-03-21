package com.example.studentmanagement.model;

import java.io.Serializable;

public class Student implements Serializable {
    private String ID;
    private String name;
    private String date;
    private String gender;
    private String email;
    private String Address;
    private String Phone;
    private String idMajor;

    // Getters và setters
    public String getID() { return ID; }
    public void setID(String ID) { this.ID = ID; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return Address; }
    public void setAddress(String address) { this.Address = address; }
    public String getPhone() { return Phone; }
    public void setPhone(String phone) { this.Phone = phone; }
    public String getIdMajor() { return idMajor; }
    public void setIdMajor(String idMajor) { this.idMajor = idMajor; }
}