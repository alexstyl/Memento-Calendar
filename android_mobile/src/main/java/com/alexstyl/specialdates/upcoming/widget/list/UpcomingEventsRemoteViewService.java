package com.alexstyl.specialdates.upcoming.widget.list;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.alexstyl.resources.Colors;
import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.upcoming.UpcomingEventsProvider;

import javax.inject.Inject;

public class UpcomingEventsRemoteViewService extends RemoteViewsService {

    @Inject Colors colors;
    @Inject ImageLoader imageLoader;
    @Inject UpcomingEventsProvider peopleEventsProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        AppComponent applicationModule = ((MementoApplication) getApplication()).getApplicationModule();
        applicationModule.inject(this);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        CircularAvatarFactory avatarFactory = new CircularAvatarFactory(
                imageLoader,
                colors
        );
        return new UpcomingEventsViewsFactory(
                getPackageName(),
                peopleEventsProvider,
                this,
                getResources(),
                avatarFactory
        );
    }
}
