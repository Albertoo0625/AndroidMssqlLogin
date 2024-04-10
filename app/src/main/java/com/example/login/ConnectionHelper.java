package com.example.login;
import static androidx.core.content.ContextCompat.startActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class ConnectionHelper{

    private static ConnectionHelper instance;
    private static String ip = "192.168.1.105";
    private static String port = "1433";
    private static String Classes = "net.sourceforge.jtds.jdbc.Driver";
    private static String database = "LoginProject";
    private static String username = "sa";
    private static String password = "albertoo";
    private static String url = "jdbc:jtds:sqlserver://"+ip+":"+port+"/"+database;

    Connection connection;

    public static synchronized ConnectionHelper getInstance() {
        if (instance == null) {
            instance = new ConnectionHelper();
        }
        return instance;
    }
    public Connection connectionclass(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            System.out.println("before connected");
            Class.forName(Classes);
            connection= DriverManager.getConnection(url, username,password);
            System.out.println("connected");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return  connection;
    }

        public void getText(Context context, TextView view){
        ConnectionHelper connectionHelp= ConnectionHelper.getInstance();
        connection = connectionHelp.connectionclass();
        try {
            if (connection != null) {
                String query = "select * from Users";
                Statement st = connection.createStatement();
                ResultSet rs = st.executeQuery(query);

                while(rs.next()){
                    Log.i("newEntry",rs.toString());
                    view.setText(rs.getString(2));
                }
            }
        } catch (SQLException e) {
            Log.e("error",e.getMessage().toString());
        }
    }


    public void handleLogin(Context context, TextView view,EditText username, EditText password){
       String usernameString=username.getText().toString();
        String passwordString=password.getText().toString();

        if(!usernameString.isEmpty() && !passwordString.isEmpty()) {
            try {
                ConnectionHelper connectionHelp = ConnectionHelper.getInstance();
                connection = connectionHelp.connectionclass();
                if (connection != null) {
                    String query = "select * from Users where user_username = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, usernameString);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        // Retrieve data from the result set
                        String retrievedUsername = resultSet.getString("user_username");
                        String retrievedPassword = resultSet.getString("user_password");

                        // Now you can use the retrieved data
                        System.out.println("Username: " + retrievedUsername);
                        System.out.println("Password: " + retrievedPassword);


                        BCrypt.Result result = BCrypt.verifyer().verify(passwordString.toCharArray(), retrievedPassword);
                        if (result.verified) {
                            // Passwords match, authentication successful
                            Toast.makeText(context, "Password matched.", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(context,HomeActivity.class);
                            intent.putExtra("UserName",retrievedUsername);
                            context.startActivity(intent);

                        } else {
                            // Passwords don't match, authentication failed
                            Toast.makeText(context, "Password doesn't match.", Toast.LENGTH_SHORT).show();
                        }




                        // Print other data as needed
                    } else {
                        Toast.makeText(context, "Username not found", Toast.LENGTH_SHORT).show();
                        view.setText("Login Failed");
                    }

                    // Close the ResultSet, PreparedStatement, and Connection
                    resultSet.close();
                    preparedStatement.close();
                    connection.close();
                }
            } catch (SQLException e) {
                Log.e("error", e.getMessage().toString());
            }
        }else{
            view.setText("Fill all required fields");
        }
    }

    public void postRegistration(EditText username, EditText password, EditText confirm_password, Context context, TextView textView){
        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();
        String confirmPasswordString = confirm_password.getText().toString();

        if(!usernameString.isEmpty() && !passwordString.isEmpty() && !confirmPasswordString.isEmpty()){
            if(passwordString.equals(confirmPasswordString)){
                try {
                    ConnectionHelper connectionHelp= ConnectionHelper.getInstance();
                    connection = connectionHelp.connectionclass();
                    if (connection != null) {
                        String bcryptHashString = BCrypt.withDefaults().hashToString(12, passwordString.toCharArray());
                        String query = "INSERT INTO Users (user_username, user_password) VALUES (?, ?)";
                        PreparedStatement preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, usernameString);
                        preparedStatement.setString(2, bcryptHashString);
                        int rowsInserted = preparedStatement.executeUpdate();
                        if (rowsInserted > 0) {
                            // Registration successful
                            // You might want to redirect user or show a success message
                            Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(context,MainActivity.class);
                            context.startActivity(intent);
                        } else {
                            // Registration failed
                            // You might want to show an error message
                            Toast.makeText(context, "user was not registered", Toast.LENGTH_SHORT).show();
                        }
                        preparedStatement.close();
                        connection.close();
                    }
                }
                catch(SQLException e){
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            } else {
                textView.setText("Password and Confirmation didn't match");
            }
        } else {
            textView.setText("Please fill in all fields");
        }
    }
}


