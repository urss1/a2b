package ch.schoeb.opendatatransport;

import com.google.gson.Gson;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import ch.schoeb.opendatatransport.model.ConnectionList;
import ch.schoeb.opendatatransport.model.StationList;

public class OnlineOpenTransportRepository implements IOpenTransportRepository {
    @Override
    public StationList findStations(String query) {
        String url = buildFindStationsUrl(query);
        String json = GetJson(url);

        Gson gson = new Gson();
        return gson.fromJson(json, StationList.class);
    }

    private String buildFindStationsUrl(String query) {
        String url = null;
        try {
            url = "http://transport.opendata.ch/v1/locations?query=" + URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        return url;
    }

    @Override
    public ConnectionList searchConnections(String from, String to) {
        return searchConnections(from, to, null, null, null, false);
    }

    @Override
    public ConnectionList searchConnections(String from, String to, String via, String date, String time, Boolean isArrivalTime) {
        String url = buildSearchConnectionUrl(from, to, via, date, time, isArrivalTime);
        String json = GetJson(url);

        Gson gson = new Gson();
        return gson.fromJson(json, ConnectionList.class);
    }

    private String GetJson(String url) {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        ResponseHandler<String> handler = new BasicResponseHandler();
        String json = "";
        try {
            json = client.execute(request, handler);
        } catch (IOException e) {
        }
        return json;
    }

    private String buildSearchConnectionUrl(String from, String to, String via, String date, String time, Boolean isArrivalTime) {
        String url = null;
        try {
            url = "http://transport.opendata.ch/v1/connections?from=" + URLEncoder.encode(from, "UTF-8") + "&to=" + URLEncoder.encode(to, "UTF-8");

            if (via != null && via != "") {
                url += "&via[]=" + via;
            }

            if (date != null && date != "") {
                url += "&date=" + date;
            }

            if (time != null && time != "") {
                url += "&date=" + time;
            }

            if (isArrivalTime) {
                url += "&url=1";
            }

        } catch (UnsupportedEncodingException e) {

        }
        return url;
    }
}
