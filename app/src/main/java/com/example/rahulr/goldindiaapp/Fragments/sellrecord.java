package com.example.rahulr.goldindiaapp.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.rahulr.goldindiaapp.Adapter.sellAdapter;
import com.example.rahulr.goldindiaapp.Pojo.pojo;
import com.example.rahulr.goldindiaapp.R;
import com.example.rahulr.goldindiaapp.Utils.ConnectionDetector;
import com.example.rahulr.goldindiaapp.Utils.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.rahulr.goldindiaapp.Activity.Login.mysp;

/**
 * A simple {@link Fragment} subclass.
 */
public class sellrecord extends Fragment {

    RecyclerView recyclerView;
    ArrayList<pojo> event=new ArrayList<pojo>();
    sellAdapter adapter;
    SharedPreferences preferences;
    String uid;



    public sellrecord() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sellrecord, container, false);
        recyclerView=(RecyclerView) view.findViewById(R.id.sellrecycler);
        //databind1();
        preferences=getActivity().getSharedPreferences( mysp,MODE_PRIVATE );
        uid=preferences.getString("uguid",null);
        String cust=preferences.getString("cust",null);


        if(uid!=null && cust.equals("1"))
        {
           databind1();
        }
        return view;

    }

    private void databind() {


        pojo pojo1=new pojo();
        pojo1.setName("Rahul");
        pojo1.setQty("100");
        event.add(pojo1);

        pojo pojo2=new pojo();
        pojo2.setName("Arpit");
        pojo2.setQty("150");
        event.add(pojo2);

        pojo pojo3=new pojo();
        pojo3.setName("Arpit");
        pojo3.setQty("150");
        event.add(pojo3);


        adapter=new sellAdapter(getContext(),event);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
    }

    void databind1(){
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

            //  Uri.Builder builder = Uri.parse(Constant.PATH +"product/getall").buildUpon();
            String url1= Constant.PATH+"Sales/getall";//= Constant.PATH+"product/ProductNoSearch?prodNo="+edtsearch.getText().toString();

            JsonObjectRequest jsonObjectRequest=new JsonObjectRequest (Request.Method.GET, url1,null, new Response.Listener<JSONObject> () {
                @Override
                public void onResponse(JSONObject response) {
                    progressDialog.dismiss ();
                    try {
                        String s1=response.getString("data");
                        if(s1.equals("null"))
                        {
                            Toast.makeText(context, "Something Went Wrong Try After Sometime....", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //JSONObject obj1=response.getJSONObject("data");
                            JSONArray array=response.getJSONArray("data");
                            if(array.length()>0) {
                                for (int i = 0; i < array.length(); i++){
                                    JSONObject obj=array.getJSONObject (i);
                                    if(obj.getBoolean("tIsOrder")==false && uid.equals(obj.get("createdby")))
                                    {
                                        pojo pojo=new pojo();
                                        pojo.setName(obj.getString("partyName"));
                                        pojo.setQty(obj.getString("quantity"));
                                        event.add(pojo);
                                    }

                                }
                                adapter=new sellAdapter(getContext(),event);
                                recyclerView.setAdapter(adapter);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                            }
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
}
