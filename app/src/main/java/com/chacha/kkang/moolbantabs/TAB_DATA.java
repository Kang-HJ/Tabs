package com.chacha.kkang.moolbantabs;

import java.util.ArrayList;

/**
 * Created by kkang on 2018. 10. 28..
 */

public class TAB_DATA {
    public String key = "";
    public String name  = "";
    public String imgFile = "";

    public int imgDrawable = 0;
    public int imgPadding = 0;
    public boolean isNew = false;
    public boolean isSelect = false;

    public ArrayList<TAB_DATA> subList = new ArrayList<>();
}
