package com.example.demofornetworkdevices;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.druk.rx2dnssd.BonjourService;

import java.util.ArrayList;


public class DeviceDataAdapter extends RecyclerView.Adapter<DeviceDataAdapter.DeviceDataViewHolder> {
    public static ArrayList<BonjourService> serviceArrayList;
    private final Context context;

    public DeviceDataAdapter(ArrayList<BonjourService> services, Context context) {
        DeviceDataAdapter.serviceArrayList = services;
        this.context = context;
    }


    @NonNull
    @Override
    public DeviceDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceDataViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return serviceArrayList.size();
    }


    public static class DeviceDataViewHolder extends RecyclerView.ViewHolder {
        public DeviceDataViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateDeviceData(ArrayList<BonjourService> songsList) {

        notifyDataSetChanged();
    }
}