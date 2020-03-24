package com.example.coals.instargramnewstest;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

public class SlidingImageAdapter extends PagerAdapter {

    private ArrayList<ImageModel> imageModels;
    private LayoutInflater layoutInflater;
    private Context context;
    ConstraintLayout constraintLayout;

    public SlidingImageAdapter(ArrayList<ImageModel> imageModels, Context context) {
        this.imageModels = imageModels;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageModels.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        View view = layoutInflater.inflate(R.layout.image, container, false);

        assert imageModels != null;
        final ImageView imageView = view.findViewById(R.id.imageView);
        imageView.setImageResource(imageModels.get(position).getImage());
//        imageView.getLayoutParams().width=800;
//        imageView.getLayoutParams().height=900;
//        imageView.getScaleType();
//        imageView.requestLayout();
        container.addView(view, 0);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewDialog=View.inflate(context,R.layout.dialog,null);
                PhotoView photoView=viewDialog.findViewById(R.id.imageDialog);
                ImageModel imageModel =imageModels.get(position);
                photoView.setImageResource(imageModel.getImage());
                AlertDialog.Builder dialog=new AlertDialog.Builder(context);
                dialog.setTitle("크게보기");
                dialog.setIcon(R.drawable.mainicon);
                dialog.setView(viewDialog);
                dialog.setPositiveButton("닫기",null);
                dialog.show();


            }
        });

        return view;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view.equals(o);
    }

    @Override
    public void restoreState(@Nullable Parcelable state, @Nullable ClassLoader loader) {

    }

    @Nullable
    @Override
    public Parcelable saveState() {
        return null;
    }
}
