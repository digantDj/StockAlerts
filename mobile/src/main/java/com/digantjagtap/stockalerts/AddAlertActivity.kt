package com.digantjagtap.stockalerts

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.ToggleButton
import com.digantjagtap.stockalerts.Database.DBHelper
import com.digantjagtap.stockalerts.Model.Alert
import java.io.IOException

class AddAlertActivity : AppCompatActivity() {

    internal lateinit var dbHelper: DBHelper

    internal lateinit var symbol: String
    internal lateinit var comparison: String
    internal lateinit var alertValue: String
    internal lateinit var status: String

    internal lateinit var symbolEditText: EditText
    internal lateinit var alertValueEditText: EditText
    internal lateinit var statusSwitch: Switch
    internal lateinit var save: Button
    internal lateinit var alert: Alert
    private var maxAlertId = 0
    private var alertTableName = "alert";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_alert)

        // Retrieving the elements from layout
        symbolEditText = findViewById<EditText>(R.id.symbolEditText)
        alertValueEditText = findViewById<EditText>(R.id.alertValueEditText)
        statusSwitch = findViewById<Switch>(R.id.statusSwitch)
        save = findViewById<Button>(R.id.saveButton)

        // Initializing the alert object
        alert = Alert()

        try {
            // Creating table alert locally on phone if not exists
            dbHelper = DBHelper.getInstance(this)
            dbHelper.onCreateAlertTable(alertTableName)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Defining OnClick function on save button
        save.setOnClickListener {
            comparison = statusSwitch.text.toString()
            symbol = symbolEditText.text.toString()
            alertValue = alertValueEditText.text.toString()
            status = statusSwitch.text.toString()

            maxAlertId = dbHelper.getMaxAlertId()
            val alertid = maxAlertId + 1
            alert.setAlertId(alertid)
            alert.setSymbol(symbol)
            alert.setAlertValue(alertValue)
            alert.setComparison(comparison)
            alert.setCurrentPrice("10")
            alert.setStatus(status)

            // Inserting into SQLite DB
            if (dbHelper.insertAlert(alert, alertTableName)) {
                val i = Intent(this@AddAlertActivity, MainActivity::class.java)
                startActivity(i)
                finish()
            }
        }
    }
}
