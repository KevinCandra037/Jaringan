package com.example.jaringan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.jaringan.databinding.ActivityDetailBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    String meal, photo, instruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent i = getIntent();
        String id = i.getStringExtra("i_idMeal");

        load(id);
    }

    private void load(String id){
        binding.progressBar.setVisibility(ProgressBar.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=" + id;

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("meals");
                            JSONObject data = jsonArray.getJSONObject(0);

                            meal = data.getString("strMeal");
                            instruction = data.getString("strInstruction");
                            photo = data.getString("strMealThumb");
                            Log.i("TAG", "onCreate: "+ id + jsonArray);
                            Glide.with(getApplicationContext())
                                    .load(photo)
                                    .into(binding.ivImage);

                            binding.tvName.setText(meal);
                            binding.tvInstruction.setText(instruction);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            binding.progressBar.setVisibility(ProgressBar.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Events: ", error.toString());
                Toast.makeText(getApplicationContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsObjRequest);
    }
}