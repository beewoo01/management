package com.example.rfid.ui.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.rfid.R;
import com.example.rfid.databinding.FragmentFindIdBinding;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindIDFragment extends BaseFragment {
    FragmentFindIdBinding binding;
    private Dialog dialog;
    public FindIDFragment() {}
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentFindIdBinding.inflate(inflater, container, false);

        DialogSet();
        InitSet();

        return binding.getRoot();
    }
    //기본설정
    private void InitSet(){
        binding.findidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.inputnameEdt.getText().toString().equals("")){
                    if(!binding.inputnumEdt.getText().toString().equals("")){
                        FindID();
                    }else{
                        Toast.makeText(requireContext(),"소속번호를 입력해주세요.",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(requireContext(),"이름을 입력해주세요.",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //아이디 찾기
    private void FindID(){
        apiService.findID(binding.inputnameEdt.getText().toString(), Integer.parseInt(binding.inputnumEdt.getText().toString())).enqueue(new Callback<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                if(response.isSuccessful()){
                    String result = response.body();
                    if(result != null){
                        Log.wtf("FindID", result);
                        dialog.show();
                        Button button = dialog.findViewById(R.id.button);
                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                binding.inputnumEdt.setText("");
                                binding.inputnameEdt.setText("");
                                dialog.dismiss();
                            }
                        });
                        TextView text = dialog.findViewById(R.id.textView);
                        if(result.equals("?????")){
                            text.setText("계정정보를 찾을수없습니다.");
                        }
                        else{
                            text.setText("아이디는 "+result+" 입니다.");
                        }
                    }
                }
            }
            @Override
            public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //다이얼로그 세팅
    private void DialogSet(){
        dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_default);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
    }
}