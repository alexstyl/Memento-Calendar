package com.alexstyl.specialdates.addevent.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.addevent.AccountData;
import com.novoda.notils.caster.Views;

public class AccountViewHolder {

    private final ImageView accountIcon;
    private final TextView accountName;

    public AccountViewHolder(View accountIcon) {
        this.accountIcon = Views.findById(accountIcon, R.id.account_icon);
        this.accountName = Views.findById(accountIcon, R.id.account_name);
    }

    public static AccountViewHolder from(View convertView) {
        return (AccountViewHolder) convertView.getTag();
    }

    public void bind(AccountData account) {
        accountIcon.setImageDrawable(account.getIcon());
        accountName.setText(account.getAccountName());
    }
}
