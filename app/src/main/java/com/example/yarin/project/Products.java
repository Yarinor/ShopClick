package com.example.yarin.project;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;



public class Products extends Fragment {

    private GridView gridView;
    private int icons[]= {R.drawable.milk,R.drawable.bananas,R.drawable.bread,R.drawable.butter, R.drawable.orange,R.drawable.pasta,
            R.drawable.strawberries,R.drawable.waterpack,R.drawable.yellow_cheese};
    private String productsNames[]={"Milk","Banana","bread","butter","orange","pasta","strawberries","waterpack","yellowcheese"};
    private String productsPrices[]={"5","10","10","5","6","10","8","10","15"};


 public Products(){


 }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_products, container, false);

        gridView =(GridView)v.findViewById(R.id.productsView);
        String userName=this.getActivity().getIntent().getExtras().getString("currentUser");
        GridAdapter adapter = new GridAdapter(this.getActivity(),icons,productsNames,productsPrices,userName);
        gridView.setAdapter(adapter);

        return v;


    }


}
