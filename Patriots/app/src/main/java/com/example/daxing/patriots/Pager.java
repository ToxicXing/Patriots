package com.example.daxing.patriots;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                ByStateTab tab1 = new ByStateTab();
                return tab1;
            case 1:
                HouseTab tab2 = new HouseTab();
                return tab2;
            case 2:
                SenateTab tab3 = new SenateTab();
                return tab3;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
