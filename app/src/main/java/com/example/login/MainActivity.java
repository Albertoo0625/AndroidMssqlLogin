package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText username,password;
    private TextView logintext;
    Button loginButton,signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username= (EditText) findViewById(R.id.login_username);
        password=(EditText) findViewById(R.id.login_password);
        loginButton=(Button) findViewById(R.id.login_button);
        logintext=(TextView) findViewById(R.id.login_text);
        signupButton=(Button) findViewById(R.id.signup_button);

        ConnectionHelper Conn= ConnectionHelper.getInstance();
        Conn.getText(MainActivity.this,logintext);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
                  ConnectionHelper con=ConnectionHelper.getInstance();
                  con.handleLogin(MainActivity.this,logintext,username,password);
              }else{
                  Toast.makeText(MainActivity.this, "Fill all required fields", Toast.LENGTH_SHORT).show();
              }
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Get signed up", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
      }
    };



