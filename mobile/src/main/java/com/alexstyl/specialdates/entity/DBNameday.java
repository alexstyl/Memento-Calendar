
package com.alexstyl.specialdates.entity;

public class DBNameday {

    public DBNameday(String date, String name) {
        this.name = name;
        this.date = date;
    }

    public String name;
    public String date;

    @Override
    public String toString() {
        return date + ": " + name;
    }
}
