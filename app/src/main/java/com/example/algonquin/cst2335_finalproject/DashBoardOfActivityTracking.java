package com.example.algonquin.cst2335_finalproject;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class DashBoardOfActivityTracking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_of_tracking);

        //Show the toolbar
        Toolbar myToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

    }

    //Create a view for the menu layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menubar, menu);
        return true;
    }

    //Menu item selection
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        FragmentManager fm = getFragmentManager();;
        FragmentTransaction transaction = fm.beginTransaction();
        Bundle bundle;
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.action_one:
                //Manager the addActivityTracking fragment
                addActivityTracking fragmentAdd = new addActivityTracking();
                transaction.replace(R.id.fl_toolbar,fragmentAdd,"add");
                transaction.commit();

                bundle = new Bundle();
                bundle.putString("fragment","add");
                fragmentAdd.setArguments(bundle);


//                MessageFragment currentFragment = (MessageFragment)getFragmentManager().findFragmentByTag("display");

                //Toast.makeText(this,"Action One",Toast.LENGTH_SHORT).show();

/*
                Snackbar.make(findViewById(android.R.id.content),message, Snackbar.LENGTH_LONG)
                        // .setAction("Undo", mOnClickListener)
                        //.setActionTextColor(Color.RED)
                        .show();
*/
                break;
            case R.id.action_two:
                //Manager the addActivityTracking fragment
                ViewActivityTracking fragmentView = new ViewActivityTracking();
                transaction.replace(R.id.fl_toolbar,fragmentView,"view");
                transaction.commit();

                bundle = new Bundle();
                bundle.putString("fragment","view");
                bundle.putString("button","click");
                fragmentView.setArguments(bundle);

               break;
            case R.id.action_three:
//                customerDialog(onCreateDialog());
//                Toast.makeText(this,"Action Three",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_menu:
                Toast.makeText(this,"Version 1.0, by Huaming Cai",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

}
