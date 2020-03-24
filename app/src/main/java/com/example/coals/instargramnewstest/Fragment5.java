package com.example.coals.instargramnewstest;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Fragment5 extends Fragment {
    private View view;
    private static ViewPager viewPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModels;

    private int[] imageList = new int[]{R.drawable.todaynews, R.drawable.todayweather, R.drawable.alarmhistory, R.drawable.weatherhistory, R.drawable.newhistory};
    private String[] stringName = new String[]{"오늘의 뉴스", "오늘의 날씨", "알람의 역사", "일기예보의 과정", "뉴스의 중요성"};


    public static Fragment5 newInstance() {
        Fragment5 fragment5 = new Fragment5();
        return fragment5;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment5, container, false);
        imageModels = new ArrayList<>();
        imageModels = populateList();
        init();
        return view;
    }

    private ArrayList<ImageModel> populateList() {
        ArrayList<ImageModel> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setImage(imageList[i]);
            imageModel.setName(stringName[i]);
            list.add(imageModel);
        }
        return list;
    }

    private void init() {
        viewPager = view.findViewById(R.id.viewPager);
        viewPager.setAdapter(new SlidingImageAdapter(imageModels, getContext()));

        CirclePageIndicator indicator = view.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);
        NUM_PAGES = imageModels.size();

        final Handler handler = new Handler();
        final Runnable upDate = new Runnable() {
            @Override
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(upDate);
            }
        }, 3000, 3000);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                currentPage = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


}

