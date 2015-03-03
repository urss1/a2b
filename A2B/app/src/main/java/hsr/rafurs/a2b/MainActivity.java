package hsr.rafurs.a2b;

import hsr.rafurs.a2b.*;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;


public class MainActivity extends ActionBarActivity {

    private DateHelper dateHelper = new DateHelper();
    private Button dateButton;
    private Button timeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setIcon(R.drawable.ic_appicon);

        setContentView(R.layout.activity_main);


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

        IOpenTransportRepository repo = OpenTransportRepositoryFactory.CreateLocalOpenTransportRepository();
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

    private void refreshDateTime() {
        if (dateButton != null) {
            setDateButtonText();
        }
        if (timeButton != null) {
            setTimeButtonText();
        }
    }

    private void setDateButtonText() {
        dateButton.setText(dateHelper.GetDateNow());
    }

    private void setTimeButtonText() {
        timeButton.setText(dateHelper.GetTimeNow());
    }

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

    private void setTimeButtonSelect() {
        TimePickerDialog tpd = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        timeButton.setText(hourOfDay + ":" + minute);
                    }
                }, dateHelper.GetHour(), dateHelper.GetMinute(), true);
        tpd.show();

    }
};
