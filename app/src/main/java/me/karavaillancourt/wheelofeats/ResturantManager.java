package me.karavaillancourt.wheelofeats;

/**
 * Created by kvaillancourt on 8/10/15.
 */
public class ResturantManager {

    private Resturant[] masterList;

    public ResturantManager() {

    }

    public void setMasterList(Resturant[] list) {
        masterList = list;

    }

    public Resturant select(int i) {
        return masterList[i];
    }

    public Resturant selectRandom() {
        int random = (int) (Math.floor(Math.random() * masterList.length) + 1);
        return masterList[random];
    }

}

