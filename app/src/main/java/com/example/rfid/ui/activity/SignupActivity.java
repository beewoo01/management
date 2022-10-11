package com.example.rfid.ui.activity;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rfid.R;
import com.example.rfid.data.JoinAccountDTO;
import com.example.rfid.databinding.ActivitySignupBinding;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends BaseActivity {

    ActivitySignupBinding binding;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);

        DialogSet();
        InitSet();
    }

    //기본설정
    private void InitSet(){
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.inputidEdt.getText().toString().equals("")){
                    if(!binding.inputpwEdt.getText().toString().equals("")){
                        if(!binding.checkpwEdt.getText().toString().equals("")){
                            if(binding.checkpwEdt.getText().toString().equals(binding.inputpwEdt.getText().toString())){
                                if(!binding.inputnameEdt.getText().toString().equals("")){
                                    if(!binding.inputnumEdt.getText().toString().equals("")){
                                        Signup(binding.inputidEdt.getText().toString(), binding.inputpwEdt.getText().toString(),
                                                binding.inputnameEdt.getText().toString(),0,Integer.parseInt(binding.inputnumEdt.getText().toString()),0);
                                    }else{
                                        Toast.makeText(getBaseContext(),"소속번호를 입력해주세요.",Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(getBaseContext(),"이름을 입력해주세요.",Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(getBaseContext(),"비밀번호가 일치하지 않습니다.",Toast.LENGTH_LONG).show();
                            }
                        } else{
                            Toast.makeText(getBaseContext(),"비밀번호를 확인해주세요.",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getBaseContext(),"비밀번호를 입력해주세요.",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getBaseContext(),"아이디를 입력해주세요.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //회원가입
    private void Signup(String id,String pw, String name, int ispro, int armyunit, int permission){
        apiService.signUpAccount(id,pw, name, ispro, armyunit, permission).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if(response.isSuccessful()){
                    int result = response.body();
                    Log.wtf("Signup", String.valueOf(result));
                    if(result == 0){
                        dialog.show();

                        TextView text = dialog.findViewById(R.id.textView);
                        text.setText("관리자에게 승인을 요청했습니다.");

                        Button button = dialog.findViewById(R.id.button);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                    else if(result == 1){
                        Toast.makeText(getBaseContext(),"소속번호와 일치하는 부대가 없습니다.",Toast.LENGTH_LONG).show();
                    }
                    else if(result == 2){
                        Toast.makeText(getBaseContext(),"이미 존재하는 아이디입니다.",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getBaseContext(),"ERROR",Toast.LENGTH_LONG).show();
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
        dialog = new Dialog(SignupActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_default);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
    }

}