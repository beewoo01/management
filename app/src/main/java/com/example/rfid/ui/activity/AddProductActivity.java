package com.example.rfid.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rfid.R;
import com.example.rfid.data.UserInfo;
import com.example.rfid.databinding.ActivityAddProductBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductActivity extends BaseActivity {

    ActivityAddProductBinding binding;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_product);

        DialogSet();
        InitSet();
    }
    private void InitSet(){
        binding.registBtn.setOnClickListener(view -> {
            if(!binding.inputObnameEdt.getText().toString().equals("")){
                if(!binding.inputTagEdt.getText().toString().equals("")){
                    if(!binding.inputIssueEdt.getText().toString().equals("")){
                        RegistProduct();
                    }else{
                        Toast.makeText(getBaseContext(),"특이사항을 입력해주세요.",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getBaseContext(),"태그정보를 입력해주세요.",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(getBaseContext(),"자재명을 입력해주세요.",Toast.LENGTH_LONG).show();
            }
        });

        binding.backBtn.setOnClickListener(view -> {
            finish();
        });
    }


    private void RegistProduct(){
        Intent intent = getIntent();
        apiService.registProduct(binding.inputObnameEdt.getText().toString(),binding.inputIssueEdt.getText().toString(),intent.getIntExtra("itemidx",0),
                        UserInfo.getInstance().accountidx, binding.inputTagEdt.getText().toString())
                .enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                        if(response.isSuccessful()){
                            int result = response.body();
                            if(result == 1){
                                dialog.show();

                                TextView text = dialog.findViewById(R.id.textView);
                                text.setText("등록되었습니다.");

                                Button button = dialog.findViewById(R.id.button);
                                button.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.dismiss();
                                        binding.inputObnameEdt.setText("");
                                        binding.inputTagEdt.setText("");
                                        binding.inputIssueEdt.setText("");
                                    }
                                });

                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });
    }
    //다이얼로그 세팅
    private void DialogSet(){
        dialog = new Dialog(AddProductActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_default);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
    }
}