package com.sc.clgg.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sc.clgg.R;
import com.sc.clgg.activity.WebActivity;
import com.sc.clgg.bean.Message;
import com.sc.clgg.config.NetField;
import com.sc.clgg.tool.helper.LogHelper;
import com.sc.clgg.widget.ShapeTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyHolder> {
    private List<Message.Data.Row> listAll = new ArrayList<>();
    private boolean noMore;
    private View.OnClickListener listener;
    private Context mContext;
    private String webDetailTitle = "";

    public String getWebDetailTitle() {
        return webDetailTitle;
    }

    public void setWebDetailTitle(String webDetailTitle) {
        this.webDetailTitle = webDetailTitle;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        if (payloads.isEmpty()) {
            // payloads 为 空，说明是更新整个 ViewHolder
            onBindViewHolder(holder, holder.getAdapterPosition());
        } else {
            // payloads 不为空，这只更新需要更新的 View 即可。
            LogHelper.e("局部刷新");
            if ((int) payloads.get(0) == 1) {
                holder.tv_loadmore.setVisibility(View.GONE);
            } else {
                holder.tv_loadmore.setVisibility(View.VISIBLE);
                holder.tv_loadmore.setText(R.string.load_more);
            }
        }

    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        if (holder.getAdapterPosition() == listAll.size() - 1) {
            if (!noMore) {
                holder.tv_loadmore.setVisibility(View.VISIBLE);
                holder.tv_loadmore.setText(R.string.load_more);
                if (listener != null) {
                    holder.tv_loadmore.setOnClickListener(listener);
                }
            }
        } else {
            holder.tv_loadmore.setVisibility(View.GONE);
        }
        bindView(holder, holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return listAll == null ? 0 : listAll.size();
    }

    private void bindView(MyHolder holder, int position) {
        final Message.Data.Row b = listAll.get(holder.getAdapterPosition());

        holder.tv_time.setText(format(b.getCreated()));
        if (!TextUtils.isEmpty(b.getArticle())) {
            holder.tv_content.setText(b.getArticle() == null ? "" : b.getArticle());
        }

        holder.tv_title.setText(b.getTitle());

        if ("1".equals(b.getTop())) {
            holder.tv_top.setVisibility(View.VISIBLE);
        } else {
            holder.tv_top.setVisibility(View.GONE);
        }
        if ("1".equals(b.getStress())) {
            holder.tv_hot.setVisibility(View.VISIBLE);
        } else {
            holder.tv_hot.setVisibility(View.GONE);
        }

        setPlate(holder.tv_type, b.getPlate());

        holder.tv_detail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, WebActivity.class)
                        .putExtra("name", getWebDetailTitle())
                        .putExtra("url", NetField.SITE + "preview/" + b.getId()));
            }
        });

    }


    public void refreshLast(int flag) {
        if (listAll.size() > 0) {
            notifyItemChanged(listAll.size() - 1, flag);
        }
    }

    public void clear() {
        listAll.clear();
        notifyDataSetChanged();
    }

    public void refresh(List<Message.Data.Row> newList, boolean noMore) {
        this.noMore = noMore;
        if (newList != null && newList.size() > 0) {
            notifyItemChanged(listAll.size() - 1, 1);

            listAll.addAll(newList);
            notifyItemRangeInserted(listAll.size(), newList.size());
        }
    }

    public void setLoadMoreListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    private void setPlate(TextView tv, String plate) {
        switch (plate) {
            case "2":
                tv.setText("平台动态");
                tv.setBackgroundColor(Color.parseColor("#767994"));
                break;
            case "3":
                tv.setText("行业新闻");
                tv.setBackgroundColor(Color.parseColor("#ffce6c"));
                break;
            case "4":
                tv.setText("保养技巧");
                tv.setBackgroundColor(Color.parseColor("#4ed7fa"));
                break;
            case "5":
                tv.setText("行业知识");
                tv.setBackgroundColor(Color.parseColor("#34cb9f"));
                break;
            case "6":
                tv.setText("行业政策");
                tv.setBackgroundColor(Color.parseColor("#8d54c6"));
                break;
            case "7":
                tv.setText("陕汽新闻");
                tv.setBackgroundColor(Color.parseColor("#26c410"));
                break;
            case "12":
                tv.setText("版本发布");
                tv.setBackgroundColor(Color.parseColor("#8d54c6"));
                break;
            case "13":
                tv.setText("新品发布");
                tv.setBackgroundColor(Color.parseColor("#327ef1"));
                break;
            case "14":
                tv.setText("推广活动");
                tv.setBackgroundColor(Color.parseColor("#767994"));
                break;
            default:
                tv.setText("德银新闻");
                tv.setBackgroundColor(Color.parseColor("#26c410"));
                break;
        }
    }

    private String format(Long s) {
        String currentDay = new SimpleDateFormat("dd", Locale.getDefault()).format(System.currentTimeMillis());
        String oldDay = new SimpleDateFormat("dd", Locale.getDefault()).format(s);

        if (Integer.parseInt(currentDay) - Integer.parseInt(oldDay) == 0) {
            return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(s);
        } else if (Integer.parseInt(currentDay) - Integer.parseInt(oldDay) == 1) {
            return "昨天" + new SimpleDateFormat("HH:mm", Locale.getDefault()).format(s);
        } else {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(s);
        }
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ShapeTextView tv_type;
        private TextView tv_time, tv_hot, tv_top, tv_title, tv_content, tv_detail, tv_loadmore;
        private View v_divider;

        public MyHolder(View itemView) {
            super(itemView);
            v_divider = itemView.findViewById(R.id.v_divider);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_hot = itemView.findViewById(R.id.tv_hot);
            tv_top = itemView.findViewById(R.id.tv_top);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_content = itemView.findViewById(R.id.tv_content);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_detail = itemView.findViewById(R.id.tv_detail);
            tv_loadmore = itemView.findViewById(R.id.tv_loadmore);
        }
    }
}
