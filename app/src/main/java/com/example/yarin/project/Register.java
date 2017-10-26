package com.example.yarin.project;

import android.app.Activity;

import android.app.Dialog;
;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class Register extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private DataBaseHelper helper = new DataBaseHelper(this);
    private CheckBox termsButton;
    private Button registerButton;
    private Button cameraButton;
    private Button galleryButton;
    private Button cancelRegisterButton;
    private ImageView profilePicture;
    static final int TERMS_ACCEPTED_REQUEST = 1;
    static final int CAMERA_INTENT_REQUEST = 2;
    static final int GALLERY_INTENT_REQUEST = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Calendar currentDate = Calendar.getInstance();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        ((TextView)findViewById(R.id.showDate)).setText(dateFormat.format(currentDate.getTime()));
        termsButton = (CheckBox)findViewById((R.id.termsAndConditions));
        registerButton = (Button)findViewById(R.id.registerButton);
        cameraButton = (Button)findViewById(R.id.cameraButton);
        galleryButton = (Button)findViewById(R.id.galleryButton);
        profilePicture = (ImageView)findViewById(R.id.profilePicture);
        cancelRegisterButton = (Button)findViewById(R.id.registerCancelButton);
        cameraButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_INTENT_REQUEST);
            }
        });
        galleryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(galleryIntent,GALLERY_INTENT_REQUEST);
            }
        });
        TextView termsLink =(TextView)findViewById(R.id.termsLink);
        termsLink.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                GoToDisclimar();
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(registerButton.isPressed() && !(termsButton.isChecked())){
                    TermsMissingError();
                }
                else {
                    goToHome(v);
                }

            }
        });

        termsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(!(termsButton.isChecked())){
                    TermsMissingError();
                }
            }
        });
        termsButton.append(" ");
        cancelRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRegistration();
            }
        });

    }


    public void datePicker(View view){
        DatePickerFragment fragment = new DatePickerFragment();
            fragment.show(getSupportFragmentManager(),"date");
    }

    private  void setDate(final Calendar calendar){
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        ((TextView)findViewById(R.id.showDate)).setText(dateFormat.format(calendar.getTime()));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year,month,day);
        setDate(cal);

    }

    public void goToHome(View view){
        if (view.getId()== R.id.registerButton){
            EditText userName =(EditText)findViewById(R.id.userNameField);
            EditText password =(EditText)findViewById(R.id.passwordTextField);
            EditText confirmPassword =(EditText)findViewById(R.id.confirmPasswordTextField);
            TextView birthDay =(TextView)findViewById(R.id.showDate);
            EditText email =(EditText)findViewById(R.id.emailTextField);
            RadioButton maleButton =(RadioButton)findViewById(R.id.maleRadioButton);
            RadioButton femaleButton =(RadioButton)findViewById(R.id.femaleRadioButton);
            Spinner citySpinner =(Spinner)findViewById(R.id.citySpinner);
            EditText address =(EditText)findViewById(R.id.addressTextField);
            ImageView profilePicture=(ImageView)findViewById(R.id.profilePicture);

            String userNameStr = userName.getText().toString();
            String passwordStr = password.getText().toString();
            String confirmPasswordStr = confirmPassword.getText().toString();
            String birthDayStr = birthDay.getText().toString();
            String emailStr = email.getText().toString();
            String maleButtonStr = maleButton.getText().toString();
            String femaleButtonStr = femaleButton.getText().toString();
            String citySpinnerStr = citySpinner.toString();
            String addressStr = address.getText().toString();
            BitmapDrawable drawable = (BitmapDrawable) profilePicture.getDrawable();
            Bitmap bitmap = drawable.getBitmap();
            if(helper.isUserExists(userNameStr)==true){
                Toast userNameExistsError = Toast.makeText(Register.this,R.string.userNameExistsError,Toast.LENGTH_SHORT);
                userNameExistsError.show();
            }
            else if(!passwordStr.equals(confirmPasswordStr)){
                Toast passMatchError = Toast.makeText(Register.this,R.string.passNotMatch,Toast.LENGTH_SHORT);
                passMatchError.show();
            }
            else if(passwordStr.length() < 6){
                Toast passLengthError = Toast.makeText(Register.this,R.string.passLessThanSixChars,Toast.LENGTH_SHORT);
                passLengthError.show();
            }
            else{
                User u = new User();
                u.setUserName(userNameStr);
                u.setPassword(passwordStr);
                u.setBirthDay(birthDayStr);
                u.setEmail(emailStr);
                if (maleButton.isChecked()){
                    u.setGender(maleButtonStr);
                }
                else{
                    u.setGender(femaleButtonStr);
                }
                u.setCity(citySpinnerStr);
                u.setAddress(addressStr);
                u.setProfilePicture(bitmap);
                helper.insertUser(u);
                Intent homeIntent= new Intent(this,Home.class);
                homeIntent.putExtra("currentUser",userNameStr);
                startActivity(homeIntent);
            }

        }



    }

   public void TermsMissingError(){
       AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

       // Setting Dialog Title
       alertDialog.setTitle("");

       // Setting Dialog Message
       alertDialog.setMessage(R.string.termsCanceled);

       // Setting OK Button
       alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int which) {
       }
       });

       // Showing Alert Message
       alertDialog.show();

   }


   public void GoToDisclimar(){
       Intent disclimarIntent = new Intent(this,Terms.class);
       startActivityForResult(disclimarIntent,TERMS_ACCEPTED_REQUEST);
   }

   public void cancelRegistration(){
        Intent loginInternt = new Intent(this,Login.class);
        startActivity(loginInternt);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == TERMS_ACCEPTED_REQUEST) {
            if (resultCode == RESULT_OK) {
                termsButton.setChecked(true);
            } else {
                termsButton.setChecked(false);
                TermsMissingError();
            }
        }
        if(requestCode == CAMERA_INTENT_REQUEST){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            profilePicture.setImageBitmap(bitmap);
        }
        if(requestCode == GALLERY_INTENT_REQUEST){
            if(data !=null)
            {
                try{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(),data.getData());
                    profilePicture.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
          /*   Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            profilePicture.setImageBitmap(bitmap);*/
        }
    }


    public static class DatePickerFragment extends DialogFragment{
        public Dialog onCreateDialog(Bundle saveInstanceState){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener)getActivity(),year,month,day);
        }
    }
}
