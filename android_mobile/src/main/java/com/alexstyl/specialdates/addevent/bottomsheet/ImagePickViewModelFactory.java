package com.alexstyl.specialdates.addevent.bottomsheet;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.alexstyl.specialdates.addevent.ImageIntentFactory;
import com.alexstyl.specialdates.addevent.UriFilePathProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class ImagePickViewModelFactory {

    private final UriFilePathProvider uriFilePathProvider;
    private final IntentResolver intentResolver;
    private final ImageIntentFactory intentCreator;

    ImagePickViewModelFactory(UriFilePathProvider uriFilePathProvider, IntentResolver intentResolver, ImageIntentFactory intentCreator) {
        this.uriFilePathProvider = uriFilePathProvider;
        this.intentResolver = intentResolver;
        this.intentCreator = intentCreator;
    }

    public List<ImagePickerOptionViewModel> createViewModels() throws IOException {
        File file = uriFilePathProvider.createImageFile();

        Uri outputUri = uriFilePathProvider.uriFor(file);
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        List<ImagePickerOptionViewModel> captureVM = intentResolver.createViewModelsFor(takePictureIntent, file.getAbsolutePath());

        Intent pickAnImage = intentCreator.pickExistingImage();
        List<ImagePickerOptionViewModel> pickVM = intentResolver.createViewModelsFor(pickAnImage, file.getAbsolutePath()); // TODO file here doesnt make sense

        List<ImagePickerOptionViewModel> list = new ArrayList<>(captureVM.size() + pickVM.size());
        list.addAll(captureVM);
        list.addAll(pickVM);
        return list;
    }

}
