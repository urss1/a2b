package hsr.rafurs.a2b;

import java.util.*;
import java.text.*;
/**
 * Created by admin on 01.03.2015.
 */
public class DateHelper {

    public String GetDateNow() {
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("dd.MM.yyyy");

        return ft.format(dNow);
    }

    public String GetTimeNow() {
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("hh:mm");

        return ft.format(dNow);
    }
}
