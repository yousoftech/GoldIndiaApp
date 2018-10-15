package com.example.rahulr.goldindiaapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.example.rahulr.goldindiaapp.Pojo.pojo;
import com.example.rahulr.goldindiaapp.Utils.ConnectionDetector;
import com.example.rahulr.goldindiaapp.Utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SellActivity extends AppCompatActivity {


    EditText txtunit;
    AutoCompleteTextView txtname;
    Button btnconfirm;
    String [] strname =null;
    ArrayList<pojo> party=new ArrayList<pojo>();
    String productGuid,createdBy,partyGuid=null,partyName=null,tIsOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        txtname=(AutoCompleteTextView) findViewById(R.id.txtname);
        txtunit=(EditText) findViewById(R.id.txtunit);
        btnconfirm=(Button)findViewById(R.id.btnconfirm);

        Intent i = getIntent();
        productGuid=i.getStringExtra("productGuid");
        createdBy=i.getStringExtra("createdBy");
        tIsOrder=i.getStringExtra("tIsOrder");

        if(tIsOrder.equals("1"))
        {
            setTitle("Order Details");
            txtname.setVisibility(View.GONE);
            btnconfirm.setText("Order");
        }
        else {
            partydatabind();
            setTitle("Sales Details");
            btnconfirm.setText("Sale");
            txtname.setVisibility(View.VISIBLE);
            partyGuid=i.getStringExtra("partyGuid");
            partyName=i.getStringExtra("partyName");
        }


        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tIsOrder.equals("1"))
                {
                    if(txtunit.getText().toString().equals(""))
                        Toast.makeText(SellActivity.this, "Please Enter Unit......", Toast.LENGTH_SHORT).show();
                    else
                        selldata(txtunit.getText().toString(),true);
                }
                else
                {
                    if(txtunit.getText().toString().equals(""))
                    {
                        Toast.makeText(SellActivity.this, "Please Enter Unit......", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        for(int i1=0;i1<party.size();i1++)
                        {
                            if(txtname.getText().toString().equals(party.get(i1).getName()))
                            {
                                partyGuid=party.get(i1).getPartyguid();
                                partyName=party.get(i1).getName();
                            }
                        }
                        if(partyGuid!=null)
                        {
                            selldata(txtunit.getText().toString(),false);
                        }
                        else
                        {
                            Toast.makeText(SellActivity.this, "Please Enter Valid Party Name", Toast.LENGTH_SHORT).show();
                            partydatabind();
                        }
                    }

                }
            }
        });
        txtunit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

                    return true;
                }

                return false;
            }
        });

        txtname.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_NEXT)
                {
                    txtunit.requestFocus();
                    return true;
                }

                return false;
            }
        });

    }
    void partydatabind(){
        final ProgressDialog progressDialog;
        final Context context=SellActivity.this;
        ConnectionDetector detector;
        detector = new ConnectionDetector(context);

        if (detector.isConnectingToInternet ()) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable (true);
            progressDialog.setMessage ("Loading......");
            progressDialog.show ();
            RequestQueue requestQueue = Volley.newRequestQueue (context);
            final JSONObject object = new JSONObject();

            //  Uri.Builder builder = Uri.parse(Constant.PATH +"product/getall").buildUpon();
            String url1=Constant.PATH+"Party/getall";//= Constant.PATH+"product/ProductNoSearch?prodNo="+edtsearch.getText().toString();


            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest (Request.Method.GET, url1,null, new Response.Listener<JSONObject> () {
                @Override
                public void onResponse(JSONObject response) {
                          progressDialog.dismiss ();
                    try {
                        String s1=response.getString("data");
                        if(s1.equals("null"))
                        {

                        }
                        //JSONObject obj1=response.getJSONObject("data");
                        JSONArray array=response.getJSONArray("data");
                        if(array.length ()>0)
                        {
                                strname= new String[array.length()];
                            for(int i=0;array.length ()>i;i++)
                            {
                                JSONObject obj=array.getJSONObject (i);
                                pojo p=new pojo();
                                String name=obj.getString("partyName");
                                String guid=obj.getString("partyGuid");
                                p.setPartyguid(guid);
                                p.setName(name);
                                strname[i]=name;
                                party.add(p);
                            }
                            if(strname.length>0) {
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                        (SellActivity.this, android.R.layout.select_dialog_item, strname);
                                txtname.setThreshold(1);
                                txtname.setAdapter(adapter);
                                txtname.setTextColor(Color.BLACK);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace ();
                    }
                }
            }, new Response.ErrorListener () {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //if (progressDialog != null)
                    //  progressDialog.dismiss();
                }
            });

            jsonObjectRequest.setRetryPolicy( new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ) );
            RequestQueue requestQueue1 = Volley.newRequestQueue( context );
            requestQueue1.add( jsonObjectRequest );
        }
        else{
            Toast.makeText(context, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }
    private void selldata(String unit,Boolean order) {

        final ProgressDialog progressDialog;
        final Context context=SellActivity.this;
        ConnectionDetector detector;
        detector = new ConnectionDetector(context);

        String date= new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());


        if (detector.isConnectingToInternet ()) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable (true);
            progressDialog.setMessage ("Loading......");
            progressDialog.show ();
            final RequestQueue requestQueue = Volley.newRequestQueue (context);

            final JSONObject object=new JSONObject();
            try {

                object.put("createdby", createdBy);
                object.put("productGuid", productGuid);
                object.put("quantity",unit);
                object.put("salesDate",date);
                if(order)
                {
                    object.put("tIsOrder",1);
                }
                else
                {
                    object.put("partyName",partyName);
                    object.put("partyGuid", partyGuid);
                    object.put("tIsOrder",0);
                }

            } catch (JSONException e) {
                Toast.makeText(context, "Something take longer time please try again..!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            //  Uri.Builder builder = Uri.parse(Constant.PATH +"product/getall").buildUpon();
            String url1= Constant.PATH+"Sales/add";

            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest (Request.Method.POST, url1,object, new Response.Listener<JSONObject> () {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss ();
                    try {
                        if(response.getString("status")=="0")
                        {
                            Toast.makeText(context, "Succesfull order.......", Toast.LENGTH_SHORT).show();
                            Intent intent1=new Intent(context,TabActivity.class);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent1);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(context, "Someting Want Wrong Try Again....", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener () {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (progressDialog != null)
                        progressDialog.dismiss();
                }
            });
            jsonObjectRequest.setRetryPolicy( new DefaultRetryPolicy(
                    30000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT ) );
            RequestQueue requestQueue1 = Volley.newRequestQueue( context );
            requestQueue1.add( jsonObjectRequest );
        }
        else{
            Toast.makeText(context, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

}
