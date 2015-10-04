package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by HenryChiang on 15-10-03.
 */
public class DatabaseUtil extends SQLiteOpenHelper {


    final String tag="db";
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "appStorage.db";


    //------------------------------ ORDERING TABLE --------------------------------------

    public static final String ordering_table = "ORDER_INTENT";

    //ACRONYM TABLE COLUMNS names
    public static final String name = "name";
    public static final String category = "category";

    private static final String create_order_table = "create table " + ordering_table + "(" + name + " text primary key," +
            category + " text not null" + ")";

    //------------------------------------------------------------------------------------



    public DatabaseUtil(Context context)
    {
        // Create Database itself
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(tag, "DatabaseUtil: Creating the database - constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase database)
    {
        Log.d(tag,"DatabaseUtil: Inside onCreate() ");
        database.execSQL(create_order_table);
        Log.d(tag,"DatabaseUtil: Creating the Table Ordering \n SQL:" + create_order_table);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.v(DatabaseUtil.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        Log.d(tag,"DatabaseUtil: Starting to destroy data ");

        db.execSQL("DROP TABLE IF EXISTS " + ordering_table);
        Log.d(tag,"DatabaseUtil: Finished to destroy data ");

        onCreate(db);
        Log.d(tag,"DatabaseUtil: Finished to calling onCreate");
    }

}