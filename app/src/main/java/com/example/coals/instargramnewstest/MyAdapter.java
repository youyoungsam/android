package com.example.coals.instargramnewstest;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<NewsData> mDataset;
    private static View.OnClickListener onClickListener;


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView TextView_title;
        public TextView TextView_content;
        public SimpleDraweeView ImageView_title;
        public View rootView;

        public MyViewHolder(View v) {
            super(v);
            TextView_title = v.findViewById(R.id.TextView_title);
            TextView_content = v.findViewById(R.id.TextView_content);
            ImageView_title = v.findViewById(R.id.ImageView_title);
            rootView = v;

            v.setClickable(true);
            v.setEnabled(true);
            //본문 어디든 클릭을 했을때 본문이 넘어갈수있게 v에 이벤트처리
            v.setOnClickListener(onClickListener);

        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(ArrayList<NewsData> myDataset, Context context, View.OnClickListener onClick) {
        mDataset = myDataset;
        onClickListener = onClick;
        Fresco.initialize(context);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_news, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // 뉴스의 데이터를 가져옴. (헤드라인,이미지,본문)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NewsData news = mDataset.get(position);

        holder.TextView_title.setText(news.getTitle());

        String content = news.getDescription();
        if (content != null && content.length() > 0) {
            holder.TextView_content.setText(content);
        } else {
            holder.TextView_content.setText("-");
        }

        Uri uri = Uri.parse(news.getUrlToImage());
        holder.ImageView_title.setImageURI(uri);

        //tag - label v에 태그를 준다.
        holder.rootView.setTag(position);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        //삼항 연산자
        return mDataset == null ? 0 : mDataset.size();
    }

    //본문 출력을 위해 함수 작성.
    public NewsData getNews(int position) {
        return mDataset != null ? mDataset.get(position) : null;
    }
    public void clear(){
        mDataset.clear();
        notifyDataSetChanged();
    }
    public void addAll(ArrayList<NewsData> list){
        mDataset.addAll(list);
        notifyDataSetChanged();
    }


}
