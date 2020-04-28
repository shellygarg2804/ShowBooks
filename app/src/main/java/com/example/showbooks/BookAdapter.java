package com.example.showbooks;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter {

    public BookAdapter(@NonNull Context context, int background, ArrayList<book> books) {
        super(context, 0, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       View Listitemview= convertView;
        if(Listitemview==null) {
            Listitemview = LayoutInflater.from(getContext()).inflate(R.layout.list_item_view,parent,false);
        }

        book current=(book) getItem(position);

        String name= current.getName();
        TextView textView1= (TextView)Listitemview.findViewById(R.id.book_title);
        textView1.setText(name);

        if (current.getImageid() != null) {
        String imageurl= current.getImageid();
            Log.e("Adapter",imageurl);
        ImageView imageView=(ImageView)Listitemview.findViewById(R.id.book_image);
            Picasso.with(getContext()).load(imageurl).resize(100,120).into(imageView);
            Picasso.with(getContext()).setLoggingEnabled(true);
        }

        String author=current.getAuthor();
        TextView textView2= (TextView)Listitemview.findViewById(R.id.book_Author);
        textView2.setText(author);

        String price=current.getPrice();
        TextView textView3= (TextView)Listitemview.findViewById(R.id.price);
        textView3.setText(price);

        String pages=current.getPages();
        TextView textView4= (TextView)Listitemview.findViewById(R.id.book_length);
        textView4.setText(pages);

        double rating = current.getRating();
        DecimalFormat ratingFormatter= new DecimalFormat("0.0");
        String stringrating= ratingFormatter.format(rating);
        TextView textView5= (TextView)Listitemview.findViewById(R.id.rating);
        textView5.setText(stringrating);
        return Listitemview;

    }
}
