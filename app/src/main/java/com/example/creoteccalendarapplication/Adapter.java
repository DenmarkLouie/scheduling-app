package com.example.creoteccalendarapplication;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {

    Context context;
    ArrayList<user> userArrayList;



    public Adapter(Context context, ArrayList<user> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);

        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.MyViewHolder holder, int position) {

        user user = userArrayList.get(position);

        holder.ClientName.setText(user.ClientName);
        holder.ClientSchool.setText(user.ClientSchool);
        holder.ClientNumber.setText(user.ClientNumber);
        holder.ProductType.setText(user.ProductType);
        holder.ScheduleTime.setText(user.ScheduleTime);
        holder.ZoomLink.setText(user.ZoomLink);
        holder.isDone.setText(user.IsDone);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = (new Intent(view.getContext(), ScheduleDetails.class));
                intent.putExtra("ID",user.ID);
                intent.putExtra("ClientName",user.ClientName);
                intent.putExtra("ClientSchool",user.ClientSchool);
                intent.putExtra("ClientNumber",user.ClientNumber);
                intent.putExtra("ProductType",user.ProductType);
                intent.putExtra("ScheduleTime",user.ScheduleTime);
                intent.putExtra("ZoomLink",user.ZoomLink);
                intent.putExtra("isDone",user.IsDone);

                view.getContext().startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView ClientName, ClientSchool,ClientNumber,ProductType,ScheduleTime,ZoomLink,isDone;

        CardView layout;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ClientName = itemView.findViewById(R.id.clientName);
            ClientSchool = itemView.findViewById(R.id.clientSchool);
            ClientNumber = itemView.findViewById(R.id.clientNumber);
            ProductType = itemView.findViewById(R.id.prodType);
            ScheduleTime = itemView.findViewById(R.id.schedTime);
            ZoomLink = itemView.findViewById(R.id.ZoomLink);
            isDone = itemView.findViewById(R.id.SchedStatus);
            layout = itemView.findViewById(R.id.cardviewLayout);


        }
    }


}
