package com.example.lazybuoy.myexam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LogoutActivity extends AppCompatActivity {
private Button btnn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        btnn=findViewById(R.id.bttn);
        btnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LogoutActivity.this,MainActivity.class);
                startActivity(i);
            }
        });


    }
}
