package com.example.skulkarni.nytimessearch;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by skulkarni on 8/14/16.
 */
public class Filter implements Serializable {
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public void setSortOrder(Boolean sortOrder) {
        this.sortOrder = sortOrder;
    }

    public void setNewsDesk(String newsDesk) {
        this.newsDesk = newsDesk;
    }

    Date beginDate = new Date();
    Boolean sortOrder = true;
    String newsDesk;

    public Date getBeginDate() {
        if (beginDate != null) {
            return beginDate;
        }
        return new Date();
    }

    public int getFormattedBeginDate() {
        Date d = getBeginDate();
        int year = d.getYear() + 1900;
        int month = d.getMonth();
        int date = d.getDate();
        String strDate = Integer.toString(date);
        String strMonth = Integer.toString(month);
        if (month<10) {
            strMonth = '0' + Integer.toString(month);
        }
        if (date<10) {
            strDate = '0' + Integer.toString(date);
        }
        return Integer.parseInt(Integer.toString(year) + strMonth + strDate);
    }

    public Boolean getSortOrder() {
        return sortOrder;
    }

    public String getNewsDesk() {
        if (newsDesk == null) {
            return "";
        }
        return newsDesk;
    }

    public Filter() {

    }

    public Filter(Date d, Boolean b, String n) {
        this.beginDate = d;
        this.sortOrder = b;
        this.newsDesk = n;
    }
}
