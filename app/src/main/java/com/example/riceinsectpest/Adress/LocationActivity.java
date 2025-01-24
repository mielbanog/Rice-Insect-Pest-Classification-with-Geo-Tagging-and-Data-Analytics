package com.example.riceinsectpest.Adress;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.riceinsectpest.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity {

    private AutoCompleteTextView autocompleteProvince, autocompleteMunicipality, autocompleteBarangay;
    private Button buttonSubmit;
    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);


        autocompleteProvince = findViewById(R.id.autocomplete_province);
        autocompleteMunicipality = findViewById(R.id.autocomplete_municipality);
        autocompleteBarangay = findViewById(R.id.autocomplete_barangay);
        buttonSubmit = findViewById(R.id.button_submit);

        requestQueue = Volley.newRequestQueue(this);

        setupAutocomplete(autocompleteProvince, "province");
        setupAutocomplete(autocompleteMunicipality, "municipality");
        setupAutocomplete(autocompleteBarangay, "barangay");

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String province = autocompleteProvince.getText().toString();
                String municipality = autocompleteMunicipality.getText().toString();
                String barangay = autocompleteBarangay.getText().toString();
                Toast.makeText(LocationActivity.this, "Address: " + province + ", " + municipality + ", " + barangay, Toast.LENGTH_LONG).show();
            }
        });
    }


    private void setupAutocomplete(final AutoCompleteTextView autoCompleteTextView, final String type) {
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1) {
                    fetchSuggestions(s.toString(), type, new VolleyCallback() {
                        @Override
                        public void onSuccess(List<String> suggestions) {
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(LocationActivity.this, android.R.layout.simple_dropdown_item_1line, suggestions);
                            autoCompleteTextView.setAdapter(adapter);
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private void fetchSuggestions(String query, String type, final VolleyCallback callback) {
        String url = "https://api.geoapify.com/v1/geocode/autocomplete?text=" + Uri.encode(query) + "&type=" + type + "&apiKey=YOUR_API_KEY";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray features = response.getJSONArray("features");
                    List<String> suggestions = new ArrayList<>();
                    for (int i = 0; i < features.length(); i++) {
                        JSONObject feature = features.getJSONObject(i);
                        String suggestion = feature.getJSONObject("properties").getString("formatted");
                        suggestions.add(suggestion);
                    }
                    callback.onSuccess(suggestions);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(request);
    }


    private interface VolleyCallback {
        void onSuccess(List<String> suggestions);
    }
}