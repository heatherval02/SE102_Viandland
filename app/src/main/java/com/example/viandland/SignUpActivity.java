package com.example.viandland;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class SignUpActivity extends AppCompatActivity {

    EditText usernameInputText;
    Button nextBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        usernameInputText = findViewById(R.id.usernameInputText);
        nextBtn = findViewById(R.id.nextBtn);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        Constants.URL_REGISTER,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject usernameExistObj =new JSONObject(response);
                                    int available = Integer.parseInt(usernameExistObj.getString("message"));

                                    String usernamecheck = usernameInputText.getText().toString();

                                    if (usernamecheck.contains(" ")){
                                        Toast.makeText(SignUpActivity.this, "Space is not allowed", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (available == 0){

                                            Intent newIntent;
                                            newIntent = new Intent(SignUpActivity.this, SignUpInfoActivity.class);
                                            newIntent.putExtra("username", usernamecheck);
                                            startActivity(newIntent);

                                        } else {
                                            Toast.makeText(SignUpActivity.this, "The username is already taken, please select another username", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                } catch (JSONException e) {
                                    Toast.makeText(SignUpActivity.this, "Error JSON : Message -> " + e, Toast.LENGTH_SHORT).show();;
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

                        String username = usernameInputText.getText().toString();
                        Map<String, String> params = new HashMap<>();
                        params.put("username", username);
                        return params;
                    }
                };

                RequestHandler.getInstance(SignUpActivity.this).addToRequestQueue(stringRequest);

            }
        });
    }
}