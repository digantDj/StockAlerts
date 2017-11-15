package com.digantjagtap.stockalerts.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.digantjagtap.stockalerts.Model.Alert;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = Environment.getExternalStorageDirectory() + File.separator + "stockAlerts.db";
    private static DBHelper dbHelper;
    private static String alertTableName;

    private String alertCOL1 = "alertId";
    private String alertCOL2 = "symbol";
    private String alertCOL3 = "comparison";
    private String alertCOL4 = "alertValue";
    private String alertCOL5 = "currentPrice";
    private String alertCOL6 = "status";

    private static SQLiteDatabase db = null;
    private Context context;


    private DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
    }

    public static DBHelper getInstance(Context context) throws IOException, SQLiteException {
        if (dbHelper == null){
            dbHelper = new DBHelper(context.getApplicationContext());
            db = dbHelper.getWritableDatabase();

        }
        return dbHelper;
    }

    public void onCreateAlertTable(String tableName) throws SQLiteException {
        alertTableName = tableName;
        db.execSQL("CREATE TABLE IF NOT EXISTS " + tableName + " ( "
                + alertCOL1 + " INTEGER NOT NULL, "
                + alertCOL2 + " text, "
                + alertCOL3 + " text, "
                + alertCOL4 + " text, "
                + alertCOL5 + " text, "
                + alertCOL6 + " text)" );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS alert ("
                + alertCOL1 + " INTEGER NOT NULL, "
                + alertCOL2 + " text, "
                + alertCOL3 + " text, "
                + alertCOL4 + " text, "
                + alertCOL5 + " text, "
                + alertCOL6 + " text)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /*
     * Inserts an alerts to Alert table
     */
    public boolean insertAlert(Alert alert, String tableName) {
      ContentValues contentValues = new ContentValues();
      contentValues.put(alertCOL1, alert.getAlertId());
      contentValues.put(alertCOL2, alert.getSymbol());
      contentValues.put(alertCOL3, alert.getComparison());
      contentValues.put(alertCOL4, alert.getAlertValue());
      contentValues.put(alertCOL5, alert.getCurrentPrice());
      contentValues.put(alertCOL6, alert.getStatus());

      long result = db.insert(tableName, null, contentValues);

      if (result == -1) {
          return false;
      } else {
          return true;
      }
    }

    /*
     * Update a particular alert from Alert table
     */
    public boolean updateAlert(Alert alert, String tableName) {
      ContentValues contentValues = new ContentValues();
      contentValues.put(alertCOL1, alert.getAlertId());
      contentValues.put(alertCOL2, alert.getSymbol());
      contentValues.put(alertCOL3, alert.getComparison());
      contentValues.put(alertCOL4, alert.getAlertValue());
      contentValues.put(alertCOL5, alert.getCurrentPrice());
      contentValues.put(alertCOL6, alert.getStatus());
      String condition = alertCOL1+" = "+ alert.getAlertId();
      long result = db.update(tableName, contentValues, condition,null);

      if (result == -1) {
          return false;
      } else {
          return true;
      }
    }

    /*
     * Retrieve all active alerts from Alert table
     */
    public List<Alert> getAlerts () {
        List<Alert> alerts = new ArrayList<Alert>();
        Cursor cursor = null;
        db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + alertTableName + " ORDER BY " + alertCOL1 + " DESC";
        Log.d("select getAlerts query",selectQuery);
        try {
            cursor = db.rawQuery(selectQuery, null);
     //       db.setTransactionSuccessful();
            while (cursor.moveToNext()) {
                Alert alert = new Alert();
                alert.setAlertId(cursor.getInt(cursor.getColumnIndex(alertCOL1)));
                alert.setSymbol(cursor.getString(cursor.getColumnIndex(alertCOL2)));
                alert.setComparison(cursor.getString(cursor.getColumnIndex(alertCOL3)));
                alert.setAlertValue(cursor.getString(cursor.getColumnIndex(alertCOL4)));
                alert.setCurrentPrice(cursor.getString(cursor.getColumnIndex(alertCOL5)));
                alert.setStatus(cursor.getString(cursor.getColumnIndex(alertCOL6)));
                alerts.add(alert);
            }
        } catch (Exception e) {
            //report problem
            alerts = null;
        } finally {
            cursor.close();
            return alerts;
        }

    }

    /*
     * Retrieve a particular alertId from Alert table
     */
    public Alert getAlertWithAlertId (int alertId) {
      Alert alert = null;
      Cursor cursor = null;
      db = dbHelper.getReadableDatabase();
      String selectQuery = "SELECT * FROM " + alertTableName + " WHERE `" + alertCOL1 + "` = "+ alertId;
      Log.d("getAlertWithAlertId query",selectQuery);
      try {
          cursor = db.rawQuery(selectQuery, null);
          alert = new Alert();
          while(cursor.moveToNext()) {
              alert.setAlertId(cursor.getInt(cursor.getColumnIndex(alertCOL1)));
              alert.setSymbol(cursor.getString(cursor.getColumnIndex(alertCOL2)));
              alert.setComparison(cursor.getString(cursor.getColumnIndex(alertCOL3)));
              alert.setAlertValue(cursor.getString(cursor.getColumnIndex(alertCOL4)));
              alert.setCurrentPrice(cursor.getString(cursor.getColumnIndex(alertCOL5)));
              alert.setStatus(cursor.getString(cursor.getColumnIndex(alertCOL6)));
          }
      } catch (Exception e) {
          e.printStackTrace();
          alert = null;
      } finally {
          cursor.close();
          return alert;
      }

    }

    /*
     * Delete a particular alertId from Alert table
     */
    public boolean deleteAlert(int alertId){
        long result = db.delete(alertTableName,alertCOL1+"="+alertId,null);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    /*
     * Retrieve the max alertId from Alert table
     */
    public int getMaxAlertId(){
        Cursor cursor = null;
        int result = 0;
        String selectQuery = "SELECT max(" + alertCOL1 + ") FROM " + alertTableName;
        try {
            cursor = db.rawQuery(selectQuery, null);
            //  db.setTransactionSuccessful();
            if (cursor.moveToNext()) {
                result = cursor.getInt(cursor.getColumnIndex(alertCOL1));
            }
        } catch (Exception e) {
            //report problem
            result = 0;

        }
        return result;
    }


}
