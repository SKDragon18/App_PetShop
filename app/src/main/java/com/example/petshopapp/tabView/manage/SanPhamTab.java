package com.example.petshopapp.tabView.manage;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.petshopapp.PetShopRegister;
import com.example.petshopapp.R;
import com.example.petshopapp.adapter.SanPhamManageAdapter;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.Const;
import com.example.petshopapp.api.apiservice.ChiNhanhService;
import com.example.petshopapp.api.apiservice.HinhAnhService;
import com.example.petshopapp.api.apiservice.LoaiSanPhamService;
import com.example.petshopapp.api.apiservice.SanPhamService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.ChiNhanh;
import com.example.petshopapp.model.LoaiSanPham;
import com.example.petshopapp.model.LoaiThuCung;
import com.example.petshopapp.model.SanPham;
import com.example.petshopapp.tabView.manageEmployee.NhanVienTab;
import com.example.petshopapp.tools.RealPathUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
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

public class SanPhamTab extends Fragment {

    //View
    private View mView;

    //View
    private ListView lvSanPham;
    private Button btnThem;

    //API
    private SanPhamService sanPhamService;
    private ChiNhanhService chiNhanhService;
    private LoaiSanPhamService loaiSanPhamService;
    private HinhAnhService hinhAnhService;

    //Data
    private List<SanPham> data= new ArrayList<>();
    private Map<Integer,String> chiNhanhMap = new HashMap<>();
    private List<String> tenChiNhanhList;
    private List<LoaiSanPham> loaiSanPhamList = new ArrayList<>();
    private List<String> tenLoaiSanPham = new ArrayList<>();

    //Adapter
    private SanPhamManageAdapter sanPhamManageAdapter;
    private ArrayAdapter adapterDSLoaiSanPham;
    private ArrayAdapter adapterDSChiNhanh;

    //Image
    private static final int MY_REQUEST_CODE = 123;
    private ImageView ivAvatar;
    private Bitmap bitmap;
    private Uri mUri =null;
    private ActivityResultLauncher<Intent> mActivityResultLancher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.e(NhanVienTab.class.getName(),"onActivityResult");
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

