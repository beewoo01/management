package com.example.rfid.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rfid.R;
import com.example.rfid.data.CurrentProductContentDTO;
import com.example.rfid.databinding.ItemDetailReBinding;

import java.util.List;

public class DetailReAdapater extends RecyclerView.Adapter<DetailReAdapater.ViewHolder>{

    ItemDetailReBinding binding;
    private final Context context;
    private OnItemClickListener itemClickListener;
    private final List<CurrentProductContentDTO> list;

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.itemClickListener = listener;
    }

    public DetailReAdapater(Context context, List<CurrentProductContentDTO> items) {
        this.context = context;
        this.list = items;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDetailReBinding binding = ItemDetailReBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if(list.get(position).getProduct_status() == 0){
            binding.lineView.setBackgroundColor(Color.parseColor("#61880C"));
            binding.statusTxv.setText("미불출");
            binding.statusTxv.setBackgroundResource(R.drawable.bg_rounded_06);
            binding.statusTxv.setTextColor(Color.parseColor("#61880C"));
        }
        else if(list.get(position).getProduct_status() == 1){
            binding.lineView.setBackgroundColor(Color.parseColor("#9F7623"));
            binding.statusTxv.setText("불출");
            binding.statusTxv.setBackgroundResource(R.drawable.bg_rounded_05);
            binding.statusTxv.setTextColor(Color.parseColor("#9F7623"));
        }else{
            binding.lineView.setBackgroundColor(Color.parseColor("#980000"));
            binding.statusTxv.setText("폐기");
            binding.statusTxv.setBackgroundResource(R.drawable.bg_rounded_07);
            binding.statusTxv.setTextColor(Color.parseColor("#980000"));
        }

        binding.objectNameTxv.setText(list.get(position).getProduct_name());

        binding.issueTxv.setText(list.get(position).getSpecial());
        binding.dateTxv.setText(list.get(position).getProvisionInfo_createtime());

        if (list.get(position).getProvisionInfo_platoon() == null ||
                list.get(position).getProvisionInfo_platoon().equals("null")
        ) {
            binding.teamPersonTxv.setVisibility(View.INVISIBLE);

        } else {
            binding.teamPersonTxv.setText(list.get(position).getProvisionInfo_platoon()+" "+list.get(position).getProvisionInfo_user_name());
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull ItemDetailReBinding itembinding) {
            super(itembinding.getRoot());
            binding = itembinding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAbsoluteAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(itemClickListener != null){
                            itemClickListener.onItemClick(view, pos);
                        }
                    }
                }
            });

        }
    }
}
