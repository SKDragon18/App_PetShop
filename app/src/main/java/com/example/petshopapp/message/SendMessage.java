package com.example.petshopapp.message;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.petshopapp.PetShopLogin;

public class SendMessage {
    //Gửi thông điệp phản hồi cho thấy yêu cầu thất bại
    public static void sendMessageFail(Context context, int code, String errorBody, String errorMessage){
        String codeResponse = String.valueOf(code);
        Log.e("ERROR_"+codeResponse,"Lỗi "+codeResponse+": " +errorMessage);
        //Có thông điệp trực tiếp thì hiển thị chi tiết hoặc không sẽ hiển thị lỗi chung
        if(errorBody!=null){
            //Với lỗi đã kiểm soát
            if(!errorBody.startsWith("{")){
                Toast.makeText(context,errorBody, Toast.LENGTH_SHORT).show();
                Log.e("ERROR_DETAIL","Lỗi chi tiết: " +errorBody);
                return;
            }
            //Với lỗi chưa kiểm soát
            else{
                Toast.makeText(context,"Lỗi "+codeResponse+": " +errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("ERROR_DETAIL","Lỗi chi tiết: " +errorBody);
            }
        }
        else{
            Toast.makeText(context,"Lỗi "+codeResponse+": " +errorMessage, Toast.LENGTH_SHORT).show();
        }
    }
    //In lỗi catch ra
    public static void sendCatch(Context context, String message){
        Log.e("Catch",message);
        Toast.makeText(context, "Catch: " +message, Toast.LENGTH_SHORT).show();
    }
    //Khi api fail
    public static void sendApiFail(Context context,Throwable throwable){
        Log.e("ERROR_API", "Call api fail: " + throwable.getMessage());
        Toast.makeText(context, "Call api fail: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
