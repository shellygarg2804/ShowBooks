package com.example.showbooks;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class search_bar_Activity extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_bar_view);
        final EditText searchview= (EditText) findViewById(R.id.search_text);
        Button searchbutton= (Button) findViewById(R.id.search_button);
        final Intent i1= new Intent(this,MainActivity.class);

        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String searchString= searchview.getText().toString();
                if(searchString.isEmpty()){
                        //if text entered is null,, display a toast message on screen
                        Toast toast = Toast.makeText(getApplicationContext(),"No Text found",Toast.LENGTH_SHORT);
                        toast.show();

                }
                else {
                    Log.i("LOG_TAG", "searchString");
                    i1.putExtra("searchQuery", searchString);
                    startActivity(i1);
                }
            }
        });
    }
}
