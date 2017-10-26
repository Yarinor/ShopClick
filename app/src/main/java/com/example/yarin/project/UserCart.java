package com.example.yarin.project;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class UserCart extends Fragment {
    private ListView listView;
    private Map<String,Integer> productsMap = new LinkedHashMap<String, Integer>();
    private ArrayList<String> productPrices=new ArrayList<String>();
    private String userName;
    private TextView totalPrice;
    private TextView totalAmount;

    ArrayList<Product> allProducts;
    private DataBaseHelper helper;

    public UserCart() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_user_cart, container, false);
        totalPrice=(TextView)v.findViewById(R.id.totalPriceField);
        totalAmount=(TextView)v.findViewById(R.id.totalAmountField);
        listView =(ListView)v.findViewById(R.id.cartView);
        helper=new DataBaseHelper(this.getActivity());
        userName=this.getActivity().getIntent().getExtras().getString("currentUser");
        if(helper.isCartExists(userName)==false){
            createCart(userName,totalAmount,totalPrice);
        }

        allProducts=getAllProducts();
        for(Product product:allProducts){
            String productName=product.getProductName();
            String productPrice=product.getPrice();
            String productAmount=product.getAmount();
            int productAmountInt=Integer.parseInt(productAmount);
            if(!(productsMap.containsKey(productName))) {
                productsMap.put(productName, productAmountInt);
            }
            else{
                productsMap.put(productName,productsMap.get(productName)+1);
            }
            productPrices.add(productPrice);
        }
        ImageButton clearCartButton=(ImageButton) v.findViewById(R.id.clearCart);
        ListAdapter adapter = new ListAdapter(this.getActivity(),productsMap,productPrices,userName,totalPrice,totalAmount,clearCartButton);
        listView.setAdapter(adapter);

        clearCartButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                helper.restTotalProductAmount(Integer.parseInt(helper.getCartTotalAmount(userName)),userName);
                helper.restTotalProductPrice(Integer.parseInt(helper.getCartTotalPrice(userName)),userName);
                totalAmount.setText("0");
                totalPrice.setText("0");
            }
        });





            return v;

    }
    public ArrayList getAllProducts(){
        String usernameValue = this.getActivity().getIntent().getExtras().getString("currentUser");
        return helper.getProducts(usernameValue);
    }

    public void createCart(String owner,TextView totalAmount,TextView totalPrice){
        Cart cart = new Cart();
        cart.setOwner(owner);
        cart.setTotalAmount(totalAmount.getText().toString());
        cart.setTotalPrice(totalPrice.getText().toString());
        helper.insertCart(cart);
    }






}
