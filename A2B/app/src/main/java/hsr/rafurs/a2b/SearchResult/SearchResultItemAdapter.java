package hsr.rafurs.a2b.SearchResult;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import ch.schoeb.opendatatransport.model.Connection;
import hsr.rafurs.a2b.DateHelper;
import hsr.rafurs.a2b.R;
import hsr.rafurs.a2b.ResultActivity;
import hsr.rafurs.a2b.ResultDetail;

/**
 * Created by admin on 07.03.2015.
 */
public class SearchResultItemAdapter extends ArrayAdapter<Connection> {

    private DateHelper dHelper = new DateHelper();

    public SearchResultItemAdapter(Context context, ArrayList<Connection> connections) {
        super(context, 0, connections);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Connection connection = getItem(position);
        if (convertView == null) {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.listview_resultitem, parent, false);
        }

        TextView fromTime = (TextView) convertView.findViewById(R.id.lvriFromTime);
        fromTime.setText(dHelper.GetTimeFromDate(connection.getFrom().getDeparture()));
        TextView toTime = (TextView) convertView.findViewById(R.id.lvriToTime);
        toTime.setText(dHelper.GetTimeFromDate(connection.getTo().getArrival()));
        TextView duration = (TextView) convertView.findViewById(R.id.lvriDuration);
        duration.setText(connection.getDuration());
        TextView transfers = (TextView) convertView.findViewById(R.id.lvriTransfers);
        transfers.setText(connection.getTransfers().toString());
        TextView informations = (TextView) convertView.findViewById(R.id.lvriInformations);
        informations.setText("wieteresss");

        ImageButton showDetail = (ImageButton) convertView.findViewById(R.id.imgBtnShowDetailConnection);
        showDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent searchResultDetailActivity = new Intent((ResultActivity)getContext(), ResultDetail.class);
                    searchResultDetailActivity.putExtra("position", position);
                    getContext().startActivity(searchResultDetailActivity);
                }
        });
        return convertView;

    }
}
