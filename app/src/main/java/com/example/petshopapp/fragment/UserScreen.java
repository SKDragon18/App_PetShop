package com.example.petshopapp.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.petshopapp.R;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.Const;
import com.example.petshopapp.api.apiservice.ChiNhanhService;
import com.example.petshopapp.api.apiservice.HinhAnhService;
import com.example.petshopapp.api.apiservice.KhachHangService;
import com.example.petshopapp.api.apiservice.TaiKhoanService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.ChiNhanh;
import com.example.petshopapp.model.KhachHang;
import com.example.petshopapp.model.TaiKhoan;
import com.example.petshopapp.tools.RealPathUtil;
import com.example.petshopapp.tools.TimeConvert;
import com.example.petshopapp.widget.CalendarDialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserScreen extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    //Đối tượng view
    private View mView;
    private EditText edtMaKH, edtGioiTinh, edtNgaySinh, edtDiaChi, edtHoTen, edtCCCD, edtEmail, edtSDT;
    private Button btnUpload, btnChangePassword, btnEdit, btnCalendar;
    private ImageView ivAvatar;
    private CalendarDialog calendarDialog;

    //API
    private KhachHangService khachHangService;
    private TaiKhoanService taiKhoanService;
    private HinhAnhService hinhAnhService;

    //Info
    private KhachHang khachHang;
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

    public UserScreen() {
    }

    public static UserScreen newInstance(String param1, String param2) {
        UserScreen fragment = new UserScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isVisible()){
            DocDLTaiKhoan();
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
        mView =  inflater.inflate(R.layout.fragment_user_screen, container, false);
        Retrofit retrofit = ApiClient.getClient();
        khachHangService =retrofit.create(KhachHangService.class);
        taiKhoanService=retrofit.create(TaiKhoanService.class);
        hinhAnhService=retrofit.create(HinhAnhService.class);
        setInit();
        setEvent();
        return mView;
    }

    private void setInit(){
        edtMaKH = mView.findViewById(R.id.edtMaKH);
        edtHoTen = mView.findViewById(R.id.edtHoTen);
        edtCCCD = mView.findViewById(R.id.edtCCCD);
        edtEmail=mView.findViewById(R.id.edtEmail);
        edtSDT=mView.findViewById(R.id.edtSDT);
        edtGioiTinh = mView.findViewById(R.id.edtGioiTinh);
        edtNgaySinh=mView.findViewById(R.id.edtNgaySinh);
        edtDiaChi=mView.findViewById(R.id.edtDiaChi);

        btnUpload=mView.findViewById(R.id.btnUpload);
        btnChangePassword=mView.findViewById(R.id.btnChangePassword);
        btnEdit = mView.findViewById(R.id.btnEdit);
        btnCalendar =mView.findViewById(R.id.btnCalendar);

        ivAvatar = mView.findViewById(R.id.ivAvatar);

        calendarDialog = new CalendarDialog();

        sharedPreferences = mView.getContext().getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE);
        username= sharedPreferences.getString("username","");
    }

    private void setEvent(){
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Mở lịch
                calendarDialog.open(mView.getContext(),edtNgaySinh);
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChangePasswordDialog(Gravity.CENTER);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!edtDiaChi.isEnabled()){
                    open();
                    return;
                }
                else{
                    khachHang.setDiaChi(edtDiaChi.getText().toString());
                    khachHang.setSoDienThoai(edtSDT.getText().toString());

                    if(calendarDialog.getDate()!=null)khachHang.setNgaySinh(calendarDialog.getDate());
                    khachHangService.update(khachHang).enqueue(new Callback<KhachHang>() {
                        @Override
                        public void onResponse(Call<KhachHang> call, Response<KhachHang> response) {
                            if(response.code()==200){
                                Toast.makeText(mView.getContext(),"Cập nhật thành công",Toast.LENGTH_SHORT).show();
                                DocDL();
                                close();
                            }
                            else{
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
                        public void onFailure(Call<KhachHang> call, Throwable throwable) {
                            SendMessage.sendApiFail(mView.getContext(),throwable);
                        }
                    });
                }
            }
        });
    }

    private void open(){
        edtDiaChi.setEnabled(true);
        edtSDT.setEnabled(true);
        btnCalendar.setEnabled(true);
        btnCalendar.setBackgroundResource(R.drawable.time_allow);
    }
    private void close(){
        edtDiaChi.setEnabled(false);
        edtSDT.setEnabled(false);
        btnCalendar.setEnabled(false);
        btnCalendar.setBackgroundResource(R.drawable.time_decline);
    }

    private String getString(String text){
        return text==null? "" :text;
    }

    private void updateEditText(){
        if(khachHang==null) {
            Toast.makeText(mView.getContext(), "KhachHang null", Toast.LENGTH_SHORT).show();
            return;
        }
        //update editText
        edtMaKH.setText(khachHang.getMaKhachHang());
        edtDiaChi.setText(khachHang.getDiaChi());
        edtHoTen.setText(getString(khachHang.getHo())+" "+getString(khachHang.getTen()));
        edtCCCD.setText(getString(khachHang.getCccd()));
        edtEmail.setText(getString(khachHang.getEmail()));
        edtSDT.setText(getString(khachHang.getSoDienThoai()));
        if(khachHang.getGioiTinh()){
            edtGioiTinh.setText("nam");
        }else{
            edtGioiTinh.setText("nữ");
        }
        try{
            edtNgaySinh.setText(TimeConvert.convertJavaDate(khachHang.getNgaySinh()));
        }
        catch(Exception e){
            SendMessage.sendCatch(mView.getContext(),e.getMessage());
        }

    }

    private void DocDL(){
        System.out.println("DocDLKhachHangID");
        khachHangService.getOneById(username).enqueue(new Callback<KhachHang>() {
            @Override
            public void onResponse(Call<KhachHang> call, Response<KhachHang> response) {
                if (response.code() == 200) {
                    khachHang = response.body();
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
            public void onFailure(Call<KhachHang> call, Throwable throwable) {
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
                        return;
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
                        System.out.println(e.getMessage());
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
                        return;
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





//    private void sendImage(String maKhachHang, String maKhachHang){
//        RequestBody requestBodyMaKhachHang = RequestBody.create(MediaType.parse("multipart/form-data"), maKhachHang);
//        RequestBody requestBodyMaKhachHang = RequestBody.create(MediaType.parse("multipart/form-data"), maKhachHang);
//
//        String imgRealPath = RealPathUtil.getRealPath(this.getContext(), mUri);
//        File file = new File(imgRealPath);
//        RequestBody requestBodyAvatar = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part multipartBodyAvatar = MultipartBody.Part.createFormData(Const.KEY_IMAGE, file.getName(), requestBodyAvatar);
//
//        hinhAnhService.saveImage(multipartBodyAvatar, requestBodyMaKhachHang, requestBodyMaKhachHang).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try{
//                    if(response.code() == 200){
//                        String result = response.body().string();
//                        Toast.makeText(mView.getContext(),result,Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        try {
//                            int code = response.code();
//                            String message = response.message();
//                            String error = response.errorBody().string();
//                            SendMessage.sendMessageFail(mView.getContext(),code,error,message);
//                        } catch (Exception e) {
//                            SendMessage.sendCatch(mView.getContext(),e.getMessage());
//                            return;
//                        }
//                    }
//                }
//                catch (Exception e){
//                    SendMessage.sendCatch(mView.getContext(),e.getMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
//                SendMessage.sendApiFail(mView.getContext(),throwable);
//            }
//        });
//    }
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


}