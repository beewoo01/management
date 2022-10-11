package com.example.rfid.ui.activity;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.rfid.R;
import com.example.rfid.data.UserInfo;
import com.example.rfid.databinding.ActivityLoginBinding;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    ActivityLoginBinding binding;


    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        DialogSet();
        InitSet();

    }

    //기본설정
    private void InitSet() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                requestPermissions(new String[]
                        {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
            }
        }

        binding.loginBtn.setOnClickListener(view -> Login(binding.idTxv.getText().toString(), binding.pwTxv.getText().toString()));

        binding.findidpwTxv.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), FindAccountActivity.class);
            startActivity(intent);
        });

        binding.signup.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
        });

    }

    //로그인
    private void Login(String id, String pw) {
        apiService.loginAccount(id, pw, 0).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                if (response.isSuccessful()) {
                    int result = response.body();
                    if (result >= 0) {
                        UserInfo.getInstance().accountidx = result;
                        Log.wtf("LoginIdx", String.valueOf(UserInfo.getInstance().accountidx));
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    } else if (result == -1) {

                        dialog.show();

                        TextView text = dialog.findViewById(R.id.textView);
                        text.setText("계정정보가 일치하지 않습니다.");

                        Button button = dialog.findViewById(R.id.button);
                        button.setOnClickListener(view -> {
                            dialog.dismiss();
                            binding.idTxv.setText("");
                            binding.pwTxv.setText("");
                        });

                    } else if (result == -2) {
                        dialog.show();

                        TextView text = dialog.findViewById(R.id.textView);
                        text.setText("관리자 승인이 필요합니다.");

                        Button button = dialog.findViewById(R.id.button);
                        button.setOnClickListener(view -> {
                            dialog.dismiss();
                            binding.idTxv.setText("");
                            binding.pwTxv.setText("");
                        });

                    } else {
                        dialog.show();

                        TextView text = dialog.findViewById(R.id.textView);
                        text.setText("관리자 앱에서 로그인 해주세요.");

                        Button button = dialog.findViewById(R.id.button);

                        button.setOnClickListener(view -> {
                            dialog.dismiss();
                            binding.idTxv.setText("");
                            binding.pwTxv.setText("");
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
    private void DialogSet() {
        dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_default);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
    }
}