package com.yodel.todoapp.yodel;

/**
 * Created by NenoandLaura on 2/15/2015.
 */


import org.json.JSONException;
import org.json.JSONObject;

import com.yodel.todoapp.yodel.DatabaseHandler;
import com.yodel.todoapp.yodel.UserFunctions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends Activity {
    Button btnRegister;
    Button btnLinkToLogin;
    EditText inputFullName;
    EditText inputEmail;
    EditText inputPassword;
    TextView registerErrorMsg;
    Matcher matcher;

    // JSON Response node names
    private static String KEY_SUCCESS = "success";
    private static String KEY_ERROR = "error";
    private static String KEY_ERROR_MSG = "error_msg";
    private static String KEY_UID = "uid";
    private static String KEY_NAME = "name";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    public final static String EXTRA_MESSAGE = "User Name";
    public final static String EXTRA_MESSAGE2 = "User ID";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        // Importing all assets like buttons, text fields
        inputFullName = (EditText) findViewById(R.id.registerName);
        inputEmail = (EditText) findViewById(R.id.registerEmail);
        inputPassword = (EditText) findViewById(R.id.registerPassword);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);
        registerErrorMsg = (TextView) findViewById(R.id.register_error);

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString();
                String email = inputEmail.getText().toString();
                String password = inputPassword.getText().toString();

                    UserFunctions userFunction = new UserFunctions();
                    JSONObject json = userFunction.registerUser(name,email, password);


                // check for login response
                try {

                    if (json.getString(KEY_SUCCESS) != null) {
                        registerErrorMsg.setText("");
                        String res = json.getString(KEY_SUCCESS);
                        if(Integer.parseInt(res) == 1){
                            // user successfully registred
                            // Store user details in SQLite Database
                            DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                            JSONObject json_user = json.getJSONObject("user");
                            String uName = json_user.getString(KEY_NAME).toString();
                            String userId = json.getString(KEY_UID).toString();
                            // Clear all previous data in database
                            userFunction.logoutUser(getApplicationContext());
                            db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));
                            // Launch Dashboard Screen

                            Intent mainTodo = new Intent(getApplicationContext(), MainTodo.class);
                            Intent AllTodos =new Intent(getApplicationContext(),AllTodoActivity.class);
                            AllTodos.putExtra(EXTRA_MESSAGE2,userId);
                            mainTodo.putExtra(EXTRA_MESSAGE,uName );
                            mainTodo.putExtra(EXTRA_MESSAGE2, userId);


                            // Close all views before launching Dashboard
                            mainTodo.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainTodo);
                            // Close Registration Screen
                            finish();
                        }else{
                            // Error in registration
                            registerErrorMsg.setText("Error occured in registration");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                // Close Registration View
                finish();
            }
        });
    }
}