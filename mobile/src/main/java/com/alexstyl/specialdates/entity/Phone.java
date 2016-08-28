
package com.alexstyl.specialdates.entity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Telephony;
import android.telephony.PhoneNumberUtils;

import com.alexstyl.specialdates.util.Utils;

public class Phone extends DataType {

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;

        DataType other = Utils.as(DataType.class, o);

        if (other == null)
            return false;

        if (this.dataType != other.dataType)
            return false;

        return PhoneNumberUtils.compare(data, other.data);
    }

    public Phone(String number, Integer type, String label) {
        super(number, type, label, TYPE_PHONE);
    }

    /**
     * Returns the number of this Phone
     */
    public String getNumber() {
        return data;
    }

    public void dial(Context context) throws ActivityNotFoundException {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + this.data));
        context.startActivity(callIntent);

    }


    /**
     * Fires up an intent with the request to send an sms to the msisdn that of
     * this Phone
     *
     * @param context The context to use
     * @throws ActivityNotFoundException Throw if there is no installed
     *                                   application to handle our request
     */
    @SuppressLint("NewApi")
    public void sendText(Context context) throws ActivityNotFoundException {

        if (Utils.hasKitKat()) {
            // forward to the default sms app if we're in kitkat

            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context);
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"
                    + Uri.encode(getNumber())));

            if (defaultSmsPackageName != null) {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            context.startActivity(sendIntent);

        } else {
            Intent textIntent = new Intent();

            textIntent.setAction(Intent.ACTION_VIEW);
            textIntent.setData(Uri.fromParts("sms", getNumber(), null));
            context.startActivity(textIntent);
        }
    }
}
