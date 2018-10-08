package com.sc.clgg.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.activity.contact.CallbackListener;
import com.sc.clgg.activity.vehiclemanager.gps.PositioningDetailActivity;
import com.sc.clgg.bean.Check;
import com.sc.clgg.bean.Location;
import com.sc.clgg.bean.Vehicle;
import com.sc.clgg.dialog.AlertDialogHelper;
import com.sc.clgg.http.retrofit.RetrofitHelper;
import com.sc.clgg.util.Tools;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author lvke
 */
public class MyVehicleAdapter extends RecyclerView.Adapter<MyVehicleAdapter.MyHolder> {
    private List<Vehicle.Bean> dataList = new ArrayList<>();
    private ArrayList<Location.Data> mCarList = new ArrayList<>();
    private Context mContext;
    private CallbackListener mCallbackListener;

    public MyVehicleAdapter() {

    }

    public void refresh(List<Vehicle.Bean> dataList) {
        mCarList.clear();
        for (Vehicle.Bean bean : dataList) {
            Location.Data data = new Location.Data();
            data.setCarno(bean.getCarNumber());
            data.setVin(bean.getVinNumber());
            mCarList.add(data);
        }
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_vehicle, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Vehicle.Bean bean = dataList.get(holder.getAdapterPosition());
        holder.tv_car_no.setText("车牌号: " + bean.getCarNumber());
        holder.tv_car_vin.setText("VIN码: " + bean.getVinNumber());

        holder.tv_car_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PositioningDetailActivity.class);
                intent.putExtra("carno", bean.getCarNumber());
                intent.putExtra("vin", bean.getVinNumber());
                intent.putParcelableArrayListExtra("array", mCarList);
                mContext.startActivity(intent);
            }
        });


        holder.tv_car_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialogHelper().show((Activity) mContext, "确定删除  " + bean.getCarNumber() + "?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        new RetrofitHelper().vehicleDelete(bean.getVinNumber()).enqueue(new Callback<Check>() {
                            @Override
                            public void onResponse(Call<Check> call, Response<Check> response) {
                                if (response.body().getSuccess()) {
                                    Tools.Toast("删除车辆成功");
                                    dataList.remove(holder.getAdapterPosition());
                                    notifyItemRemoved(holder.getAdapterPosition());

                                } else {
                                    Tools.Toast("删除车辆失败");
                                }
                            }

                            @Override
                            public void onFailure(Call<Check> call, Throwable t) {
                                Tools.Toast("删除车辆失败");
                            }
                        });
                    }
                });
            }
        });
    }

    public void setCallbackListener(CallbackListener callbackListener) {
        mCallbackListener = callbackListener;
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_car_no, tv_car_vin, tv_car_position, tv_car_delete;

        public MyHolder(View itemView) {
            super(itemView);
            tv_car_no = itemView.findViewById(R.id.tv_car_no);
            tv_car_vin = itemView.findViewById(R.id.tv_car_vin);
            tv_car_position = itemView.findViewById(R.id.tv_car_position);
            tv_car_delete = itemView.findViewById(R.id.tv_car_delete);
        }

    }
}
