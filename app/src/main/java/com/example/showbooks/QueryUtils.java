package com.example.showbooks;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import static android.graphics.BitmapFactory.decodeStream;

public final class QueryUtils {

    public static final String LOG_TAG= QueryUtils.class.getSimpleName();

    public static ArrayList<book> fetchdatafromgoogle(String requesturl){
        URL url= createUrl(requesturl);
        String jsonresponse=null ;
        try{
            jsonresponse=makeHTTPrequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG,"error in making HTTP request and finding Input Stream",e);
        }
        ArrayList<book> books=null;
        books = extractBookInfo(jsonresponse);
        return books;

    }

    // function to create url from string.
    public static URL createUrl(String stringURl)
    {
        URL url =null;
        try{
            url= new URL(stringURl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG,"problem in creatin url",e); //if url request fails
        }
        return url;
    }


    //function to make http request from url
    private static  String makeHTTPrequest(URL url) throws IOException {
        String JsonResponse= "";   //returns a jsonResponse

        if(url==null){
            return  JsonResponse;
        }

        HttpURLConnection urlConnection= null;
        InputStream inputStream= null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.

            if(urlConnection.getResponseCode()==200){

                inputStream = urlConnection.getInputStream(); // get input stream from url link,,, then convert it to readable format

                JsonResponse= readfromStream(inputStream); // jsonResponse:: readable format
            }
            else
            {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }

        catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return JsonResponse;

    }

//function to read from input Stream. Input stream is present in 0 and 1's format
    private static String readfromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static ArrayList<book> extractBookInfo(String jsonresponse) {
        // base case: if json response is empty, return null
        if (TextUtils.isEmpty(jsonresponse)) {

            return null;
        }
        ArrayList<book> books= new ArrayList<>();
        try{
            //parsing jsonResponse
            JSONObject baseobject= new JSONObject(jsonresponse); // throws exception handled in catch block
            JSONArray Items= baseobject.getJSONArray("items");

            for(int i=0;i<10;i++){  //displaying top 10 results

                JSONObject currentbook= Items.getJSONObject(i);
                JSONObject volumeinfo=currentbook.getJSONObject("volumeInfo");

                String Title= volumeinfo.getString("title");
                String Publisher=volumeinfo.getString("publisher");
                String pageString= "Page count not available";
                if(!volumeinfo.isNull("pageCount")) {
                    int pages = volumeinfo.getInt("pageCount");
                    pageString=pages+" pages";
                }
                double rating= 0.0;
                if(!volumeinfo.isNull("averageRating")) {
                    rating = volumeinfo.getDouble("averageRating");
                }

                String previewlink=volumeinfo.getString("previewLink");

                JSONObject saleinfo=currentbook.getJSONObject("saleInfo");
                String priceString="NOT FOR SALE";

                if(saleinfo.getString("saleability").contentEquals("FOR_SALE")) {
                    JSONObject priceobject = saleinfo.getJSONObject("listPrice");
                    int price = priceobject.getInt("amount");
                    String currency = priceobject.getString("currencyCode");
                    priceString= price+" "+currency;

                }

                String thumbnail=null;
                if(volumeinfo.has("imageLinks")) {

                    JSONObject image = volumeinfo.getJSONObject("imageLinks");
                    if(image.has("smallThumbnail")) {
                         thumbnail = image.getString("smallThumbnail");
                    }
                }

                // creating an object to book class

                book Book= new book(Title,Publisher,pageString,priceString,rating,thumbnail,previewlink);
                //add object in arraylist
                books.add(Book);
            }

        } catch (JSONException e) {
           Log.e(LOG_TAG,"problem parsing the jsonresponse",e);
        }
        return books; //returning arraylist
    }


}
