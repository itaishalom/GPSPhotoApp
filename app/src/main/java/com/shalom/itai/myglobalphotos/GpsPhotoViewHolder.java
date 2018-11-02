package com.shalom.itai.myglobalphotos;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.io.File;

/**
 * Proudly written by Itai on 24/09/2018.
 * The recycler view's view holder, also implements the GpsRowView
 */

class GpsPhotoViewHolder extends RecyclerView.ViewHolder implements GpsRowView, View.OnClickListener {
    private ImageView imageView;
    private ImageView imageViewGps;

    private ProgressBar progressBar;
    private GpsPhotoListPresenter mPresenter;
    private Context viewsContext;

    /**
     * The viewHolder's C-tor
     *
     * @param presenter - the presenter
     * @param itemView  - The itemView to show
     */
    GpsPhotoViewHolder(GpsPhotoListPresenter presenter, View itemView) {
        super(itemView);
        mPresenter = presenter;
        imageView = (ImageView) itemView.findViewById(R.id.iv_photo);
        imageViewGps = (ImageView) itemView.findViewById(R.id.gps_photo);
        progressBar = (ProgressBar) itemView.findViewById(R.id.iv_progress);
        itemView.setOnClickListener(this);
        viewsContext = itemView.getContext();
    }

    @Override
    public void setImage(final GpsPhoto gpsPhoto) {
        progressBar.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        imageViewGps.setVisibility(View.GONE);

        //Using Picasso, I make the image showed up faster and with progressbar in the meantime
        Picasso.with(viewsContext)
                .load(new File(gpsPhoto.getPath()))
                .resize(300, 300).centerCrop()
                .into(imageView, new com.squareup.picasso.Callback() {

                    @Override
                    public void onSuccess() {
                        if (progressBar != null) {
                            progressBar.setVisibility(View.GONE);
                        }
                        imageView.setVisibility(View.VISIBLE);
                        if (gpsPhoto.hasGpsData()) {
                            imageViewGps.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onError() {
                        mPresenter.removePhotoOnError(gpsPhoto);
                    }
                });
    }

    /**
     * When image is pressed the app will go to full screen in order to show it.
     * @param view - The view
     */
    @Override
    public void onClick(View view) {
        int position = getAdapterPosition();
        if (position != RecyclerView.NO_POSITION) {
            GpsPhoto gpsPhoto = mPresenter.getPhotoFromArray(position);
            Intent intent = new Intent(viewsContext, FullScreenActivity.class);
            intent.putExtra(FullScreenActivity.GPS_PHOTO_DATA, gpsPhoto);
            viewsContext.startActivity(intent);
        }
    }
}