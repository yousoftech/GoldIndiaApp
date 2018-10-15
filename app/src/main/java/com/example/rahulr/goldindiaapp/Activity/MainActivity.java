package com.example.rahulr.goldindiaapp.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.rahulr.goldindiaapp.Adapter.ProdAdapter;
import com.example.rahulr.goldindiaapp.Pojo.pojo;
import com.example.rahulr.goldindiaapp.R;
import com.example.rahulr.goldindiaapp.Utils.ConnectionDetector;
import com.example.rahulr.goldindiaapp.Utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.rahulr.goldindiaapp.Activity.Login.mysp;

public class MainActivity extends AppCompatActivity {

    RecyclerView view;
    ArrayList<pojo> event=new ArrayList<pojo>();
    ProdAdapter adapter;
    String role;
    SharedPreferences preferences;
    Button logout,btnsearch;
    View popupView;
    EditText edtsearch;
    Button sell;
    PopupWindow popupWindow;
    TextView name,error,qty,price,wareHouse;
    ImageView img;
    LinearLayout l1,l2, l3,l4,l5;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name=(TextView)findViewById(R.id.name);
        qty=(TextView)findViewById(R.id.qty);
        price=(TextView)findViewById(R.id.price);
        error=(TextView)findViewById(R.id.error);
        sell=(Button)findViewById(R.id.btnsell);
        wareHouse=(TextView)findViewById(R.id.warehouse);
        img=(ImageView)findViewById( R.id.img1 );
        l1=(LinearLayout)findViewById(R.id.l1);
        l2=(LinearLayout)findViewById(R.id.l2);
        l3=(LinearLayout)findViewById(R.id.l3);
        l4=(LinearLayout)findViewById(R.id.l4);
        l4=(LinearLayout)findViewById(R.id.l4);
        l5=(LinearLayout)findViewById(R.id.l5);
        img.setImageResource(R.drawable.ic_try);
        l1.setVisibility(View.GONE);
        l2.setVisibility(View.GONE);
        l3.setVisibility(View.GONE);
        l4.setVisibility(View.GONE);
        logout=(Button)findViewById(R.id.logout);
        btnsearch=(Button)findViewById(R.id.btnsearch);
        edtsearch=(EditText) findViewById(R.id.edtsearch);
        error.setVisibility(View.GONE);
        preferences=getSharedPreferences( mysp,MODE_PRIVATE );
        role=preferences.getString( "role",null );
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error.setVisibility(View.GONE);
                l1.setVisibility(View.GONE);
                l2.setVisibility(View.GONE);
                l3.setVisibility(View.GONE);
                l4.setVisibility(View.GONE);
                l5.setVisibility(View.GONE);
                img.setVisibility(View.GONE);
                getbyid();
            }
        });
        if(role!=null)
        {

        }
        else
        {
            Intent i=new Intent( this, Login.class );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity( i );
            finish();
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater=MainActivity.this.getLayoutInflater();
                dialogBuilder.setMessage("Are you sure you want to logout");
                dialogBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences.Editor editor = getSharedPreferences(mysp, MODE_PRIVATE).edit();
                        editor.putString("role", null);
                        editor.apply();
                        Intent i=new Intent(MainActivity.this, Login.class);
                        startActivity(i);
                        finish();
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog a=dialogBuilder.create();
                a.show();

            }
        });

        //data();


    }

    private void getbyid() {

            final ProgressDialog progressDialog;
            final Context context=this;
            ConnectionDetector detector;
            detector = new ConnectionDetector(context);

            if (detector.isConnectingToInternet ()) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setCancelable (true);
                progressDialog.setMessage ("Loading......");
                progressDialog.show ();
                RequestQueue requestQueue = Volley.newRequestQueue (context);
                final JSONObject object = new JSONObject();
                try {
                    object.put("prodNo", edtsearch.getText().toString());
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, "Something take longer time please try again..!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

                //  Uri.Builder builder = Uri.parse(Constant.PATH +"product/getall").buildUpon();
                 String url1= Constant.PATH+"product/ProductNoSearch?prodNo="+edtsearch.getText().toString();





                JsonObjectRequest jsonObjectRequest=new JsonObjectRequest (Request.Method.GET, url1,null, new Response.Listener<JSONObject> () {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss ();
                        try {
                            String s1=response.getString("data");
                            if(s1.equals("null"))
                            {
                                error.setVisibility(View.VISIBLE);
                            }
                            JSONObject obj1=response.getJSONObject("data");
                            name.setText(obj1.getString("productNo"));
                            price.setText("Rs " + obj1.getString("salesRate"));
                            qty.setText(obj1.getString("qty"));
                            wareHouse.setText(obj1.getString("wareHouse"));
                            error.setVisibility(View.GONE);
                            l1.setVisibility(View.VISIBLE);
                            l2.setVisibility(View.VISIBLE);
                            l3.setVisibility(View.VISIBLE);
                            l4.setVisibility(View.VISIBLE);
                            l5.setVisibility(View.VISIBLE);
                            img.setVisibility(View.VISIBLE);

                            if (role.equals("Mobile User")) {
                                Glide.with(MainActivity.this).load("http://goldindiacard.com/assets/img/productsimage/"+obj1.getString("productImage")).into(img);
                            } else {
                                img.setImageResource(R.drawable.ic_try);
                            }
                            img.setVisibility(View.VISIBLE);

                        } catch (JSONException e) {
                            e.printStackTrace ();
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



    private void data()
    {
        pojo p=new pojo();
        p.setName("Gold1");
        p.setName1("Gold2");
        p.setPrice("Rs 10,000");
        p.setPrice1("Rs 20,000");
        p.setQty("10");
        p.setQty1("20");
        event.add(p);

        pojo p1=new pojo();
        p1.setName("Gold3");
        p1.setName1("Gold4");
        p1.setPrice("Rs 10,000");
        p1.setPrice1("Rs 20,000");
        p1.setQty("10");
        p1.setQty1("20");
        event.add(p1);

        pojo p3=new pojo();
        p3.setName("Gold5");
        p3.setName1("Gold6");
        p3.setPrice("Rs 10,000");
        p3.setPrice1("Rs 20,000");
        p3.setQty("10");
        p3.setQty1("20");
        event.add(p3);

        pojo p4=new pojo();
        p4.setName("Gold7");
        p4.setName1("Gold8");
        p4.setPrice("Rs 10,000");
        p4.setPrice1("Rs 20,000");
        p4.setQty("10");
        p4.setQty1("20");
        event.add(p4);

        adapter=new ProdAdapter(this,event);
        view.setAdapter(adapter);
        view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

    }

    public void getall()
    {
        String url1;
        final ProgressDialog progressDialog;
        final Context context=this;
        ConnectionDetector detector;
        detector = new ConnectionDetector(context);

        if (detector.isConnectingToInternet ()) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable (false);
            progressDialog.setMessage ("Loading......");
            progressDialog.show ();
            RequestQueue requestQueue = Volley.newRequestQueue (context);

          //  Uri.Builder builder = Uri.parse(Constant.PATH +"product/getall").buildUpon();
            //    url1= builder.toString();

            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest (Request.Method.GET, Constant.PATH +"product/getall", null, new Response.Listener<JSONObject> () {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss ();
                    try {

                        String jstring=response.toString();
                       // JSONObject object= response.getJSONObject("data");
                        JSONArray array=response.getJSONArray("data");




                        if(array.length ()>0)
                        {

                            int j=1;
                            for(int i=0;array.length ()>i;i++)
                            {
                                JSONObject obj=array.getJSONObject (i);
                                pojo p=new pojo();
                                String name = obj.getString("productNo");
                                String price = "Rs " + obj.getString("salesRate");
                                String qty = obj.getString("qty");
                                p.setName(name);
                                p.setPrice(price);
                                p.setQty(qty);
                                if (role.equals("Mobile User")) {
                                    if (i == 0) {
                                        p.setImgname("other");
                                    } else {
                                        p.setImgname(obj.getString("productImage"));
                                    }
                                } else {
                                    p.setImgname("other");
                                }
                                event.add(p);


/*
                                while(j<3) {
                                    if (j==2) {
                                        String name1 = obj.getString("productName");
                                        String price1 = "Rs " + obj.getString("salesRate");
                                        String qty1 = obj.getString("qty");
                                        p.setName1(name1);
                                        p.setPrice1(price1);
                                        p.setQty1(qty1);
                                        if (role.equals("Mobile User")) {
                                            if (i == 0) {

                                            } else {
                                                p.setImgname1(obj.getString("productImage"));
                                            }
                                        } else {
                                            p.setImgname("other");
                                        }


                                    } else if(j==1){
                                        String name = obj.getString("productName");
                                        String price = "Rs " + obj.getString("salesRate");
                                        String qty = obj.getString("qty");
                                        p.setName(name);
                                        p.setPrice(price);
                                        p.setQty(qty);
                                        if (role.equals("Mobile User")) {
                                            if (i == 0) {

                                            } else {
                                                p.setImgname(obj.getString("productImage"));
                                            }
                                        } else {
                                            p.setImgname("other");
                                        }
                                    }

                                    j++;
                                }
                                if(j==3) {
                                    event.add(p);
                                    j=1;
                                }
                                */
                            }



                            adapter=new ProdAdapter(context,event);
                            view.setAdapter(adapter );
                            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(context, 2);
                            view.setLayoutManager(mLayoutManager);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace ();
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


    void popup()
    {
        //LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.popup, null);
        popupWindow=new PopupWindow(popupView,RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT,
                true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        final EditText txtname,txtunit;
        Button cancel,sell;
        txtname=(EditText)popupView.findViewById(R.id.edtname);
        txtunit=(EditText)popupView.findViewById(R.id.txtunit);
        cancel=(Button)popupView.findViewById(R.id.cancel);
        sell=(Button)popupView.findViewById(R.id.btnconfirm);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtname.getText().equals("") && !txtunit.getText().equals(""))
                {
                    String name=txtname.getText().toString();
                    String unit=txtunit.getText().toString();
                    //datastore(name,unit,uid);

                    popupWindow.dismiss();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Input Valid Data", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}
