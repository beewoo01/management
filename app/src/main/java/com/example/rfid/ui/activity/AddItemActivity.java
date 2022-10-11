package com.example.rfid.ui.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rfid.data.UserInfo;
import com.example.rfid.databinding.ActivityAddItemBinding;
import com.example.rfid.R;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.xml.transform.Result;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddItemActivity extends BaseActivity {

    ActivityAddItemBinding binding;
    private static final int REQUEST_GALLARY = 0;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 100;

    private String imgpath;
    private File gtempFile;
    private File tempFile;
    private Thread thread;

    private int status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_item);

        setPic();
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.registBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(thread != null){
            thread.interrupt();
        }
    }

    private void addItem(){
        Log.wtf("addItem", String.valueOf(status));
        if(status == 1){
            sendFTP(tempFile);
            if(!binding.inputObnameEdt.getText().toString().equals("")){
                apiService.registItem(binding.inputObnameEdt.getText().toString(), tempFile.getName(), UserInfo.getInstance().accountidx).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                        if(response.isSuccessful()){
                            int result = response.body();
                            if(result == 1){
                                Toast.makeText(AddItemActivity.this, "등록되었습니다.", Toast.LENGTH_SHORT).show();
                                binding.inputObnameEdt.setText("");
                                binding.profileImgBtn.setImageResource(R.drawable.ic_camera2);
                                binding.imgSelectTxv.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });
            }else{
                Toast.makeText(AddItemActivity.this, "품목명을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        }else if(status == 2){
            sendFTP(gtempFile);
            if(!binding.inputObnameEdt.getText().toString().equals("")){
                apiService.registItem(binding.inputObnameEdt.getText().toString(), gtempFile.getName(), UserInfo.getInstance().accountidx).enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(@NonNull Call<Integer> call, @NonNull Response<Integer> response) {
                        if(response.isSuccessful()){
                            int result = response.body();
                            if(result == 1){
                                Toast.makeText(AddItemActivity.this, "등록되었습니다.", Toast.LENGTH_SHORT).show();
                                binding.inputObnameEdt.setText("");
                                binding.profileImgBtn.setImageResource(R.drawable.ic_camera2);
                                binding.imgSelectTxv.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Integer> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });
            }else{
                Toast.makeText(AddItemActivity.this, "품목명을 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        }


    }
    private void setPic(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            binding.profileImgBtn.setClipToOutline(true);
        }
        binding.profileImgBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                int permissionCheck = ContextCompat.checkSelfPermission(AddItemActivity.this, Manifest.permission.CAMERA);

                if(permissionCheck == PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(AddItemActivity.this, new String[]{Manifest.permission.CAMERA},REQUEST_IMAGE_CAPTURE);
                }else{
                    final Dialog dialog = new Dialog(AddItemActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_photo);
                    WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

                    params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
                    params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                    dialog.getWindow().setAttributes(params);

                    Button camera_btn = dialog.findViewById(R.id.camera_btn);
                    camera_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, REQUEST_CAMERA);
                            status = 1;
                            dialog.dismiss();
                        }
                    });
                    Button gallery_btn = dialog.findViewById(R.id.gallery_btn);
                    gallery_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, REQUEST_GALLARY);
                            status = 2;
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    /*final String[] items = {"사진 촬영", "갤러리에서 가져오기"};
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddItemActivity.this)
                            .setIcon(R.drawable.ic_camera)
                            .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int item) {
                                    dialogInterface.dismiss();
                                    if(item == 0){
                                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                        startActivityForResult(intent, REQUEST_CAMERA);
                                        status = 1;
                                    }
                                    else{
                                        Intent intent = new Intent();
                                        intent.setType("image/*");
                                        intent.setAction(Intent.ACTION_GET_CONTENT);
                                        startActivityForResult(intent, REQUEST_GALLARY);
                                        status = 2;
                                    }
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();*/
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(mContext, "카메라 권한 승인됨", Toast.LENGTH_SHORT).show();

                final Dialog dialog = new Dialog(AddItemActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_photo);
                WindowManager.LayoutParams params = dialog.getWindow().getAttributes();

                params.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
                params.height = WindowManager.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(params);

                Button camera_btn = dialog.findViewById(R.id.camera_btn);
                camera_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, REQUEST_CAMERA);
                        status = 1;
                        dialog.dismiss();
                    }
                });
                Button gallery_btn = dialog.findViewById(R.id.gallery_btn);
                gallery_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, REQUEST_GALLARY);
                        status = 2;
                        dialog.dismiss();
                    }
                });
                dialog.show();
                /*AlertDialog.Builder builder = new AlertDialog.Builder(AddItemActivity.this)
                        .setIcon(R.drawable.ic_camera)
                        .setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int item) {
                                dialogInterface.dismiss();
                                if(item == 0){
                                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                    startActivityForResult(intent, REQUEST_CAMERA);
                                    status = 1;
                                }
                                else{
                                    Intent intent = new Intent();
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                    startActivityForResult(intent, REQUEST_GALLARY);
                                    status = 2;
                                }
                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();*/
            } else {
                Toast.makeText(mContext, "카메라 권한이 승인되지 않았습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.wtf("permissionCheck", String.valueOf(resultCode));
        if(resultCode == RESULT_OK){
            if (requestCode == REQUEST_GALLARY) {
                try {
                    if(data != null){
                        Log.wtf("REQUEST_GALLARY", String.valueOf(status));
                        Log.wtf("REQUEST_GALLARY", data.getData().toString());
                        Uri uri = data.getData();
                        imgpath = uriToFilename(uri);
                        Log.wtf("REQUEST_GALLARY_imgpath", imgpath);
                        gtempFile = new File(imgpath);
                        boolean bExist = gtempFile.exists();
                        if(bExist){
                            Bitmap img = BitmapFactory.decodeFile(String.valueOf(imgpath));
                            binding.profileImgBtn.setImageBitmap(img);
                            binding.imgSelectTxv.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {e.printStackTrace();}
            }
            else if(requestCode == REQUEST_CAMERA){
                if(data != null){
                    Log.wtf("REQUEST_CAMERA", String.valueOf(status));
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
                    Bitmap bm = (Bitmap) data.getExtras().get("data");
                    binding.imgSelectTxv.setVisibility(View.GONE);
                    saveImage(bm, simpleDateFormat.format(new Date()));
                    Glide.with(binding.profileImgBtn.getContext()).load(tempFile).into(binding.profileImgBtn);
                }
            }
        }else{
            Toast.makeText(AddItemActivity.this, "사진 선택 취소", Toast.LENGTH_SHORT).show();
        }
    }

    private String uriToFilename(Uri uri) {
        String path = null;

        if (Build.VERSION.SDK_INT < 11) {
            path = RealPathUtil.getRealPathFromURI_BelowAPI11(this, uri);
        } else if (Build.VERSION.SDK_INT < 19) {
            path = RealPathUtil.getRealPathFromURI_API11to18(this, uri);
        } else {
            path = RealPathUtil.getRealPathFromURI_API19(this, uri);
        }

        return path;
    }

    public void saveImage(Bitmap bitmap, String saveImageName) {
        String saveDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/material_management";
        File file = new File(saveDir);
        if (!file.exists()) {
            file.mkdir();
        }

        String fileName = saveImageName + ".png";
        tempFile = new File(saveDir, fileName);
        FileOutputStream output = null;
        try {
            if (tempFile.createNewFile()) {
                Log.wtf("tempFile", tempFile.getAbsolutePath());
                Log.wtf("tempFile", tempFile.getName());
                output = new FileOutputStream(tempFile);
                Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
                newBitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
            } else {
                // 같은 이름의 파일 존재
                Log.d("TEST_LOG", "같은 이름의 파일 존재:"+saveImageName);

            }
        } catch (FileNotFoundException e) {
            Log.d("TEST_LOG", "파일을 찾을 수 없음");

        } catch (IOException e) {
            Log.d("TEST_LOG", "IO 에러");
            e.printStackTrace();

        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void sendFTP(final File file){
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                FTPClient ftpClient = new FTPClient();
                try {
                    ftpClient.connect("183.111.199.157", 21);
                    ftpClient.login("zzipbbong", "admin1237!");
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileInputStream fin;
                try{
                    ftpClient.changeWorkingDirectory("/tomcat/webapps/imagefile/material_management");
                    fin = new FileInputStream(file);
                    boolean isSuccess = ftpClient.storeFile(file.getName(), fin);
                    if(isSuccess){
                        Log.wtf("isSuccess", "업로드성공");
                    }else{
                        Log.wtf("isSuccess", "FAILLLLLLLLLLLLL");
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            }
        });
        thread.start();
    }
    public static class RealPathUtil {
        @SuppressLint("NewApi")
        public static String getRealPathFromURI_API19(Context context, Uri uri) {
            Log.e("uri", uri.getPath());
            String filePath = "";
            if (DocumentsContract.isDocumentUri(context, uri)) {
                String wholeID = DocumentsContract.getDocumentId(uri);
                Log.e("wholeID", wholeID);
// Split at colon, use second item in the array
                String[] splits = wholeID.split(":");
                if (splits.length == 2) {
                    String id = splits[1];

                    String[] column = {MediaStore.Images.Media.DATA};
// where id is equal to
                    String sel = MediaStore.Images.Media._ID + "=?";
                    Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);
                    int columnIndex = cursor.getColumnIndex(column[0]);
                    if (cursor.moveToFirst()) {
                        filePath = cursor.getString(columnIndex);
                    }
                    cursor.close();
                }
            } else {
                filePath = uri.getPath();
            }
            return filePath;
        }

        @SuppressLint("NewApi")
        public static String getRealPathFromURI_API11to18(Context context, Uri contentUri) {
            String[] proj = {MediaStore.Images.Media.DATA};
            String result = null;
            CursorLoader cursorLoader = new CursorLoader(
                    context,
                    contentUri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();
            if (cursor != null) {
                int column_index =
                        cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                result = cursor.getString(column_index);
            }
            return result;
        }

        public static String getRealPathFromURI_BelowAPI11(Context context, Uri contentUri) {
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index
                    = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
    }
}