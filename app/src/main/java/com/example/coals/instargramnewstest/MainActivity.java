package com.example.coals.instargramnewstest;

import android.content.Context;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;


public class MainActivity extends AppCompatActivity{









    public static BottomNavigationView bottomMenu;
    private FrameLayout frameLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private FragmentPagerAdapter fragmentPagerAdapter;
    public static ViewPager viewPager;
    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;
    private Fragment4 fragment4;
    private Fragment5 fragment5;
    private MenuItem menuItem;
    private static final String TAG = "MainActivity";
    public static Context context;
    public static int num=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=getApplicationContext();




        bottomMenu = findViewById(R.id.bottomMenu);
        frameLayout = findViewById(R.id.frameLayout);
        viewPager = findViewById(R.id.viewPager);

//        fragment1 = new Fragment1();
//        fragment2 = new Fragment2();
//        fragment3 = new Fragment3();
//        fragment4 = new Fragment4();
//        fragment5 = new Fragment5();
//        Log.d(TAG, "onCreate");

//        CoordinatorLayout.LayoutParams layoutParams= (CoordinatorLayout.LayoutParams) bottomMenu.getLayoutParams();
//        layoutParams.setBehavior(new BottonNavigationMenu());

        //bottomMenu 를 변경을 했을때 해당된 fragment를 세팅하는 리스너
        bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_1: //프레그먼트 화면전환
                        viewPager.setCurrentItem(0);
//                        setChangeFragment(0);
                        break;
//                        break;
                    case R.id.action_2:
                        viewPager.setCurrentItem(1);
                        fragment2.btn_edit.setVisibility(View.INVISIBLE);
                        Fragment2.btn_start.setVisibility(View.VISIBLE);
//                        setChangeFragment(1);
                        break;
                    case R.id.action_3:
                        viewPager.setCurrentItem(2);
//                        setChangeFragment(2);
                        break;
                    case R.id.action_4:
                        viewPager.setCurrentItem(3);
//                        setChangeFragment(3);
                        break;
                    case R.id.action_5:
                        viewPager.setCurrentItem(4);
//                        setChangeFragment(4);
                        break;
                }

                return false;
            }
        });
//        setChangeFragment(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomMenu.getMenu().getItem(0).setChecked(false);
                }
                bottomMenu.getMenu().getItem(i).setChecked(true);
                menuItem = bottomMenu.getMenu().getItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        setupViewPager(viewPager);
    }



    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();
        fragment5 = new Fragment5();
        adapter.addFragment(fragment1);
        adapter.addFragment(fragment2);
        adapter.addFragment(fragment3);
        adapter.addFragment(fragment4);
        adapter.addFragment(fragment5);
        viewPager.setAdapter(adapter);
    }




}
