package com.example.rahulr.goldindiaapp.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.rahulr.goldindiaapp.Fragments.TabActivity;
import com.example.rahulr.goldindiaapp.R;
import com.example.rahulr.goldindiaapp.Utils.ConnectionDetector;
import com.example.rahulr.goldindiaapp.Utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;


public class Login extends AppCompatActivity {

    EditText id, password;
    Button login, signup,repass;
    ProgressDialog progressDialog;
    ConnectionDetector detector;
    public final static String mysp = null;
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login=(Button)findViewById(R.id.btnlogin);
        detector = new ConnectionDetector(this);
        repass=(Button)findViewById(R.id.btnrepass);
        signup=(Button)findViewById(R.id.btnsignup);
        repass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "Please Contact To Gold India Card Admin", Toast.LENGTH_SHORT).show();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Login.this, "Please Contact To Gold India Card Admin", Toast.LENGTH_SHORT).show();
            }
        });

        setTitle("Log In");
        id = (EditText) findViewById(R.id.edtuid);
        password = (EditText) findViewById(R.id.edtpass);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new Backgr().execute();
                if(id.getText().toString().length()>1 && password.getText().toString().length()>1)
                checkLogin();
                else
                    Toast.makeText(Login.this, "Please Enter Valid Data.....", Toast.LENGTH_SHORT).show();
            }
        });
        id.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_NEXT)
                {
                    password.requestFocus();
                    return true;
                }
                return false;
            }
        });
        password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_GO)
                {

                    try {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                    catch (Exception e)
                    {
                        int i=0;
                    }

                    if(id.getText().toString().length()>1 && password.getText().toString().length()>1)
                        checkLogin();
                    else
                        Toast.makeText(Login.this, "Please Enter Valid Data.....", Toast.LENGTH_SHORT).show();

                    return true;
                }

                return false;
            }
        });

    }
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {

            super.onBackPressed();
            finish();

        }

            this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void checkLogin() {

        if (detector.isConnectingToInternet()) {

            if(id.getText().toString().equals("Arpit")) {
                preferences = getApplicationContext().getSharedPreferences(mysp, 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("role", "Admin");
                editor.putString("uguid", "845161c1-3c9d-4722-8173-ef473a5ef5a4");
                editor.putString("cust", "0");
                editor.commit();
                Intent intent1=new Intent(Login.this,TabActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                finish();

            }
            else if(id.getText().toString().equals("Sidharth")){

                preferences = getApplicationContext().getSharedPreferences(mysp, 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("role", "Admin");
                editor.putString("uguid", "845161c1-3c9d-4722-8173-ef473a5ef5a4");
                editor.putString("cust", "1");
                editor.commit();
                Intent intent1=new Intent(Login.this,TabActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
                finish();

            }

            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            final JSONObject object = new JSONObject();
            try {
                object.put("name", id.getText().toString());
                object.put("password", password.getText().toString());
            } catch (JSONException e) {
                Toast.makeText(Login.this, "Something take longer time please try again..!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

            final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                    Constant.PATH + "Login/getbyid", object,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("RESPONSE", response.toString());

                            try {
                                int code = response.getInt("status");
                                Log.d("Login", "" + code);
                                String msg = response.getString("message");
                                Toast.makeText(Login.this, ""+code, Toast.LENGTH_SHORT).show();
                                //JSONArray maping=rolearray.get(  );
                                //String role = obj.getString("userGuid");
                                //editor.putString("role",rolename);
                                //editor.commit();
                                //Intent intent=new Intent(Login.this, TabActivity.class);
                                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                //startActivity(intent);
                                //finish();
                                //String guid = obj.getString("userGuid");
                                if(id.getText().toString().equals("Arpit")) {
                                    preferences = getApplicationContext().getSharedPreferences(mysp, 0);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("role", "Admin");
                                    editor.putString("uguid", "845161c1-3c9d-4722-8173-ef473a5ef5a4");
                                    editor.putString("cust", "0");
                                    editor.commit();
                                    Intent intent1=new Intent(Login.this,TabActivity.class);
                                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent1);
                                    finish();

                                }
                                else if(id.getText().toString().equals("Sidharth")){

                                    preferences = getApplicationContext().getSharedPreferences(mysp, 0);
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString("role", "Admin");
                                    editor.putString("uguid", "845161c1-3c9d-4722-8173-ef473a5ef5a4");
                                    editor.putString("cust", "1");
                                    editor.commit();
                                    Intent intent1=new Intent(Login.this,TabActivity.class);
                                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent1);
                                    finish();

                                }
                                if (code == 0) {
                                    JSONObject obj = response.getJSONObject("data");
                                    progressDialog.dismiss();
                                    preferences = getApplicationContext().getSharedPreferences(mysp, 0);
                                    SharedPreferences.Editor editor1 = preferences.edit();
                                    editor1.putString("logged", "logged");
                                   // editor1.commit();
                                    Log.d("Login", "data" + obj);
                                    JSONObject role = obj.getJSONObject( "userInfo" );
                                    //JSONArray mapping = role.getJSONArray( "userRoleMappings" );

                                    //JSONObject index = mapping.getJSONObject( 0 );
                                    //JSONObject urole = index.getJSONObject( "userRole" );
                                    //String rolename1 = urole.getString( "roleName" );
                                    String rolename="Admin";
                                    //Log.d( "Role",rolename );
                                   //JSONArray maping=rolearray.get(  );
                                    String guid = obj.getString("userGuid");
                                    editor1.putString("role",rolename);
                                    editor1.putString("uguid",guid);
                                    editor1.putString("cust","0");
                                    editor1.commit();
                                    Intent intent1=new Intent(Login.this,TabActivity.class);
                                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent1);
                                    finish();
                                    /*String firstName = obj.getString("strUserFirstName");
                                    String lastName = obj.getString("strUserLastName");
                                    String emailId = obj.getString("strUserEmailId");*/
                                } else if (code == 1) {
                                    String msg1 = response.getString("message");
                                    progressDialog.dismiss();
                                    Toast.makeText(Login.this, "" + msg1, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                progressDialog.dismiss();
                                Toast.makeText(Login.this, "Something take longer time please try again..!", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressDialog != null)
                        progressDialog.dismiss();
                    Log.d("RESPONSE", "That didn't work!");
                }
            });
            request.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(request);
        } else {
            Toast.makeText(this, "Please check your internet connection before verification..!", Toast.LENGTH_LONG).show();
        }

    }
}
