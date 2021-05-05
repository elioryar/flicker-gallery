package com.example.moovitgallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private ArrayList<String> imagesUrl = new ArrayList();
    private RequestQueue requestQueue;
    private CustomAdapter customAdapter;
    private int page=0;

    //Load images from flicker website by http request
    private void loadImages(int page) {
        String url = "https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&extras=url_s&api_key=aabca25d8cd75f676d3a74a72dcebf21&format=json&nojsoncallback=1&page=" + page;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {

            @Override
            //Save the urls of the images in imageUrl array
            public void onResponse(JSONObject response) {
                try {
                    JSONArray photosJsonArray = response.getJSONObject("photos").getJSONArray("photo");
                    for (int i = 0; i < photosJsonArray.length(); i++) {
                        JSONObject photoObject = photosJsonArray.getJSONObject(i);
                        String url = photoObject.getString("url_s");
                        if (!(imagesUrl.contains(url)))
                            imagesUrl.add(url);
                        customAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }

        });
        this.page++;
        requestQueue.add(req);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        requestQueue = Volley.newRequestQueue(this);
        loadImages(0);
        gridView = findViewById(R.id.gridView);
        customAdapter = new CustomAdapter(imagesUrl, this);
        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //Opens the selected image on a separate screen
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                String selectedImage = imagesUrl.get(pos);
                startActivity(new Intent(MainActivity.this, ClickedItemActivity.class).putExtra("image", selectedImage));


            }
        });

        //This function allow continuous scrolling of photos.
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount > 0) {
                    int lastVisibleItem = firstVisibleItem + visibleItemCount;
                    if (lastVisibleItem == totalItemCount) {
                        loadImages(page);

                    }
                }
            }
        });
    }

    //The gridview adapter
    public class CustomAdapter extends BaseAdapter {

        private ArrayList<String> imagesUrl;
        private Context context;
        private LayoutInflater layoutInflater;

        public CustomAdapter(ArrayList<String> imagesUrl, Context context) {
            this.imagesUrl = imagesUrl;
            this.layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            this.context = context;
        }

        public ArrayList<String> getImages() {
            return imagesUrl;
        }

        public void setImages(ArrayList<String> imagesUrl) {
            this.imagesUrl = imagesUrl;
        }

        public LayoutInflater getLayoutInflater() {
            return layoutInflater;
        }

        public void setLayoutInflater(LayoutInflater layoutInflater) {
            this.layoutInflater = layoutInflater;
        }

        @Override
        public int getCount() {
            return imagesUrl.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.row_items, parent, false);
            }
            ImageView imageView = convertView.findViewById(R.id.imageView);
            Glide
                    .with(context)
                    .load(this.imagesUrl.get(pos))
                    .placeholder(R.drawable.placeholder).centerCrop()
                    .into(imageView);

            return convertView;


        }
    }

}