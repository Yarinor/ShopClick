package com.example.yarin.project;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class Home extends AppCompatActivity {
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    private FragmentTransaction fragmentTransaction;
    private DataBaseHelper helper = new DataBaseHelper(this);
    private NavigationView navigationView;
    private  Intent loginIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loginIntent= new Intent(this,Login.class);



        mDrawerlayout = (DrawerLayout)findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerlayout,R.string.open,R.string.close){


            public void onDrawerOpened(View drawerView) {
                String usernameValue = getIntent().getExtras().getString("currentUser");
                TextView usernamePlace = (TextView)findViewById(R.id.userNamePlace);
                usernamePlace.setText(usernameValue);
                ImageView profilePicture = (ImageView)findViewById(R.id.navProfilePicture);
                Bitmap dbBitMap = helper.getImage(usernamePlace.getText().toString());
                profilePicture.setImageBitmap(dbBitMap);

            }
        };

        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container,new Products());
        fragmentTransaction.commit();
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.nav_products:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container,new Products());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle(R.string.navProducts);
                        mDrawerlayout.closeDrawers();
                        break;
                    case R.id.nav_cart:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container,new UserCart());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle(R.string.navCart);
                        mDrawerlayout.closeDrawers();
                        break;
                    case R.id.nav_account:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container,new Account());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle(R.string.navAccount);
                        mDrawerlayout.closeDrawers();
                        break;
                    case R.id.nav_logout:
                        startActivity(loginIntent);
                        break;
                    case R.id.nav_exit:
                        finishAffinity();


                }
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




}
