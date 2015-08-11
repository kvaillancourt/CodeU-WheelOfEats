package me.karavaillancourt.wheelofeats;

/**
 * Created by kvaillancourt on 8/9/15.
 */
public class Resturant {

    private final String id;
    private final String name;
    private double latitude;
    private double longitude;
    private String icon;

    public Resturant(String name, String id, double latitude, double longitude, String icon) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.icon = icon;
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
        return id;
    }

    public String getName() {
        return name;
    }
}
