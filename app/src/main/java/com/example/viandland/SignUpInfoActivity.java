package com.example.viandland;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpInfoActivity extends AppCompatActivity {

    int emailCheck;
    EditText firstname, lastname, phone, email, password, confirmPassword;
    Button signUpBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_info);
        getSupportActionBar().hide();

        String username =  getIntent().getStringExtra("username");
        firstname = findViewById(R.id.firstname);
        lastname = findViewById(R.id.lastname);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmPassword = findViewById(R.id.confirmPassword);
        signUpBtn = findViewById(R.id.signUpBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstname.getText().toString().isEmpty() ){
                    Toast.makeText(SignUpInfoActivity.this, "Please input your firstname", Toast.LENGTH_SHORT).show();
                } else if (lastname.getText().toString().isEmpty()){
                    Toast.makeText(SignUpInfoActivity.this, "Please input your lastname", Toast.LENGTH_SHORT).show();
                } else if (phone.getText().toString().isEmpty()){
                    Toast.makeText(SignUpInfoActivity.this, "Please input your Phone number", Toast.LENGTH_SHORT).show();
                } else if(email.getText().toString().isEmpty()){
                    Toast.makeText(SignUpInfoActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                } else if (!isEmailValid(email.getText().toString())){
                    Toast.makeText(SignUpInfoActivity.this, "Email is invalid", Toast.LENGTH_SHORT).show();
                } else if(password.getText().toString().isEmpty()){
                    Toast.makeText(SignUpInfoActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                } else if ( confirmPassword.getText().toString().isEmpty()){
                    Toast.makeText(SignUpInfoActivity.this, "Please Re-type your Password", Toast.LENGTH_SHORT).show();
                } else if (!(password.getText().toString().equals(confirmPassword.getText().toString()))){
                    Toast.makeText(SignUpInfoActivity.this, "Password entered did not match", Toast.LENGTH_SHORT).show();
                    confirmPassword.setText("");
                } else if (emailExists(email.getText().toString())){
                    Toast.makeText(SignUpInfoActivity.this, "Email already registered, please choose a different email", Toast.LENGTH_SHORT).show();
                }
                else {
                    String userFirstname = firstname.getText().toString();
                    String userLastname = lastname.getText().toString();
                    String userEmail = email.getText().toString();
                    String userPhone =phone.getText().toString();
                    String userPassword = confirmPassword.getText().toString();
                    String userUsername = username;


                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                            Constants.URL_ADDUSER,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONObject messageObj = new JSONObject(response);

                                        Toast.makeText(SignUpInfoActivity.this, messageObj.getString("message"), Toast.LENGTH_SHORT).show();


                                    } catch (JSONException e) {
                                        Toast.makeText(SignUpInfoActivity.this, "Error on JSON : " + e, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }

                    ){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> params = new HashMap<>();
                            params.put("firstname", userFirstname );
                            params.put("lastname", userLastname);
                            params.put("phone", userEmail);
                            params.put("email", userPhone );
                            params.put("password", userPassword );
                            params.put("username", userUsername);

                            return params;
                        }
                    };

                    RequestHandler.getInstance(SignUpInfoActivity.this).addToRequestQueue(stringRequest);



                    Toast.makeText(SignUpInfoActivity.this, "User now Registered", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }

    public boolean emailExists(String email){
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_EMAILEXIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject emailExistObj =new JSONObject(response);
                            emailCheck = emailExistObj.getInt("message");

                        } catch (JSONException e) {
                            Toast.makeText(SignUpInfoActivity.this, "Error JSON : Message -> " + e, Toast.LENGTH_SHORT).show();;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", email);
                return params;
            }
        };

        RequestHandler.getInstance(SignUpInfoActivity.this).addToRequestQueue(stringRequest);

        if (emailCheck == 0){
            return true;
        } else {
            return false;
        }



    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}