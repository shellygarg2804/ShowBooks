package com.example.showbooks;

import android.content.AsyncTaskLoader;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Loaderclass {
    public static class BooksLoader extends AsyncTaskLoader<ArrayList<book>> {
        private String murl;

        public BooksLoader(@NonNull Context context, String url) {
            super(context);
            murl=url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Nullable
        @Override
        public ArrayList<book> loadInBackground() {
            if(murl==null)
            {
                return null;
            }
            else{
                ArrayList<book> books=QueryUtils.fetchdatafromgoogle(murl);
                return books;
            }
        }


    }

}
