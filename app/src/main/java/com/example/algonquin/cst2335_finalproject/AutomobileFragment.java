package com.example.algonquin.cst2335_finalproject;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class AutomobileFragment extends Fragment {
    public boolean isPhone;
    ListView lv;
    EditText gasPriceET, gasVolumeET;
    private Button savebtn;
    private Button deletebtn;
    private Button editBtn;
    ArrayList<String> messageList;
    SQLiteDatabase db;
    private AutomobileInformation autoInformation;
    private String [] automobileInfoArray;
    private String gasPrice, gasVolume, date = "";

    public  AutomobileFragment(){

    }

    @SuppressLint("ValidFragment")
    public AutomobileFragment(boolean isPhone){
        this.isPhone = isPhone;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_automobile_fragment, container, false);



        return null;
    }
}
