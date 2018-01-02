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

        for (int i = 0; i < tempArray.length; i++){
            tempArray[i] = Math.round(tempArray[i]*100D)/100D;
        }

        Log.i("wth happening here", Double.toString(tempArray[0]));

        gasPriceArrayList.add(getResources().getString(R.string.AvergaeGasLastMonth)+ " " + Double.toString(tempArray[0]) + " $");
        gasPriceArrayList.add(getResources().getString(R.string.youBoughtTotalOf)+ " " + Double.toString(tempArray[1]) + " " + getResources().getString(R.string.litersOfGasLastMonth));
        gasPriceArrayList.add(getResources().getString(R.string.totalGasHeader)+" ");
        gasPriceArrayList.add(getResources().getString(R.string.January)+ " " + Double.toString(tempArray[2])+ " L");
        gasPriceArrayList.add(getResources().getString(R.string.Febuary)+" " + Double.toString(tempArray[3])+ " L");
        gasPriceArrayList.add(getResources().getString(R.string.March)+" " + Double.toString(tempArray[4])+ " L");
        gasPriceArrayList.add(getResources().getString(R.string.April)+" " + Double.toString(tempArray[5])+ " L");
        gasPriceArrayList.add(getResources().getString(R.string.May)+" " + Double.toString(tempArray[6])+ " L");
        gasPriceArrayList.add(getResources().getString(R.string.June)+" " + Double.toString(tempArray[7])+ " L");
        gasPriceArrayList.add(getResources().getString(R.string.July)+" " + Double.toString(tempArray[8])+ " L");
        gasPriceArrayList.add(getResources().getString(R.string.August)+" " + Double.toString(tempArray[9])+ " L");
        gasPriceArrayList.add(getResources().getString(R.string.September)+" " + Double.toString(tempArray[10])+ " L");
        gasPriceArrayList.add(getResources().getString(R.string.October)+" " + Double.toString(tempArray[11])+ " L");
        gasPriceArrayList.add(getResources().getString(R.string.November)+" " + Double.toString(tempArray[12])+ " L");
        gasPriceArrayList.add(getResources().getString(R.string.December)+" " + Double.toString(tempArray[13])+ " L");

        gasPirceListView = (ListView) findViewById(R.id.gasPriceHistoryListView);
        stringArrayAdapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item ,gasPriceArrayList);
        gasPirceListView.setAdapter(stringArrayAdapter);


    }
}
