package com.digantjagtap.stockalerts.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Digant on 11/11/2017.
 */

public class Alert implements Parcelable {
    private int alertId;
    private String symbol;
    private String comparison;
    private String alertValue;
    private String currentPrice;
    private String status;

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public int getAlertId(){
        return alertId;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol(){
        return symbol;
    }

    public void setComparison(String comparison) {
        this.comparison = comparison;
    }

    public String getComparison(){
        return comparison;
    }

    public void setAlertValue(String alertValue) {
        this.alertValue = alertValue;
    }

    public String getAlertValue(){
        return alertValue;
    }

    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {String.valueOf(this.alertId),
                this.symbol,
                this.comparison,
                this.alertValue,
                this.currentPrice,
                this.status
        });
    }

    public Alert(){

    }


    // Parcelling part
    public Alert(Parcel in){
        String[] data = new String[6];

        in.readStringArray(data);
        this.alertId = Integer.parseInt(data[0]);
        this.symbol = data[1];
        this.comparison = data[2];
        this.alertValue = data[3];
        this.currentPrice = data[4];
        this.status = data[5];
    }

    public static final Creator CREATOR = new Creator() {
        public Alert createFromParcel(Parcel in) {
            return new Alert(in);
        }

        public Alert[] newArray(int size) {
            return new Alert[size];
        }
    };
}
