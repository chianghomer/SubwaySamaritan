package samaritan.wearhacks.ca.subwaysamaritan;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import Objects.Food;
import database.DatabaseConnection;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivty";
    DatabaseConnection dataConnection;

    private final String[] breadArray = {"italian bread","honey oat bread","wheat bread","parmesan oregano bread","hearty italian bread"};
    private final String[] cheeseArray = {"cheddars cheese","swiss cheese","no cheese"};
    private final String[] vegetableArray = {"cucumbers","green bell peppers","lettuce","red onions","spinach","tomatoes","olives","pickles","jalapenos","no vegetable","all dress"};
    private final String[] sauceArray = {"honey mustard sauce","sweet onion sauce","mayonnaise sauce","olive oil blend sauce","chili sauce","ketchup sauce","barbecue sauce","ranch sauce","thousand island sauce","no sauce"};
    private final String[] toastedArray = {"toasted","not toasted"};
    private final String[] kindArray = {"meatball sandwich","chicken sandwich","ham sandwich","steak sandwich","turkey sandwich","tuna sandwich","roast beef sandwich","lobster sandwich","pizza sandwich"};

    private final String[] categoryArray = {"bread","cheese","vegetable","sauce","toasted","kind"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addFoodToDatabase();

        TextView tv = (TextView)findViewById(R.id.mainActivity_txt);
        tv.setTypeface(FontManager.getTypeface(this));


        Button btn = (Button)findViewById(R.id.mainActivity_btn);
        btn.setTypeface(FontManager.getTypeface(this));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), OrderActivity.class));

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String strSeparator = ", ";
    public static String convertArrayToString(String[] array){
        String str = "";
        for (int i = 0;i<array.length; i++) {
            str = str+array[i];
            // Do not append comma at the end of last element
            if(i<array.length-1){
                str = str+strSeparator;
            }
        }
        return str;
    }

    private void addFoodToDatabase() {
        dataConnection = new DatabaseConnection(this);
        try{
            dataConnection.open();
        }
        catch (Exception e)
        { }
        addFoodArray(breadArray,categoryArray[0]);
        addFoodArray(cheeseArray,categoryArray[1]);
        addFoodArray(vegetableArray,categoryArray[2]);
        addFoodArray(sauceArray, categoryArray[3]);
        addFoodArray(toastedArray,categoryArray[4]);
        addFoodArray(kindArray,categoryArray[5]);
    }

    private void addFoodArray(String [] foodArray, String category){
        for(int i = 0;i<foodArray.length;i++){
            dataConnection.addFood(foodArray[i],category);
        }

    }


}
