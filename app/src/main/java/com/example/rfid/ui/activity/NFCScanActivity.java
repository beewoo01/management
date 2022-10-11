package com.example.rfid.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;

import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcF;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rfid.R;
import com.example.rfid.data.ProductDTO;
import com.example.rfid.data.UserInfo;
import com.example.rfid.databinding.ActivityNfcScanBinding;


import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class NFCScanActivity extends BaseActivity implements NfcAdapter.ReaderCallback {

    ActivityNfcScanBinding binding;
    private Dialog dialog;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private String nfcState;
    private int productidx = 0;
    private static String tagNum = null;
    private int productstatus = 0;
    public static final String CHARS = "0123456789ABCDEF";

    public static final String NFC_DISABLE = "DISABLE";
    public static final String NFC_ACTIVE = "ACTIVE";
    public static final String NFC_INACTIVE = "INACTIVE";

    private IntentFilter[] intentFiltersArray;

    private String[][] techListsArray = new String[][]{new String[]{NfcF.class.getName()}};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_nfc_scan);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_nfc_scan);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, this.getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE
            );
        } else {
            pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    intent,
                    0
            );
        }

        initViews();


    }

    private void initViews() {
        binding.backBtn.setOnClickListener(view -> finish());

        binding.moveBtn.setOnClickListener(view -> {
            if (productidx <= 0) {
                Toast.makeText(mContext, "스캔된 자재가 없습니다.", Toast.LENGTH_SHORT).show();
            } else if (productstatus != 0) {
                Toast.makeText(mContext, "불출할 수 없는 자재입니다.", Toast.LENGTH_SHORT).show();
            } else {
                dialog.show();
                initDialog();
            }
        });

        binding.returnBtn.setOnClickListener(view -> {
            //반납하기 넣으면됨
            if (productidx <= 0) {
                Toast.makeText(mContext, "스캔된 자재가 없습니다.", Toast.LENGTH_SHORT).show();
            } else if (productstatus != 1) {
                Toast.makeText(mContext, "불출되지 않은 자재입니다.", Toast.LENGTH_SHORT).show();
            } else {
                ReturnProduct();
            }
        });

    }

    @Override
    protected void onPause() {
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(NFCScanActivity.this);
        }
        tagNum = null;
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DialogSet();
        InitSet();
        if (nfcAdapter != null) {
            if (nfcState.equals(NFC_ACTIVE)) {
                Log.wtf("onResume", NFC_ACTIVE);
                nfcAdapter.enableForegroundDispatch(NFCScanActivity.this, pendingIntent, null, null);
            }

            Bundle bundle = new Bundle();
            bundle.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250);

            nfcAdapter.enableReaderMode(
                    this,
                    this,
                    NfcAdapter.FLAG_READER_NFC_A |
                            NfcAdapter.FLAG_READER_NFC_B |
                            NfcAdapter.FLAG_READER_NFC_F |
                            NfcAdapter.FLAG_READER_NFC_V |
                            NfcAdapter.FLAG_READER_NFC_BARCODE |
                            NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS,
                    bundle
            );
        }

        IntentFilter intentFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            intentFilter.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }

        intentFiltersArray = new IntentFilter[]{intentFilter,};

        nfcAdapter.enableForegroundDispatch(
                this,
                pendingIntent,
                intentFiltersArray,
                techListsArray
        );
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMessages != null) {
                Log.wtf("onNewIntent", "rawMessages != null");
                NdefMessage[] messages = new NdefMessage[rawMessages.length];
                for (int i = 0; i < rawMessages.length; i++) {
                    messages[i] = (NdefMessage) rawMessages[i];
                    showMsg(messages[i]);
                }
            } else {
                Log.wtf("onNewIntent", "rawMessages is null");
            }

        }
        /*NdefMessage[] messages = getNdefMessages(intent);
        Log.wtf("onNewIntent", "onNewIntent");

        if (messages != null) {
            Log.wtf("messages", Arrays.toString(messages));
            Log.wtf("ndefmessages", displayByteArray(messages[0].toByteArray()));

            tagNum = displayByteArray(messages[0].toByteArray());

            Log.wtf("tagNum", tagNum.toString());
        }*/

    }


    private void showMsg(NdefMessage mMessage) {
        NdefRecord[] recs = mMessage.getRecords();
        for (NdefRecord record : recs) {
            //record.getPayload();
            String str = new String(record.getPayload());
            Log.wtf("showMsg", str);
            if (Arrays.equals(record.getType(), NdefRecord.RTD_URI)) {
                Uri u = record.toUri();
                Intent j = new Intent(Intent.ACTION_VIEW);
                j.setData(u);
                startActivity(j);
                finish();
            }
        }
        /*for (i in recs.indices) {
            val record = recs[i]
            if (Arrays.equals(record.type, NdefRecord.RTD_URI)) {
                val u: Uri = record.toUri()
                val j = Intent(Intent.ACTION_VIEW)
                j.data = u
                startActivity(j)
                finish()
            }
        }*/
    }

    private void InitSet() {

        nfcState = getNfcState();
        switch (nfcState) {
            case NFC_DISABLE:
                Log.wtf("nfcState", "NFC_DISABLE");
                Toast.makeText(NFCScanActivity.this, "NFC 기능을 사용할 수 없는 단말기입니다.", Toast.LENGTH_SHORT).show();
                break;
            case NFC_INACTIVE:
                Log.wtf("nfcState", "NFC_INACTIVE");
                AlertDialog.Builder ad = new AlertDialog.Builder(NFCScanActivity.this);
                ad.setTitle("NFC INACTIVE");
                ad.setMessage("NFC 기능을 활성화 해주세요.");
                ad.setPositiveButton("OK", (dialogInterface, i) -> {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                        startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
                    } else {
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                });
                ad.show();
                break;
            case NFC_ACTIVE:
                Log.wtf("nfcState", "NFC_ACTIVE");
                NFCset();
                break;
        }

        //binding.backBtn.setOnClickListener(view -> finish());


        /*if (tagNum != null) { //스캔된게 있을때
            //getProductIdxOFTag();
            binding.scanObjectLayout.setVisibility(View.VISIBLE);
            binding.scanTxv.setVisibility(View.GONE);

            binding.moveBtn.setOnClickListener(view -> {
                if (productstatus != 0) {
                    Toast.makeText(mContext, "불출할 수 없는 자재입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.show();
                    initDialog();
                }
            });

            binding.returnBtn.setOnClickListener(view -> {
                //반납하기 넣으면됨
                if (productstatus != 1) {
                    Toast.makeText(mContext, "불출되지 않은 자재입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    ReturnProduct();
                }
            });

        } else { //스캔안됨
            binding.scanObjectLayout.setVisibility(View.GONE);
            binding.scanTxv.setVisibility(View.VISIBLE);

            binding.moveBtn.setOnClickListener(view -> Toast.makeText(NFCScanActivity.this, "스캔된 자재가 없습니다.", Toast.LENGTH_SHORT).show());
            binding.returnBtn.setOnClickListener(view -> Toast.makeText(NFCScanActivity.this, "스캔된 자재가 없습니다.", Toast.LENGTH_SHORT).show());

        }*/
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private void NFCset() {
        Log.wtf("NFCset", "NFCsetNFCset");
        Intent intent = new Intent(NFCScanActivity.this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(NFCScanActivity.this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(NFCScanActivity.this, 0, intent, 0);
        }
    }

    private String getNfcState() {
        try {
            nfcAdapter = NfcAdapter.getDefaultAdapter(NFCScanActivity.this);
            if (nfcAdapter == null) {
                Log.wtf("getNfcState", NFC_DISABLE);
                return NFC_DISABLE;
            } else {
                if (nfcAdapter.isEnabled()) {
                    Log.wtf("getNfcState", NFC_ACTIVE);
                    return NFC_ACTIVE;
                } else {
                    Log.wtf("getNfcState", NFC_INACTIVE);
                    return NFC_INACTIVE;
                }
            }
        } catch (Exception e) {
            Log.wtf("getNfcState", NFC_DISABLE);
            return NFC_DISABLE;
        }
    }

    /*private void getProductInfoOfTag(String tag) {
        apiService.getProductInfoOfTag(tag).enqueue(new Callback<HashMap<String, Object>>() {
            @Override
            public void onResponse(@NonNull Call<HashMap<String, Object>> call, @NonNull Response<HashMap<String, Object>> response) {

            }

            @Override
            public void onFailure(@NonNull Call<HashMap<String, Object>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }*/

    private void getProductIdxOFTag(String tag) {
        //tagNum
        apiService.getProductIdxOfTagInfo(tag).enqueue(new Callback<ProductDTO>() {
            @Override
            public void onResponse(@NonNull Call<ProductDTO> call, @NonNull Response<ProductDTO> response) {
                if (response.isSuccessful()) {
                    ProductDTO result = response.body();
                    if (result != null) {
                        productidx = result.getProduct_idx();
                        Log.wtf("getProductIdxOfTagInfo", "productidx = "  + result.getProduct_idx());
                        binding.scanObjectLayout.setVisibility(View.VISIBLE);
                        binding.scanTxv.setVisibility(View.GONE);
                        binding.objectNameTxv.setText(result.getProduct_name());
                        //이미지도 바인딩해야됨
                        Glide.with(binding.objectImv.getContext())
                                .load("http://raon-soft.com/imagefile/material_management/" + result.getItem_image())
                                .circleCrop()
                                .into(binding.objectImv);

                        productstatus = result.getProduct_status();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ProductDTO> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void initDialog() {
        final Spinner spinner1 = dialog.findViewById(R.id.squadron_sp);
        final Spinner spinner2 = dialog.findViewById(R.id.platoon_sp);
        final EditText name = dialog.findViewById(R.id.inputname_edt);
        final EditText context = dialog.findViewById(R.id.inputcontext_txv);
        Button button = dialog.findViewById(R.id.move_btn);

        String[] company = {"선택해주세요.", "1중대", "2중대", "3중대", "4중대", "본부중대"};
        String[] platoon = {"선택해주세요.", "1소대", "2소대", "3소대", "4소대", "기타"};
        SpinnerAdapter adapter1 = new SpinnerAdapter(NFCScanActivity.this, R.layout.item_spinner, company);
        SpinnerAdapter adapter2 = new SpinnerAdapter(NFCScanActivity.this, R.layout.item_spinner, platoon);
        spinner1.setAdapter(adapter1);
        spinner2.setAdapter(adapter2);

        button.setOnClickListener(view -> {

            if (!(spinner1.getSelectedItem().equals("선택해주세요."))) {
                if (!(spinner2.getSelectedItem().equals("선택해주세요."))) {
                    if (!name.getText().toString().equals("")) {
                        Log.wtf("setOnClickListener", String.valueOf(productidx));
                        apiService.provistionProduct(productidx, UserInfo.getInstance().accountidx, spinner1.getSelectedItem().toString(),
                                spinner2.getSelectedItem().toString(), name.getText().toString(), context.getText().toString()).enqueue(new Callback<Integer>() {
                            @Override
                            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                                if (response.isSuccessful()) {
                                    Log.wtf("provistionProduct", "SUCCESS");
                                    name.setText("");
                                    context.setText("");
                                    Log.wtf("provistionProduct", tagNum);
                                    dialog.dismiss();
                                    Toast.makeText(mContext, "불출되었습니다.", Toast.LENGTH_SHORT).show();

                                    finish();
                                    overridePendingTransition(0, 0);
                                    Intent intent = getIntent();
                                    startActivity(intent);
                                    overridePendingTransition(0, 0);
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                                t.printStackTrace();
                            }
                        });
                    } else {
                        Toast.makeText(NFCScanActivity.this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(NFCScanActivity.this, "소대를 선택해주세요.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(NFCScanActivity.this, "중대를 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ReturnProduct() {
        Log.wtf("returnProject", "product_status = " + productstatus + "   product_idx = " + productidx);
        apiService.returnProduct(productstatus, productidx).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                Log.wtf("ReturnProduct", tagNum);
                int result = response.body();

                if (result == -1) {
                    Toast.makeText(NFCScanActivity.this, "반납할 수 없는 자재입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NFCScanActivity.this, "반납되었습니다.", Toast.LENGTH_SHORT).show();
                }
                finish();
                overridePendingTransition(0, 0);
                Intent intent = getIntent();
                startActivity(intent);
                overridePendingTransition(0, 0);

            }

            @Override
            public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }


    //다이얼로그 세팅
    private void DialogSet() {
        dialog = new Dialog(NFCScanActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_move_info);
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

        params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);
    }

    @Override
    public void onTagDiscovered(Tag tag) {
        Ndef mNdef = Ndef.get(tag);

        if (mNdef != null) {
            NdefMessage mNdefMessage = mNdef.getCachedNdefMessage();

            if (mNdefMessage != null && mNdefMessage.getRecords() != null) {
                String payload = new String(mNdefMessage.getRecords()[0].getPayload());
                String productTag = payload.substring(3);
                Log.wtf("productTag", productTag);

                if (payload.length() > 0) {
                    getProductIdxOFTag(productTag);
                    //viewModel.getProductInfo(productTag)
                } else {
                    runOnUiThread(() -> {
                        Toast.makeText(NFCScanActivity.this, "스캔된 NFC TAG가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                    });
                }

                //Log.wtf("tag is ", payload);
            }
        } else {
            runOnUiThread(() -> {
                Toast.makeText(NFCScanActivity.this, "스캔된 NFC TAG가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            });

            Log.wtf("mNdefMessage.records", "mNdefMessage.records null");
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    public static class SpinnerAdapter extends ArrayAdapter<String> {
        Context context;
        String[] items;


        public SpinnerAdapter(final Context context,
                              final int textViewResourceId, final String[] objects) {
            super(context, textViewResourceId, objects);
            this.items = objects;
            this.context = context;
        }

        @Override
        public boolean isEnabled(int position) {
            return position != 0;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = super.getDropDownView(position, convertView, parent);
            TextView txt = (TextView) v;
            if (position == 0) {
                txt.setTextColor(Color.GRAY);
            } else {
                txt.setTextColor(Color.BLACK);
            }
            return v;
        }
    }
}