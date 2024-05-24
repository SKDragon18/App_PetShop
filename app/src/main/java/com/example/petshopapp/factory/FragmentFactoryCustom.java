package com.example.petshopapp.factory;

import androidx.fragment.app.Fragment;

import com.example.petshopapp.R;
import com.example.petshopapp.fragment.CartScreen;
import com.example.petshopapp.fragment.HomeScreen;
import com.example.petshopapp.fragment.ManageScreen;
import com.example.petshopapp.fragment.UserScreen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentFactoryCustom {
    //Danh sách các thẻ bottom navigation theo role
    //Role customer
    private static final String []tagRoleCustomer= new String[]{"home","cart","user"};
    private static final Integer []idRoleCustomer=new Integer[]{R.id.home_screen,R.id.cart_screen,R.id.user_screen};

    //Role employee
    private static final String []tagRoleEmployee= new String[]{"home","manage","user"};
    private static final Integer []idRoleEmployee=new Integer[]{R.id.home_screen,R.id.manage,R.id.user_screen};

    //Role manager
    private static final String []tagRoleManager= new String[]{"home","manage","user"};
    private static final Integer []idRoleManager=new Integer[]{R.id.home_screen,R.id.manage,R.id.user_screen};

    //Role admin
    private static final String []tagRoleAdmin= new String[]{"home","manage_account","user"};
    private static final Integer []idRoleAdmin=new Integer[]{R.id.home_screen,R.id.manage_account,R.id.user_screen};
    //Defind role methods
    public List getTagArray(String role){
        String []tagArray=null;
        if(role.equals("customer")){
            tagArray= tagRoleCustomer;
        }
        else if(role.equals("employee")){
            tagArray= tagRoleEmployee;
        }
        else if(role.equals("admin")){
            tagArray= tagRoleAdmin;
        }
        else if(role.equals("manager")){
            tagArray=tagRoleManager;
        }
        if(tagArray==null)return null;
        return new ArrayList<String>(Arrays.asList(tagArray));
    }

    public List getIdArray(String role){
        Integer []idArray=null;
        if(role.equals("customer")){
            idArray= idRoleCustomer;
        }
        else if(role.equals("employee")){
            idArray= idRoleEmployee;
        }
        else if(role.equals("admin")){
            idArray= idRoleAdmin;
        }
        else if(role.equals("manager")){
            idArray=idRoleManager;
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
            case "manage":
                return new ManageScreen();
            case "manage_account":
                return new CartScreen();
            default:
                return new Fragment();
        }

    }
}
