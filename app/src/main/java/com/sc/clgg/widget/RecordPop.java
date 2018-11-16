package com.sc.clgg.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.sc.clgg.R;
import com.sc.clgg.adapter.RecordAdapter;
import com.sc.clgg.bean.Record;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.DrawableRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author：lvke
 * @date：2018/5/11 11:28
 */
public class RecordPop {

    public String[] incomeTypes = new String[]{"运费", "工资", "外快", "其他"};

    public int[] incomeTypeId = {22, 23, 24, 25};

    public @DrawableRes int[] incomeTypeImages = {
            R.drawable.account_ico_feight, R.drawable.account_ico_salary,
            R.drawable.account_ico_extra, R.drawable.account_ico_else};

    public @DrawableRes int[] incomeTypeImage_press = {
            R.drawable.account_ico_feight_press, R.drawable.account_ico_salary_press,
            R.drawable.account_ico_extra_press, R.drawable.account_ico_else_press};

    public String[] spendingTypes = new String[]{
            "高速费", "加油", "餐饮", "食宿",
            "进场费", "停车费", "修车洗车", "买零配件",
            "出车费", "司机工资", "信息费", "交罚款",
            "上保险", "审车", "购车", "其他"};

    public @DrawableRes int[] spendingTypeImages = {
            R.drawable.account_ico_highway, R.drawable.account_ico_addoil, R.drawable.account_ico_meal, R.drawable.account_ico_stay,
            R.drawable.account_ico_enterfee, R.drawable.account_ico_park, R.drawable.account_ico_require, R.drawable.account_ico_part,
            R.drawable.account_ico_out, R.drawable.account_ico_driverfee, R.drawable.account_ico_infofee, R.drawable.account_ico_fine,
            R.drawable.account_ico_insure, R.drawable.account_ico_inspection, R.drawable.account_ico_buycar, R.drawable.account_ico_else};

    public @DrawableRes int[] spendingTypeImage_press = {
            R.drawable.account_ico_highway_press, R.drawable.account_ico_addoil_press, R.drawable.account_ico_meal_press, R.drawable.account_ico_stay_press,
            R.drawable.account_ico_enterfee_press, R.drawable.account_ico_park_press, R.drawable.account_ico_require_press, R.drawable.account_ico_part_press,
            R.drawable.account_ico_out_press, R.drawable.account_ico_driverfee_press, R.drawable.account_ico_infofee_press, R.drawable.account_ico_fine_press,
            R.drawable.account_ico_insure_press, R.drawable.account_ico_inspection_press, R.drawable.account_ico_buycar_press, R.drawable.account_ico_else_press};

    public int[] spendingTypeId = {27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42};

    private PopupWindow mPopupWindow = new PopupWindow();
    private RecordAdapter mRecordAdapter;
    private List<Record> incomeList = new ArrayList<>();
    private List<Record> spendingList = new ArrayList<>();

    public List<Record> getIncomeList(){
        return incomeList;
    }

    public List<Record> getSpendingList(){
        return spendingList;
    }

    public RecordAdapter getAdapter(){
        return mRecordAdapter;
    }


    public PopupWindow initIncome(Context context) {

        for (int i = 0; i < incomeTypes.length; i++) {
            Record r = new Record();
            r.setChecked(false);
            r.setName(incomeTypes[i]);
            r.setNomalImg(incomeTypeImages[i]);
            r.setPressImg(incomeTypeImage_press[i]);
            r.setTypeId(incomeTypeId[i]);
            incomeList.add(r);
        }

        View view = View.inflate(context, R.layout.pop_record, null);
        RecyclerView mRecyclerView = view.findViewById(R.id.rv_area);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 4, RecyclerView.VERTICAL, false));

        mRecordAdapter = new RecordAdapter(incomeList);

        mRecyclerView.setAdapter(mRecordAdapter);
        mRecyclerView.setHasFixedSize(true);

        mPopupWindow.setContentView(view);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        return mPopupWindow;
    }


    public PopupWindow initSpending(Context context) {

        for (int i = 0; i < spendingTypes.length; i++) {
            Record r = new Record();
            r.setChecked(false);
            r.setName(spendingTypes[i]);
            r.setNomalImg(spendingTypeImages[i]);
            r.setPressImg(spendingTypeImage_press[i]);
            r.setTypeId(spendingTypeId[i]);
            spendingList.add(r);
        }

        View view = View.inflate(context, R.layout.pop_record, null);
        RecyclerView mRecyclerView = view.findViewById(R.id.rv_area);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 4, RecyclerView.VERTICAL, false));

        mRecordAdapter = new RecordAdapter(spendingList);

        mRecyclerView.setAdapter(mRecordAdapter);
        mRecyclerView.setHasFixedSize(true);

        mPopupWindow.setContentView(view);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        return mPopupWindow;
    }

    public void setItemClickListener(RecordAdapter.OnItemClickListener itemClickListener) {
        if (mRecordAdapter != null) {
            mRecordAdapter.setItemClickListener(itemClickListener);
        }
    }



    public void showAsDropDown(View anchor, int xoff, int yoff) {
        mPopupWindow.showAsDropDown(anchor, xoff, yoff);
    }

    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }


}
