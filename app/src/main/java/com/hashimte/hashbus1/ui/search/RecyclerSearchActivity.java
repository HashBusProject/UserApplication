package com.hashimte.hashbus1.ui.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import  androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.CalendarView;

import com.hashimte.hashbus1.R;

public class RecyclerSearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private SearchData[]searchData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_search);
       recyclerView =  findViewById(R.id.searchRecyclerView);
       recyclerView.setHasFixedSize(true);
       recyclerView.setLayoutManager(new LinearLayoutManager(this));


        searchData=new SearchData[]{
                new SearchData("bus1", "the first bus data",22,"min"),
                new SearchData("bus1", "the first bus data",22,"min"),
                new SearchData("bus1", "the first bus data",22,"min"),
                new SearchData("bus1", "the first bus data",22,"min"),
                new SearchData("bus1", "the first bus data",22,"min"),
                new SearchData("bus1", "the first bus data",22,"min"),
                new SearchData("bus1", "the first bus data",22,"min"),
                new SearchData("bus1", "the first bus data",22,"min"),



        };

        SearchAdapter searchAdapter=new SearchAdapter(searchData,RecyclerSearchActivity.this);
        recyclerView.setAdapter(searchAdapter);



    }
}