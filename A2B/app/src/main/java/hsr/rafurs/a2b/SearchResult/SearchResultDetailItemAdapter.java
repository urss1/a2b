package hsr.rafurs.a2b.SearchResult;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import ch.schoeb.opendatatransport.model.Connection;
import ch.schoeb.opendatatransport.model.Section;
import hsr.rafurs.a2b.DateHelper;
import hsr.rafurs.a2b.R;
import hsr.rafurs.a2b.ResultActivity;
import hsr.rafurs.a2b.ResultDetail;

/**
 * Created by admin on 07.03.2015.
 */
public class SearchResultDetailItemAdapter extends ArrayAdapter<Section> {

    private DateHelper dHelper = new DateHelper();

    public SearchResultDetailItemAdapter(Context context, ArrayList<Section> sections) {
        super(context, 0, sections);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Section section = getItem(position);
        if (convertView == null) {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.listview_resultdetail, parent, false);
        }

        TextView from = (TextView) convertView.findViewById(R.id.liDetFromStation);
        from.setText(section.getDeparture().getStation().getName());

        TextView fromTime = (TextView) convertView.findViewById(R.id.liDetFromStationTime);
        fromTime.setText("Ab: " + dHelper.GetTimeFromDate(section.getDeparture().getDeparture()));

        TextView fromPlatform = (TextView) convertView.findViewById(R.id.liDetFromStationPlatform);
        fromPlatform.setText("Gleis: " + section.getDeparture().getPlatform());

        TextView journeyName = (TextView) convertView.findViewById(R.id.liDetJourneyName);
        if (section.getJourney() == null) {
            if (section.getWalk() != null) {
                journeyName.setText("Fussweg: " + section.getWalk().getDuration());
            }
            else {
                journeyName.setText("-");
            }
        }
        else {
            journeyName.setText(section.getJourney().getName());
        }

        TextView to = (TextView) convertView.findViewById(R.id.liDetToStation);
        to.setText(section.getArrival().getStation().getName());

        TextView toTime = (TextView) convertView.findViewById(R.id.liDetToStationTime);
        toTime.setText("An: " + dHelper.GetTimeFromDate(section.getArrival().getArrival()));


        TextView toPlatform = (TextView) convertView.findViewById(R.id.liDetToStationPlatform);
        toPlatform.setText("Gleis: " + section.getArrival().getPlatform());

//        TextView delay = (TextView) convertView.findViewById(R.id.lvriDelay);
//        if (connection.getFrom().getDelay().equals("0")) {
//            delay.setText("");
//        }
//        else {
//            delay.setText(connection.getFrom().getDelay());
//        }
//        TextView first = (TextView) convertView.findViewById(R.id.lvriFristClass);
//        String fCap = "1: ";
//        for (int i = 0; i < connection.getCapacity1st().intValue(); i++) {
//            fCap = fCap + "*";
//        }
//        first.setText(fCap);
//        TextView second = (TextView) convertView.findViewById(R.id.lvriSecondClass);
//        String sCap = "2: ";
//        for (int i = 0; i < connection.getCapacity2nd().intValue(); i++) {
//            sCap = sCap + "*";
//        }
//        second.setText(sCap);

        return convertView;

    }
}
