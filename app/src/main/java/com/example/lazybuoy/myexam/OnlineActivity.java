package com.example.lazybuoy.myexam;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

public class OnlineActivity extends AppCompatActivity {


    public static final String EXTRA_MAIL = "extramail";
    public static final String EXTRA_CATEGORY_ID = "extraCategoryID";
    public static final String EXTRA_CATEGORY_NAME = "extraCategoryName";

    private Spinner spinnerCategory;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);

        Intent myintent = getIntent();
        email=myintent.getStringExtra(LoginActivity.EXTRA_EMAIL);

        TextView text = (TextView) findViewById(R.id.editmail);
        text.setText("Your Email:" +email);


        spinnerCategory = findViewById(R.id.spinner_category);



        loadCategories();



        Button buttonStartTest = findViewById(R.id.button_start_test);


        buttonStartTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startTest();
            }
        });
    }

    private void startTest()
    {
        Category selectedCategory = (Category) spinnerCategory.getSelectedItem();
        int categoryID = selectedCategory.getId();
        String categoryName = selectedCategory.getName();

        Intent intent = new Intent(OnlineActivity.this,ExamActivity.class);
        intent.putExtra(EXTRA_CATEGORY_ID, categoryID);
        intent.putExtra(EXTRA_CATEGORY_NAME, categoryName);
        intent.putExtra(EXTRA_MAIL,email);
        Log.i("Infoo", email);
        startActivity(intent);
    }

    private void loadCategories() {
        TestDbHelper dbHelper = TestDbHelper.getInstance(this);
        List<Category> categories = dbHelper.getAllCategories();

        ArrayAdapter<Category> adapterCategories = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, categories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapterCategories);


    }
}
