package com.alexstyl.specialdates.permissions;

import android.Manifest;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
@StringDef({Manifest.permission.READ_CONTACTS, Manifest.permission.READ_EXTERNAL_STORAGE})
public @interface MementoPermissions {
}
