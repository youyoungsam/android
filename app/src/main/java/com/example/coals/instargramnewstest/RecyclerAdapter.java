package com.example.coals.instargramnewstest;


import android.app.AlarmManager;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CustomViewHolder> {
    private ArrayList<ItemData> arrayList;
    private Context context;

    public void setArrayList(ArrayList<ItemData> arrayList) {
        this.arrayList = arrayList;
    }

    public RecyclerAdapter(ArrayList<ItemData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //실제리스트뷰가 어댑터에 연결된다음에 이쪽에서 뷰홀더를 최초로만들어낸다.
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {

        holder.tvTime.setText(arrayList.get(position).getTvTime());
        holder.txtWeek.setText(arrayList.get(position).getTvWeek());
        holder.tvAmpm.setText(arrayList.get(position).getTvAmpm());
        holder.tvItemPri.setText(String.valueOf(arrayList.get(position).getTvItemPri()));

        //스위치 버튼 세팅
        int switchOn = arrayList.get(position).getMission();
        if (switchOn == 0) {
            holder.onOff.setChecked(true);
        } else {
            holder.onOff.setChecked(false);
        }

        if (holder.onOff.isChecked() == false) {
            Fragment2.alarmManager.cancel(Fragment2.getPendingIntent(arrayList.get(position).getTvItemPri(),
                    Fragment2.alarmIntent));
            Fragment2.getPendingIntent(arrayList.get(position).getTvItemPri()
                    , Fragment2.alarmIntent).cancel();
        }

        switch (arrayList.get(position).getMusic()) {
            case R.raw.teemo:
                holder.tvMusic.setText("기상음악 : 티모믹스");
                break;
            case R.raw.yayou:
                holder.tvMusic.setText("기상음악 : 야유로봇");
                break;
            case R.raw.army:
                holder.tvMusic.setText("기상음악 : 군대기상");
                break;

        }


        holder.onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int misson;
                if (holder.onOff.isChecked()) {
                    misson = 0;
                } else {
                    misson = 1;
                }
                boolean[] week = new boolean[7];
                ContentValues contentValues = new ContentValues();
                contentValues.put(AlarmItemMemo.MemoEntry.COLUMN_NAME_MISSION, misson);

                //db수정등록
                SQLiteDatabase db = MyAlarmDBHelper.getsInstance(context).getWritableDatabase();
                long newRowId = db.update(AlarmItemMemo.MemoEntry.TABLE_NAME, contentValues, "_id=" +
                        arrayList.get(position).getTvItemPri(), null);
                if (newRowId == -1) {
                    Toast.makeText(context, "수정에문제가발생하였습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "수정하였습니다.", Toast.LENGTH_SHORT).show();
                }
                db.close();

                if (arrayList != null) {
                    MyAlarmDBHelper dpHelper = MyAlarmDBHelper.getsInstance(context.getApplicationContext());
                    Cursor c = dpHelper.getReadableDatabase()
                            .rawQuery("SELECT * FROM alarm WHERE _ID='" + arrayList.get(position)
                                    .getTvItemPri() + "';", null);
                    int[] dbb = new int[7];
                    int dbtime, dbmin;
                    if (c != null && c.getCount() != 0) {
                        c.moveToFirst();
                        do {
                            dbtime = c.getInt(c.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_TIME));
                            dbmin = c.getInt(c.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_MINUTE));
                            dbb[0] = c.getInt(c.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_SUNDAY));
                            dbb[1] = c.getInt(c.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_MONDAY));
                            dbb[2] = c.getInt(c.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_TUESDAY));
                            dbb[3] = c.getInt(c.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_WEDNESDAY));
                            dbb[4] = c.getInt(c.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_THURSDAY));
                            dbb[5] = c.getInt(c.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_FRIDAY));
                            dbb[6] = c.getInt(c.getColumnIndexOrThrow(AlarmItemMemo.MemoEntry.COLUMN_NAME_SATURDAY));
                        } while (c.moveToNext());
                        c.close();
                        dpHelper.close();

                        //캘린더 시간 세팅
                        Fragment2.calendar=Calendar.getInstance();
                        Fragment2.calendar.set(Calendar.HOUR_OF_DAY, dbtime);
                        Fragment2.calendar.set(Calendar.MINUTE, dbmin);
                        Fragment2.calendar.set(Calendar.SECOND, 0);
                        // 시간 가져옴
                        long timeCheck = (Fragment2.calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) ?
                                Fragment2.calendar.getTimeInMillis() + AlarmManager.INTERVAL_HOUR :
                                Fragment2.calendar.getTimeInMillis();

                        for (int i = 0; i < week.length; i++) {
                            if (dbb[i] == 0) {
                                week[i] = true;
                            } else {
                                week[i] = false;
                            }

                        }


                        Fragment2.alarmIntent.putExtra("music", arrayList.get(position).getMusic());
                        Fragment2.alarmIntent.putExtra("state", "alarm on");
                        Fragment2.alarmIntent.putExtra("week", week);
                        Fragment2.alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                timeCheck, AlarmManager.INTERVAL_DAY, Fragment2.getPendingIntent(
                                        arrayList.get(position).getTvItemPri(), Fragment2.alarmIntent));
                    }


                    arrayList.removeAll(arrayList);
                }
                Fragment1.getMyDB();
//                        ((RecyclerAdapter) adapter).setArrayList(arrayList);
                Fragment1.adapter.notifyDataSetChanged();


            }
        });

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }


    public interface OnItemClickListener {

        void onItemClick(View v, int position);

    }

    public interface OnItemLongClickListener {

        void onItemLongClick(View v, int position);

    }

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null;
    private OnItemLongClickListener mLListener = null;


    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    // OnItemClickLongListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mLListener = listener;
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        TextView txtWeek;
        TextView tvAmpm;
        TextView tvItemPri;
        TextView tvMusic;
        Switch onOff;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvTime = itemView.findViewById(R.id.txtTime);
            this.txtWeek = itemView.findViewById(R.id.txtWeek);
            this.tvAmpm = itemView.findViewById(R.id.tvAmpm);
            this.tvItemPri = itemView.findViewById(R.id.tvItemPri);
            this.onOff = itemView.findViewById(R.id.onOff);
            this.tvMusic = itemView.findViewById(R.id.txtMusic);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        //리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        //리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mLListener.onItemLongClick(v, pos);
                        }
                    }
                    return false;
                }

            });

        }
    }
}
