package org.o7planning.saeapplication.Service;

import android.content.Context;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ImageApiService {
    private GoogleCredentials credentials ;
    private ImageAnnotatorSettings imageAnnotatorSettings;
    private ImageAnnotatorClient vision;
    private static ImageApiService _ImageApiService = null;
    private ImageApiService(Context context) throws IOException {
        credentials = GoogleCredentials.fromStream(DefaultValueService.jsonConnectionString(context));
        imageAnnotatorSettings = ImageAnnotatorSettings.newBuilder()
                        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                        .build();
        vision = ImageAnnotatorClient.create(imageAnnotatorSettings);
    }

    public static ImageApiService getImageApiService(Context context) throws IOException {
        if(_ImageApiService == null) _ImageApiService = new ImageApiService(context);
        return _ImageApiService;
    }

    public static Map<String, Float> getImageInfo(String pathFile,Context context) throws IOException {
        Path path = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            path = Paths.get(pathFile);
        }
        byte[] data = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            data = Files.readAllBytes(path);
        }
        return getImageinfo(data,context);
    }
    public static Map<String, Float> getImageInfo(byte[] imageByte,Context context) throws IOException {
        return getImageinfo(imageByte,context);
    }

    public static List<String>getImageInfoFrench(byte[] imageByte,Context context) throws IOException {
        return getImageinfoFrench(imageByte,context);
    }

    public GoogleCredentials getCredentials() {
        return credentials;
    }

    public ImageAnnotatorSettings getImageAnnotatorSettings() {
        return imageAnnotatorSettings;
    }

    public ImageAnnotatorClient getVision() throws IOException {
        if(vision == null)
            vision = ImageAnnotatorClient.create(imageAnnotatorSettings);
        return vision;
    }

    private static List<String> getImageinfoFrench(byte[] data,Context context) throws IOException{
        return ImageApiService
                .getImageinfo(data,context)
                .keySet()
                .stream()
                .map(s->TranslationService.Translation(s,context))
                .collect(Collectors.toList());
    }
    private static Map<String, Float> getImageinfo(byte[] data,Context context) throws IOException {
        ImageApiService imageApiService = ImageApiService.getImageApiService(context);

        Map<String, Float> imageInfo = new HashMap<>();
        ByteString imgBytes = ByteString.copyFrom(data);

        // Builds the image annotation request
        List<AnnotateImageRequest> requests = new ArrayList<>();
        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                                        .addFeatures(feat)
                                        .setImage(img).build();

        requests.add(request);

        // Performs label detection on the image file
        BatchAnnotateImagesResponse response = imageApiService.getVision().batchAnnotateImages(requests);
        List<AnnotateImageResponse> responses = response.getResponsesList();

        for (AnnotateImageResponse res : responses) {
            if (res.hasError()) {
                System.out.format("Error: %s%n", res.getError().getMessage());
                return null;
            }

            for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                imageInfo.put(annotation.getDescription(),annotation.getScore());
            }
        }
        return imageInfo;
    }
}
