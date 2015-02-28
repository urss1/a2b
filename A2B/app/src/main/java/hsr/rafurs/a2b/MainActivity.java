package hsr.rafurs.a2b;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        switch(item.getItemId()) {
            case R.id.actionAbout:
                //Toast.makeText(getApplicationContext(),"Hi, my name is",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, about.class));
                break;
        }

        return super.onOptionsItemSelected(item);


//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.actionAbout) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
    }
}
