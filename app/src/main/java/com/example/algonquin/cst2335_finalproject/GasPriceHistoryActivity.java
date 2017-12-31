package com.example.algonquin.cst2335_finalproject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class GasPriceHistoryActivity extends Activity {
    private ListView gasPirceListView;
    private ArrayList<String> gasPriceArrayList = new ArrayList<>();
    private ArrayAdapter<String> stringArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_price_history);

        Bundle bundle = getIntent().getExtras();
        Bundle args = bundle.getBundle("package");
        double [] tempArray = args.getDoubleArray("displayArray");
        Log.i("wth happening here", Double.toString(tempArray[0]));


        gasPriceArrayList.add("Your Average gas price last month was : "+Double.toString(tempArray[0]) + " $");
        gasPriceArrayList.add("You bought a total of : "+Double.toString(tempArray[1]) + " L of gas last month");
        gasPriceArrayList.add("Total amount of gas bought in: ");
        gasPriceArrayList.add("January was : " + Double.toString(tempArray[2])+ " L");
        gasPriceArrayList.add("Febuary was : " + Double.toString(tempArray[3])+ " L");
        gasPriceArrayList.add("March was : " + Double.toString(tempArray[4])+ " L");
        gasPriceArrayList.add("April was : " + Double.toString(tempArray[5])+ " L");
        gasPriceArrayList.add("May was : " + Double.toString(tempArray[6])+ " L");
        gasPriceArrayList.add("June was : " + Double.toString(tempArray[7])+ " L");
        gasPriceArrayList.add("July was : " + Double.toString(tempArray[8])+ " L");
        gasPriceArrayList.add("August was : " + Double.toString(tempArray[9])+ " L");
        gasPriceArrayList.add("September was : " + Double.toString(tempArray[10])+ " L");
        gasPriceArrayList.add("October was : " + Double.toString(tempArray[11])+ " L");
        gasPriceArrayList.add("November was : " + Double.toString(tempArray[12])+ " L");
        gasPriceArrayList.add("December was : " + Double.toString(tempArray[13])+ " L");

        gasPirceListView = (ListView) findViewById(R.id.gasPriceHistoryListView);
        stringArrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item ,gasPriceArrayList);
        gasPirceListView.setAdapter(stringArrayAdapter);


    }
}
