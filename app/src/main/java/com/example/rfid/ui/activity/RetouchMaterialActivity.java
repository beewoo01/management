package com.example.rfid.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.rfid.R;
import com.example.rfid.data.UserInfo;
import com.example.rfid.databinding.ActivityRetouchMaterialBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetouchMaterialActivity extends BaseActivity {

    ActivityRetouchMaterialBinding binding;
    private Dialog dialog;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retouch_material);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_retouch_material);
        intent = getIntent();
        DialogSet();
        InitSet();
    }

    @SuppressLint("SetTextI18n")
    private void InitSet(){
        binding.exobjeTxv.setText(intent.getStringExtra("title"));
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        if(intent.getIntExtra("product_status",0) == 0){
            binding.infoTxv.setText(intent.getStringExtra("title")+"은 현재 미불출상태입니다.");
//            binding.checkBox.setChecked(false);
        }else if(intent.getIntExtra("product_status",0) == 1){
            binding.infoTxv.setText(intent.getStringExtra("title")+"은 현재 불출상태입니다.");
//            binding.checkBox.setChecked(false);
        }/*else if(intent.getIntExtra("product_status",0) == 2){
            binding.infoTxv.setText(intent.getStringExtra("title")+"은 현재 폐기상태입니다.");
            binding.checkBox.setChecked(true);
        }*/
        binding.inputObnameEdt.setText(intent.getStringExtra("title"));
        binding.inputTagEdt.setText(intent.getStringExtra("taginfo"));
        binding.inputIssueEdt.setText(intent.getStringExtra("special"));
        binding.retouchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.inputObnameEdt.getText().toString().equals("")){
                    if(!binding.inputTagEdt.getText().toString().equals("")){
                        if(!binding.inputIssueEdt.getText().toString().equals("")){
                            dialog.show();

                            Button ok_btn = dialog.findViewById(R.id.ok_btn);
                            ok_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    updateProduct();
                                    dialog.dismiss();
                                }
                            });
                            Button cancel_btn = dialog.findViewById(R.id.cancel_btn);
                            cancel_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                        }
                        else{
                            Toast.makeText(getBaseContext(),"특이사항을 입력해주세요.",Toast.LENGTH_LONG).show();
                        }
                    }
                    else{
                        Toast.makeText(getBaseContext(),"태그정보를 입력해주세요.",Toast.LENGTH_LONG).show();
                    }
                }
                else{
                    Toast.makeText(getBaseContext(),"자재명을 입력해주세요.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void updateProduct(){
        int status;
        if(binding.checkBox.isChecked()){
            status = 2;
        }else{
            status = intent.getIntExtra("product_status",0);
        }
        apiService.updateProduct(UserInfo.getInstance().accountidx,binding.inputObnameEdt.getText().toString(),binding.inputIssueEdt.getText().toString(),
                binding.inputTagEdt.getText().toString(),status,intent.getIntExtra("product_idx",0))
                .enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                        if(response.isSuccessful()){
                            int result = response.body();
                            if(result == 1){
                                finish();
                            }
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });

    }

    private void DialogSet(){
        dialog = new Dialog(RetouchMaterialActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_retouch);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

        params.width = (int)(getResources().getDisplayMetrics().widthPixels*0.9);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

    }
}