package com.shalom.itai.myglobalphotos;

import android.media.ExifInterface;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.IOException;

/**
 * Proudly written by Itai on 23/09/2018.
 * GpsPhoto - The model - it's an image with GPs data on it.
 */

class GpsPhoto implements Parcelable {
    //Fields
    private float mLongitude;
    private float mLatitude;
    private String mPath;


    //Functions

    /**
     * @return the Longitude of the image
     */
    double getLongitude() {
        return (double) mLongitude;
    }

    /**
     * @return the Latitude of the image
     */
    double getLatitude() {
        return (double) mLatitude;
    }

    String getPath() {
        return mPath;
    }

    /**
     * @return True of image has GPS info, false otherwise
     */
    boolean hasGpsData() {
        return (mLatitude != 0.0f && mLongitude != 0.0f);
    }

    /**
     * Constructor of GpsPhoto (Model)
     *
     * @param path - Path to image
     */
    GpsPhoto(String path) {
        mPath = path;
        extractGpsData();
    }

    @Override
    public boolean equals(Object other) {
        return other != null && other instanceof GpsPhoto && ((GpsPhoto) other).getPath().equals(this.getPath());
    }

    /**
     * Check if the image has GPS info saved on it and if so, pull the Latitude and Longitude
     */
    private void extractGpsData() {
        try {
            ExifInterface exifInterface = new ExifInterface(mPath);
            float[] latLong = new float[2];
            exifInterface.getLatLong(latLong);
            mLatitude = latLong[0];
            mLongitude = latLong[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mPath);
        parcel.writeFloat(mLatitude);
        parcel.writeFloat(mLongitude);
    }

    private GpsPhoto(Parcel in) {
        mPath = in.readString();
        mLatitude = in.readFloat();
        mLongitude = in.readFloat();
    }

    public static final Parcelable.Creator<GpsPhoto> CREATOR = new Creator<GpsPhoto>() {
        @Override
        public GpsPhoto createFromParcel(Parcel in) {
            return new GpsPhoto(in);
        }

        @Override
        public GpsPhoto[] newArray(int size) {
            return new GpsPhoto[size];
        }
    };
}