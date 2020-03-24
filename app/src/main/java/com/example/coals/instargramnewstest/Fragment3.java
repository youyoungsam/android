package com.example.coals.instargramnewstest;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.willowtreeapps.spruce.Spruce;
import com.willowtreeapps.spruce.animation.DefaultAnimations;
import com.willowtreeapps.spruce.sort.DefaultSort;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.coals.instargramnewstest.MainActivity.bottomMenu;

public class Fragment3 extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private String[] mDataset = {"1", "2"};
    private static final String TAG = "Fragment3";
    ArrayList<NewsData> news = new ArrayList<>();
    private Animator spruceAnimator;
    RequestQueue queue;
    Context context;

    public static Fragment3 newInstance() {
        Fragment3 fragment3 = new Fragment3();
        return fragment3;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment3, container, false);
        recyclerView = view.findViewById(R.id.my_recycler_view);
        recyclerView.setHasFixedSize(true);
        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_bright),
                getResources().getColor(android.R.color.holo_green_light),
                getResources().getColor(android.R.color.holo_orange_light),
                getResources().getColor(android.R.color.holo_red_light));

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                ((MyAdapter) adapter).clear();
                Log.d(TAG, "삭제후 불러오기");
                ((MyAdapter) adapter).addAll(news);
                getNews();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        swipeRefreshLayout.setRefreshing(false);
                        Log.d(TAG, "새로고침");
                        Toast.makeText(getContext(), "새로고침 했습니다.", Toast.LENGTH_LONG).show();
                    }
                }, 3000);
            }
        });
        layoutManager = new LinearLayoutManager(container.getContext()) {
            @Override
            public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
//                initSpruce();
            }
        };
        List<NewsData> list =new ArrayList<>();
        for(int i=0;i<list.size();i++){
            list.add(new NewsData());
        }

        recyclerView.setLayoutManager(layoutManager);

        queue = Volley.newRequestQueue(container.getContext());
        getNews();


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 && bottomMenu.isShown()) {
                    bottomMenu.setVisibility(View.GONE);
                } else if (dy < 0) {
                    bottomMenu.setVisibility(View.VISIBLE);
                }
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (spruceAnimator != null) {
            spruceAnimator.start();
        }
    }

    private void initSpruce() {
        spruceAnimator = new Spruce.SpruceBuilder(recyclerView)
                .sortWith(new DefaultSort(100))
                .animateWith(DefaultAnimations.shrinkAnimator(recyclerView, 800),
                        ObjectAnimator.ofFloat(recyclerView, "translationX", -recyclerView.getWidth(), 0f).setDuration(800))
                .start();
    }


    private void getNews() {
        String url = "https://newsapi.org/v2/top-headlines?country=kr&apiKey=89a3a7b6c6194cbead591d2c5396ae0b";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray arrayArtcles = jsonObject.getJSONArray("articles");

                    news = new ArrayList<>();

                    for (int i = 0, j = arrayArtcles.length(); i < j; i++) {
                        JSONObject obj = arrayArtcles.getJSONObject(i);

                        Log.d("NEWS", obj.toString());

                        NewsData newsData = new NewsData();
                        newsData.setTitle(obj.getString("title"));
                        newsData.setUrlToImage(obj.getString("urlToImage"));
                        newsData.setDescription(obj.getString("description"));
                        newsData.setUrl(obj.getString("url"));
                        news.add(newsData);
                    }
                    adapter = new MyAdapter(news, context, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Object obj = v.getTag();
                            if (obj != null) {
                                int position = (int) obj;
                                ((MyAdapter) adapter).getNews(position).getUrl(); //클래스 형변환 -> 부모의 자식확인(친자확인)
                                Intent intent = new Intent(context, NewsDetailActivity.class);
                                intent.putExtra("news", ((MyAdapter) adapter).getNews(position));
                                startActivity(intent);
                            }
                        }
                    });
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });
        queue.add(stringRequest);
    }
}

