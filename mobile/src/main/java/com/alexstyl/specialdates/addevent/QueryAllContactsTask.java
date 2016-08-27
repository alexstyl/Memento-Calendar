package com.alexstyl.specialdates.addevent;

import android.content.Context;

import com.alexstyl.specialdates.util.AsynchronousTask;

import java.util.List;

public abstract class QueryAllContactsTask extends AsynchronousTask<List<AccountData>> {

    private final WriteableAccountsProvider provider;

    protected QueryAllContactsTask(Context context) {
        this.provider = WriteableAccountsProvider.from(context);
    }

    @Override
    public List<AccountData> performTask() {
        provider.getAvailableAccounts();
        return provider.getAvailableAccounts();
    }
}
