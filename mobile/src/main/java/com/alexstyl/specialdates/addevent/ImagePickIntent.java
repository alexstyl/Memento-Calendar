package com.alexstyl.specialdates.addevent;

import android.content.Intent;
import android.net.Uri;

interface ImagePickIntent {

    Intent createIntentWithOutput(Uri imagePath);
}
