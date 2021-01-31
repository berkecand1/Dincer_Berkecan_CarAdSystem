package com.example.dincer_berkecan_caradsystem

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_add_car.*
import kotlinx.android.synthetic.main.cars_edit_page.view.*
import kotlinx.android.synthetic.main.io_cars.view.*

class CarAdapters(mCtx : Context, val cars: ArrayList<Cars>) : RecyclerView.Adapter<CarAdapters.ViewHolder>(){

    val mCtx = mCtx
    
    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val txtCarNames = itemView.txtCarName
        val txtCarPrices = itemView.txtCarPrice
        val btnDelete = itemView.btnDelete
        val btnEdit = itemView.btnEdit
        val cardview = itemView.cardview
        val txtCarYear = itemView.txtCarYear
    }
    

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.io_cars,p0, false)
        return ViewHolder(v)

    }

    override fun onBindViewHolder(p0: CarAdapters.ViewHolder, p1: Int) {
        val car : Cars = cars[p1]
        p0.txtCarNames.text = car.carName
        p0.txtCarPrices.text = car.carPrice.toString()
        p0.txtCarYear.text = car.carYear.toString()
        //where the color of the recyclerview changes according to the checkbox
        if (car.carStatement == true){
            p0.cardview.setCardBackgroundColor(Color.parseColor("#ffaaaa"))
        }
        else {
            p0.cardview.setCardBackgroundColor(Color.parseColor("#e8cd9b"))
        }
        // delete process
        p0.btnDelete.setOnClickListener {
            val carName = car.carName
            //The alertdialog section that appears when clicking the delete icon
            var alertDialog = AlertDialog.Builder(mCtx)
                .setTitle("Warning")
                .setMessage("Are you Sure to Delete : $carName ?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    //If he hits yes, the object is deleted from the database
                    if (MainActivity.dbHandler.deleteCar(car.carID)){
                        cars.removeAt(p1)
                        notifyItemRemoved(p1)
                        notifyItemRangeChanged(p1,cars.size)

                    }else{

                    }
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->  })
                .setIcon(R.drawable.ic_baseline_warning_24)
                .show()
        }
        //edit process
        p0.btnEdit.setOnClickListener {
            val inflater = LayoutInflater.from(mCtx)
            val view = inflater.inflate(R.layout.cars_edit_page, null)

            val txtCrName : TextView = view.findViewById(R.id.editUpdateCarName)
            val txtCrPrice: TextView = view.findViewById(R.id.editUpdateCarPrice)
            val txtCrYear: TextView = view.findViewById(R.id.editUpdateCarYear)
            val btneditcheckbox : CheckBox = view.findViewById(R.id.btneditcheckbox)

            //Values from class car are read one by one and written to the box.
            txtCrName.text = car.carName
            txtCrPrice.text = car.carPrice.toString()
            txtCrYear.text = car.carYear.toString()
            btneditcheckbox.isChecked = car.carStatement

            //When the edit button is pressed, a dialog is created and the necessary editing operations take place here.
            val builder = AlertDialog.Builder(mCtx)
                .setTitle("Edit Car Ad. Informations")
                .setView(view)
                .setPositiveButton("Edit", DialogInterface.OnClickListener { dialog, which ->

                    val isEdit : Boolean = MainActivity.dbHandler.editCar(
                            car.carID.toString(),
                            view.editUpdateCarName.text.toString(),
                            view.editUpdateCarPrice.text.toString(),
                            view.editUpdateCarYear.text.toString(),
                            view.btneditcheckbox.text.toString(),
                    )
                    if (isEdit==true){

                        cars[p1].carName = view.editUpdateCarName.text.toString()
                        cars[p1].carPrice = view.editUpdateCarPrice.text.toString().toDouble()
                        cars[p1].carYear = view.editUpdateCarYear.text.toString().toInt()
                        cars[p1].carStatement = view.btneditcheckbox.isChecked

                        if (view.editUpdateCarName.text.isEmpty() ) {
                            Toast.makeText(mCtx, "Please write the all text box", Toast.LENGTH_SHORT).show()
                        }
                        else {
                            //The new values are replaced with the values in the nail polish.
                            notifyDataSetChanged()
                        }
                    }
                    else {

                    }
                }).setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->

                })
            val alert : AlertDialog = builder.create()
            alert.show()
        }
    }
    override fun getItemCount(): Int {
        return cars.size
    }
}