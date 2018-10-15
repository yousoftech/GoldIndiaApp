package com.example.rahulr.goldindiaapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.example.rahulr.goldindiaapp.Pojo.pojo;
import com.example.rahulr.goldindiaapp.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ProdAdapter extends RecyclerView.Adapter<ProdAdapter.RecycleViewHodler> {

    Context context;
    ArrayList<pojo> event;
    LayoutInflater inflater;

    public ProdAdapter(Context context,ArrayList<pojo> event)
    {

        this.context=context;
        this.event=event;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public RecycleViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View v=inflater.inflate(R.layout.product_row,parent,false);
        RecycleViewHodler hodler=new RecycleViewHodler(v);
        return hodler;
    }

    @Override
    public void onBindViewHolder(RecycleViewHodler holder, int position) {
        String name=event.get(position).getName();
     //   String name1=event.get(position).getName1();
        String qty=event.get(position).getQty();
       // String qty1=event.get(position).getQty1();
        String price=event.get(position).getPrice();
     //   String price1=event.get(position).getPrice1();
        String imgname=event.get( position ).getImgname();
        String wareHouse=event.get( position ).getWareHouse();
      //  String imgname1=event.get( position ).getImgname1();
        holder.name.setText(name);
   //     holder.name1.setText(name1);
        holder.qty.setText(qty);
     //   holder.qty1.setText(qty1);
        holder.price.setText(price);
        holder.warehouse.setText(wareHouse);
      //  holder.price1.setText(price1);
        if(imgname.equals( "other" )) {
            holder.img.setImageResource(R.drawable.ic_try);
        }
        else{
     /*     URL url = new URL( "http://www.goldindiacard.com/httpdocs/assests/img/" + imgname );
                Bitmap bmp = BitmapFactory.decodeStream( url.openConnection().getInputStream() );
                holder.img.setImageBitmap( bmp );*/
//               holder.img.setImageURI(Uri.parse("https://i.stack.imgur.com/fYoB9.png"));

            //Glide.with(context).load("http://www.goldindiacard.com/httpdocs/assests/img/productsimage"+imgname).into(holder.img);

            Glide.with(context).load("http://goldindiacard.com/assets/img/productsimage/"+imgname).override( 100,130 ).into(holder.img);
/*
            try {
                URL url = new URL( "http://www.goldindiacard.com/httpdocs/assests/img/" + imgname1 );
                Bitmap bmp = BitmapFactory.decodeStream( url.openConnection().getInputStream() );
                holder.img1.setImageBitmap( bmp );

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
*/
        }
    }

    @Override
    public int getItemCount() {
        return event.size();
    }

    public class RecycleViewHodler extends RecyclerView.ViewHolder {

        TextView name,qty,price,warehouse,name1,qty1,price1;
        ImageView img1,img;

        public RecycleViewHodler(View itemView) {
            super(itemView);
            name=(TextView)itemView.findViewById(R.id.name);
  //          name1=(TextView)itemView.findViewById(R.id.name1);
            qty=(TextView)itemView.findViewById(R.id.qty);
   //         qty1=(TextView)itemView.findViewById(R.id.qty1);
            price=(TextView)itemView.findViewById(R.id.price);
            warehouse=(TextView)itemView.findViewById(R.id.warehouse);
     //       price1=(TextView)itemView.findViewById(R.id.price1);
       //     img1=(ImageView)itemView.findViewById( R.id.img2 );
            img=(ImageView)itemView.findViewById( R.id.img1 );
        }
    }
}
