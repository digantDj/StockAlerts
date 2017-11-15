package com.digantjagtap.stockalerts

import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.digantjagtap.stockalerts.Adapter.ListViewAdapter
import com.digantjagtap.stockalerts.Database.DBHelper
import com.digantjagtap.stockalerts.Model.Alert
import com.digantjagtap.stockalerts.R.id.drawer_layout
import io.vrinda.kotlinpermissions.PermissionCallBack
import io.vrinda.kotlinpermissions.PermissionsActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import java.io.IOException
import java.util.*
import android.Manifest




class MainActivity :  NavigationView.OnNavigationItemSelectedListener, PermissionsActivity() {


    private var listView: ListView? = null
    internal var allAlerts: TypedArray? = null
    internal lateinit var dbHelper: DBHelper
    internal var alertsList: ArrayList<Alert> = ArrayList<Alert>()
    internal lateinit var listViewAdapter: ListViewAdapter

    private var alertTableName = "alert"

    internal lateinit var adapter: ArrayAdapter<Alert>
    // List view
    private var lv: ListView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


            requestPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, object : PermissionCallBack {
                override fun permissionGranted() {
                    super.permissionGranted()

                }

                override fun permissionDenied() {
                    super.permissionDenied()
                    return;
                }
            })


        try {
            // Get Instance of DB
            dbHelper = DBHelper.getInstance(this@MainActivity)
            dbHelper.onCreateAlertTable(alertTableName)
        } catch (e: IOException) {
            Log.e("error", "DBHelper getInstance error")
        }

        // Bind to our new adapter.
        lv = findViewById<ListView>(R.id.listview)
        adapter = ArrayAdapter<Alert>(this, R.layout.item_alert, alertsList)

        setAdapter()

        // Function onClick after addAlert button press
        fab.setOnClickListener {
            val intent = Intent(this, AddAlertActivity::class.java)
            startActivity(intent)
            // Notification bottom Snacks
//            view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show()
        }



        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }


    private fun setAdapter() {

        // Getting Alerts
        try {
            alertsList = getAlerts()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }


        listViewAdapter = ListViewAdapter(this@MainActivity, alertsList)
        lv?.setAdapter(listViewAdapter)
        lv?.setTextFilterEnabled(true)
        lv?.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
//            val intent = Intent(view.context, MemoUpdateActivity::class.java)
//            intent.putExtra("memo", memosList.get(position))
//            //intent.putExtra("memoId", memosList.get(position).getMemoId());
//            intent.putExtra("userEmail", HomeActivity.getInstance().getIntent().getStringExtra("userEmail"))
//            // listView.getItemAtPosition(position)
//            startActivity(intent)
        })

//        setupSearchView()

    }

    // Function to retrieve Alerts from DB
    private fun getAlerts(): ArrayList<Alert> {
        // Querying Alerts
        val tempAlertsList = ArrayList<Alert>()
        try {

            val alerts = dbHelper.getAlerts()

            val i = 0
            for (alert in alerts) {
                tempAlertsList.add(alert)
            }
            return tempAlertsList
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

        return tempAlertsList
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
