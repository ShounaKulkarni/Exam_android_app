package com.example.lazybuoy.myexam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_EMAIL = "extraemail";
    EditText e1,e2;
    Button b1;
    LoginDbHelper db;
    public  String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new LoginDbHelper(this);

        e1=(EditText)findViewById(R.id.editmail);
        e2=(EditText)findViewById(R.id.editText1);
        b1=(Button) findViewById(R.id.button23);


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=e1.getText().toString();
                String password=e2.getText().toString();
                Boolean checkmail=db.emailpassword(email,password);
                if (checkmail==true) {
                    Toast.makeText(getApplicationContext(), "Successfully Login", Toast.LENGTH_SHORT).show();
                    Intent myintent = new Intent(LoginActivity.this,OnlineActivity.class);
                    myintent.putExtra(EXTRA_EMAIL,email);
                    startActivity(myintent);
                }
                else
                    Toast.makeText(getApplicationContext(),"Wrong email or password",Toast.LENGTH_SHORT).show();
            }
        });
    }
}