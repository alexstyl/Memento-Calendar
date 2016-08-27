package com.alexstyl.specialdates.addevent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.alexstyl.specialdates.R;
import com.alexstyl.specialdates.addevent.ui.AccountViewHolder;

import java.util.ArrayList;
import java.util.List;

public class AccountSelectionAdapter extends BaseAdapter {

    private final List<AccountData> accounts = new ArrayList<>();

    public AccountSelectionAdapter() {
    }

    public void setAccounts(List<AccountData> accounts) {
        this.accounts.clear();
        this.accounts.addAll(accounts);

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AccountViewHolder vh;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_account, parent, false);
            vh = new AccountViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = AccountViewHolder.from(convertView);
        }

        AccountData account = getItem(position);
        vh.bind(account);
        return convertView;
    }

    @Override
    public AccountData getItem(int position) {
        return accounts.get(position);
    }

    @Override
    public int getCount() {
        return accounts.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
