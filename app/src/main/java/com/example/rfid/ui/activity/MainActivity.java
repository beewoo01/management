package com.example.rfid.ui.activity;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;

import com.example.rfid.R;
import com.example.rfid.adapter.MainReAdapter;
import com.example.rfid.data.ItemOfProducts;
import com.example.rfid.data.JoinAccountDTO;
import com.example.rfid.data.UserInfo;
import com.example.rfid.databinding.ActivityMainBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;
    private List<ItemOfProducts> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.mainRe.addItemDecoration(new RecyclerViewDecoration(30));
    }

    @Override
    protected void onResume() {
        super.onResume();
        InitSet();
        getItem();
    }

    //품목조회
    private void getItem(){
        apiService.getProductCountOfItems(UserInfo.getInstance().accountidx).enqueue(new Callback<List<ItemOfProducts>>() {
            @Override
            public void onResponse(@NonNull Call<List<ItemOfProducts>> call, @NonNull Response<List<ItemOfProducts>> response) {
                if(response.isSuccessful()){
                    list = response.body();
                    MainReAdapter adapter = new MainReAdapter(MainActivity.this, list);
//                    adapter.update(list);

                    adapter.setOnItemClickListener((v, position) -> {

                        Intent intent = new Intent(getApplicationContext(), DetailPageActivity.class);
                        intent.putExtra("title", list.get(position).getItem_name());
                        intent.putExtra("item_idx", list.get(position).getItem_idx());
                        intent.putExtra("item_img", list.get(position).getItem_image());
                        startActivity(intent);
                    });

                    binding.mainRe.setAdapter(adapter);
//                    adapter.update(list);
                    binding.mainRe.setLayoutManager(new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false));
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ItemOfProducts>> call, @NonNull Throwable t) {

            }
        });
    }

    //기본설정
    private void InitSet(){
        apiService.getArmyUnitName(UserInfo.getInstance().accountidx).enqueue(new Callback<JoinAccountDTO>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<JoinAccountDTO> call, @NonNull Response<JoinAccountDTO> response) {
                if(response.isSuccessful()){
                    JoinAccountDTO result = response.body();
                    if(result != null){
                        binding.teamTxv.setText(result.getArmyunit_name());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<JoinAccountDTO> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddItemActivity.class);
                startActivity(intent);
            }
        });
        binding.manageTxv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NFCScanActivity.class);
                startActivity(intent);
            }
        });
    }

    public static class RecyclerViewDecoration extends RecyclerView.ItemDecoration {
        private final int divHeight;
        public RecyclerViewDecoration(int divHeight)
        {
            this.divHeight = divHeight;
        }
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state)
        {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.top = divHeight;
        }
    }
}