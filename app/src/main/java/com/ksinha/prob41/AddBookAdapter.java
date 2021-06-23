package com.ksinha.prob41;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AddBookAdapter extends RecyclerView.Adapter<AddBookAdapter.MyViewHolder> {
    private List<Contact> contactList;

    // Required Constructor
    public AddBookAdapter(List<Contact> contactList){
        this.contactList = contactList;
    }

    // Inner Class MyViewHolder
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView tvid,tvname,tvphone,tvemail;

        public MyViewHolder(View view) {
            super(view);
            tvid = view.findViewById(R.id.tv_id);
            tvname = view.findViewById(R.id.tv_name);
            tvphone = view.findViewById(R.id.tv_phone);
            tvemail = view.findViewById(R.id.tv_email);
        }
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public AddBookAdapter.MyViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull AddBookAdapter.MyViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.tvid.setText(String.valueOf(contact.getId()));
        holder.tvname.setText(contact.getName());
        holder.tvphone.setText(contact.getPhone());
        holder.tvemail.setText(contact.getEmail());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}
