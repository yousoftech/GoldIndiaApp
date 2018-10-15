package com.example.rahulr.goldindiaapp.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.rahulr.goldindiaapp.Activity.Login;
import com.example.rahulr.goldindiaapp.Adapter.ProdAdapter;
import com.example.rahulr.goldindiaapp.Pojo.pojo;
import com.example.rahulr.goldindiaapp.R;
import com.example.rahulr.goldindiaapp.SellActivity;
import com.example.rahulr.goldindiaapp.Utils.ConnectionDetector;
import com.example.rahulr.goldindiaapp.Utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.example.rahulr.goldindiaapp.Activity.Login.mysp;

/**
 * A simple {@link Fragment} subclass.
 */
public class searchdata extends Fragment {

    RecyclerView view;
    ArrayList<pojo> event=new ArrayList<pojo>();
    ProdAdapter adapter;
    String role,partyguid=null,partyname=null;
    SharedPreferences preferences;
    Button logout,btnsearch;
    View popupView,popupView1;
    EditText edtsearch;
    Button sell,order;
    PopupWindow popupWindow,popupWindow1;
    TextView name,error,qty,price,wareHouse,pguid;
    ImageView img;
    RelativeLayout l1,l2;
    String uid;
    Boolean cust=false;
    RelativeLayout mainrel;
    int blur = R.color.blur;
    ArrayList<pojo> party=new ArrayList<pojo>();


