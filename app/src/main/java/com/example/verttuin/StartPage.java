package com.example.verttuin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.verttuin.LoginPage.LoginPage;
import com.example.verttuin.RegisterPage.Registory;
import com.example.verttuin.databinding.ActivityMainBinding;
import com.example.verttuin.databinding.ActivityStartpageBinding;



public class StartPage extends AppCompatActivity {
    private ActivityStartpageBinding binding;

    Button Login_Button;
    Button Create_Button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartpageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Login_Button = binding.LoginBtn;
        Create_Button = binding.CreationBtn;



        Create_Button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartPage.this, Registory.class);
                    startActivity(intent);
            }
        });

        Login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartPage.this, LoginPage.class);
                startActivity(intent);
            }
        });
    }

}




