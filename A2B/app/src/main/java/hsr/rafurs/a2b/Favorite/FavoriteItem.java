package hsr.rafurs.a2b.Favorite;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import hsr.rafurs.a2b.Global;

/**
 * Created by admin on 08.03.2015.
 */
public class FavoriteItem {
    private final String sharedSetKey = "favorite";
    private SharedPreferences sharedPreferences = Global.sharePreference;
    private HashSet<String> hashSet = new HashSet<String>();

    public FavoriteItem() {
        hashSet =  new HashSet<String>(sharedPreferences.getStringSet(sharedSetKey, new HashSet<String>()));
    }

    public String GetFavorite(int position) {
        return hashSet.toArray()[position].toString();
    }

    public boolean AddFavorite(String from, String to) {
        String addFavorite = CreateFormat(from, to);

        if (CheckIsFavoriteInStore(addFavorite)) {
            return false;
        }
        hashSet.add(addFavorite);
        SaveHashSet();
        return true;
    }

    private boolean CheckIsFavoriteInStore(String toCheck) {
        return hashSet.contains(toCheck);
    }

    private String CreateFormat(String from, String to) {
        return from + ";" + to;
    }

    public boolean DeleteFavorite(int position) {
        hashSet.remove(hashSet.toArray()[position]);
        SaveHashSet();
        return true;
    }

    public ArrayList<String> GetArrayList() {
        ArrayList<String> result = new ArrayList<String>();
        for (String s : hashSet) {
            result.add(s.replace(";", " - "));
        }
        return result;
    }

    private void SaveHashSet() {
        sharedPreferences.edit().putStringSet(sharedSetKey, hashSet).commit();
    }
}