    public searchdata() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_searchdata, container, false);
        name=(TextView)v.findViewById(R.id.name);
        qty=(TextView)v.findViewById(R.id.qty);
        price=(TextView)v.findViewById(R.id.price);
        error=(TextView)v.findViewById(R.id.error);
        mainrel=(RelativeLayout)v.findViewById(R.id.mainrel);
        sell=(Button)v.findViewById(R.id.btnsell);
        order=(Button)v.findViewById(R.id.btnorder);
        pguid=(TextView)v.findViewById(R.id.pguid);
        wareHouse=(TextView)v.findViewById(R.id.warehouse);
        img=(ImageView)v.findViewById( R.id.img1 );
        l1=(RelativeLayout)v.findViewById(R.id.l1);
       // partydatabind();
        l2=(RelativeLayout) v.findViewById(R.id.l2);
        img.setImageResource(R.drawable.ic_try);
        l1.setVisibility(View.GONE);
        l2.setVisibility(View.GONE);
        order.setVisibility(View.GONE);
        sell.setVisibility(View.GONE);
        sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(getContext(), SellActivity.class);
                i.putExtra("productGuid",pguid.getText().toString());
                i.putExtra("createdBy",uid);
                i.putExtra("partyGuid",partyguid);
                i.putExtra("partyName",partyname);
                i.putExtra("tIsOrder","0");
                startActivity(i);
                //popupsell();
            }
        });
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent i=new Intent(getContext(), SellActivity.class);
                i.putExtra("productGuid",pguid.getText().toString());
                i.putExtra("createdBy",uid);
                i.putExtra("partyGuid",partyguid);
                i.putExtra("partyName",partyname);
                i.putExtra("tIsOrder","1");
                startActivity(i);
                */
                popuporder();
            }
        });
        logout=(Button)v.findViewById(R.id.logout);
        btnsearch=(Button)v.findViewById(R.id.btnsearch);
        edtsearch=(EditText) v.findViewById(R.id.edtsearch);
        edtsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    try {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    }
                    catch (Exception e)
                    {
                        int i=0;
                    }
                    if (edtsearch.getText().toString().length() > 1) {
                        error.setVisibility(View.GONE);
                        l1.setVisibility(View.GONE);
                        l2.setVisibility(View.GONE);
                        order.setVisibility(View.GONE);
                        img.setVisibility(View.GONE);
                        sell.setVisibility(View.GONE);
                        getbyid();
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Enter Valid Data.....", Toast.LENGTH_SHORT).show();
                    }

                    return true;
                }
                //performSearch();
                return false;
            }
        });
        error.setVisibility(View.GONE);
        preferences=getActivity().getSharedPreferences( mysp,MODE_PRIVATE );
        role=preferences.getString( "role",null );
        uid=preferences.getString( "uguid",null );
        String cu=preferences.getString("cust",null);
        if(cu==null)
        {
            cu="0";
        }
        if(cu.equals("1"))
        {
            cust=false;
        }
        else
        {
            cust=true;
        }
       // Toast.makeText(getContext(), "GUID = "+uid, Toast.LENGTH_SHORT).show();
        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                }
                catch (Exception e)
                {
                    int i=0;
                }
                if(edtsearch.getText().toString().length()>1) {
                    error.setVisibility(View.GONE);
                    l1.setVisibility(View.GONE);
                    l2.setVisibility(View.GONE);
                    order.setVisibility(View.GONE);
                    img.setVisibility(View.GONE);
                    sell.setVisibility(View.GONE);
                    getbyid();
                }
                else
                {
                    Toast.makeText(getContext(), "Enter Valid Data.....", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if(role!=null)
        {

        }
        else
        {
            Intent i=new Intent( getActivity(), Login.class );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity( i );
            getActivity().finish();
        }


        return  v;
    }

    private void selldata(String unit) {

        final ProgressDialog progressDialog;
        final Context context=getActivity();
        ConnectionDetector detector;
        detector = new ConnectionDetector(context);

        if (detector.isConnectingToInternet ()) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable (true);
            progressDialog.setMessage ("Loading......");
            progressDialog.show ();
            RequestQueue requestQueue = Volley.newRequestQueue (context);

            String date= new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            final JSONObject object=new JSONObject();
            try {

                object.put("createdBy", uid);
                object.put("productGuid", pguid.getText().toString());
                object.put("quantity",unit);
                object.put("salesDate",date);
                if(cust)
                {
                    object.put("tIsOrder",1);
                }
                else
                {
                    object.put("partyName","SONAL");
                    object.put("partyGuid", "880f5151-0014-4602-9e6b-0088f4222fd0");
                    object.put("tIsOrder",0);
                }
            } catch (JSONException e) {
                Toast.makeText(getContext(), "Something take longer time please try again..!", Toast.LENGTH_LONG).show();
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
                            getActivity().finish();
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

   /* private void orderdata() {

        final ProgressDialog progressDialog;
        final Context context=getActivity();
        ConnectionDetector detector;
        detector = new ConnectionDetector(context);

        if (detector.isConnectingToInternet ()) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable (true);
            progressDialog.setMessage ("Loading......");
            progressDialog.show ();
            RequestQueue requestQueue = Volley.newRequestQueue (context);

            //  Uri.Builder builder = Uri.parse(Constant.PATH +"product/getall").buildUpon();
            String url1= Constant.PATH+"product/ProductNoSearch?prodNo="+edtsearch.getText().toString();

            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest (Request.Method.GET, url1,null, new Response.Listener<JSONObject> () {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss ();
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

    }*/

    void popupsell() {

        mainrel.setVisibility(View.VISIBLE);
        //LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.popup, null);
        popupWindow=new PopupWindow(popupView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT,
                true);
        popupWindow.setTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
        final AutoCompleteTextView txtname;
        final EditText txtunit;
        Button cancel,sell1;
        txtunit=(EditText)popupView.findViewById(R.id.txtunit);
        cancel=(Button)popupView.findViewById(R.id.cancel);
        sell1=(Button)popupView.findViewById(R.id.btnconfirm);

/*        txtunit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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
  */
        String [] strname =null;
        if(party!=null)
        {
            strname= new String[party.size()];
            for(int i=0;i<party.size();i++)
            {
                strname[i]=party.get(i).getName();
            }
        }
        else
        {
            Toast.makeText(getContext(), "Check Internet Connection.....", Toast.LENGTH_SHORT).show();
            mainrel.setVisibility(View.GONE);
            popupWindow.dismiss();
        }
        txtname=(AutoCompleteTextView) popupView.findViewById(R.id.txtname);
        if(strname.length>0)
        {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (getContext(), android.R.layout.select_dialog_item, strname);
            txtname.setThreshold(1);
            txtname.setAdapter(adapter);
            txtname.setTextColor(Color.BLACK);
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainrel.setVisibility(View.GONE);
                popupWindow.dismiss();
            }
        });

        sell1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!txtname.getText().equals("") && !txtunit.getText().equals(""))
                {
                    String name=txtname.getText().toString();
                    String unit=txtunit.getText().toString();
                    //datastore(name,unit,uid);
                    mainrel.setVisibility(View.GONE);
                    popupWindow.dismiss();
                }
                else
                {
                    Toast.makeText(getContext(), "Input Valid Data", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    void popuporder() {
        mainrel.setVisibility(View.VISIBLE);
        //LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView1 = layoutInflater.inflate(R.layout.popuporder, null);
        popupWindow1=new PopupWindow(popupView1, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT,
                true);
        popupWindow1.setTouchable(true);
        popupWindow1.setFocusable(true);
        popupWindow1.setOutsideTouchable(true);
        popupWindow1.showAtLocation(popupView1, Gravity.CENTER, 0, 0);
        final EditText txtunit;
        Button cancel,sell1;
        txtunit=(EditText)popupView1.findViewById(R.id.txtunit);
        cancel=(Button)popupView1.findViewById(R.id.cancel);
        sell1=(Button)popupView1.findViewById(R.id.btnconfirm);

        txtunit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_GO)
                {
                    try {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(txtunit.getWindowToken(), 0);
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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainrel.setVisibility(View.GONE);
                popupWindow1.dismiss();
            }
        });

        sell1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtunit.getText().toString().length()>0)
                {
                    String unit=txtunit.getText().toString();
                    selldata(unit);
                    mainrel.setVisibility(View.GONE);
                    popupWindow1.dismiss();

                }
                else
                {
                    Toast.makeText(getContext(), "Please Enter Valid Data....", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void getbyid() {

        final ProgressDialog progressDialog;
        final Context context=getActivity();
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
                Toast.makeText(context, "Something take longer time please try again..!", Toast.LENGTH_LONG).show();
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
                        pguid.setText(obj1.getString("productGuid"));
                        error.setVisibility(View.GONE);
                        l1.setVisibility(View.VISIBLE);
                        l2.setVisibility(View.VISIBLE);
                        img.setVisibility(View.VISIBLE);
                        if(cust)
                        {
                            sell.setVisibility(View.GONE);
                            order.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            sell.setVisibility(View.VISIBLE);
                            order.setVisibility(View.GONE);
                        }
                        if (true) {
                            Glide.with(context).load("http://goldindiacard.com/assets/img/productsimage/"+obj1.getString("productImage")).into(img);
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
    void partydatabind(){
        final ProgressDialog progressDialog;
        final Context context=getActivity();
        ConnectionDetector detector;
        detector = new ConnectionDetector(context);

        if (detector.isConnectingToInternet ()) {
            //progressDialog = new ProgressDialog(context);
            //progressDialog.setCancelable (true);
            //progressDialog.setMessage ("Loading......");
            //progressDialog.show ();
            RequestQueue requestQueue = Volley.newRequestQueue (context);
            final JSONObject object = new JSONObject();

            //  Uri.Builder builder = Uri.parse(Constant.PATH +"product/getall").buildUpon();
            String url1=Constant.PATH+"Party/getall";//= Constant.PATH+"product/ProductNoSearch?prodNo="+edtsearch.getText().toString();


            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest (Request.Method.GET, url1,null, new Response.Listener<JSONObject> () {
                @Override
                public void onResponse(JSONObject response) {
              //      progressDialog.dismiss ();
                    try {
                        String s1=response.getString("data");
                        if(s1.equals("null"))
                        {

                        }
                        //JSONObject obj1=response.getJSONObject("data");
                        JSONArray array=response.getJSONArray("data");
                        if(array.length ()>0)
                        {
                            for(int i=0;array.length ()>i;i++)
                            {
                                JSONObject obj=array.getJSONObject (i);
                                pojo p=new pojo();
                                String name=obj.getString("partyName");
                                String guid=obj.getString("partyGuid");
                                p.setPartyguid(guid);
                                p.setName(name);
                                party.add(p);
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

}
