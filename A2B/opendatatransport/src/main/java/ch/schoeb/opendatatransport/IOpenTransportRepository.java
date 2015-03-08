package ch.schoeb.opendatatransport;

import ch.schoeb.opendatatransport.model.ConnectionList;
import ch.schoeb.opendatatransport.model.StationList;

public interface IOpenTransportRepository {

    StationList findStations(String query);

    StationList findStationsByRafurs(Double x, Double y);

    ConnectionList searchConnections(String from, String to);

    ConnectionList searchConnections(String from, String to, String via, String date, String time, Boolean isArrivalTime);

}
