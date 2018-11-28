package com.kkang.mbtabs.component;

import java.util.ArrayList;

/**
 * Created by kkang on 2018. 10. 28..
 */

public class CUSTOM_TAB_DATA {
    public String key = "";
    public String name = "";
    public String imgFile = "";

    public int imgDrawable = 0;
    public int imgPadding = 0;
    public boolean isNew = false;
    public boolean isSelect = false;

    public ArrayList<CUSTOM_TAB_DATA> subList = new ArrayList<>();
}
