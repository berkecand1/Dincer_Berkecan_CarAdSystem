package com.example.dincer_berkecan_caradsystem

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

class DBHandler(context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION){

    companion object{
        private val DATABASE_NAME = "MyDatabase.db"
        private val DATABASE_VERSION = 1

        val CARS_TABLE_NAME = "cars"
        val COLUMN_CARID = "carsid"
        val COLUMN_CARNAME = "carsname"
        val COLUMN_CARPRICE = "carsprice"
        val COLUMN_CARSTATEMENT = "carstatement"
        val COLUMN_CARYEAR = "caryear"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CARS_TABLE : String = ("CREATE TABLE $CARS_TABLE_NAME (" +
                "$COLUMN_CARID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_CARNAME TEXT," +
                "$COLUMN_CARPRICE DOUBLE DEFAULT 0,"+
                "$COLUMN_CARYEAR INTEGER DEFAULT 0," +
                "$COLUMN_CARSTATEMENT flag INT DEFAULT 0)")
        db?.execSQL(CREATE_CARS_TABLE)
        //The database is created and columns are created according to the values.


    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun getCars(mCtx : Context) : ArrayList<Cars>{
        val qry = "Select * From $CARS_TABLE_NAME"
        val  db: SQLiteDatabase = this.readableDatabase
        var cursor = db.rawQuery(qry, null)
        val cars = ArrayList<Cars>()

        if (cursor.count==0)
            Toast.makeText(mCtx, "No Records Found", Toast.LENGTH_SHORT).show()
        else {
            //The part where the object is taken from the database
                cursor.moveToFirst()
                while (!cursor.isAfterLast()) {
                    val car = Cars()
                    car.carID = cursor.getInt(cursor.getColumnIndex(COLUMN_CARID))
                    car.carName = cursor.getString(cursor.getColumnIndex(COLUMN_CARNAME))
                    car.carPrice = cursor.getDouble(cursor.getColumnIndex(COLUMN_CARPRICE))
                    car.carYear = cursor.getInt(cursor.getColumnIndex(COLUMN_CARYEAR))
                    car.carStatement = (cursor.getInt(cursor.getColumnIndex(COLUMN_CARSTATEMENT)) == 1)
                    cars.add(car)
                    cursor.moveToNext()
                }

        }
        cursor.close()
        db.close()
        return cars
    }
    //When a new record arrives, the object is added to the database according to the values.
    fun addCar(mCtx: Context, car: Cars){
        val values = ContentValues()
        values.put(COLUMN_CARNAME,car.carName)
        values.put(COLUMN_CARPRICE, car.carPrice)
        values.put(COLUMN_CARYEAR, car.carYear)
        values.put(COLUMN_CARSTATEMENT, car.carStatement)
        val db = this.writableDatabase
        try {
            db.insert(CARS_TABLE_NAME, null,values)


        }catch (e: Exception){
            Toast.makeText(mCtx,e.message, Toast.LENGTH_SHORT).show()
        }
        db.close()

    }
    //the part where the object is deleted from the database
    fun deleteCar(carID : Int) : Boolean {
        var qry = "Delete From $CARS_TABLE_NAME where $COLUMN_CARID = $carID"
        var db : SQLiteDatabase = this.writableDatabase
        var result : Boolean = false
        try {
            val cursor = db.execSQL(qry)
            result = true
        }catch (e : Exception){
            Log.e(ContentValues.TAG, "Error Deleting")
        }
        db.close()
        return result
    }
    // the part where the object is edit from the database.
    fun editCar(id: String, carName: String, carPrice: String, carYear: String, carStatement: String) : Boolean{
        val db : SQLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        var result : Boolean = false
        contentValues.put(COLUMN_CARNAME, carName)
        contentValues.put(COLUMN_CARPRICE, carPrice.toDouble())
        contentValues.put(COLUMN_CARYEAR, carYear.toInt())
        contentValues.put(COLUMN_CARSTATEMENT,carStatement.toBoolean())
        try {
            db.update(CARS_TABLE_NAME, contentValues, "$COLUMN_CARID = ?", arrayOf(id))
            result = true
        }catch (e : Exception){
            result = false
        }
        return result
    }
}