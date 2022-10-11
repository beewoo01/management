package com.example.rfid.ui.activity;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rfid.R;
import com.example.rfid.adapter.DetailReAdapater;
import com.example.rfid.data.CurrentProductContentDTO;
import com.example.rfid.databinding.ActivityDetailPageBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPageActivity extends BaseActivity {

    ActivityDetailPageBinding binding;
    private List<CurrentProductContentDTO> list;
    private int itemidx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.wtf("DetailPageActivity","onCreate");
        setContentView(R.layout.activity_detail_page);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail_page);

        binding.backBtn.setOnClickListener(view -> finish());

        binding.detailRe.addItemDecoration(new MainActivity.RecyclerViewDecoration(40));

        binding.addBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AddProductActivity.class);
            Log.wtf("onClick", String.valueOf(itemidx));
            intent.putExtra("itemidx", itemidx);
            startActivity(intent);
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getProduct();
    }


    private void getProduct(){
        Intent intent = getIntent();
        binding.objectNameTxv.setText(intent.getStringExtra("title"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.objectDetailImv.setClipToOutline(true);
        }
        Glide.with(binding.objectDetailImv.getContext()).load("http://raon-soft.com/imagefile/material_management/" + intent.getStringExtra("item_img")).into(binding.objectDetailImv);
        itemidx = intent.getIntExtra("item_idx",0);
        apiService.getProductOfItemIdx(itemidx).enqueue(new Callback<List<CurrentProductContentDTO>>() {
            @Override
            public void onResponse(@NonNull Call<List<CurrentProductContentDTO>> call, @NonNull Response<List<CurrentProductContentDTO>> response) {
                if(response.isSuccessful()){
                    list = response.body();
                    DetailReAdapater adapter = new DetailReAdapater(DetailPageActivity.this, list);
                    adapter.setOnItemClickListener(new DetailReAdapater.OnItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position) {
                            if(list.get(position).getProduct_status() != 2){
                                Intent intent = new Intent(getApplicationContext(), RetouchMaterialActivity.class);
                                intent.putExtra("title", list.get(position).getProduct_name());
                                intent.putExtra("product_idx", list.get(position).getProduct_idx());
                                intent.putExtra("product_status", list.get(position).getProduct_status());
                                intent.putExtra("special", list.get(position).getSpecial());
                                intent.putExtra("taginfo", list.get(position).getProduct_taginfo());
                                startActivity(intent);
                            }else{
                                Toast.makeText(mContext, "폐기 상태의 자재는 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    binding.detailRe.setAdapter(adapter);
                    binding.detailRe.setLayoutManager(new LinearLayoutManager(DetailPageActivity.this, RecyclerView.VERTICAL, false));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CurrentProductContentDTO>> call, @NonNull Throwable t) {

            }
        });
    }
}