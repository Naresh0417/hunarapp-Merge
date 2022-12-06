package com.hamstechonline.datamodel;

import java.util.ArrayList;

/**
 * Created by ADMIN on 5/23/2016.
 */
public class HeaderInfo {

    private String name;
    //  private ArrayList<ListInfo> productList = new ArrayList<ListInfo>();;
    private ArrayList<DetailInfo> productList = new ArrayList<DetailInfo>();;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<DetailInfo> getProductList() {
        return productList;
    }
    public void setProductList(ArrayList<DetailInfo> productList) {
        this.productList = productList;
    }
//    public ArrayList<ListInfo> getProductList() {
//        return productList;
//    }
//    public void setProductList(ArrayList<ListInfo> productList) {
//        this.productList = productList;
//    }

}
