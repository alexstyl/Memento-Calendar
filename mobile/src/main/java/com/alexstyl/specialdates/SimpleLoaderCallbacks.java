package com.alexstyl.specialdates;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.alexstyl.specialdates.events.bankholidays.BankHoliday;

import java.util.List;

public class SimpleLoaderCallbacks<T> implements LoaderManager.LoaderCallbacks<List<BankHoliday>> {

    @Override
    public Loader<List<BankHoliday>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<BankHoliday>> loader, List<BankHoliday> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<BankHoliday>> loader) {

    }
}
