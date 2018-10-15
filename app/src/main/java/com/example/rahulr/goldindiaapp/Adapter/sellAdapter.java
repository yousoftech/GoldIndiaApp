package com.example.rahulr.goldindiaapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.rahulr.goldindiaapp.Pojo.pojo;
import com.example.rahulr.goldindiaapp.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class sellAdapter extends RecyclerView.Adapter<sellAdapter.RecyclerViewHolder> {

    ArrayList<pojo> event;
    Context context;
    LayoutInflater inflater;

    public sellAdapter(Context context,ArrayList<pojo> event)
    {
        this.context=context;
        this.event=event;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.sellrecord_row,parent,false);
        RecyclerViewHolder holder=new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {

        String name=event.get(position).getName();
        String qty=event.get(position).getQty();
        holder.name.setText(name);
        holder.qty.setText(qty);
    }

    @Override
    public int getItemCount() {
        return event.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView name,qty;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name);
            qty=(TextView)itemView.findViewById(R.id.units);
        }
    }
}
