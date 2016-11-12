package com.alexstyl.specialdates.events.peopleevents;

import android.net.Uri;

final class UriQuery {
    private final Uri uri;
    private final String[] projection;
    private final String selection;
    private final String[] selectionArgs;
    private final String sortOrder;

    UriQuery(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        this.uri = uri;
        this.projection = projection;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
        this.sortOrder = sortOrder;
    }

    Uri getUri() {
        return uri;
    }

    String[] getProjection() {
        return projection;
    }

    String getSelection() {
        return selection;
    }

    String[] getSelectionArgs() {
        return selectionArgs;
    }

    String getSortOrder() {
        return sortOrder;
    }
}
