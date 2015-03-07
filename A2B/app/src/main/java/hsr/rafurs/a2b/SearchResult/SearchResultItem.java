package hsr.rafurs.a2b.SearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.schoeb.opendatatransport.IOpenTransportRepository;
import ch.schoeb.opendatatransport.OpenTransportRepositoryFactory;
import ch.schoeb.opendatatransport.model.Connection;
import ch.schoeb.opendatatransport.model.ConnectionList;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 */
public class SearchResultItem {
    public IOpenTransportRepository repo;
    private String from;
    private String to;
    private String via;
    private boolean isArrival;
    private String strTime;
    private String strDate;


    public String GetFromStation() {
        return  this.from;
    }
    public String GetToStation() {
        return this.to;
    }

    public String GetConnectionDate() {
        return this.strDate;
    }
    public String GetConnectionTime() {
        return this.strTime;
    }
    public boolean GetIsDeparture() {
        return isArrival;
    }
    public String GetStrTime() {
        return strTime;
    }
    public String GetStrDate() {
        return strDate;
    }
    public String GetViaStation() {
        return this.via;
    }

    public SearchResultItem(String from, String to, String date, String time, boolean isArrival) {
        this.from = from;
        this.to = to;
        this.strDate = date;
        this.strTime = time;
        this.isArrival = isArrival;
        this.via = "";
    }

    public void SetViaStation(String via) {
        this.via = via;
    }

    public void search() {
        repo = OpenTransportRepositoryFactory.CreateOnlineOpenTransportRepository();
        ConnectionList cL = repo.searchConnections(this.from, this.to, this.via, this.strDate, this.strTime, this.isArrival);
        if (cL.getConnections().size() > 0) {
            for (Connection c : cL.getConnections()) {
                addItem(c);
            }
        }
    }

    public List<Connection> ITEMS = new ArrayList<Connection>();

    public Map<String, Connection> ITEM_MAP = new HashMap<String, Connection>();

    private void addItem(Connection item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.toString(), item);
    }
}
