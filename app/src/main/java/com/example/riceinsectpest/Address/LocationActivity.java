package com.example.riceinsectpest.Address;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.riceinsectpest.Dashboard.DashboardActivity;
import com.example.riceinsectpest.MainActivity;
import com.example.riceinsectpest.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends AppCompatActivity {

    private Spinner spinnerRegion, spinnerProvince, spinnerCity, spinnerBarangay;
    private Button buttonSubmit;
    private RequestQueue requestQueue;
    private List<String> regionCodes = new ArrayList<>();
    private List<String> provinceCodes = new ArrayList<>();
    private List<String> cityCodes = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        init();
        loadRegions();

        buttonSubmit = findViewById(R.id.button_submit);

        requestQueue = Volley.newRequestQueue(this);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected_regionCode = regionCodes.get(position);
                        loadProvinces(selected_regionCode);
                Log.d("Location", "Selected Region Code: " + selected_regionCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected_provinceCode = provinceCodes.get(position);
                        loadCities(selected_provinceCode);
                Log.d("PROVINCE: ","SelectedProv" +selected_provinceCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected_cityCode = cityCodes.get(position);
                        loadBarangays(selected_cityCode);
                        Log.d("LocationActivity","Selected :"+selected_cityCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



    }
    public void init(){
        spinnerRegion = findViewById(R.id.spinner_region);
        spinnerProvince = findViewById(R.id.spinner_province);
        spinnerCity = findViewById(R.id.spinner_city);
        spinnerBarangay = findViewById(R.id.spinner_barangay);

    }
    private void loadRegions() {
        String url = "https://psgc.gitlab.io/api/regions/";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<String> regions = new ArrayList<>();
                            List<String> regionCodes = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject region = response.getJSONObject(i);
                                String regionName = region.getString("regionName");
                                String regionCode = region.getString("code");
                                regions.add(regionName);
                                regionCodes.add(regionCode);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    LocationActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    regions
                            );
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerRegion.setAdapter(adapter);

                            spinnerRegion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String regionCode = regionCodes.get(position);
                                   // Toast.makeText(LocationActivity.this, "Selected Region Code: " + regionCode, Toast.LENGTH_LONG).show();
                                    Log.d("AddressSelector", "Selected Region Code: " + regionCode);
                                    loadProvinces(regionCode);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }


    private void loadProvinces(String regionCode) {
        String url = "https://psgc.gitlab.io/api/regions/" + regionCode + "/provinces";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<String> provinces = new ArrayList<>();
                            List<String> provinceCodes = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject province = response.getJSONObject(i);
                                String provinceName = province.getString("name");
                                String provinceCode = province.getString("code");
                                provinces.add(provinceName);
                                provinceCodes.add(provinceCode);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    LocationActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    provinces
                            );
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerProvince.setAdapter(adapter);

                            spinnerProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String provinceCode = provinceCodes.get(position);
                                    //Toast.makeText(LocationActivity.this, "Selected Province Code: " + provinceCode, Toast.LENGTH_LONG).show();
                                    Log.d("AddressSelector", "Selected Province Code: " + provinceCode);
                                    loadCities(provinceCode);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }


    private void loadCities(String provinceCode) {
        String url = "https://psgc.gitlab.io/api/provinces/" + provinceCode + "/cities";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<String> cities = new ArrayList<>();
                            List<String> cityCodes = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject city = response.getJSONObject(i);
                                String cityName = city.getString("name");
                                String cityCode = city.getString("code");
                                cities.add(cityName);
                                cityCodes.add(cityCode);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    LocationActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    cities
                            );
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerCity.setAdapter(adapter);

                            spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    String cityCode = cityCodes.get(position);
                                    Toast.makeText(LocationActivity.this, "Selected City Code: " + cityCode, Toast.LENGTH_LONG).show();
                                    Log.d("AddressSelector", "Selected City Code: " + cityCode);
                                    loadBarangays(cityCode);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }



    private void loadBarangays(String cityCode) {
        String url = "https://psgc.gitlab.io/api/cities/" + cityCode + "/barangays";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            List<String> barangays = new ArrayList<>();
                            List<String> barangayCodes = new ArrayList<>();

                            for (int i = 0; i < response.length(); i++) {
                                JSONObject barangay = response.getJSONObject(i);
                                String barangayName = barangay.getString("name");
                                String barangayCode = barangay.getString("code");
                                barangays.add(barangayName);
                                barangayCodes.add(barangayCode);
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                    LocationActivity.this,
                                    android.R.layout.simple_spinner_item,
                                    barangays
                            );
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerBarangay.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LocationActivity.this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_SINGLE_TOP |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }


}