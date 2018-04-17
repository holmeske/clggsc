package com.sc.clgg.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.activity.contact.StringContact;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：lvke
 * @date：2017/11/24 11:47
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolder> {
    private List<String> mStringList = new ArrayList<>();
    private StringContact mStringContact;
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mStringContact != null) {
                mStringContact.callback((String) v.getTag());
            }
        }
    };

    public void refresh(List<String> newData) {
        this.mStringList = newData;
        notifyDataSetChanged();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_input_associate, parent, false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.mTextView.setText(mStringList.get(position));
        holder.mTextView.setTag(mStringList.get(position));

        holder.mTextView.setOnClickListener(mClickListener);
    }

    @Override
    public int getItemCount() {
        return mStringList == null ? 0 : mStringList.size();
    }

    public void setStringContact(StringContact stringContact) {
        mStringContact = stringContact;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView mTextView;

        public MyHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_text);
        }
    }
}
