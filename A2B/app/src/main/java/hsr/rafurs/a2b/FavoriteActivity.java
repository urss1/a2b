package hsr.rafurs.a2b;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import hsr.rafurs.a2b.Favorite.FavoriteItem;


public class FavoriteActivity extends ActionBarActivity {

    private  ArrayList<String> arrayListFavorites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Main for Menu and Icons
        final android.support.v7.app.ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setIcon(R.drawable.ic_appicon);
        // Set View
        setContentView(R.layout.activity_favorite);

        // Write Data
        final FavoriteItem favItem = new FavoriteItem();

        arrayListFavorites = favItem.GetArrayList();

        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayListFavorites);

        ListView listView = (ListView) findViewById(R.id.listViewFavorite);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                Global.searchResultItem.SetFavorite(favItem.GetFavorite(position));
                Intent indent = new Intent(view.getContext(), MainActivity.class);
                startActivity(indent);
//                Toast.makeText(view.getContext(), "Ich bin " + item, Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                favItem.DeleteFavorite(position);
                arrayListFavorites.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(view.getContext(), R.string.deleteFavoriteMessage, Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.actionMain:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.actionAbout:
                startActivity(new Intent(this, about.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
