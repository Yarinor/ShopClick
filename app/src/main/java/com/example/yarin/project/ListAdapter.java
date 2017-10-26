package com.example.yarin.project;


import android.content.Context;;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Yarin on 07/05/2017.
 */

public class ListAdapter extends BaseAdapter {


    private ArrayList<String> prices;
    private Map<String, Integer> productsMap = new LinkedHashMap<String, Integer>();
    private Context context;
    private LayoutInflater inflater;
    private DataBaseHelper helper;
    private String userName;
    private TextView totalPrice;
    private TextView totalAmount;
    private ListView listView;
    private ImageButton clearCartButton;
    private int position;
    private FragmentTransaction fragmentTransaction;


    public ListAdapter(Context context, Map<String, Integer> productsMap, ArrayList<String> prices, String userName, TextView totalPrice, TextView totalAmount, ImageButton clearCartButton) {
        this.context = context;
        this.productsMap = productsMap;
        this.prices = prices;
        this.userName = userName;
        this.totalPrice = totalPrice;
        this.totalAmount = totalAmount;
        helper = new DataBaseHelper(context);
        int intTotalAmount = Integer.parseInt(helper.getCartTotalAmount(this.userName));
        int intTotalPrice = Integer.parseInt(helper.getCartTotalPrice(this.userName));
        String totalAmountString = Integer.toString(intTotalAmount);
        String totalPriceString = Integer.toString(intTotalPrice);
        this.totalPrice.setText(totalPriceString);
        this.totalAmount.setText(totalAmountString);
        this.clearCartButton = clearCartButton;



    }

    @Override
    public int getCount() {
        return this.productsMap.size();
    }

    @Override
    public Object getItem(int position) {
        return productsMap.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listView = convertView;

        if (convertView == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listView = inflater.inflate(R.layout.custom_list_view, null);
        }
        this.position=position;
        final TextView productName = (TextView) listView.findViewById(R.id.cartProductName);
        final TextView productPrice = (TextView) listView.findViewById(R.id.cartProductPrice);
        final EditText productAmount = (EditText) listView.findViewById(R.id.cartProductAmount);
        Button addButton = (Button) listView.findViewById(R.id.cartAddProduct);
        Button decButton = (Button) listView.findViewById(R.id.cartDecProduct);
        Button removeButton = (Button) listView.findViewById(R.id.cartRemoveProduct);
        final String key = (new ArrayList<String>(productsMap.keySet())).get(position);
        int value = (new ArrayList<Integer>(productsMap.values())).get(position);
        String strValue = Integer.toString(value);
        productName.setText(key);
        productAmount.setText(strValue);
        productPrice.setText(prices.get(position));
        this.listView = (ListView) parent;


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentAmount = productAmount.getText().toString();
                int intAmount = Integer.parseInt(currentAmount);
                helper.updateProductAmount(productName.getText().toString(), intAmount, 1, userName);
                intAmount += 1;
                currentAmount = Integer.toString(intAmount);
                productAmount.setText(currentAmount);
                updatePrice(productPrice, productName, Integer.parseInt(helper.getProductIntialPrice(productName.getText().toString())));
                helper.updateTotalProductAmount(Integer.parseInt(helper.getCartTotalAmount(userName)), 1, userName);
                updateTotal();

            }
        });
        decButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentAmount = productAmount.getText().toString();
                int intAmount = Integer.parseInt(currentAmount);
                if (intAmount == 1) {
                    removeProduct(key, productName, userName);
                    updateTotal();

                } else {
                    helper.updateProductAmount(productName.getText().toString(), intAmount, -1, userName);
                    updatePrice(productPrice, productName, -(Integer.parseInt(helper.getProductIntialPrice(productName.getText().toString()))));
                    helper.updateTotalProductAmount(Integer.parseInt(helper.getCartTotalAmount(userName)), -1, userName);
                    updateTotal();
                }
                intAmount -= 1;
                currentAmount = Integer.toString(intAmount);
                productAmount.setText(currentAmount);


            }
        });
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeProduct(key, productName, userName);
                updateTotal();


            }
        });

        this.clearCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAllProducts();
            }
        });

        return listView;
    }


    private void updatePrice(TextView productPrice, TextView productName, int intialPrice) {

        String currentPrice = productPrice.getText().toString();
        int intPrice = Integer.parseInt(currentPrice);
        helper.updateProductPrice(productName.getText().toString(), intPrice, intialPrice, userName);
        intPrice += intialPrice;
        currentPrice = Integer.toString(intPrice);
        productPrice.setText(currentPrice);
        helper.updateTotalProductPrice(Integer.parseInt(helper.getCartTotalPrice(userName)), intialPrice, userName);
    }

    public void removeProduct(String mapKey, TextView productName, String owner) {
        helper.updateTotalProductAmount(Integer.parseInt(helper.getCartTotalAmount(userName))
                ,-(Integer.parseInt(helper.getProductAmount(productName.getText().toString(),userName))),userName);
        helper.updateTotalProductPrice(Integer.parseInt(helper.getCartTotalPrice(userName))
                ,-(Integer.parseInt(helper.getProductCurrentPrice(productName.getText().toString(),userName))),userName);
        helper.RemoveProduct(productName.getText().toString(), owner);
        productsMap.remove(mapKey);
        notifyDataSetChanged();
        refreshProductList();



    }

    public void updateTotal() {
        int totalAmount = Integer.parseInt(helper.getCartTotalAmount(userName));
        int totalPrice = Integer.parseInt(helper.getCartTotalPrice(userName));
        String totalAmountString = Integer.toString(totalAmount);
        String totalPriceString = Integer.toString(totalPrice);
        this.totalPrice.setText(totalPriceString);
        this.totalAmount.setText(totalAmountString);

    }

    public void removeAllProducts() {
        int itemsCount = this.listView.getChildCount();
        for (int i = 0; i < itemsCount; i++) {
            View view = listView.getChildAt(i);
            TextView productName = (TextView) view.findViewById(R.id.cartProductName);
            removeProduct(productName.getText().toString(),productName,userName);
            helper.restTotalProductAmount(Integer.parseInt(helper.getCartTotalAmount(userName)),userName);
            helper.restTotalProductPrice(Integer.parseInt(helper.getCartTotalPrice(userName)),userName);
            totalAmount.setText("0");
            totalPrice.setText("0");
        }
    }

    public void refreshProductList(){
        /*int itemsCount=this.listView.getChildCount();
        for(int i=0;i<itemsCount;i++){
            View view = listView.getChildAt(i);
            TextView productName=(TextView)view.findViewById(R.id.cartProductName);
            TextView productPrice =(TextView)view.findViewById(R.id.cartProductPrice);
            TextView productAmount=(TextView)view.findViewById(R.id.cartProductAmount);
            String productPriceStr=helper.getProductCurrentPrice(productName.getText().toString(),userName);
            String productAmountStr=helper.getProductAmount(productName.getText().toString(),userName);
            productPrice.setText(productPriceStr);
            productAmount.setText(productAmountStr);*/
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_container,new UserCart());
            fragmentTransaction.commit();

        }
    }


