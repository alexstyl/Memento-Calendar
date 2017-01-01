package com.alexstyl.specialdates.addevent.bottomsheet;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.alexstyl.specialdates.addevent.ImageIntentFactory;

import java.util.ArrayList;
import java.util.List;

final public class ImagePickResolver {

    private final IntentResolver intentResolver;
    private final ImageIntentFactory intentCreator;

    public ImagePickResolver(IntentResolver intentResolver, ImageIntentFactory intentCreator) {
        this.intentResolver = intentResolver;
        this.intentCreator = intentCreator;
    }

    /**
     * @param outputImageUri The output for the {@linkplain MediaStore#ACTION_IMAGE_CAPTURE} intent.
     */
    public List<IntentOptionViewModel> createFor(Uri outputImageUri) {
        Intent takeAPicture = intentCreator.captureNewPhoto(outputImageUri);
        List<IntentOptionViewModel> captureVM = intentResolver.createViewModelsFor(takeAPicture);

        Intent pickAnImage = intentCreator.pickExistingImage();
        List<IntentOptionViewModel> pickVM = intentResolver.createViewModelsFor(pickAnImage);

        List<IntentOptionViewModel> list = new ArrayList<>(captureVM.size() + pickVM.size());
        list.addAll(captureVM);
        list.addAll(pickVM);
        return list;
    }

}
