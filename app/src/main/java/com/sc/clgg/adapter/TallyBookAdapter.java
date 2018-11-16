package com.sc.clgg.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.sc.clgg.R;
import com.sc.clgg.bean.TallyBook;
import com.sc.clgg.tool.helper.DateHelper;
import com.sc.clgg.tool.helper.DecimalFormatHelper;
import com.sc.clgg.tool.helper.LogHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author：lvke
 * @date：2018/5/25 10:10
 */
public class TallyBookAdapter extends RecyclerView.Adapter<TallyBookAdapter.MyHolder> {
    private Map<Integer, String> map = new HashMap<>();
    private List<TallyBook.Info> dataList = new ArrayList<>();
    private onSwipeListener mOnSwipeListener;

    public TallyBookAdapter() {
        map.put(22, "运费收入");
        map.put(23, "工资收入");
        map.put(24, "外快收入");
        map.put(25, "其他收入");

        map.put(27, "高速费支出");
        map.put(28, "加油支出");
        map.put(29, "餐饮支出");
        map.put(30, "住宿支出");
        map.put(31, "进场费支出");
        map.put(32, "停车费支出");
        map.put(33, "修车洗车支出");
        map.put(34, "买零配件支出");
        map.put(35, "出车费支出");
        map.put(36, "司机工资支出");
        map.put(37, "信息费支出");
        map.put(38, "交罚款支出");
        map.put(39, "上保险支出");
        map.put(40, "审车支出");
        map.put(41, "购车支出");
        map.put(42, "其他支出");

    }

    public void refresh(List<TallyBook.Info> dataList) {
        LogHelper.e("dataList = " + dataList.size());
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tally_book, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        TallyBook.Info bean = dataList.get(holder.getAdapterPosition());

        if (bean.getShow()) {
            holder.tv_date.setText(DateHelper.format(bean.getTimeStamp(), "yyyy-MM-dd"));
            holder.tv_date.setVisibility(View.VISIBLE);
        } else {
            holder.tv_date.setVisibility(View.GONE);
            holder.tv_date.setVisibility(View.GONE);
        }

        if (bean.getRecordType() == 0) {
            holder.tv_type.setText("收");
            holder.tv_type.setBackgroundResource(R.drawable.account_bg_income);
        } else {
            holder.tv_type.setText("支");
            holder.tv_type.setBackgroundResource(R.drawable.account_bg_expand);
        }

        holder.tv_bill.setText(map.get(bean.getCostType()) + DecimalFormatHelper.formatTwo(bean.getAmount()) + "元");

        if (!TextUtils.isEmpty(bean.getRemark())) {
            holder.tv_describe.setText(bean.getRemark());
            holder.tv_describe.setVisibility(View.VISIBLE);
        } else {
            holder.tv_describe.setVisibility(View.GONE);
        }


        holder.mSwipeMenuLayout.setIos(false).setLeftSwipe(true);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    mOnSwipeListener.onDel(bean.getId(),holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }


    public interface onSwipeListener {
        void onDel(int id,int pos);

    }

    class MyHolder extends RecyclerView.ViewHolder {
        SwipeMenuLayout mSwipeMenuLayout;
        Button btnDelete;
        Button btnUnRead;
        Button btnTop;
        private TextView tv_date, tv_type, tv_bill, tv_describe;

        public MyHolder(View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_bill = itemView.findViewById(R.id.tv_bill);
            tv_describe = itemView.findViewById(R.id.tv_describe);

            mSwipeMenuLayout = itemView.findViewById(R.id.swipeMenuLayout);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUnRead = itemView.findViewById(R.id.btnUnRead);
            btnTop = itemView.findViewById(R.id.btnTop);
        }
    }

}