    public SanPhamTab() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ApiClient apiClient = ApiClient.getApiClient();
        sanPhamService =apiClient.getRetrofit().create(SanPhamService.class);
        chiNhanhService = apiClient.getRetrofit().create(ChiNhanhService.class);
        loaiSanPhamService = apiClient.getRetrofit().create(LoaiSanPhamService.class);
        hinhAnhService = apiClient.getRetrofit().create(HinhAnhService.class);
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_san_pham_tab, container, false);
        setInit();
        setEvent();
        return mView;
    }
    @Override
    public void onResume() {
        super.onResume();
        if(isVisible()){
            DocDL();
            DocDLChiNhanh();
        }
    }

    public void setInit(){
        btnThem = mView.findViewById(R.id.btnThem);
        lvSanPham = mView.findViewById(R.id.lvSanPham);
    }

    public void setEvent(){
        if(chiNhanhMap.size()==0){
            DocDLChiNhanh();
        }
        if(tenLoaiSanPham.size()==0){
            DocDLLoaiSanPham();
        }

        sanPhamManageAdapter=new SanPhamManageAdapter(mView.getContext(),R.layout.item_sanpham_manage,
                data,chiNhanhMap, loaiSanPhamList, tenLoaiSanPham);
        lvSanPham.setAdapter(sanPhamManageAdapter);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDialog(Gravity.CENTER);
            }
        });
    }

    public void DocDL(){
        System.out.println("DocDLSanPham");
        sanPhamService.getAll().enqueue(new Callback<List<SanPham>>() {
            @Override
            public void onResponse(Call<List<SanPham>> call, Response<List<SanPham>> response) {
                if (response.code() == 200) {
                    data.clear();
                    for (SanPham x : response.body()) {
                        data.add(x);
                    }
                    sanPhamManageAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<List<SanPham>> call, Throwable throwable) {
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
                    tenChiNhanhList = new ArrayList<>(chiNhanhMap.values());
                    if(adapterDSChiNhanh!=null){
                        adapterDSChiNhanh.notifyDataSetChanged();
                    }
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
            public void onFailure(Call<List<ChiNhanh>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }
    public void DocDLLoaiSanPham(){
        System.out.println("DocDLLoaiSanPham");
        loaiSanPhamService.getAll().enqueue(new Callback<List<LoaiSanPham>>() {
            @Override
            public void onResponse(Call<List<LoaiSanPham>> call, Response<List<LoaiSanPham>> response) {
                if (response.code() == 200) {
                    loaiSanPhamList.clear();
                    tenLoaiSanPham.clear();
                    for (LoaiSanPham x : response.body()) {
                        loaiSanPhamList.add(x);
                        tenLoaiSanPham.add(x.getTenLoaiSanPham());
                    }
                    if(adapterDSLoaiSanPham!=null){
                        adapterDSLoaiSanPham.notifyDataSetChanged();
                    }
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
            public void onFailure(Call<List<LoaiSanPham>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }
    private BigDecimal convertBigDecimal(String text){
        if(text==null){
            SendMessage.sendCatch(mView.getContext(), "Text is null");
            return null;
        }
        try{
            if(text.isEmpty()){
                SendMessage.sendCatch(mView.getContext(), "Text is empty");
                return null;
            }
            return new BigDecimal(text);
        }
        catch (Exception e){
            SendMessage.sendCatch(mView.getContext(), e.getMessage());
        }
        return null;
    }

    private void openAddDialog(int gravity){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sanpham_add);

        Window window = dialog.getWindow();
        if(window == null)return;

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity=gravity;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        EditText edtTenSanPham = dialog.findViewById(R.id.edtTenSanPham);
        EditText edtGiaHienTai = dialog.findViewById(R.id.edtGiaHienTai);
        EditText edtSLTon = dialog.findViewById(R.id.edtSLTon);
        Spinner spLoaiSanPham = dialog.findViewById(R.id.spLoaiSanPham);
        Spinner spChiNhanh = dialog.findViewById(R.id.spChiNhanh);
        ivAvatar = dialog.findViewById(R.id.ivAvatar);

        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);


        if(chiNhanhMap==null||chiNhanhMap.size()==0)DocDLChiNhanh();
        if(tenLoaiSanPham==null||tenLoaiSanPham.size()==0)DocDLLoaiSanPham();

        adapterDSChiNhanh= new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, tenChiNhanhList);
        spChiNhanh.setAdapter(adapterDSChiNhanh);


        adapterDSLoaiSanPham= new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, tenLoaiSanPham);
        spLoaiSanPham.setAdapter(adapterDSLoaiSanPham);

        ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SanPham sanPham = new SanPham();
                String tenSanPham = edtTenSanPham.getText().toString();
                String giaHienTai = edtGiaHienTai.getText().toString();
                String soLuongTon = edtSLTon.getText().toString();
                String tenChiNhanh = spChiNhanh.getSelectedItem().toString();
                String tenLoaiSanPham = spLoaiSanPham.getSelectedItem().toString();

                if(tenSanPham.isEmpty()||giaHienTai.isEmpty()||soLuongTon.isEmpty()){
                    Toast.makeText(mView.getContext(),"Mời nhập đầy đủ thông tin",Toast.LENGTH_SHORT).show();
                    return;
                }

                sanPham.setTenSanPham(tenSanPham);
                sanPham.setGiaHienTai(convertBigDecimal(giaHienTai));
                sanPham.setSoLuongTon(Integer.parseInt(soLuongTon));
                for(Map.Entry<Integer,String> x:chiNhanhMap.entrySet()) {
                    if (x.getValue().equals(tenChiNhanh)) {
                        sanPham.setMaChiNhanh(x.getKey());
                        break;
                    }
                }
                for(LoaiSanPham x: loaiSanPhamList){
                    if(x.getTenLoaiSanPham().equals(tenLoaiSanPham)){
                        sanPham.setLoaiSanPham(x);
                        break;
                    }
                }
                System.out.println(sanPham.getMaChiNhanh());
                sanPhamService.insert(sanPham).enqueue(new Callback<SanPham>() {
                    @Override
                    public void onResponse(Call<SanPham> call, Response<SanPham> response) {
                        if(response.code()== 200){
                            SanPham sanPhamMoi = response.body();
                            if (mUri!=null){
                                sendImage("","","",String.valueOf(sanPhamMoi.getMaSanPham()));
                            }

                            DocDL();
                            Toast.makeText(mView.getContext(),"Thêm thành công",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
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
                    public void onFailure(Call<SanPham> call, Throwable throwable) {
                        SendMessage.sendApiFail(mView.getContext(),throwable);
                    }
                });
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MY_REQUEST_CODE){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                getGallery();
            }
        }
    }
    private void sendImage(String maNhanVien, String maKhachHang, String maThuCung, String maSanPham){
        RequestBody requestBodyMaNhanVien = RequestBody.create(MediaType.parse("multipart/form-data"), maNhanVien);
        RequestBody requestBodyMaKhachHang = RequestBody.create(MediaType.parse("multipart/form-data"), maKhachHang);
        RequestBody requestBodyMaThuCung = RequestBody.create(MediaType.parse("multipart/form-data"), maThuCung);
        RequestBody requestBodyMaSanPham = RequestBody.create(MediaType.parse("multipart/form-data"), maSanPham);

        String imgRealPath = RealPathUtil.getRealPath(this.getContext(), mUri);
        File file = new File(imgRealPath);
        RequestBody requestBodyAvatar = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part multipartBodyAvatar = MultipartBody.Part.createFormData(Const.KEY_IMAGE, file.getName(), requestBodyAvatar);

        hinhAnhService.saveImageCenter(multipartBodyAvatar,requestBodyMaNhanVien,requestBodyMaKhachHang, requestBodyMaThuCung, requestBodyMaSanPham).enqueue(new Callback<ResponseBody>() {
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
}