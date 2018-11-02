package com.shalom.itai.myglobalphotos;

import android.content.Context;

/**
 * Proudly written by Itai on 26/09/2018.
 * GpsView Interface - The interface which the view has to implement
 */

interface GpsView {
    void notifyDataSetChange();

    Context getViewContext();
}
