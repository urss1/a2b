package ch.schoeb.opendatatransport.model;

import java.util.List;

public class Connection{
    private Number capacity1st;
    private Number capacity2nd;
    private String duration;
    private ConnectionStation from;
    private List<String> products;
    private List<Section> sections;
    private Service service;
    private ConnectionStation to;
    private Number transfers;

    public Number getCapacity1st(){
        if (this.capacity1st != null) {
            return this.capacity1st;
        }
        return 0;
    }
    public void setCapacity1st(Number capacity1st){
        this.capacity1st = capacity1st;
    }
    public Number getCapacity2nd(){
        if (this.capacity2nd != null) {
            return this.capacity2nd;
        }
        return 0;
    }
    public void setCapacity2nd(Number capacity2nd){
        this.capacity2nd = capacity2nd;
    }
    public String getDuration(){
        return this.duration;
    }
    // ToDo: Anpassung für die Anzeige
    public String getDurationFormat() {
        if (this.duration == "") {
            return "";
        }
        String[] splitDuration = this.duration.split("d");
        if (Integer.parseInt(splitDuration[0]) > 0) {
            return "+" + Integer.parseInt(splitDuration[0]);
        }
        else {
            String[] splitTimeDuration = splitDuration[1].split(":");
            return Integer.parseInt(splitTimeDuration[0]) + ":" + splitTimeDuration[1];
        }
    }
    public void setDuration(String duration){
        this.duration = duration;
    }
    public ConnectionStation getFrom(){
        return this.from;
    }
    public void setFrom(ConnectionStation from){
        this.from = from;
    }
    public List getProducts(){
        return this.products;
    }
    public void setProducts(List products){
        this.products = products;
    }
    public List<Section> getSections(){
        return this.sections;
    }
    public void setSections(List<Section> sections){
        this.sections = sections;
    }
    public Service getService(){
        return this.service;
    }
    public void setService(Service service){
        this.service = service;
    }
    public ConnectionStation getTo(){
        return this.to;
    }
    public void setTo(ConnectionStation to){
        this.to = to;
    }
    public Number getTransfers(){
        return this.transfers;
    }
    public void setTransfers(Number transfers){
        this.transfers = transfers;
    }

    @Override
    public String toString(){
//        return "Test" + this.transfers;
        return from.getStation().getName() + " -> " + to.getStation().getName() + " on " + from.getDeparture();
    }
}