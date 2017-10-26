package com.example.yarin.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Yarin on 04/05/2017.
 */

public class GridAdapter extends BaseAdapter {

    private DataBaseHelper helper ;
    private int icons[];
    private String productsNames[];
    private String prices[];
    private Context context;
    private String userName;
    private LayoutInflater inflater;

    public GridAdapter(Context context,int icons[],String productsNames[],String prices[],String userName){
        this.context=context;
        this.icons=icons;
        this.productsNames=productsNames;
        this.prices=prices;
        this.userName=userName;


    }
    @Override
    public int getCount() {
        return this.productsNames.length;
    }

    @Override
    public Object getItem(int position) {
        return productsNames[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View gridView = convertView;
        helper=new DataBaseHelper(context);

        if(convertView == null){
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.custom_layout,null);



        }

        ImageView icon =(ImageButton) gridView.findViewById(R.id.productImage);
        final TextView productName=(TextView) gridView.findViewById(R.id.productName);
        final TextView productPrice=(TextView) gridView.findViewById(R.id.productPrice);
        final EditText productAmount = (EditText) gridView.findViewById(R.id.productAmount);


        icon.setImageResource(icons[position]);
        productName.setText(productsNames[position]);
        productPrice.setText(prices[position]);
        final String productIntialPrice= prices[position];


        icon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String productNameStr = productName.getText().toString();
                String productPriceStr = productPrice.getText().toString();
                String productAmountStr = productAmount.getText().toString();
                int productAmountInt = Integer.parseInt(productAmountStr);
                productAmountInt++;
                productAmountStr = Integer.toString(productAmountInt);
                helper.updateTotalProductAmount(Integer.parseInt(helper.getCartTotalAmount(userName)), 1, userName);
                helper.updateTotalProductPrice(Integer.parseInt(helper.getCartTotalPrice(userName)),Integer.parseInt(productIntialPrice), userName);

                if(helper.isProductExists(productNameStr,userName)){
                    helper.updateProductAmount(productNameStr,Integer.parseInt(helper.getProductAmount(productNameStr,userName)),1,userName);
                    helper.updateProductPrice(productNameStr,Integer.parseInt(helper.getProductCurrentPrice(productNameStr,userName)),Integer.parseInt(helper.getProductIntialPrice(productNameStr)),userName);
                }
                else {
                    Product product = new Product();
                    product.setProductName(productNameStr);
                    product.setPrice(productPriceStr);
                    product.setAmount(productAmountStr);
                    product.setOwner(userName);
                    product.setIntialPrice(productPriceStr);
                    helper.insertProduct(product);
                }

                String currentAmount = productAmount.getText().toString();
                int intAmount = Integer.parseInt(currentAmount);
                intAmount +=1;
                currentAmount = Integer.toString(intAmount);
                productAmount.setText(currentAmount);
                showToast(R.string.itemAdded);
            }
        });



        return gridView;
    }
    private void showToast(int msg) {
        Toast.makeText(this.context, msg, Toast.LENGTH_SHORT).show();
    }


}

