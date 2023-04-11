package org.o7planning.saeapplication.Activity.Other;

import android.content.Context;

import org.o7planning.saeapplication.Service.ImageApiService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ImageApiThread extends Thread {
    private List<String> folderNames;
    private byte[] mPhoto;
    private Context context;

    public ImageApiThread(byte[] photo, Context context) {
        this.folderNames = new ArrayList<>();
        this.mPhoto = photo;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            Map<String, Float> imageInfo = ImageApiService.getImageInfo(mPhoto, context);
            folderNames.addAll(imageInfo.keySet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> getFolderNames() {
        return folderNames;
    }
}
