package com.example.algonquin.cst2335_finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AutomobileActivity extends Activity {
    private ListView automobileList;
    private TextView autoTv;
    private ArrayAdapter ad;
    private ArrayList<String> listItems;
    private CustomAdapter ca;
    Button purchaseBT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_automobile);
        listItems = new ArrayList<String>();
        purchaseBT = (Button)findViewById(R.id.fuelCostB);

        listItems.add("Gas Pirce History");
        listItems.add("Gas Amount History");
        listItems.add("Gas used (km)");


        ad = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        ca = new CustomAdapter(this);
        automobileList = (ListView) findViewById(R.id.automobileList);
        automobileList.setAdapter(ad); //adapt the list


        automobileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AutomobileActivity.this, GasPriceHistoryActivity.class);
                startActivity(intent);

                ad.notifyDataSetChanged();
            }
        });

        purchaseBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AutomobileActivity.this, PurchaseGasActivity.class);
                startActivity(intent);
            }
        });



    }

    class CustomAdapter extends ArrayAdapter<String>{

        public CustomAdapter(Context context){super(context,0);}
        public int getCount(){return listItems.size();}
        public String getItem(int position){return listItems.get(position);}


    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart(){
        super.onStart();
    }
    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
