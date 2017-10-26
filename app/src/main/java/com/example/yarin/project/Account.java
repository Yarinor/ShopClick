package com.example.yarin.project;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;


public class Account extends Fragment {
    private ImageView profilePicture;
    private String userName;
    private DataBaseHelper helper;
    private String password;
    private Button saveButton;
    private Button cameraButton;
    private Button gallaryButton;
    private EditText currentPassword;
    private EditText newPassword;
    private EditText confirmNewPassword;
    private FragmentTransaction fragmentTransaction;
    private static final int CAMERA_INTENT_REQUEST=1;
    private static final int GALLERY_INTENT_REQUEST=2;



    public Account() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_account, container, false);
        helper=new DataBaseHelper(this.getActivity());
        userName=this.getActivity().getIntent().getExtras().getString("currentUser");
        profilePicture=(ImageView)v.findViewById(R.id.profilePictureMyAccount);
        profilePicture.setImageBitmap(helper.getImage(userName));
        password=helper.getPassword(userName);
        saveButton = (Button)v.findViewById(R.id.saveButtonAccount);
        cameraButton=(Button) v.findViewById(R.id.cameraButtonAccount);
        gallaryButton=(Button)v.findViewById(R.id.gallaryButtonAccount);
        currentPassword=(EditText)v.findViewById(R.id.currentPasswordFieldAccount);
        newPassword=(EditText)v.findViewById(R.id.newPasswordFieldAccount);
        confirmNewPassword=(EditText)v.findViewById(R.id.confirmNewPasswordFieldAccount);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPasswordStr = currentPassword.getText().toString();
                String newPasswordStr= newPassword.getText().toString();
                String newPasswordConfirmStr=confirmNewPassword.getText().toString();
                BitmapDrawable profilePictureDrawable = (BitmapDrawable) profilePicture.getDrawable();
                boolean checkPasswordResult= checkPassword(currentPasswordStr);
                boolean checkPasswordConfirmationResult=checkPasswordConfirmation(newPasswordStr,newPasswordConfirmStr);
                boolean checkPasswordLengthResult = checkPasswordLength(newPasswordStr);
                if(currentPasswordStr.equals("")&&newPasswordStr.equals("")&&newPasswordConfirmStr.equals("")){
                    helper.updateImage(profilePictureDrawable.getBitmap(),userName);
                    updateSucessfull();

                }
                else {
                    if (checkPasswordResult == false) {
                        passInccorectError();
                    } else if (checkPasswordConfirmationResult == false) {
                        passNotMatch();
                    } else if (checkPasswordLengthResult == false) {
                        passLessThanSixChars();
                    } else if (checkPasswordConfirmationResult == true && checkPasswordResult == true && checkPasswordLengthResult == true) {
                        helper.updatePassword(userName, newPasswordStr);
                        helper.updateImage(profilePictureDrawable.getBitmap(), userName);
                        currentPassword.setText("");
                        newPassword.setText("");
                        confirmNewPassword.setText("");
                        updateSucessfull();


                    }
                }
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_INTENT_REQUEST);
            }
        });
        gallaryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(galleryIntent,GALLERY_INTENT_REQUEST);
            }
        });

        return v;

    }

    public boolean checkPassword(String password){
        if(this.password.equals(password)){
            return true;
        }
        else{
            return false;
        }
    }
    public boolean checkPasswordConfirmation(String password,String confirmPassword){
        if(password.equals(confirmPassword)){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean checkPasswordLength(String password){
        if (password.length() >=6){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == CAMERA_INTENT_REQUEST){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            profilePicture.setImageBitmap(bitmap);
        }
        if(requestCode == GALLERY_INTENT_REQUEST){
            if(data !=null)
            {
                try{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),data.getData());
                    profilePicture.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void updateSucessfull(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("");

        // Setting Dialog Message
        alertDialog.setMessage(R.string.detailsUpdated);

        // Setting OK Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
        FragmentManager fragmentManager = ( this.getActivity()).getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_container,new Products());
        fragmentTransaction.commit();


    }
    public void passInccorectError(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("");

        // Setting Dialog Message
        alertDialog.setMessage(R.string.passIncorrentError);

        // Setting OK Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }
    public void passNotMatch(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("");

        // Setting Dialog Message
        alertDialog.setMessage(R.string.passNotMatch);

        // Setting OK Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }
    public void passLessThanSixChars(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("");

        // Setting Dialog Message
        alertDialog.setMessage(R.string.passLessThanSixChars);

        // Setting OK Button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();

    }



}
