package com.example.petshopapp.tabView.manageEmployee;

import static android.companion.CompanionDeviceManager.RESULT_OK;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.petshopapp.R;
import com.example.petshopapp.adapter.NhanVienManageAdapter;
import com.example.petshopapp.api.ApiClient;
import com.example.petshopapp.api.Const;
import com.example.petshopapp.api.apiservice.ChiNhanhService;
import com.example.petshopapp.api.apiservice.HinhAnhService;
import com.example.petshopapp.api.apiservice.NhanVienService;
import com.example.petshopapp.message.SendMessage;
import com.example.petshopapp.model.ChiNhanh;
import com.example.petshopapp.model.HinhAnh;
import com.example.petshopapp.model.LoaiThuCung;
import com.example.petshopapp.model.NhanVien;
import com.example.petshopapp.tools.ImageInteract;
import com.example.petshopapp.tools.MultipartParser;
import com.example.petshopapp.tools.RealPathUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Base64;
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

public class NhanVienTab extends Fragment {
    //Đối tượng view
    private View mView;
    private Button btnThem;
    private ListView lvNhanVien;

    //API
    private NhanVienService nhanVienService;
    private ChiNhanhService chiNhanhService;
    private HinhAnhService hinhAnhService;

    //Data
    private List<NhanVien> data = new ArrayList<>();
    private Map<Integer, String> chiNhanhMap = new HashMap<>();

    //Adapter
    private NhanVienManageAdapter nhanVienManageAdapter;
    private ArrayAdapter adapterDSChiNhanh = null;

    //Avatar
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


    public NhanVienTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void openAddDialog(int gravity){
        final Dialog dialog = new Dialog(mView.getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_nhanvien_add);

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

        Button btnUpload=dialog.findViewById(R.id.btnUpload);
        Button btnAdd = dialog.findViewById(R.id.btnAdd);
        Button btnCancel= dialog.findViewById(R.id.btnCancel);
        EditText edtMaNhanVien = dialog.findViewById(R.id.edtMaNhanVien);
        EditText edtHo=dialog.findViewById(R.id.edtHo);
        EditText edtTen= dialog.findViewById(R.id.edtTen);
        EditText edtCCCD = dialog.findViewById(R.id.edtCCCD);
        EditText edtEmail=dialog.findViewById(R.id.edtEmail);
        EditText edtSDT= dialog.findViewById(R.id.edtSDT);
        Spinner spChiNhanh=dialog.findViewById(R.id.spChiNhanh);
        ivAvatar = dialog.findViewById(R.id.ivAvatar);

        adapterDSChiNhanh = new ArrayAdapter<>(mView.getContext(), android.R.layout.simple_list_item_1, new ArrayList<>(chiNhanhMap.values()));
        spChiNhanh.setAdapter(adapterDSChiNhanh);
        DocDLChiNhanh();
        NhanVien nhanVien = new NhanVien();

        spChiNhanh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String ten = spChiNhanh.getSelectedItem().toString();
                for(Map.Entry<Integer,String> x:chiNhanhMap.entrySet()){
                    if(x.getValue().equals(ten)){
                        nhanVien.setMaChiNhanh(x.getKey());
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nhanVien.setMaNhanVien(edtMaNhanVien.getText().toString());
                nhanVien.setHo(edtHo.getText().toString());
                nhanVien.setTen(edtTen.getText().toString());
                nhanVien.setCccd(edtCCCD.getText().toString());
                nhanVien.setSoDienThoai(edtSDT.getText().toString());
                nhanVien.setEmail(edtEmail.getText().toString());
                nhanVien.setHinhAnh(null);
                nhanVienService.insert(nhanVien).enqueue(new Callback<NhanVien>() {
                    @Override
                    public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                        if(response.code()== 200){
                            NhanVien nhanVienMoi = response.body();
                            if (mUri!=null){
                                sendImage(nhanVienMoi.getMaNhanVien(), "" );
                            }

                            DocDL();
                            Toast.makeText(mView.getContext(),"Thêm thành công",Toast.LENGTH_SHORT).show();
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
    public void DocDLChiNhanh(){
        chiNhanhService.getAll().enqueue(new Callback<List<ChiNhanh>>() {
            @Override
            public void onResponse(Call<List<ChiNhanh>> call, Response<List<ChiNhanh>> response) {
                if (response.code() == 200) {
                    chiNhanhMap.clear();
                    for (ChiNhanh x : response.body()) {
                        chiNhanhMap.put(x.getMaChiNhanh(),x.getTenChiNhanh());
                    }
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
    public void DocDL(){
        System.out.println("DocDLNhanVien");
        nhanVienService.getAll().enqueue(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                if (response.code() == 200) {
                    data.clear();
                    for (NhanVien x : response.body()) {
                        data.add(x);
                    }
                    nhanVienManageAdapter.notifyDataSetChanged();
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
            public void onFailure(Call<List<NhanVien>> call, Throwable throwable) {
                SendMessage.sendApiFail(mView.getContext(),throwable);
            }
        });
    }

    public void setInit(){
        btnThem=mView.findViewById(R.id.btnThem);
        lvNhanVien=mView.findViewById(R.id.lvNhanVien);
    }

    public void setEvent(){
        nhanVienManageAdapter=new NhanVienManageAdapter(mView.getContext(),R.layout.item_nhanvien_manage,data, chiNhanhMap);
        lvNhanVien.setAdapter(nhanVienManageAdapter);
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddDialog(Gravity.CENTER);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isVisible()){
            DocDL();
            DocDLChiNhanh();
        }
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
        if(this.getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED){
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_nhan_vien_tab, container, false);

        ApiClient apiClient = ApiClient.getApiClient();
        nhanVienService =apiClient.getRetrofit().create(NhanVienService.class);
        chiNhanhService=apiClient.getRetrofit().create(ChiNhanhService.class);
        hinhAnhService=apiClient.getRetrofit().create(HinhAnhService.class);

        setInit();
        setEvent();
        return mView;
    }

}