package database;

/**
 * Created by HenryChiang on 15-10-03.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import Objects.Food;

public class DatabaseConnection {

    private SQLiteDatabase database;
    private DatabaseUtil   dbHelper;
    final String tag="db";

    // Column names for Database
    private String[] Order_Col = { DatabaseUtil.name, DatabaseUtil.category};

    public DatabaseConnection(Context context)
    {
        Log.d(tag, "DataConnection: Inside Constructor");
        dbHelper = new DatabaseUtil(context);

    }

    public void open() throws SQLException
    {
        // Assign the DatabaseUtil to the SQL Object to retrieve the data
        database = dbHelper.getWritableDatabase();
        Log.d(tag, "DataConnection: Opening the Database");
    }

    public void close()
    {
        dbHelper.close();
        Log.d(tag, "DataConnection: Closing Connection");

    }

    //--------------------  Return full Acronym List in alphabetical order ------------------
    public ArrayList<Food> getAllFood()
    {
        Log.d(tag, "Inside getAllFood()" );

        ArrayList<Food> foodList = new ArrayList<Food>();

        Log.d(tag, "getAllFood: Going to run query on database " );
        Cursor cursor1 = database.query(DatabaseUtil.ordering_table,  Order_Col,  null, null, null, null, null);
        Log.d(tag, "getAllFood: Number of food returned: " + cursor1.getCount());

        cursor1.moveToFirst();
        while (!cursor1.isAfterLast()) {
            Food food = new Food(cursor1.getString(0),cursor1.getString(1));
            foodList.add(food);
            cursor1.moveToNext();
        }
        cursor1.close();

        /*
        Collections.sort(foodList, new Comparator<Food>() {
            public int compare(Food a1, Food a2) {
                return a1.getCategory().compareTo(a2.getCategory());
            }
        });
        */

        Log.d(tag, "Number of food found: " + foodList.size());
        return foodList;
    }

    //--------------------  Add Acronym to database  --------------------
    //----- Returns true if successful else return false ----------------
    public void addFood(String name, String category)
    {


        ContentValues values = new ContentValues();
        values.put(DatabaseUtil.name, name.trim());
        values.put(DatabaseUtil.category, category.trim());
        Cursor cursor = database.query(DatabaseUtil.ordering_table, Order_Col, DatabaseUtil.name + "='" +name.trim()+"'" , null, null, null, null);
        database.insert(DatabaseUtil.ordering_table, null, values);

        if(cursor.getCount() <= 0)
        {
            database.insert(DatabaseUtil.ordering_table, null, values);
            Log.d(tag, "Finished updating food to database" );
        }
        else
        {
            Log.d(tag, "food already exists" );
        }



    }










}
