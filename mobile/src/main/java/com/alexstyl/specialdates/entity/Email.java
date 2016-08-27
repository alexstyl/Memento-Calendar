
package com.alexstyl.specialdates.entity;

import android.content.Context;
import android.content.Intent;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.util.Utils;

public class Email extends DataType {

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        DataType other = Utils.as(DataType.class, o);

        if (other == null)
            return false;

        if (this.dataType != other.dataType)
            return false;

        return data.equals(other.data);
    }

    public Email(String address, Integer type, String label) {
        super(address, type, label, TYPE_EMAIL);
    }

    public String getEmail() {
        return data;
    }

    public void sendMail(Context context) {
        Intent emailIntent = Utils.getEmailIntent(data, null, null);
        context.startActivity(emailIntent);
    }

}
