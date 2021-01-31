package com.example.dincer_berkecan_caradsystem

import android.os.Bundle
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_car.*

class AddCarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_car)

        btnSave.setOnClickListener {
            //checking whether the information is entered or not.
            if (editCarName.text.isEmpty()){
                Toast.makeText(this, "Please write the name of the car", Toast.LENGTH_SHORT).show()
                editCarName.requestFocus()
            }
            else if(editCarPrice.text.isEmpty()) {
                Toast.makeText(this, "Please write the price of the car", Toast.LENGTH_SHORT).show()
                editCarPrice.requestFocus()
            }
            else if(editCarYear.text.isEmpty()) {
                Toast.makeText(this, "Please write the model year of the car", Toast.LENGTH_SHORT).show()
                editCarPrice.requestFocus()
            }
            else{
                //If the information is entered, it is synchronized with the values in the Cars class.
                val car = Cars()
                car.carName = editCarName.text.toString()
                car.carPrice = editCarPrice.text.toString().toDouble()
                car.carStatement = btncheckbox.isChecked
                car.carYear = editCarYear.text.toString().toInt()
                MainActivity.dbHandler.addCar(this, car)
                clearEdits()
                editCarName.requestFocus()
                finish()
            }
        }
    }
    //boxes are cleaned afterwards
    private fun clearEdits() {
        editCarName.text.clear()
        editCarPrice.text.clear()
        editCarYear.text.clear()
    }

}