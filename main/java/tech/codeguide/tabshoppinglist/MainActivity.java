package tech.codeguide.tabshoppinglist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

// TODO: 3/5/2022 Refactor save and load methods to an class
// TODO: 3/5/2022 Refactor lists to class
// TODO: 3/5/2022 Refactor setAdapter method to class
// TODO: 3/5/2022 Create addItem interface
// TODO: 3/5/2022 Create additional lists
// TODO: 3/5/2022 Top menu: New List, Open List, Close List, Help, Sort
// TODO: 3/5/2022 Create page for new lists
// TODO: 3/5/2022 Create Main Menu
// TODO: 3/5/2022 Color coded grocery items
// TODO: 3/5/2022 Separated grocery items (by isle)
// TODO: 3/5/2022 Additional Values for lists (Quantity, Description, Price)
// TODO: 3/5/2022 Total estimated cost at bottom of cart and shopping list (how much remaining in shopping list and cost in cart)


public class MainActivity extends AppCompatActivity {

    FrameLayout simpleFrameLayout;
    TabLayout tabLayout;
    static String currentTab;
    public static ArrayList<String> shoppingList = null;
    public static ArrayList<String> cartList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // IDs
        simpleFrameLayout = findViewById(R.id.frameLayout);
        tabLayout = findViewById(R.id.tabLayout);

        // Tabs and Names
        TabLayout.Tab firstTab = tabLayout.newTab();
        firstTab.setText("Shopping List");
        tabLayout.addTab(firstTab);
        TabLayout.Tab secondTab = tabLayout.newTab();
        secondTab.setText("In Cart");
        tabLayout.addTab(secondTab);

        // Fill Shopping List fragment on open
        currentTab = "Shopping List";
        updateFragment();
        // Listener to switch tabs
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        currentTab = "Shopping List";
                        updateFragment();
                        break;
                    case 1:
                        currentTab = "Cart List";
                        updateFragment();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Load lists on open
        shoppingList = getArrayVal("shoppingList", getApplicationContext());
        cartList = getArrayVal("cartList", getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // FIXME: 3/6/2022 ALL BELOW
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sort) {
            Collections.sort(shoppingList);
            Collections.sort(cartList);
            updateFragment();
            return true;
        }
        if(id == R.id.action_add) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Add Item to " + currentTab + "?" );
            final EditText input = new EditText(this);
            builder.setView(input);
            builder.setPositiveButton("OK", (dialogInterface, which) -> {
                if(currentTab.equals("Shopping List")){
                    shoppingList.add(preferredCase(input.getText().toString()));
                    storeArrayVal(shoppingList, getApplicationContext());
                } else {
                    cartList.add(preferredCase(input.getText().toString()));
                    storeArrayVal(cartList, getApplicationContext());
                }
                updateFragment();
            });
            builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());
            builder.show();
            return true;
        }
        if(id == R.id.action_clear) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete " + currentTab + "?");
            builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                if(currentTab.equals("Shopping List")){
                    shoppingList.clear();
                    storeArrayVal(shoppingList, getApplicationContext());
                } else {
                    cartList.clear();
                    storeArrayVal(cartList, getApplicationContext());
                }
                updateFragment();
            });
            builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());

            builder.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String preferredCase(String input) {
        if(input.isEmpty()){
            return input;
        }
        StringBuilder sb = new StringBuilder();
        for(String s : input.split(" ")){
            sb.append(s.substring(0,1).toUpperCase()).append(s.substring(1).toLowerCase()).append(" ");
        }
        return sb.toString().trim();
    }

    private void updateFragment() {
        System.out.println("Updating Fragment - current tab =" + currentTab);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if(currentTab.equals("Shopping List")) {
            ft.replace(R.id.frameLayout, new ShoppingListFragment());
        } else {
            ft.replace(R.id.frameLayout, new CartListFragment());
        }

        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    // Save and Load from Phone
    public static void storeArrayVal (ArrayList list, Context context){
        Set mySet = new HashSet(list);
        SharedPreferences WordSearchPutPrefs = context.getSharedPreferences("dbArrayValues", Activity.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = WordSearchPutPrefs.edit();
        if(list.equals(shoppingList)){
            prefEditor.putStringSet("myShoppingList", mySet);
        } else {
            prefEditor.putStringSet("myCartList", mySet);
        }
        prefEditor.apply();
    }

    public static ArrayList getArrayVal (String list, Context context) {
        SharedPreferences WordSearchGetPrefs = context.getSharedPreferences("dbArrayValues", Activity.MODE_PRIVATE);
        Set tempSet = new HashSet();
        if(list.equals("shoppingList")){
            tempSet = WordSearchGetPrefs.getStringSet("myShoppingList", tempSet);
        } else {
            tempSet = WordSearchGetPrefs.getStringSet("myCartList", tempSet);
        }
        return new ArrayList<>(tempSet);
    }

}