package com.alexstyl.specialdates.addevent;

import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;

import java.util.List;

public class AccountSelectionSpinner extends AppCompatSpinner {

    private final AccountSelectionAdapter adapter = new AccountSelectionAdapter();

    public AccountSelectionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setAdapter(adapter);
    }

    public void setAccounts(List<AccountData> accounts) {
        adapter.setAccounts(accounts);
    }

    public AccountData getSelectedAccount() {
        return adapter.getItem(getSelectedItemPosition());
    }
}
