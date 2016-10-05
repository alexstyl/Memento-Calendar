package com.alexstyl.specialdates.upcoming;

import com.alexstyl.specialdates.date.Date;

import java.io.Serializable;

public class LoadingTimeDuration implements Serializable {

    private Date from;
    private Date to;

    public LoadingTimeDuration(Date from, Date to) {
        this.from = from;
        this.to = to;
    }

    public Date getFrom() {
        return from;
    }

    public Date getTo() {
        return to;
    }

}
