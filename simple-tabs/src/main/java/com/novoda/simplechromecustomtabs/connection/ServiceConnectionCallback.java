package com.novoda.simplechromecustomtabs.connection;

interface ServiceConnectionCallback {

    void onServiceConnected(ConnectedClient client);

    void onServiceDisconnected();

}
