package com.example.coals.instargramnewstest;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList=new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    //뷰페이저에서 프레그먼트 교체를 보여주는 역할을 한다.
    @Override
    public Fragment getItem(int position) {
//        switch (position) {
////            case 0:
////                return null;
////            //     return Fragment1.newInstance();
////            case 1:
////                return Fragment2.newInstance();
////            case 2:
////                return Fragment3.newInstance();
////            case 3:
////                return Fragment4.newInstance();
////            case 5:
////                return Fragment5.newInstance();
////            default:
////                return null;
////        }
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Nullable
    @Override //상단의 탭 레이아웃 제목에 대한 텍스트를 선언하는 역할
    public CharSequence getPageTitle(int position) {
//        switch (position) {
//            case 0:
//                return "Left";
//            case 1:
//                return "Center";
//            case 2:
//                return "Right";
//            default:
//                return null;
//        }
        return null;
    }
    public void addFragment(Fragment fragment){
        fragmentList.add(fragment);
    }

}



