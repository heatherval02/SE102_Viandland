package com.example.viandland;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class MainActivity extends AppCompatActivity {

    TextView signUpLink;
    EditText usernameInputText, passwordInputText;
    Button loginBtn;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                                    Intent newIntent;

                                    newIntent = new Intent(MainActivity.this, MainpageDashboard.class);
                                    startActivity(newIntent);

                                    Toast.makeText(MainActivity.this, messageObj.getString("message"), Toast.LENGTH_SHORT).show();


                                } catch (JSONException e) {
                                    Toast.makeText(MainActivity.this, "Error on JSON : " + e, Toast.LENGTH_SHORT).show();
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
    });


    signUpLink = findViewById(R.id.signUpLink);
    signUpLink.setOnClickListener(new View.OnClickListener() {


        @Override
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, SignUpActivity.class));
        }
    });

    }
}