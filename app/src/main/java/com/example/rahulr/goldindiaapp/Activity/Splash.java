package com.example.rahulr.goldindiaapp.Activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rahulr.goldindiaapp.Activity.MainActivity;
import com.example.rahulr.goldindiaapp.Fragments.Main2Activity;
import com.example.rahulr.goldindiaapp.Fragments.TabActivity;
import com.example.rahulr.goldindiaapp.R;
import com.example.rahulr.goldindiaapp.Utils.ConnectionDetector;

public class Splash extends AppCompatActivity {

    Button retyr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final ConnectionDetector connectionDetector=new ConnectionDetector(Splash.this);
        retyr =(Button)findViewById(R.id.retry);
        retyr.setVisibility(View.GONE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {



                if(connectionDetector.isConnectingToInternet())
                {
                    retyr.setVisibility(View.GONE);
                    Intent i=new Intent(Splash.this, TabActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                }
                else
                {
                    retyr.setVisibility(View.VISIBLE);
                    Toast.makeText(Splash.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                    retyr.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(connectionDetector.isConnectingToInternet())
                            {
                                Intent i=new Intent(Splash.this,TabActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();
                            }
                            else{
                                Toast.makeText(Splash.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        },5000);

    }
}
