package com.example.viandland;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UploadRecipeActivity extends AppCompatActivity{

        int attempts = 0;
        Button btnChoose;
        Button saveButton;
        ImageView imageUpload;
        ImageView backBtn;

        Button previewButton;

        final int CODE_GALLERY_REQUEST = 999;
        Bitmap bitmap;

        private ProgressDialog progressDialog;

        String currentUniqId, currentTempId;
        EditText recipeName, recipePrepTime, recipeDescription;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_upload_recipe);

            recipeName = findViewById(R.id.recipeNameText);
            recipePrepTime = findViewById(R.id.recipeDurationText);
            recipeDescription = findViewById(R.id.recipeDescriptionText);

            btnChoose = findViewById(R.id.selectImageBtn);
            imageUpload = findViewById(R.id.recipeImage);
            saveButton = findViewById(R.id.saveBtn);

            previewButton = findViewById(R.id.previewBtn);
            previewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (recipeName.getText().toString().isEmpty() || recipePrepTime.getText().toString().isEmpty() ||recipeDescription.getText().toString().isEmpty()){
                        Toast.makeText(UploadRecipeActivity.this, "Please check some empty fields", Toast.LENGTH_SHORT).show();
                    } else if (imageUpload.getDrawable() == null){
                        Toast.makeText(UploadRecipeActivity.this, "Please select a thumbnail", Toast.LENGTH_SHORT).show();
                    }else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(UploadRecipeActivity.this);
                        ViewGroup viewGroup = findViewById(android.R.id.content);
                        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.dialog_preview_recipe, viewGroup, false);

                        Button okButton = dialogView.findViewById(R.id.okButton);
                        ImageButton closeButton = dialogView.findViewById(R.id.closeBtn);
                        TextView recipeNameText = dialogView.findViewById(R.id.recipeNameText);
                        TextView recipePrepTimeText = dialogView.findViewById(R.id.recipePrepText);
                        TextView recipeDescriptionText = dialogView.findViewById(R.id.recipeDescriptionText);
                        ImageView recipeImageView = dialogView.findViewById(R.id.imageView2);

                        Glide.with(dialogView)
                                .load(bitmap)
                                .into(recipeImageView);

                        recipeNameText.setText("Recipe name : " + recipeName.getText().toString());
                        recipePrepTimeText.setText("Estimated preperation : " + recipePrepTime.getText().toString());
                        recipeDescriptionText.setText("Recipe Description : " + recipeDescription.getText().toString());

                        builder.setView(dialogView);
                        AlertDialog alertDialog = builder.create();
                        closeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                        okButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });

                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        alertDialog.show();




                    }
                }
            });

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    if (recipeName.getText().toString().isEmpty() || recipePrepTime.getText().toString().isEmpty() ||recipeDescription.getText().toString().isEmpty()){
                        Toast.makeText(UploadRecipeActivity.this, "Please check some empty fields", Toast.LENGTH_SHORT).show();
                    } else if (imageUpload.getDrawable() == null){
                        Toast.makeText(UploadRecipeActivity.this, "Please select a thumbnail", Toast.LENGTH_SHORT).show();
                    }else {

                        String recipeNameText = recipeName.getText().toString();
                        String recipePrepTimeText = recipePrepTime.getText().toString();
                        String recipeDescriptionText = recipeDescription.getText().toString();

                        saveButton.setText("Continue");
                        if (attempts < 1){
                            saveData(recipeNameText, recipePrepTimeText, recipeDescriptionText);
                            uploadPhoto();
                            Toast.makeText(UploadRecipeActivity.this, "Uploading recipe to the database, please press Continue", Toast.LENGTH_LONG).show();
                            } else {
                            uploadPhoto();
                        }
                        attempts = attempts + 1;
                    }
                }
            });


            backBtn = findViewById(R.id.backBtn);
            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });


            btnChoose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(
                            UploadRecipeActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            CODE_GALLERY_REQUEST
                    );
                }
            });
            progressDialog = new ProgressDialog(UploadRecipeActivity.this);


        }

    private void uploadPhoto() {
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_UPLOADRECIPEIMAGE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);

                            addToRecipes(currentUniqId.trim());
                            Toast.makeText(UploadRecipeActivity.this, "Recipe Added Successfully", Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                String imageData = imageToString(bitmap);
                String uniq_id = currentUniqId.trim();

                params.put("image", imageData);
                params.put("uniq_id", uniq_id);
                return params;
            }
        };

        RequestHandler.getInstance(UploadRecipeActivity.this).addToRequestQueue(stringRequest);
    }

    private void addToRecipes(String trim) {
        progressDialog.dismiss();
            Intent newIntent = new Intent(UploadRecipeActivity.this, ActivityAddIngredients.class);
            newIntent.putExtra("temp_id",currentTempId);
            newIntent.putExtra("uniq_id", currentUniqId);
            // Intent newIntent = new Intent(MainActivity.this, ActivityAddIngredients.class);
            startActivity(newIntent);
            finish();
    }

    private void saveData(String recipeNameText, String recipePrepTimeText, String recipeDescriptionText) {

        progressDialog.setMessage("Adding recipe to the database");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_SAVERECIPEDATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject obj = new JSONObject(response);
                            currentUniqId = obj.getString("current_uniq_id");
                            currentTempId = obj.getString("temp_id");

                        } catch (JSONException e) {
                            Toast.makeText(UploadRecipeActivity.this, "Error on JSON : "+ e, Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("recipe_name", recipeNameText);
                params.put("recipe_preptime", recipePrepTimeText);
                params.put("recipe_description",  recipeDescriptionText);
                return params;
            }
        };

        RequestHandler.getInstance(UploadRecipeActivity.this).addToRequestQueue(stringRequest);
        }

    @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

            if (requestCode == CODE_GALLERY_REQUEST) {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Select image "), CODE_GALLERY_REQUEST);
                } else {
                    Toast.makeText(getApplicationContext(), "Permission not Granted", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }



        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            if (requestCode == CODE_GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
                Uri filePath = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(filePath);
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    imageUpload.setImageBitmap(bitmap);

                    Glide.with(UploadRecipeActivity.this)
                            .load(bitmap)
                            .into(imageUpload);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
            super.onActivityResult(requestCode, resultCode, data);
        }

        private String imageToString(Bitmap bitmap) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] imageBytes = outputStream.toByteArray();

            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;

        }

    }

