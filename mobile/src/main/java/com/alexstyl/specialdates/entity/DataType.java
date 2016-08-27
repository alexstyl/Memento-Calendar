
package com.alexstyl.specialdates.entity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.ContactsContract.CommonDataKinds;

import com.alexstyl.specialdates.R;

public class DataType implements Parcelable {

    @Override
    public String toString() {
        return data + " (" + label + ")";
    }

    @Override
    public int hashCode() {
        return dataType + data.hashCode();
    }

    public DataType(String data, Integer type, String label, int dataType) {
        this.data = data;
        this.type = type;
        this.label = label;
        this.dataType = dataType;
    }

    protected String data;
    protected String label;
    protected int type;

    protected int dataType = -1;

    public String getData() {
        return data;
    }

    public String getLabel() {
        return label;
    }

    public int getType() {
        return type;
    }

    public static final int TYPE_PHONE = 0;
    public static final int TYPE_EMAIL = 1;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(data);
        dest.writeString(label);
        dest.writeInt(type);
        dest.writeInt(dataType);

    }

    public DataType(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<DataType> CREATOR = new Parcelable.Creator<DataType>() {
        public DataType createFromParcel(Parcel in) {
            return new DataType(in);
        }

        public DataType[] newArray(int size) {

            return new DataType[size];
        }

    };

    public void readFromParcel(Parcel in) {
        data = in.readString();
        label = in.readString();
        type = in.readInt();
        dataType = in.readInt();

    }

    public CharSequence getDisplayingLabel(Resources res) {
        if (label == null) {
            if (dataType == TYPE_PHONE) {
                label = CommonDataKinds.Phone.getTypeLabel(res, type, label).toString();
            } else if (dataType == TYPE_EMAIL) {
                label = CommonDataKinds.Email.getTypeLabel(res, type, label).toString();
            }
        }
        return label;
    }


    protected String getActionViaText(Context context, int resStringID) {
        return context.getString(R.string.action_via, context.getString(resStringID));
    }

}
