package com.example.dincer_berkecan_caradsystem

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import java.util.Locale.filter

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var dbHandler: DBHandler
    }

    var carlist = ArrayList<Cars> ()
    lateinit var adapter : RecyclerView.Adapter<*>
    lateinit var rv : RecyclerView

    //the part where the menu was created
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mainmenu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            //// The part where the registration window is opened and the new activity is installed to get a new record
            R.id.btnadd -> {

                val i = Intent(this, AddCarActivity::class.java)
                startActivity(i)

                true

            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHandler = DBHandler(this, null, null, 1)

        viewCars()

        //The part where the search is made among the posted ads (Search Process)
        btnSearch.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?){

            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int){

            }
            override fun onTextChanged(s: CharSequence?, start: Int, count: Int, before: Int){
                var filteredCars = ArrayList<Cars>()
                if (!btnSearch.text.isEmpty()){
                    for (i in 0..carlist.size -1){
                        if (carlist.get(i).carName!!.toLowerCase().contains(s.toString().toLowerCase()))
                            filteredCars.add(carlist[i])
                    }
                    adapter = CarAdapters(this@MainActivity,filteredCars)
                    rv.adapter = adapter
                }else {
                    adapter = CarAdapters(this@MainActivity,carlist)
                    rv.adapter = adapter
                }
            }
        })
    }
    //The recyclerview is the part where cardviews are shown one by one
    @SuppressLint("WrongConstant")
    private fun viewCars(){
        carlist   = dbHandler.getCars(this)
        adapter = CarAdapters(this, carlist)
        rv = findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false) as RecyclerView.LayoutManager
        rv.adapter = adapter
    }

    override fun onResume() {
        viewCars()
        super.onResume()
    }
}