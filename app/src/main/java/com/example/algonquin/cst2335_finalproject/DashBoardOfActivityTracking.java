package com.example.algonquin.cst2335_finalproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

public class DashBoardOfActivityTracking extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_of_tracking);

        //Show the top toolbar
        Toolbar topToolbar = (Toolbar)findViewById(R.id.start_toolbar_inActivityTracking);
        setSupportActionBar(topToolbar);
        getSupportActionBar().setTitle(getString(R.string.projectNameInActivityTracking));

        topToolbar.inflateMenu(R.menu.topboolbarmenu_activitytracking);//changed
        //topToolbar menu items CallBack listener
        topToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                Intent intent2;
                 if(arg0.getItemId() == R.id.action_two_p){
                     //Nutrition Tracker
                     intent2 = new Intent(DashBoardOfActivityTracking.this, FoodList.class);
                     startActivity(intent2);
                 }

                if(arg0.getItemId() == R.id.action_three_p){
                    //Start Thermostat Activity
                    intent2 = new Intent(DashBoardOfActivityTracking.this, ThermostatActivity.class);
                    startActivity(intent2);
                }

                if(arg0.getItemId() == R.id.action_four_p){
                    //Start Automobile Activity
                    intent2 = new Intent(DashBoardOfActivityTracking.this, AutomobileActivity.class);
                    startActivity(intent2);
                }

                if(arg0.getItemId() == R.id.action_five_p){
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DashBoardOfActivityTracking.this);
                    builder.setTitle(R.string.alertFinalProject);
                    builder.setMessage(getResources().getString(R.string.intro));
// Add the button
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    // Create the AlertDialog
                    android.support.v7.app.AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return true;
            }
        });

        //Show the bottom toolbar
        Toolbar myToolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

    }

    //Create a view for the menu(bottom) layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottomtoolbarmenu_activitytracking, menu);
        return true;
    }

    //Menu item selection (bottom)
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
                //Manager the calculteActivityTracking fragment
                CalculateActivityTracking fragmentCalculate = new CalculateActivityTracking();
                transaction.replace(R.id.fl_toolbar,fragmentCalculate,"calculate");
                transaction.commit();

                bundle = new Bundle();
                bundle.putString("fragment","calculate");
//                bundle.putString("button","click");
                fragmentCalculate.setArguments(bundle);
                break;
            case R.id.action_menu:
                customerDialog(onCreateDialog());
                break;
        }
        return true;
    }

    //Show customer dialog
    private void customerDialog(Dialog dialog){
        // Include dialog.xml file
        dialog.setContentView(R.layout.activitytrackinghelpdialog);
        // Set dialog title
//        dialog.setTitle("Custom Dialog");
        dialog.show();

    }

    //Set the dialog
    private Dialog onCreateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        final LayoutInflater inflater = this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.activitytrackinghelpdialog,null);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(getString(R.string.activityTrackingHelpMenuClose), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Nothing to do
                    }
                });
        return builder.create();
    }


}
