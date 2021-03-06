package com.example.viandland;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    TextView signUpLink;
    EditText usernameInputText, passwordInputText;
    Button loginBtn;

    SharedPrefManager userCredentials;

    boolean connected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
                    if (SharedPrefManager.getInstance(this).isLoggedIn()){
                        finish();
                        Intent newIntent = new Intent(MainActivity.this, MainpageDashboard.class);
                        // Intent newIntent = new Intent(MainActivity.this, ActivityAddIngredients.class);
                        startActivity(newIntent);
                    }


            connected = true;
        }
        else {

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            ViewGroup viewGroup = findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_connection_lost, viewGroup, false);

            Button closeButton = dialogView.findViewById(R.id.closeButton);

            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();

            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });


            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();

            connected = false;
        }



    usernameInputText = findViewById(R.id.usernameInputText);
    passwordInputText = findViewById(R.id.passwordInputText);
    loginBtn = findViewById(R.id.loginBtn);

    loginBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (usernameInputText.getText().toString().isEmpty()){
                Toast.makeText(MainActivity.this, "Please fill the Username field!", Toast.LENGTH_SHORT).show();
            } else if (passwordInputText.getText().toString().isEmpty()) {
                Toast.makeText(MainActivity.this, "Password field is Empty!", Toast.LENGTH_SHORT).show();
            } else{
                String username = usernameInputText.getText().toString();
                String password = passwordInputText.getText().toString();

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        Constants.URL_LOGUSER,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject messageObj = new JSONObject(response);
                                    if (messageObj.getString("message").equalsIgnoreCase("access granted")){
                                        validateUser(username, password);
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "Access denied please double check your credentials", Toast.LENGTH_SHORT).show();
                                    }

                                } catch (JSONException e) {
                                    Toast.makeText(MainActivity.this, "Kindly Check your Internet Connection", Toast.LENGTH_SHORT).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), "Kindly Check your Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        }

                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {

                        Map<String, String> params = new HashMap<>();
                        params.put("username", username);
                        params.put("password", password);


                        return params;
                    }
                };

                RequestHandler.getInstance(MainActivity.this).addToRequestQueue(stringRequest);


            }
        }
    });


    signUpLink = findViewById(R.id.signUpLink);
    signUpLink.setOnClickListener(new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, SignUpActivity.class));
        }
    });

    }

    private void validateUser(String username, String password) {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GETUSERDATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray responseArray = new JSONArray(response);

                            for (int i = 0; i < responseArray.length();i++){
                                JSONObject userObj = responseArray.getJSONObject(i);
                                userCredentials.getInstance(getApplicationContext()).userLogin(userObj.getInt("uid"), userObj.getString("username"), userObj.getString("email"), userObj.getString("fullname"), userObj.getString("profile_img"));
                                Intent newIntent = new Intent(MainActivity.this, MainpageDashboard.class);
                                startActivity(newIntent);
                            }

                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "Kindly Check your Internet Connection,", Toast.LENGTH_SHORT).show();
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
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        RequestHandler.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
    }
}