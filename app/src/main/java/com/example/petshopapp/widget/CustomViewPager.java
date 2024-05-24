package com.example.petshopapp.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {
    private boolean enable;//Trạng thái enable

    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.enable=true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(this.enable){
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){
        if(this.enable){
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    //Hàm thay đổi trạng thái cho phép di chuyển trang bằng cách luớt
    public void setPagingEnabled(boolean enable){
        this.enable=enable;
    }
}
