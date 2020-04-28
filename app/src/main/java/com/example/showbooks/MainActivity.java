package com.example.showbooks;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MainActivity extends Activity implements LoaderManager.LoaderCallbacks<ArrayList<book>> {


    private static String GOOGLE_BOOKS_URL= "https://www.googleapis.com/books/v1/volumes?";
   // private static final String GOOGLE_BOOKS_URL= "https://www.googleapis.com/books/v1/volumes?q=android&maxResults=11";
   // private static final String GOOGLE_BOOKS_URL="https://www.googleapis.com/books/v1/volumes?q=harraypotter=11";
    private static final int Books_unique_loader_id=1;
    private ListView listView;
    private BookAdapter adapter;
    private String LOG_TAG= MainActivity.class.getSimpleName();
    private TextView emptyTextDisplay;
    private android.app.LoaderManager loaderManager = getLoaderManager();


    @Override
    public Loader<ArrayList<book>> onCreateLoader(int i,Bundle bundle) {
        Log.e(LOG_TAG,"New Loader Created");
        return new Loaderclass.BooksLoader(this,GOOGLE_BOOKS_URL);
    }

    @Override
    public void onLoadFinished( Loader<ArrayList<book>> loader, ArrayList<book> books) {
        Log.e("Log_TAG","On Load finishedddd");
        ProgressBar progressBar= (ProgressBar)findViewById(R.id.LodingBar);
        progressBar.setVisibility(View.GONE);

        if (books != null && !books.isEmpty()){
            if(adapter!=null){
                adapter.clear();}
            UpdateUI(books);
        }
        else{ listView = (ListView) findViewById(R.id.list);
             emptyTextDisplay=(TextView)findViewById(R.id.emptyTextView);
             emptyTextDisplay.setText(R.string.no_bookResult);
             listView.setEmptyView(emptyTextDisplay);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<book>> loader) {
        adapter.clear();
    }



//

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle SearchBundle= getIntent().getExtras();
        String searchQuery= SearchBundle.getString("searchQuery");
        if(loaderManager.getLoader(Books_unique_loader_id)!=null){
            loaderManager.destroyLoader(Books_unique_loader_id);
        }





        //checking if device id connected to internet or not

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isConnected()){
            ProgressBar progressBar= (ProgressBar)findViewById(R.id.LodingBar); // remove progress bar
            progressBar.setVisibility(View.GONE);
            listView = (ListView) findViewById(R.id.list); // set empty view text to no internet connection
            emptyTextDisplay=(TextView)findViewById(R.id.emptyTextView);
            emptyTextDisplay.setText(R.string.No_Internet);
            listView.setEmptyView(emptyTextDisplay);
        }

        else {
                // appending url according to the search text entered by user
                Uri base= Uri.parse(GOOGLE_BOOKS_URL);
                Uri.Builder uribuilder= base.buildUpon();
                uribuilder.appendQueryParameter("q",searchQuery);
                uribuilder.appendQueryParameter("maxResults","15");
                GOOGLE_BOOKS_URL=uribuilder.toString();
                Log.i("Log_TAG",GOOGLE_BOOKS_URL);

                //calling loader for network request
                loaderManager.initLoader(Books_unique_loader_id, null, this);
        }

    }

    private void UpdateUI(ArrayList<book> books){
         listView= (ListView) findViewById(R.id.list);
         adapter= new BookAdapter(this,0,books);
         listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                book currentbook=(book)adapter.getItem(i);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookUri = Uri.parse(currentbook.getSelfurl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

    }



}
