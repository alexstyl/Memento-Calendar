package com.alexstyl.specialdates.upcoming.widget.list;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.alexstyl.resources.ColorResources;
import com.alexstyl.resources.DimensionResources;
import com.alexstyl.resources.Strings;
import com.alexstyl.specialdates.AppComponent;
import com.alexstyl.specialdates.MementoApplication;
import com.alexstyl.specialdates.images.ImageLoader;
import com.alexstyl.specialdates.upcoming.UpcomingEventsProvider;

import javax.inject.Inject;
import javax.inject.Named;

public class UpcomingEventsRemoteViewService extends RemoteViewsService {

    @Inject Strings strings;
    @Inject DimensionResources dimensResources;
    @Inject ColorResources colorResources;
    @Inject ImageLoader imageLoader;
    @Inject
    @Named("widget")
    UpcomingEventsProvider peopleEventsProvider;

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
                colorResources
        );
        return new UpcomingEventsViewsFactory(
                getPackageName(),
                peopleEventsProvider,
                dimensResources,
                this,
                avatarFactory
        );
    }
}
