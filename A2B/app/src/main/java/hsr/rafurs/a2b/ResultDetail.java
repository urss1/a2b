package hsr.rafurs.a2b;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import ch.schoeb.opendatatransport.model.Connection;
import hsr.rafurs.a2b.SearchResult.SearchResultItem;


public class ResultDetail extends ActionBarActivity {
    private Connection connection;
    private int actPostion = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Main for Menu and Icons
        android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setIcon(R.drawable.ic_appicon);


        actPostion = getIntent().getIntExtra("position", -1);

        if (actPostion == -1) {
            setContentView(R.layout.activity_result_detail);
        }
        else {
            setContentView(R.layout.activity_result_detail);
            // Get Data
            SearchResultItem searchResultItem = Global.searchResultItem;
            connection = searchResultItem.GetConnection(actPostion);
            // Show Data
            TextView text = (TextView) findViewById(R.id.text1);
            text.setText(connection.toString());
            TextView text2 = (TextView) findViewById(R.id.text2);
            text2.setText("Ich bin " + actPostion);
            // Add to Favortie
            final ImageButton shareConnection = (ImageButton) findViewById(R.id.imgButShareConnection);
            shareConnection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareConnection();
                }
            });
            // Add to Clock

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_result_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


    private void shareConnection() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        // ToDo: Besserer Text machen
        sendIntent.putExtra(Intent.EXTRA_TEXT, connection.toString());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }



}
