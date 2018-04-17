package com.sc.clgg.activity.presenter;

import com.sc.clgg.R;
import com.sc.clgg.bean.TypeBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：lvke
 * CreateDate：2017/9/26 20:35
 */

public class TallyPresenter {

    public static List<TypeBean> getExpendTypeList() {
        List<TypeBean> list = new ArrayList<>();
        list.add(new TypeBean(R.mipmap.jiayou_unselected, R.mipmap.jiayou_selected, "加油", false, "01"));
        list.add(new TypeBean(R.mipmap.xiche_unselected, R.mipmap.xiche_selected, "洗车", false, "02"));
        list.add(new TypeBean(R.mipmap.tingche_unselected, R.mipmap.tingche_selected, "停车", false, "03"));
        list.add(new TypeBean(R.mipmap.guolu_unselected, R.mipmap.guolu_selected, "过路", false, "04"));
        list.add(new TypeBean(R.mipmap.baoyang_unselected, R.mipmap.baoyang_selected, "保养", false, "05"));
        list.add(new TypeBean(R.mipmap.weixiu_unselected, R.mipmap.weixiu_selected, "维修", false, "06"));
        list.add(new TypeBean(R.mipmap.weizhang_unselected, R.mipmap.weizhang_selected, "违章", false, "07"));
        list.add(new TypeBean(R.mipmap.yanche_unselected, R.mipmap.yanche_selected, "验车", false, "08"));
        list.add(new TypeBean(R.mipmap.peijian_unselected, R.mipmap.peijian_selected, "配件", false, "09"));
        list.add(new TypeBean(R.mipmap.chedai_unselected, R.mipmap.chedai_selected, "车贷", false, "10"));
        list.add(new TypeBean(R.mipmap.chexian_unselected, R.mipmap.chexian_selected, "车险", false, "11"));
        list.add(new TypeBean(R.mipmap.qita_unselected, R.mipmap.qita_selected, "其他", false, "12"));
        return list;
    }

    public static List<TypeBean> getIncomeTypeList() {
        List<TypeBean> list = new ArrayList<>();
        list.add(new TypeBean(R.mipmap.gongzi_unselected, R.mipmap.gongzi_selected, "工资", false, "13"));
        list.add(new TypeBean(R.mipmap.jianzhi_unselected, R.mipmap.jianzhi_selected, "兼职", false, "14"));
        list.add(new TypeBean(R.mipmap.licai_unselected, R.mipmap.licai_selected, "理财", false, "15"));
        list.add(new TypeBean(R.mipmap.qitashouru_unselected, R.mipmap.qitashouru_selected, "其他", false, "16"));
        return list;
    }

}
