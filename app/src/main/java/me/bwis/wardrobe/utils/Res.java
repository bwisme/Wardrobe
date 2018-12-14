package me.bwis.wardrobe.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Res {

    public static Set<String> TYPE_SET;

    public static ArrayList<String> getTypeSet()
    {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(TYPE_SET);
        return list;
    }



}
