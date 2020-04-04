package com.prajwal331.qfs.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class User {
    public String username;
    public String address;
    public String startDate;
    public String endDate;
    public boolean quarantined;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String address, String startDate, String endDate, boolean quarantined) {
        this.username = username;
        this.address = address;
        this.startDate = startDate;
        this.endDate = endDate;
        this.quarantined = quarantined;
    }
}
