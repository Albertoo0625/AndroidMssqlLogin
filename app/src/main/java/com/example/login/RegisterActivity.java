package com.example.login;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText username, password, confirm_password;
    private TextView register_text;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.register_username);
        password = findViewById(R.id.register_password);
        confirm_password = findViewById(R.id.register_confirm_password);
        register = findViewById(R.id.register_button);
        register_text = findViewById(R.id.register_text);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty() && !confirm_password.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, password.getText().toString()+ confirm_password.getText().toString(), Toast.LENGTH_SHORT).show();
                    if (password.getText().toString().equals(confirm_password.getText().toString())) {
                        ConnectionHelper con = ConnectionHelper.getInstance();
                        con.postRegistration(username, password, confirm_password, RegisterActivity.this, register_text);
                        System.out.println("register sent");
                    } else {
                        Toast.makeText(RegisterActivity.this, "Password and Confirmed password don't match", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "All field must be filled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}