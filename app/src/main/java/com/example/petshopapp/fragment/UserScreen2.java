package com.example.petshopapp.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.petshopapp.R;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.Const;
import com.example.petshopapp.api.apiservice.ChiNhanhService;
import com.example.petshopapp.api.apiservice.HinhAnhService;
import com.example.petshopapp.api.apiservice.NhanVienService;
import com.example.petshopapp.api.apiservice.TaiKhoanService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.ChiNhanh;
import com.example.petshopapp.model.HinhAnh;
import com.example.petshopapp.model.NhanVien;
import com.example.petshopapp.model.TaiKhoan;
import com.example.petshopapp.tools.ImageInteract;
import com.example.petshopapp.tools.RealPathUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class UserScreen2 extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    //Đối tượng view
    private View mView;
    private EditText edtChiNhanh, edtChucVu, edtMaNV, edtHoTen, edtCCCD, edtEmail, edtSDT;
    private Button btnUpload, btnChangePassword;
    private ImageView ivAvatar;

    //API
    private NhanVienService nhanVienService;
    private TaiKhoanService taiKhoanService;
    private HinhAnhService hinhAnhService;
    private ChiNhanhService chiNhanhService;

    //Data
    private Map<Integer,String> chiNhanhMap = new HashMap<>();

    //Info
    private NhanVien nhanVien;
    private TaiKhoan taiKhoan;
    private SharedPreferences sharedPreferences;
    private String username;

    //Avatar
    private static final int MY_REQUEST_CODE = 123;
    private Bitmap bitmap;
    private Uri mUri;
    private ActivityResultLauncher<Intent> mActivityResultLancher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data == null ){
                            return;
                        }
                        Uri uri = data.getData();
                        mUri=uri;
                        try{
                            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);
                            ivAvatar.setImageBitmap(bitmap);
                            sendImage(username,"");
                        } catch (FileNotFoundException e) {
                            Log.e("FileNotFoundException", e.getMessage());
                            Toast.makeText(getContext(),"FileNotFoundException" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            bitmap=null;
                            ivAvatar.setImageResource(R.mipmap.ic_launcher);
                        } catch (IOException e) {
                            Log.e("IOException", e.getMessage());
                            Toast.makeText(getContext(),"IOException" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            bitmap=null;
                            ivAvatar.setImageResource(R.mipmap.ic_launcher);
                        }
                    }
                }
            }
    );

    public UserScreen2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isVisible()){
            DocDLTaiKhoan();
            DocDLChiNhanh();
            DocDL();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MY_REQUEST_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getGallery();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.fragment_user_screen2, container, false);
        ApiClient apiClient = ApiClient.getApiClient();
        nhanVienService =apiClient.getRetrofit().create(NhanVienService.class);
        taiKhoanService=apiClient.getRetrofit().create(TaiKhoanService.class);
        hinhAnhService=apiClient.getRetrofit().create(HinhAnhService.class);
        chiNhanhService=apiClient.getRetrofit().create(ChiNhanhService.class);
        setInit();
        setEvent();
        return mView;
    }

    private void setInit(){
        edtChiNhanh = mView.findViewById(R.id.edtChiNhanh);
        edtMaNV=mView.findViewById(R.id.edtMaNV);
        edtHoTen = mView.findViewById(R.id.edtHoTen);
        edtCCCD = mView.findViewById(R.id.edtCCCD);
        edtEmail=mView.findViewById(R.id.edtEmail);
        edtSDT=mView.findViewById(R.id.edtSDT);
        edtChucVu=mView.findViewById(R.id.edtChucVu);

        btnUpload=mView.findViewById(R.id.btnUpload);
        btnChangePassword=mView.findViewById(R.id.btnChangePassword);

        ivAvatar = mView.findViewById(R.id.ivAvatarNV);

        sharedPreferences = mView.getContext().getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE);
        username= sharedPreferences.getString("username","");
    }

    private void setEvent(){
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangePasswordDialog(Gravity.CENTER);
            }
        });
    }

    private String getString(String text){
        return text==null? "" :text;
    }

    private void updateEditText(){
        if(chiNhanhMap == null||chiNhanhMap.size()==0) {
            DocDLChiNhanh();//Thử đọc lại data chi nhánh
            if(chiNhanhMap == null||chiNhanhMap.size()==0) {
                Toast.makeText(mView.getContext(), "Lỗi: ChiNhanhList null", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(nhanVien==null) {
            Toast.makeText(mView.getContext(), "NhanVien null", Toast.LENGTH_SHORT).show();
            return;
        }
        //update editText
        edtChiNhanh.setText(chiNhanhMap.get(nhanVien.getMaChiNhanh()));
        edtMaNV.setText(getString(nhanVien.getMaNhanVien()));
        edtChucVu.setText(getString(nhanVien.getChucVu()));
        edtHoTen.setText(getString(nhanVien.getHo())+" "+getString(nhanVien.getTen()));
        edtCCCD.setText(getString(nhanVien.getCccd()));
        edtEmail.setText(getString(nhanVien.getEmail()));
        edtSDT.setText(getString(nhanVien.getSoDienThoai()));
        getImage();
    }

    private void DocDL(){
        System.out.println("DocDLNhanVienID");
        nhanVienService.getOneById(username).enqueue(new Callback<NhanVien>() {
            @Override
            public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                if (response.code() == 200) {
                    nhanVien = response.body();
                    updateEditText();
                } else {
                    try {
                        int code = response.code();
                        String message = response.message();
                        String error = response.errorBody().string();
                        SendMessage.sendMessageFail(mView.getContext(),code,error,message);
                    } catch (Exception e) {
                        SendMessage.sendCatch(mView.getContext(),e.getMessage());
                        return;
                    }
                }
            }
            @Override
            public void onFailure(Call<NhanVien> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });


    }
    public void DocDLChiNhanh(){
        System.out.println("DocDLChiNhanh");
        chiNhanhService.getAll().enqueue(new Callback<List<ChiNhanh>>() {
            @Override
            public void onResponse(Call<List<ChiNhanh>> call, Response<List<ChiNhanh>> response) {
                if (response.code() == 200) {
                    chiNhanhMap.clear();
                    for (ChiNhanh x : response.body()) {
                        chiNhanhMap.put(x.getMaChiNhanh(),x.getTenChiNhanh());
                    }
                } else {
                    try {
                        int code = response.code();
                        String message = response.message();
                        String error = response.errorBody().string();
                        SendMessage.sendMessageFail(mView.getContext(),code,error,message);
                    } catch (Exception e) {
                        SendMessage.sendCatch(mView.getContext(),e.getMessage());
                        return;
                    }
                }
            }

            @Override
            public void onFailure(Call<List<ChiNhanh>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }

    private void DocDLTaiKhoan(){
        System.out.println("DocDLTaiKhoan");
        taiKhoanService.getOneById(username).enqueue(new Callback<TaiKhoan>() {
            @Override
            public void onResponse(Call<TaiKhoan> call, Response<TaiKhoan> response) {

                if (response.code() == 200) {
                    taiKhoan=response.body();
                } else {
                    try {
                        int code = response.code();
                        String message = response.message();
                        String error = response.errorBody().string();
                        SendMessage.sendMessageFail(mView.getContext(),code,error,message);
                    } catch (Exception e) {
                        SendMessage.sendCatch(mView.getContext(),e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<TaiKhoan> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }



    private void updatePassword(Dialog dialog, String password){
        if(taiKhoan==null|| !taiKhoan.getTrangThai()){
            Toast.makeText(getContext(),"Tài khoản vô hiệu lực",Toast.LENGTH_SHORT).show();
            return;
        }
        if(password==null){
            Toast.makeText(getContext(),"Mật khẩu vô hiệu lực",Toast.LENGTH_SHORT).show();
            return;
        }
        taiKhoan.setMatKhau(password);
        taiKhoanService.update(taiKhoan).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()== 200){
                    try {
                        String result = response.body().string();
                        Toast.makeText(mView.getContext(),result,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } catch (IOException e) {
                        SendMessage.sendCatch(mView.getContext(),e.getMessage());
                    }
                }
                else{
                    try {
                        int code = response.code();
                        String message = response.message();
                        String error = response.errorBody().string();
                        SendMessage.sendMessageFail(mView.getContext(),code,error,message);
                    } catch (Exception e) {
                        SendMessage.sendCatch(mView.getContext(),e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }

    private void openChangePasswordDialog(int gravity){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_password);

        Window window = dialog.getWindow();
        if(window == null)return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);

        if(Gravity.BOTTOM == gravity){
            dialog.setCancelable(false);
        }
        else{
            dialog.setCancelable(true);
        }

        EditText edtOldPassword = dialog.findViewById(R.id.edtOldPassword);
        EditText edtNewPassword = dialog.findViewById(R.id.edtNewPassword);
        EditText edtAgainPassword = dialog.findViewById(R.id.edtAgainPassword);
        Button btnChange = dialog.findViewById(R.id.btnChange);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = edtOldPassword.getText().toString();
                String newPassword = edtNewPassword.getText().toString();
                String againPassword = edtAgainPassword.getText().toString();
                if(oldPassword.equals("")||newPassword.equals("")||againPassword.equals("")){
                    Toast.makeText(getContext(),"Lỗi khoảng trắng input giữa các trường",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(taiKhoan==null){
                    Toast.makeText(getContext(),"Lỗi không lấy được dữ liệu tài khoản",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!taiKhoan.getMatKhau().equals(oldPassword)){
                    Toast.makeText(getContext(),"Mật khẩu cũ không khớp",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!newPassword.equals(againPassword)){
                    Toast.makeText(getContext(),"Mật khẩu nhập lại không đúng",Toast.LENGTH_SHORT).show();
                    return;
                }
                updatePassword(dialog, newPassword);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void sendImage(String maNhanVien, String maKhachHang){
        RequestBody requestBodyMaNhanVien = RequestBody.create(MediaType.parse("multipart/form-data"), maNhanVien);
        RequestBody requestBodyMaKhachHang = RequestBody.create(MediaType.parse("multipart/form-data"), maKhachHang);

        String imgRealPath = RealPathUtil.getRealPath(this.getContext(), mUri);
        File file = new File(imgRealPath);
        RequestBody requestBodyAvatar = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBodyAvatar = MultipartBody.Part.createFormData(Const.KEY_IMAGE, file.getName(), requestBodyAvatar);

        hinhAnhService.saveImage(multipartBodyAvatar, requestBodyMaNhanVien, requestBodyMaKhachHang).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    if(response.code() == 200){
                        String result = response.body().string();
                        Toast.makeText(mView.getContext(),result,Toast.LENGTH_SHORT).show();
                    }
                    else{
                        try {
                            int code = response.code();
                            String message = response.message();
                            String error = response.errorBody().string();
                            SendMessage.sendMessageFail(mView.getContext(),code,error,message);
                        } catch (Exception e) {
                            SendMessage.sendCatch(mView.getContext(),e.getMessage());
                        }
                    }
                }
                catch (Exception e){
                    SendMessage.sendCatch(mView.getContext(),e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }
    private void getGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        mActivityResultLancher.launch(Intent.createChooser(intent, "Select picture"));
    }

    private void onClickRequestPermission(){
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.M){
            getGallery();
            return;
        }
        if(this.getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            getGallery();
        }
        else{
            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            this.requestPermissions(permission,MY_REQUEST_CODE);
        }
    }

    private void getImage(){
        if(nhanVien!=null && nhanVien.getHinhAnh()!=null&&nhanVien.getHinhAnh().size()!=0){
            long idHinh = nhanVien.getHinhAnh().get(0);
            hinhAnhService.getImage(new long[]{idHinh}).enqueue(new Callback<List<HinhAnh>>() {
                @Override
                public void onResponse(Call<List<HinhAnh>> call, Response<List<HinhAnh>> response) {
                    try{
                        if(response.code() == 200){
                            List<HinhAnh> list = response.body();
                            String source = list.get(0).getSource();
                            bitmap= ImageInteract.convertStringToBitmap(source);
                            if(bitmap == null){
                                Toast.makeText(getContext(),"Bitmap null",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            ivAvatar.setImageBitmap(bitmap);
                        }
                        else{
                            try {
                                int code = response.code();
                                String message = response.message();
                                String error = response.errorBody().string();
                                SendMessage.sendMessageFail(getContext(),code,error,message);
                            } catch (Exception e) {
                                SendMessage.sendCatch(getContext(),e.getMessage());
                            }
                        }
                    }
                    catch (Exception e){
                        SendMessage.sendCatch(mView.getContext(),e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<List<HinhAnh>> call, Throwable throwable) {
                    SendMessage.sendApiFail(mView.getContext(),throwable);
                }
            });
        }
    }
}