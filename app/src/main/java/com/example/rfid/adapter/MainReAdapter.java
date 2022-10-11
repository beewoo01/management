package com.example.rfid.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rfid.data.ItemOfProducts;
import com.example.rfid.databinding.ItemMainreBinding;

import java.util.ArrayList;
import java.util.List;


public class MainReAdapter extends RecyclerView.Adapter<MainReAdapter.ViewHolder> {

    private ItemMainreBinding binding;
    private Context context;
    private OnItemClickListener itemClickListener;
    private List<ItemOfProducts> list;

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.itemClickListener = listener;
    }

    public MainReAdapter(Context context, List<ItemOfProducts> items) {
        this.context = context;
        this.list = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMainreBinding binding = ItemMainreBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        binding.numTxv.setText(list.get(position).getRemainingProduct()+"/"+list.get(position).getAllCount());
        binding.obnameTxv.setText(list.get(position).getItem_name());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.objectImv.setClipToOutline(true);
        }
        Glide.with(binding.objectImv.getContext()).load("http://raon-soft.com/imagefile/material_management/" + list.get(position).getItem_image()).into(binding.objectImv);
//        binding.objectImv.setImageResource(R.drawable.ic_item1);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull ItemMainreBinding itembinding) {
            super(itembinding.getRoot());
            binding = itembinding;

            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION){
                        if(itemClickListener != null){
                            itemClickListener.onItemClick(view, pos);
                        }
                    }
                }
            });
        }

    }
    public void update(List<ItemOfProducts> item){
        final BookmarkDiffCallback bookCallback = new BookmarkDiffCallback(this.list, item);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(bookCallback);

        this.list.clear();
        this.list.addAll(item);
        diffResult.dispatchUpdatesTo(this);
    }

    class BookmarkDiffCallback extends DiffUtil.Callback {
        private final List<ItemOfProducts> mOlditem;
        private final List<ItemOfProducts> mNewitem;

        public BookmarkDiffCallback(List<ItemOfProducts> mOlditem, List<ItemOfProducts> mNewitem) {
            this.mOlditem = mOlditem;
            this.mNewitem = mNewitem;
        }

        @Override
        public int getOldListSize() {
            return mOlditem.size();
        }

        @Override
        public int getNewListSize() {
            return mNewitem.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return mOlditem.get(oldItemPosition).getItem_idx() == mNewitem.get(newItemPosition).getItem_idx();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            final ItemOfProducts oldBookmark = mOlditem.get(oldItemPosition);
            final ItemOfProducts newBookmark = mNewitem.get(newItemPosition);
            return oldBookmark.getItem_name().equals(newBookmark.getItem_name());
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }
    }

}
