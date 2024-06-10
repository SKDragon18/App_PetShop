package com.example.petshopapp.factory;

import androidx.fragment.app.Fragment;

import com.example.petshopapp.R;
import com.example.petshopapp.fragment.BillCheckScreen;
import com.example.petshopapp.fragment.BillScreen;
import com.example.petshopapp.fragment.CartScreen;
import com.example.petshopapp.fragment.HomeScreen;
import com.example.petshopapp.fragment.ImportProductScreen;
import com.example.petshopapp.fragment.ManageFinanceScreen;
import com.example.petshopapp.fragment.ManageScreen;
import com.example.petshopapp.fragment.ManageEmployeeScreen;
import com.example.petshopapp.fragment.ManageUserScreen;
import com.example.petshopapp.fragment.UserScreen;
import com.example.petshopapp.fragment.UserScreen2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentFactoryCustom {

    //Phân quyền
    //khachhang-0
    //nhanvien-1
    //admin-2
    //quanly-3
    private static final String []ROLE = new String []{"khachhang","nhanvien","admin","quanly"};

    //Danh sách các thẻ bottom navigation theo role
    //Role customer
    private static final String []TAGROLECUSTOMER= new String[]{"home","cart","bill","user"};
    private static final Integer []IDROLECUSTOMER=new Integer[]{R.id.home_screen,R.id.cart_screen,R.id.bill_screen,R.id.user_screen};

    //Role employee
    private static final String []TAGROLEEMPLOYEE= new String[]{"home","product_import","bill_check","user2"};
    private static final Integer []IDROLEEMPLOYEE=new Integer[]{R.id.home_screen,R.id.product_import,R.id.bill_check,R.id.user_screen2};

    //Role manager
    private static final String []TAGROLEMANAGER= new String[]{"home","manage","manage_employee","manage_finance","user2"};
    private static final Integer []IDROLEMANAGER=new Integer[]{R.id.home_screen,R.id.manage, R.id.manage_employee,R.id.manage_finance,R.id.user_screen2};

    //Role admin
    private static final String []TAGROLEADMIN= new String[]{"home","manage_account","user2"};
    private static final Integer []IDROLEADMIN=new Integer[]{R.id.home_screen,R.id.manage_account,R.id.user_screen2};
    //Defind role methods
    public String getRole(int index){
        return ROLE[index];
    }
    public List getRoleList(){
        return new ArrayList<String>(Arrays.asList(ROLE));
    }
    public List getTagArray(String role){
        String []tagArray=null;
        if(role.equals(ROLE[0])){
            tagArray= TAGROLECUSTOMER;
        }
        else if(role.equals(ROLE[1])){
            tagArray= TAGROLEEMPLOYEE;
        }
        else if(role.equals(ROLE[2])){
            tagArray= TAGROLEADMIN;
        }
        else if(role.equals(ROLE[3])){
            tagArray=TAGROLEMANAGER;
        }
        if(tagArray==null)return null;
        return new ArrayList<String>(Arrays.asList(tagArray));
    }

    public List getIdArray(String role){
        Integer []idArray=null;
        if(role.equals(ROLE[0])){
            idArray= IDROLECUSTOMER;
        }
        else if(role.equals(ROLE[1])){
            idArray= IDROLEEMPLOYEE;
        }
        else if(role.equals(ROLE[2])){
            idArray= IDROLEADMIN;
        }
        else if(role.equals(ROLE[3])){
            idArray=IDROLEMANAGER;
        }
        if(idArray==null)return null;
        return new ArrayList<Integer>(Arrays.asList(idArray));
    }

    //Factory Method
    public Fragment createFragment(String fragmentName){
        switch (fragmentName){
            case "home":
                return new HomeScreen();
            case "cart":
                return new CartScreen();
            case "user":
                return new UserScreen();
            case "user2":
                return new UserScreen2();
            case "manage":
                return new ManageScreen();
            case "manage_account":
                return new ManageUserScreen();
            case "manage_employee":
                return new ManageEmployeeScreen();
            case "product_import":
                return new ImportProductScreen();
            case "manage_finance":
                return new ManageFinanceScreen();
            case "bill":
                return new BillScreen();
            case "bill_check":
                return new BillCheckScreen();
            default:
                return new Fragment();
        }

    }
}
