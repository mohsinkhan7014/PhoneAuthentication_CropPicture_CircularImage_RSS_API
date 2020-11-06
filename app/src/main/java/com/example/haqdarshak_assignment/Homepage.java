package com.example.haqdarshak_assignment;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.example.haqdarshak_assignment.rssfeed.Adapter.FeedAdapter;
import com.example.haqdarshak_assignment.rssfeed.Comman.HttpDataHandler;
import com.example.haqdarshak_assignment.rssfeed.module.RSSOBJECT;
import com.google.gson.Gson;

public class Homepage extends AppCompatActivity {

    Button Logout;
    Toolbar toolbar;
    RecyclerView recyclerView;
    RSSOBJECT rssobject;
    private final String RSS_Link="https://rss.nytimes.com/services/xml/rss/nyt/US.xml";
    private final String RSS_to_jsn="https://api.rss2json.com/v1/api.json?rss_url=";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("News");
        setSupportActionBar(toolbar);
        recyclerView=findViewById(R.id.recyvler);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getBaseContext(), LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        LoadRSS();

        Logout=findViewById(R.id.logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homepage.this,MainActivity.class));
            }
        });
    }
    private void setSupportActionBar(Toolbar toolbar) {
    }



    private void LoadRSS()
    {
        AsyncTask<String,String,String> load_RssAsync= new AsyncTask<String, String, String>()
        {
            ProgressDialog dialog=new ProgressDialog(Homepage.this);

            @Override
            protected void onPreExecute() {
                dialog.setMessage("Please Wait.......");
                dialog.show();
            }

            @Override
            protected String doInBackground(String... strings) {
                String result;
                HttpDataHandler htt=new HttpDataHandler();
                result=htt.GetHttpData(strings[0]);
                return result;
            }

            @Override
            protected void onPostExecute(String s) {
                dialog.dismiss();
                rssobject=new Gson().fromJson(s,RSSOBJECT.class);
                FeedAdapter adapter=new FeedAdapter(rssobject,getBaseContext());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        };

        StringBuilder url_get_data=new StringBuilder(RSS_to_jsn);
        url_get_data.append(RSS_Link);
        load_RssAsync.execute(url_get_data.toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.refresh)
            LoadRSS();
        return true;

    }
}