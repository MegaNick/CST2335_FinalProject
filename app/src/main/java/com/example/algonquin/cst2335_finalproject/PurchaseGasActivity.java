package com.example.algonquin.cst2335_finalproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PurchaseGasActivity extends Fragment {
    protected static final String ACTIVITY_NAME = "PurchaseGas";

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
    private boolean isPhone = true;

    public PurchaseGasActivity (){

    }
    @SuppressLint("ValidFragment")
    public PurchaseGasActivity(boolean isPhone){
        this.isPhone = isPhone;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_purchase_gas, container, false);
        lv = (ListView) view.findViewById(R.id.list);
        savebtn = (Button) view.findViewById(R.id.confirmPurchaseGasButton);
        deletebtn = (Button) view.findViewById(R.id.deleteGas);
        editBtn = (Button) view.findViewById(R.id.editGasButton);
        gasPriceET = (EditText) view.findViewById(R.id.enterGasPriceET);
        gasVolumeET = (EditText) view.findViewById(R.id.enterGasVolumeET);

        //Bundle bundle = new Bundle();
        //bundle.getBundle("package");
        final Bundle args = this.getArguments();

        messageList = new ArrayList<>();

        int addButtonClicked = args.getInt("addButtonClicked");

        //add not clicked
        if (addButtonClicked != 1){
           // Bundle bundle = new Bundle();
           // Bundle args1 = bundle.getBundle("package");
            String [] tempArray = args.getStringArray("info");
            gasPriceET.setText(tempArray[1]);
            gasVolumeET.setText(tempArray[2]);

        }else{
            deletebtn.setEnabled(false);
            editBtn.setEnabled(false);
        }


        //add a purchase
        savebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                date = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                gasPrice = gasPriceET.getText().toString();
                gasVolume = gasVolumeET.getText().toString();
                automobileInfoArray = new String [] {date, gasPrice, gasVolume};
                for (int i = 0; i<automobileInfoArray.length;i++){
                    if (automobileInfoArray[i]==null){
                        automobileInfoArray[i]="0";
                    }
                }

                if (checkInput()){
                    String phone = (String.valueOf(isPhone));
                    Log.i("IS PHONE",phone);

                    if (isPhone){
                        Toast.makeText(getContext(), "Your Purchase has been recorded!", Toast.LENGTH_SHORT).show();
                        Log.i("ARRAY VALUE", automobileInfoArray[0]);
                        Intent intent = new Intent();
                        intent.putExtra("data", automobileInfoArray);
                        getActivity().setResult(11,intent);
                        getActivity().finish();
                    }else{
                        Toast.makeText(getContext(), "Your Purchase has been recorded!", Toast.LENGTH_SHORT).show();
                        ((AutomobileActivity)getActivity()).addEntry((arrayToEntry(automobileInfoArray)));
                        getFragmentManager().popBackStackImmediate();
                    }

                }

            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Bundle bundle = new Bundle();
                //Bundle args = bundle.getBundle("package");
                String [] tempArray =  args.getStringArray("info");
                gasPriceET.setText(tempArray[1]);
                gasVolumeET.setText(tempArray[2]);
                Intent intent = new Intent();
                intent.putExtra("secondClass",args);

                if (isPhone){


                    getActivity().setResult(12,intent);
                    getActivity().finish();
                }else{
                    int position = args.getInt("position");
                    long ID = args.getLong("ID");


                    ((AutomobileActivity)getActivity()).deleteEntry(position,ID);
                    getFragmentManager().popBackStackImmediate();
                }

                Toast.makeText(getContext(), "Entry deleted", Toast.LENGTH_SHORT).show();

            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                date = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
                gasPrice = gasPriceET.getText().toString();
                gasVolume = gasVolumeET.getText().toString();
                automobileInfoArray = new String [] {date, gasPrice, gasVolume};

                if (checkInput()){
                    if (isPhone){

                        Intent intent = new Intent();
                        intent.putExtra("data", automobileInfoArray);
                        getActivity().setResult(13,intent);
                        getActivity().finish();
                    }else{

                        int position = args.getInt("position");
                        long ID = args.getLong("ID");

                        ((AutomobileActivity)getActivity()).editEntry(position,ID,arrayToEntry(automobileInfoArray));
                        getFragmentManager().popBackStackImmediate();
                    }
                }
                Toast.makeText(getContext(), "Your Purchase has been Saved", Toast.LENGTH_SHORT).show();
            }
        });


        return view;

    }

    public AutomobileInformation arrayToEntry(String[] tempArray){
        return new AutomobileInformation(tempArray[0], tempArray[1],tempArray[2]);
    }

    public boolean checkInput(){
        if (gasPriceET.getText().toString().equals("")){
            Toast.makeText(getContext(), "Please enter a gas price", Toast.LENGTH_SHORT).show();
            return false;
        }else if (gasVolumeET.getText().toString().equals("")){
            Toast.makeText(getContext(), "Please enter a the amount of gas (Liters)", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }

    }

}
