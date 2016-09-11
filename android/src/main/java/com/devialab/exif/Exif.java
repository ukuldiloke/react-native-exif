package com.devialab.exif;

import android.media.ExifInterface; 

import com.facebook.react.bridge.*;

import java.io.IOException;

import com.facebook.react.bridge.Arguments;

import com.facebook.react.bridge.WritableMap;

import android.net.Uri;
import android.provider.MediaStore;
import android.database.Cursor;

public class Exif extends ReactContextBaseJavaModule  {

    private static final String[] EXIF_ATTRIBUTES = new String[] {
        ExifInterface.TAG_APERTURE,
        ExifInterface.TAG_DATETIME,
        ExifInterface.TAG_DATETIME_DIGITIZED,
        ExifInterface.TAG_EXPOSURE_TIME,
        ExifInterface.TAG_FLASH,
        ExifInterface.TAG_FOCAL_LENGTH,
        ExifInterface.TAG_GPS_DATESTAMP,
        ExifInterface.TAG_GPS_PROCESSING_METHOD,
        ExifInterface.TAG_GPS_TIMESTAMP,
        ExifInterface.TAG_IMAGE_LENGTH,
        ExifInterface.TAG_IMAGE_WIDTH,
        ExifInterface.TAG_ISO,
        ExifInterface.TAG_MAKE,
        ExifInterface.TAG_MODEL,
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.TAG_SUBSEC_TIME,
        ExifInterface.TAG_SUBSEC_TIME_DIG,
        ExifInterface.TAG_SUBSEC_TIME_ORIG,
        ExifInterface.TAG_WHITE_BALANCE
    };
    
    public Exif(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "ReactNativeExif";
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getCurrentActivity()
            .managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor
            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @ReactMethod
    public void getExif(String uri, Promise promise) throws Exception {
        ExifInterface exif; 
        try {
            String path = getRealPathFromURI(Uri.parse(uri));
            exif = new ExifInterface(path);
        } catch (IOException e) { 
            promise.reject(e.toString());
            return;
        } 

        WritableMap exifMap = Arguments.createMap();
        float[] output = new float[2];
        if (exif.getLatLong(output)) {
            exifMap.putDouble("GPSLatitude", output[0]);
            exifMap.putDouble("GPSLongitude", output[1]);
        }

        for (String attribute : EXIF_ATTRIBUTES) {
            String value = exif.getAttribute(attribute);
            exifMap.putString(attribute, value);
        }

        promise.resolve(exifMap);
    }

}
