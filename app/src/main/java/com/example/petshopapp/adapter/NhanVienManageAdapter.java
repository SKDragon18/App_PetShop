package com.example.petshopapp.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.petshopapp.R;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.Const;
import com.example.petshopapp.api.apiservice.ChiNhanhService;
import com.example.petshopapp.api.apiservice.HinhAnhService;
import com.example.petshopapp.api.apiservice.NhanVienService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.ChiNhanh;
import com.example.petshopapp.model.HinhAnh;
import com.example.petshopapp.model.NhanVien;
import com.example.petshopapp.model.NhanVien;
import com.example.petshopapp.tabView.manageEmployee.NhanVienTab;
import com.example.petshopapp.tools.ImageInteract;
import com.example.petshopapp.tools.RealPathUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NhanVienManageAdapter extends ArrayAdapter {
    Context context;
    int resource;

    //View
    View mView;
    TextView tvIdNhanVien,tvTenNhanVien;
    Button btnUpdate, btnDelete;

    //Api
    NhanVienService nhanVienService;
    ChiNhanhService chiNhanhService;
    HinhAnhService hinhAnhService;

    //Data
    List<NhanVien> data;
    List<ChiNhanh> chiNhanhList;
    List<String> tenChiNhanhList;

    //Adapter
    ArrayAdapter adapterDSChiNhanh;

    //Avatar
//    private static final int MY_REQUEST_CODE = 123;
//    ImageView ivAvatar;
//    Bitmap bitmap;
//    Uri mUri;
//    private ActivityResultLauncher<Intent> mActivityResultLancher = ((Activity)context).registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<ActivityResult>() {
//                @Override
//                public void onActivityResult(ActivityResult result) {
//                    Log.e(NhanVienTab.class.getName(),"onActivityResult");
//                    if(result.getResultCode() == Activity.RESULT_OK){
//                        Intent data = result.getData();
//                        if(data == null ){
//                            return;
//                        }
//                        Uri uri = data.getData();
//                        mUri=uri;
//                        try{
//                            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(),uri);
//                            ivAvatar.setImageBitmap(bitmap);
//                        } catch (FileNotFoundException e) {
//                            Log.e("FileNotFoundException", e.getMessage());
//                            Toast.makeText(getContext(),"FileNotFoundException" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            bitmap=null;
//                            ivAvatar.setImageResource(R.mipmap.ic_launcher);
//                        } catch (IOException e) {
//                            Log.e("IOException", e.getMessage());
//                            Toast.makeText(getContext(),"IOException" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                            bitmap=null;
//                            ivAvatar.setImageResource(R.mipmap.ic_launcher);
//                        }
//                    }
//                }
//            }
//    );

    public NhanVienManageAdapter(@NonNull Context context, int resource, List<NhanVien> data,
                                 List<ChiNhanh>chiNhanhList, List<String>tenChiNhanhList) {
        super(context, resource,data);
        this.context = context;
        this.resource = resource;
        this.data = data;
        this.chiNhanhList = chiNhanhList;
        this.tenChiNhanhList=tenChiNhanhList;
    }

    private void openUpdateDialog(int gravity, NhanVien nhanVien){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_nhanvien_update);

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

        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);
        EditText edtHo=dialog.findViewById(R.id.edtHo);
        EditText edtTen= dialog.findViewById(R.id.edtTen);
        EditText edtCCCD = dialog.findViewById(R.id.edtCCCD);
        EditText edtEmail=dialog.findViewById(R.id.edtEmail);
        EditText edtSDT= dialog.findViewById(R.id.edtSDT);
        EditText edtChucVu = dialog.findViewById(R.id.edtChucVu);
        EditText edtMaNhanVien=dialog.findViewById(R.id.edtMaNhanVien);
        Spinner spChiNhanh=dialog.findViewById(R.id.spChiNhanh);
        
        edtMaNhanVien.setText(nhanVien.getMaNhanVien());
        edtHo.setText(nhanVien.getHo());
        edtTen.setText(nhanVien.getTen());
        edtChucVu.setText(nhanVien.getChucVu());
        edtCCCD.setText(nhanVien.getCccd());
        edtEmail.setText(nhanVien.getEmail());
        edtSDT.setText(nhanVien.getSoDienThoai());

        adapterDSChiNhanh = new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, tenChiNhanhList);
        spChiNhanh.setAdapter(adapterDSChiNhanh);
        DocDLChiNhanh();
        final NhanVien nhanVienTemp=nhanVien;

        spChiNhanh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ten = spChiNhanh.getSelectedItem().toString();
                for(ChiNhanh x: chiNhanhList){
                    if(x.getTenChiNhanh().equals(ten)){
                        nhanVienTemp.setMaChiNhanh(x.getMaChiNhanh());
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spChiNhanh.setSelection(tenChiNhanhList.indexOf(chiNhanhList.get(nhanVienTemp.getMaChiNhanh()-1).getTenChiNhanh()));

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nhanVienTemp.setHo(edtHo.getText().toString());
                nhanVienTemp.setTen(edtTen.getText().toString());
                nhanVienTemp.setChucVu(edtChucVu.getText().toString());
                nhanVienTemp.setCccd(edtCCCD.getText().toString());
                nhanVienTemp.setEmail(edtEmail.getText().toString());
                nhanVienTemp.setSoDienThoai(edtSDT.getText().toString());
                nhanVienService.update(nhanVienTemp).enqueue(new Callback<NhanVien>() {
                    @Override
                    public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                        if(response.code() == 200) {
                            NhanVien nhanVienTemp = response.body();
                            notifyDataSetChanged();
                            Toast.makeText(mView.getContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
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
                    public void onFailure(Call<NhanVien> call, Throwable throwable) {
                        SendMessage.sendApiFail(mView.getContext(),throwable);
                    }
                });

                dialog.dismiss();
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

    private void openDeleteDialog(int gravity, NhanVien nhanVien){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_nhanvien_delete);

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

        Button btnConfirm = dialog.findViewById(R.id.btnConfirm);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);

        EditText edtMaNhanVien=dialog.findViewById(R.id.edtMaNhanVien);
        EditText edtHo=dialog.findViewById(R.id.edtHo);
        EditText edtTen= dialog.findViewById(R.id.edtTen);
        EditText edtCCCD = dialog.findViewById(R.id.edtCCCD);
        EditText edtEmail=dialog.findViewById(R.id.edtEmail);
        EditText edtSDT= dialog.findViewById(R.id.edtSDT);
        EditText edtChucVu = dialog.findViewById(R.id.edtChucVu);
        EditText edtChiNhanh = dialog.findViewById(R.id.edtChiNhanh);
        Spinner spChiNhanh=dialog.findViewById(R.id.spChiNhanh);

        edtMaNhanVien.setText(nhanVien.getMaNhanVien());
        edtHo.setText(nhanVien.getHo());
        edtTen.setText(nhanVien.getTen());
        edtChucVu.setText(nhanVien.getChucVu());
        edtCCCD.setText(nhanVien.getCccd());
        edtEmail.setText(nhanVien.getEmail());
        edtSDT.setText(nhanVien.getSoDienThoai());
        edtChiNhanh.setText(chiNhanhList.get(nhanVien.getMaChiNhanh()-1).getTenChiNhanh());

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nhanVienService.delete(nhanVien.getMaNhanVien()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try{
                            if(response.code() == 200){
                                String result = response.body().string();
                                data.remove(nhanVien);
                                notifyDataSetChanged();
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
                                    return;
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

                dialog.dismiss();
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

    public void DocDLChiNhanh(){
        chiNhanhService.getAll().enqueue(new Callback<List<ChiNhanh>>() {
            @Override
            public void onResponse(Call<List<ChiNhanh>> call, Response<List<ChiNhanh>> response) {
                if (response.code() == 200) {
                    chiNhanhList.clear();
                    tenChiNhanhList.clear();
                    for (ChiNhanh x : response.body()) {
                        chiNhanhList.add(x);
                        tenChiNhanhList.add(x.getTenChiNhanh());
                    }
                    adapterDSChiNhanh.notifyDataSetChanged();
                } else {
                    Toast.makeText(mView.getContext(), "Lỗi: " + String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ChiNhanh>> call, Throwable throwable) {
                Log.e("ERROR_API", "Call api fail: " + throwable.getMessage());
                Toast.makeText(mView.getContext(), "Call api fail: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void sendImage(String maNhanVien, String maKhachHang, String maThuCung, String maSanPham){
//        RequestBody requestBodyMaNhanVien = RequestBody.create(MediaType.parse("multipart/form-data"), maNhanVien);
//        RequestBody requestBodyMaKhachHang = RequestBody.create(MediaType.parse("multipart/form-data"), maKhachHang);
//        RequestBody requestBodyMaThuCung = RequestBody.create(MediaType.parse("multipart/form-data"), maThuCung);
//        RequestBody requestBodyMaSanPham = RequestBody.create(MediaType.parse("multipart/form-data"), maSanPham);
//
//        String imgRealPath = RealPathUtil.getRealPath(this.getContext(), mUri);
//        File file = new File(imgRealPath);
//        RequestBody requestBodyAvatar = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part multipartBodyAvatar = MultipartBody.Part.createFormData(Const.KEY_IMAGE, file.getName(), requestBodyAvatar);
//
//        hinhAnhService.saveImage(multipartBodyAvatar, requestBodyMaNhanVien, requestBodyMaKhachHang,
//                requestBodyMaThuCung, requestBodyMaSanPham).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                try{
//                    if(response.code() == 200){
//                        String result = response.body().string();
//                        Toast.makeText(mView.getContext(),result,Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        String message="Lỗi: "+String.valueOf(response.code())
//                                +"\n"+"Chi tiết: "+ response.errorBody().string();
//                        Log.e("ERROR","Call api fail: "+message);
//                    }
//                }
//                catch (Exception e){
//                    System.out.println(e.getMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
//                Log.e("ERROR_API","Call api fail: "+throwable.getMessage());
//                Toast.makeText(mView.getContext(),"Call api fail: "+throwable.getMessage(),Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void getImage(){
//        hinhAnhService.getImage(new long[]{13}).enqueue(new Callback<List<HinhAnh>>() {
//            @Override
//            public void onResponse(Call<List<HinhAnh>> call, Response<List<HinhAnh>> response) {
//                try{
//                    if(response.code() == 200){
//                        List<HinhAnh> list = response.body();
//                        String source = list.get(0).getSource();
//                        bitmap= ImageInteract.convertStringToBitmap(source);
////                        InputStream inputStream = response.body().byteStream();
////                        System.out.println(inputStream);
////                        List<Bitmap> bitmaps = new ArrayList<>();
////                        Bitmap bitmapTemp;
////                        while((bitmapTemp=BitmapFactory.decodeStream(inputStream))!=null){
////                            bitmaps.add(bitmapTemp);
////                        }
////                        System.out.println(bitmaps.size());
////                        bitmap=bitmaps.get(0);
////                        System.out.println(bitmap);
//                        if(bitmap == null){
//                            Toast.makeText(getContext(),"Bitmap null",Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        ivAvatar.setImageBitmap(bitmap);
//                    }
//                    else{
//                        String message="Lỗi: "+String.valueOf(response.code())
//                                +"\n"+"Chi tiết: "+ response.errorBody().string();
//                        Log.e("ERROR","Call api fail: "+message);
//                    }
//                }
//                catch (Exception e){
//                    System.out.println(e.getMessage());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<HinhAnh>> call, Throwable throwable) {
//                System.out.println(throwable.getMessage());
//            }
//        });
//    }
//
//    private void getGallery(){
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        mActivityResultLancher.launch(Intent.createChooser(intent, "Select picture"));
//    }
//
//    private void onClickRequestPermission(){
//        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.M){
//            getGallery();
//            return;
//        }
//        if(this.getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
//            getGallery();
//        }
//        else{
//            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
//            ((Activity)context).requestPermissions(permission,MY_REQUEST_CODE);
//        }
//    }
//
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        ((Activity) context).onRequestPermissionsResult(requestCode, permissions,grantResults);
//    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(resource, null);
        this.mView=convertView;

        TextView tvHoTen = convertView.findViewById(R.id.tvHoTen);
        TextView tvMaNhanVien = convertView.findViewById(R.id.tvMaNhanVien);
        TextView tvSDT= convertView.findViewById(R.id.tvSDT);
        TextView tvEmail=convertView.findViewById(R.id.tvEmail);
        Button btnUpdate = convertView.findViewById(R.id.btnUpdate);
        Button btnDelete=convertView.findViewById(R.id.btnDelete);

        NhanVien nhanVien = data.get(position);
        tvHoTen.setText(nhanVien.getHo()+" "+nhanVien.getTen());
        tvMaNhanVien.setText(nhanVien.getMaNhanVien());
        tvEmail.setText(nhanVien.getEmail());
        tvSDT.setText(nhanVien.getSoDienThoai());

        Retrofit retrofit = ApiClient.getClient();
        nhanVienService =retrofit.create(NhanVienService.class);
        chiNhanhService=retrofit.create(ChiNhanhService.class);
        hinhAnhService=retrofit.create(HinhAnhService.class);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openUpdateDialog(Gravity.CENTER, nhanVien);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDeleteDialog(Gravity.CENTER, nhanVien);
            }
        });

        return convertView;
    }
}
