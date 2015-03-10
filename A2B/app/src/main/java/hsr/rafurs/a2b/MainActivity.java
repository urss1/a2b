package hsr.rafurs.a2b;

import ch.schoeb.opendatatransport.model.Station;
import ch.schoeb.opendatatransport.model.StationList;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;
import hsr.rafurs.a2b.SearchResult.SearchResultItem;


public class MainActivity extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // Date Buttons and Helper-Method
    private DateHelper dateHelper = new DateHelper();
    private Button dateButton;
    private Button timeButton;
    private ToggleButton tb;
    // AutoComplete for From- and To-Station
    public int setAdapterOnView = 0; // 1 = From, 2 = To, 3 = via
    public AutoCompleteTextView fromStation;
    public AutoCompleteTextView toStation;
    public AutoCompleteTextView viaStation;
    // AutoComplete
    public Context mContext;
    private ArrayAdapter adaptorAutoComplete;
    //public ListView lvStations;
    ArrayList<String> alStations;
    String searchString;
    public int actAsyncTasks = 0;

    // Set Field for Update with Location
    public int setPositionOnView = 0; // 1 = From, 2 = To, 3 = via
    // Google API
    private GoogleApiClient googleApiClient;
    private Location lastLocation;

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

        // Preference
        SharedPreferences sharePreference = this.getSharedPreferences(getString(R.string.sharePreferenceKey), Context.MODE_PRIVATE);
        Global.sharePreference = sharePreference;

        if (Global.searchResultItem == null) {
            Global.searchResultItem = new SearchResultItem();
        }
        // Inti Google API
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        // From-, To- and Via-Station AutoCompleteTextView
        mContext = this;
        alStations = new ArrayList<String>();

        fromStation = (AutoCompleteTextView) findViewById(R.id.fromStation);
        fromStation.setText(Global.searchResultItem.GetFromStation());
        fromStation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setAdapterOnView = 1;
                runFetchStationList(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Global.searchResultItem.SetFromStation(s.toString());
            }
        });
        toStation = (AutoCompleteTextView) findViewById(R.id.toStation);
        toStation.setText(Global.searchResultItem.GetToStation());
        toStation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setAdapterOnView = 2;
                runFetchStationList(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Global.searchResultItem.SetToStation(s.toString());
            }
        });
        viaStation = (AutoCompleteTextView) findViewById(R.id.viaStation);
        viaStation.setText(Global.searchResultItem.GetViaStation());
        viaStation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setAdapterOnView = 3;
                runFetchStationList(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                Global.searchResultItem.SetViaStation(s.toString());
            }
        });

        // GPS-Buttons
        final ImageButton setFromGps = (ImageButton) findViewById(R.id.fromSetGps);
        setFromGps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPositionOnView = 1;
                SetGpsPosition();
            }
        });

        final ImageButton setToGps = (ImageButton) findViewById(R.id.toSetGps);
        setToGps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPositionOnView = 2;
                SetGpsPosition();
            }
        });

        final ImageButton setViaGps = (ImageButton) findViewById(R.id.viaSetGps);
        setViaGps.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPositionOnView = 3;
                SetGpsPosition();
            }
        });


        // Date and Time Buttons
        dateButton = (Button) findViewById(R.id.btnDate);
        dateButton.setText(Global.searchResultItem.GetConnectionDate());
        dateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setDateButtonSelect();
            }
        });
        timeButton = (Button) findViewById(R.id.btnTime);
        timeButton.setText(Global.searchResultItem.GetConnectionTime());
        timeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setTimeButtonSelect();
            }
        });
        tb = (ToggleButton) findViewById(R.id.togBtnDepArr);
        tb.setChecked(Global.searchResultItem.GetIsDeparture());
        tb.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View v) {
                                      Global.searchResultItem.SetIsArrival(tb.isChecked());
                                  }
                              }
        );

        final ImageButton refreshDateTimeButton = (ImageButton) findViewById(R.id.refreshDateTime);
        refreshDateTimeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                refreshDateTime();
            }
        });

        // Change Directions
        final Button changeDirections = (Button) findViewById(R.id.btnChangeDirection);
        changeDirections.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeDirections(v);
            }
        });

        // Search Button
        final Button searchConnection = (Button) findViewById(R.id.btnSearchConnection);
        searchConnection.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                runSearchConnection();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
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
                startActivity(new Intent(this, FavoriteActivity.class));
                break;
            case R.id.actionAbout:
                startActivity(new Intent(this, about.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showMessage( String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void changeDirections(View v) {

        if (isEmpty(fromStation)) {
            showMessage(getString(R.string.errorMessageFromIsEmpty));
            return;
        }
        if (isEmpty(toStation)) {
            showMessage(getString(R.string.errorMessageToIsEmpty));
            return;
        }

        String fStation = fromStation.getText().toString();
        String tStation = toStation.getText().toString();
        fromStation.setText(tStation);
        toStation.setText(fStation);
        Global.searchResultItem.SetFromStation(tStation);
        Global.searchResultItem.SetToStation(fStation);
        return;
    }

    private boolean isEmpty(AutoCompleteTextView etText) {
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    // Setzt das aktuelle Datum und die Zeit
    // beim onCreate und beim Klicken des Refresh Buttons
    private void refreshDateTime() {
        if (dateButton != null) {
            Global.searchResultItem.SetDate(dateHelper.GetDateNow());
            dateButton.setText(dateHelper.GetDateNow());
        }
        if (timeButton != null) {
            Global.searchResultItem.SetTime(dateHelper.GetTimeNow());
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
                        Global.searchResultItem.SetDate(dateHelper.GetDateFormat(dayOfMonth, monthOfYear, year));
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
                        String newTimeString = (hourOfDay < 10 ? "0" :"") +  hourOfDay + ":" + (minute < 10 ? "0" : "") +  minute;
                        timeButton.setText(newTimeString);
                        Global.searchResultItem.SetTime(newTimeString);
                    }
                }, dateHelper.GetHour(), dateHelper.GetMinute(), true); // True für 24h Format, False für 12h Format.
        tpd.show();
    }

    private void runSearchConnection() {

        if (isEmpty(fromStation)) {
            showMessage(getString(R.string.errorMessageFromIsEmpty));
            return;
        }
        if (isEmpty(toStation)) {
            showMessage(getString(R.string.errorMessageToIsEmpty));
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new FetchSearchConnections().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new FetchSearchConnections().execute();
        }
    }

    public class FetchSearchConnections extends AsyncTask<Void, Void, SearchResultItem> {
        public ProgressDialog pDia;
        public SearchResultItem sItem;

        @Override
        protected void onPreExecute() {
            sItem = Global.searchResultItem;
            pDia = ProgressDialog.show(MainActivity.this, getString(R.string.pleaseWaitTitle), getString(R.string.pleaseWaitSearchMsg), true);
            pDia.setCancelable(false);
        }

        @Override
        protected SearchResultItem doInBackground(Void... arg0) {
            if (viaStation.getText().length() > 0) {
                sItem.SetViaStation(viaStation.getText().toString());
            }

            sItem.search();
            return sItem;
        } // method ends

        @Override
        protected void onPostExecute(SearchResultItem sItem) {
            super.onPostExecute(sItem);

            pDia.hide();
            pDia.dismiss();

            if (sItem.ITEMS.size() > 0) {
                Global.searchResultItem = sItem;
                Intent searchResultActivity = new Intent(MainActivity.this, ResultActivity.class);
                startActivity(searchResultActivity);
            } else {
                showMessage(getString(R.string.pleaseWaitSearchError));
            }

        }
    }

    private void runFetchStationList(CharSequence s, int start, int before, int count) {
        searchString = s.toString();
        actAsyncTasks++;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new FetchStationList().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            new FetchStationList().execute();
        }
    }

    public class FetchStationList extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            alStations.clear();

            StationList sl = repo.findStations(searchString);

            if (sl.getStations().size() > 0) {
                for (Station s : sl.getStations()) {
                    alStations.add(s.getName());
                }
            }

//            runOnUiThread(new Runnable() {
//                              public void run() {
//                                  if (actAsyncTasks == 1) {
//                                      adaptorAutoComplete = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, alStations);
//                                      if (setAdapterOnView == 1) {
//                                          fromStation.setAdapter(adaptorAutoComplete);
//                                      } else if (setAdapterOnView == 2) {
//                                          toStation.setAdapter(adaptorAutoComplete);
//                                      } else if (setAdapterOnView == 3) {
//                                          viaStation.setAdapter(adaptorAutoComplete);
//                                      }
//                                      adaptorAutoComplete.notifyDataSetChanged();
//                                      actAsyncTasks--;
//                                  } else {
//                                      actAsyncTasks--;
//                                  }
//                              }
//                          }
//
//            );
            return null;
        } // method ends

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (actAsyncTasks == 1) {
                                      adaptorAutoComplete = new ArrayAdapter<String>(mContext, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>(alStations));
                                      if (setAdapterOnView == 1) {
                                          fromStation.setAdapter(adaptorAutoComplete);
                                      } else if (setAdapterOnView == 2) {
                                          toStation.setAdapter(adaptorAutoComplete);
                                      } else if (setAdapterOnView == 3) {
                                          viaStation.setAdapter(adaptorAutoComplete);
                                      }
                                      adaptorAutoComplete.notifyDataSetChanged();
                                      actAsyncTasks--;
                                  } else {
                                      actAsyncTasks--;
                                  }
        }
    }

    private void SetGpsPosition() {
        if (lastLocation == null) {
            showMessage(getString(R.string.pleaseWaitLocationError));
            return;
        }
        double lat = lastLocation.getLatitude(); //47.466004 = x
        double lon = lastLocation.getLongitude(); // 8.9931286 = y
//        StationList sListe = repo.findStations("&x=47.466004&y=8.9931286");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new FetchStationLocation().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, lat, lon);
        } else {
            new FetchStationLocation().execute(lat,lon);
        }
    }

    public class FetchStationLocation extends AsyncTask<Double, Void, String> {
        public ProgressDialog pDia1;
        public SearchResultItem sItem;

        @Override
        protected void onPreExecute() {
            sItem = Global.searchResultItem;
            pDia1 = ProgressDialog.show(MainActivity.this, getString(R.string.pleaseWaitTitle), getString(R.string.pleaseWaitLocationMsg), true);
            pDia1.setCancelable(true);
        }

        @Override
        protected String doInBackground(Double... arg0) {
            StationList sl = repo.findStationsByRafurs(arg0[0].doubleValue(), arg0[1].doubleValue());

            if (sl.getStations().size() > 0) {
                for (Station s : sl.getStations()) {
                    return s.getName();
                }
            }
            return "";
        } // method ends

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDia1.hide();

            if (result.length() == 0) {
                showMessage(getString(R.string.pleaseWaitSearchError));
            }
            else if (setPositionOnView == 1) {
                fromStation.setText(result);
                sItem.SetFromStation(result);
            } else if (setPositionOnView == 2) {
                toStation.setText(result);
                sItem.SetToStation(result);
            } else if (setPositionOnView == 3) {
                viaStation.setText(result);
                sItem.SetViaStation(result);
            } else {
            }

        }
    }



    // Google API -->
    @Override
    public void onConnected(Bundle bundle) {
        lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        showMessage(getString(R.string.errorMessageConnection) + connectionResult.getErrorCode());
    }
    // -->

    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        View v = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (v instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP && (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w.getBottom()) ) {
                hideSoftKeyboard(v);
            }
        }
        return ret;
    }
};
