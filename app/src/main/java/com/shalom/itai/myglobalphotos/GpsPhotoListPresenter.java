package com.shalom.itai.myglobalphotos;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Proudly written by Itai on 24/09/2018.
 * The presenter - Note: no "import android..." on it
 */

class GpsPhotoListPresenter {
    private List<GpsPhoto> allPhotos;
    private GpsView mGpsView;
    private static int NOTIFY_CONSTANT = 500;
    private static int NOTIFY_CONSTANT_FIRST = 10;
    private boolean isNotifiedOnce = false;

    /**
     * Constructor
     *
     * @param gpsView - A class that implements the GpsView interface
     */
    GpsPhotoListPresenter(GpsView gpsView) {
        allPhotos = new ArrayList<>();
        mGpsView = gpsView;
        ImagesReaderThread thread = new ImagesReaderThread();
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }

    /**
     * Called when the DB finishing to read data.
     */
    void finishedReading() {
        notifyDataSetChange();
    }

    /**
     * Different thread to read all images from local DB since it make a long time.
     */
    private class ImagesReaderThread extends Thread {
        public void run() {
            DBInteractor.getInstance().getAllImages(mGpsView.getViewContext(), GpsPhotoListPresenter.this);
        }
    }

    /**
     * Adds an image to the list and notifies and view about that
     *
     * @param pathToImage - Path to image
     */
    void addImageToList(String pathToImage) {
        allPhotos.add(new GpsPhoto(pathToImage));
        int notifyValue = isNotifiedOnce ? NOTIFY_CONSTANT : NOTIFY_CONSTANT_FIRST;
        if (allPhotos.size() % notifyValue == 0) {
            notifyDataSetChange();
            isNotifiedOnce = true;
        }

    }

    /**
     * Notifies the view that are changes to the set
     */
    private void notifyDataSetChange() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mGpsView.notifyDataSetChange();
            }
        });
    }

    /**
     * Sets the image to the rowView
     *
     * @param position - The position of the image
     * @param rowView  - The current rowView
     */
    void onBindRepositoryRowViewAtPosition(int position, GpsPhotoViewHolder rowView) {
        rowView.setImage(allPhotos.get(position));
    }


    /**
     * In case there was an error, removes the image from the set and notifies the view
     *
     * @param gpsPhoto - Bad image to be removed
     */
    void removePhotoOnError(GpsPhoto gpsPhoto) {
        allPhotos.remove(gpsPhoto);
        mGpsView.notifyDataSetChange();
    }

    /**
     * Returns the image from a given index
     *
     * @param index index of the image
     * @return the image on that index
     */
    GpsPhoto getPhotoFromArray(int index) {
        return allPhotos.get(index);
    }

    /**
     * @return The number of images on the set
     */
    int getRepositoriesRowsCount() {
        return allPhotos.size();
    }
}
