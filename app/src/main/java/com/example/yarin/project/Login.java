package com.example.yarin.project;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private DataBaseHelper helper = new DataBaseHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView goRegisterText =(TextView)findViewById(R.id.goRegisterText);
        goRegisterText.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                goToRegistration(v);
            }
        });
    }

    public void goToRegistration(View view){
        Intent registerIntent= new Intent(this,Register.class);
        startActivity(registerIntent);
    }
    public void goToHome(View view){
        EditText userName = (EditText)findViewById(R.id.userNameLoginField);
        String UserNameStr =userName.getText().toString();
        EditText password = (EditText)findViewById(R.id.passwordLoginField);
        String passwordStr =password.getText().toString();

        String pass = helper.searchPass(UserNameStr);
        if(passwordStr.equals(pass)){
            Intent homeIntent= new Intent(this,Home.class);
            homeIntent.putExtra("currentUser",UserNameStr);
            startActivity(homeIntent);
        }
        else{
            Toast passError = Toast.makeText(Login.this,R.string.UserPassError,Toast.LENGTH_SHORT);
            passError.show();
        }



    }
}
