package me.karavaillancourt.wheelofeats;

/**
 * Created by kvaillancourt on 8/9/15.
 */
public class Resturant {

    private final String id;
    private final String name;
    private double latitude;
    private double longitude;
    private boolean open;

    public Resturant(String name, String id, double latitude, double longitude, boolean open) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.open = open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public boolean getOpen() {
        return open;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
