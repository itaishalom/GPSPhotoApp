package com.shalom.itai.myglobalphotos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Proudly written by Itai on 24/09/2018.
 * GpsPhotoRecyclerAdapter - The adapter for the images RV
 */

class GpsPhotoRecyclerAdapter extends RecyclerView.Adapter<GpsPhotoViewHolder> {

    private final GpsPhotoListPresenter presenter;


    GpsPhotoRecyclerAdapter(GpsView gpsView) {
        this.presenter = new GpsPhotoListPresenter(gpsView);
    }

    @Override
    public GpsPhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GpsPhotoViewHolder(presenter, LayoutInflater.from(parent.getContext()).inflate(R.layout.image_in_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(GpsPhotoViewHolder holder, int position) {
        presenter.onBindRepositoryRowViewAtPosition(position, holder);
    }

    @Override
    public int getItemCount() {
        return presenter.getRepositoriesRowsCount();
    }
}