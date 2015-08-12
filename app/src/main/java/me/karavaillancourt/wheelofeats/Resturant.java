package me.karavaillancourt.wheelofeats;

/**
 * Created by kvaillancourt on 8/9/15.
 */
public class Resturant {

    private static final String LOG_TAG = Resturant.class.getSimpleName();
    private final String place_id;
    private final String name;
    private double latitude;
    private double longitude;
    private String icon;
    private String address;
    private Boolean open;

    public Resturant(String name, String id, double latitude, double longitude, String icon) {
        this.place_id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.icon = icon;
        this.address = "has not been set yet";
        this.open = true;
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getIcon() {
        return icon;
    }

    public String getId() {
        return place_id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }


    public void setOpen(Boolean open) {
        this.open = open;
    }

    public boolean isOpen() {
        return this.open;
    }


    public void setAddress(String address) {
        this.address = address;
    }
}
