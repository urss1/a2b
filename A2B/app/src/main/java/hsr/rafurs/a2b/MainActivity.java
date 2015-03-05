package hsr.rafurs.a2b;

import ch.schoeb.opendatatransport.model.ConnectionList;
import ch.schoeb.opendatatransport.model.Station;
import ch.schoeb.opendatatransport.model.StationList;
import hsr.rafurs.a2b.*;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;


public class MainActivity extends ActionBarActivity {

    // Date Buttons and Helper-Method
    private DateHelper dateHelper = new DateHelper();
    private Button dateButton;
    private Button timeButton;
    // AutoComplete for From- and To-Station
    public AutoCompleteTextView fromStation;
    public AutoCompleteTextView toStation;
    public AutoCompleteTextView viaStation;
    // AutoComplete
    public Context mContext;
    private ArrayAdapter adaptorAutoComplete;
    //public ListView lvStations;
    ArrayList<String> alStations;
    String searchString;

    // Transport Repository
    public IOpenTransportRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Main for Menu and Icons
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setIcon(R.drawable.ic_appicon);
        // Set Content view
        setContentView(R.layout.activity_main);

        // OpenTransport
        //repo = OpenTransportRepositoryFactory.CreateLocalOpenTransportRepository();
        repo = OpenTransportRepositoryFactory.CreateOnlineOpenTransportRepository();

        // From-, To- and Via-Station AutoCompleteTextView
        mContext = this;
        alStations = new ArrayList<String>();


//        AutoCompleteTextView autoCompView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
//        autoCompView.setAdapter(new PlacesAutoCompleteAdapter(this, R.layout.list_item));
//        autoCompView.setOnItemClickListener(this);

//        adaptorAutoComplete = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, alStations);
        fromStation = (AutoCompleteTextView) findViewById(R.id.fromStation);
//        fromStation.setAdapter(adaptorAutoComplete);
        fromStation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchString = s.toString();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new FetchStationList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
                else {
                    new FetchStationList().execute();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        toStation = (AutoCompleteTextView) findViewById(R.id.toStation);
        viaStation = (AutoCompleteTextView) findViewById(R.id.viaStation);

//        fromStation.setAdapter(adapter);
//       // from.setAdapter(new PlacesAutoCompleteAdapter(this, android.R.layout.simple_dropdown_item_1line));
//        toStation.setAdapter(adapter);
//        viaStation.setAdapter(adapter);

        // GPS-Buttons
        final ImageButton setFromGps = (ImageButton) findViewById(R.id.fromSetGps);
        setFromGps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showMessage(v);
            }
        });

        final ImageButton setToGps = (ImageButton) findViewById(R.id.toSetGps);
        setToGps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showMessage(v);
            }
        });

        final ImageButton setViaGps = (ImageButton) findViewById(R.id.viaSetGps);
        setViaGps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showMessage(v);
            }
        });


        // Date and Time Buttons
        dateButton = (Button) findViewById(R.id.btnDate);
        dateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setDateButtonSelect();
            }
        });
        timeButton = (Button) findViewById(R.id.btnTime);
        timeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setTimeButtonSelect();
            }
        });
        refreshDateTime();

        final ImageButton refreshDateTimeButton = (ImageButton) findViewById(R.id.refreshDateTime);
        refreshDateTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                refreshDateTime();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.actionFavorite:
                // TODO: Favoriten korrekt implementieren
                startActivity(new Intent(this, about.class));
                break;
            case R.id.actionClock:
                startActivity(new Intent(this, clock.class));
                break;
            case R.id.actionAbout:
                startActivity(new Intent(this, about.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showMessage(View v) {
        Toast.makeText(getApplicationContext(), "From GPS", Toast.LENGTH_SHORT).show();
    }

    // Setzt das aktuelle Datum und die Zeit
    // beim onCreate und beim Klicken des Refresh Buttons
    private void refreshDateTime() {
        if (dateButton != null) {
            dateButton.setText(dateHelper.GetDateNow());
        }
        if (timeButton != null) {
            timeButton.setText(dateHelper.GetTimeNow());
        }
    }

    // Date Dialog für die Auswahl des Buttons
    private void setDateButtonSelect() {
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        dateButton.setText(dateHelper.GetDateFormat(dayOfMonth, monthOfYear, year));
                    }
                }, dateHelper.GetYear(), dateHelper.GetMonth(), dateHelper.GetDay());
        dpd.show();
    }
    // Time Dialog für die Auswahl der Zeit, anzeige auf dem Button
    private void setTimeButtonSelect() {
        TimePickerDialog tpd = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        timeButton.setText(hourOfDay + ":" + minute);
                    }
                }, dateHelper.GetHour(), dateHelper.GetMinute(), true); // True für 24h Format, False für 12h Format.
        tpd.show();
    }

    public class FetchStationList extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            alStations.clear();

            StationList sl = repo.findStations(searchString);

            if (sl.getStations().size() > 0) {
                for (Station s : sl.getStations()) {
                    alStations.add(s.getName());
                }
            }

            runOnUiThread(new Runnable() {
                public void run() {
                  adaptorAutoComplete = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, alStations);
                  fromStation.setAdapter(adaptorAutoComplete);
                  adaptorAutoComplete.notifyDataSetChanged();
                }
            });
            return null;
        } // method ends
    }


//    class GetStations extends AsyncTask<String, Void, ArrayList<String>> {
//        @Override
//        // three dots is java for an array of strings
//        protected ArrayList<String> doInBackground(String... args) {
//
//            ArrayList<String> predictionsArr = new ArrayList<String>();
//
//            try {
//                String searchString = args[0].toString();
//                StationList sl = repo.findStations(searchString);
//
//                if (sl.getStations().size() > 0) {
//                    for (Station s : sl.getStations()) {
//                        predictionsArr.add(s.getName());
//                    }
//                }
//            } catch (Exception e) {
//                return predictionsArr;
//            }
//
//                return predictionsArr;
//
//        }
//
////then our post
//
//        @Override
//        protected void onPostExecute(ArrayList<String> result) {
//            adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1);
//            adapter.setNotifyOnChange(true);
//            //attach the adapter to textview
//            fromStation.setAdapter(adapter);
//            for (String string : result) {
//
//                adapter.add(string);
//                adapter.notifyDataSetChanged();
//
//            }
//        }
//
//    }

};
