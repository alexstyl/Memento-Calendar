package com.alexstyl.specialdates.addevent;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorDescription;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public final class WriteableAccountsProvider {

    private static final String GOOGLE_ACCOUNT = "com.google";

    private final AccountManager accountManager;
    private final PackageManager packageManager;

    public static WriteableAccountsProvider from(Context context) {
        AccountManager manager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        PackageManager packageManager = context.getPackageManager();

        return new WriteableAccountsProvider(manager, packageManager);
    }

    private WriteableAccountsProvider(AccountManager accountManager, PackageManager packageManager) {
        this.accountManager = accountManager;
        this.packageManager = packageManager;
    }

    ArrayList<AccountData> getAvailableAccounts() {
        ArrayList<AccountData> accounts = new ArrayList<>();

        AuthenticatorDescription[] accountTypes = accountManager.getAuthenticatorTypes();
        for (Account account : accountManager.getAccounts()) {
            String accountType = account.type;
            if (accountIsWritable(accountType)) {
                AuthenticatorDescription description = getAuthenticatorDescription(accountType, accountTypes);
                Drawable icon = packageManager.getDrawable(description.packageName, description.iconId, null);
                accounts.add(new AccountData(account.name, description.type, icon));
            }
        }
        return accounts;
    }

    private static boolean accountIsWritable(String accountType) {
        return GOOGLE_ACCOUNT.equals(accountType);
    }

    private static AuthenticatorDescription getAuthenticatorDescription(String type,
                                                                        AuthenticatorDescription[] dictionary) {
        for (int i = 0; i < dictionary.length; i++) {
            if (dictionary[i].type.equals(type)) {
                return dictionary[i];
            }
        }
        throw new RuntimeException("Unable to find matching authenticator");
    }

}
