package tech.codeguide.tabshoppinglist;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import androidx.fragment.app.Fragment;

public class ShoppingListFragment extends Fragment{
    // Quick Fix - Not sure what this does
    @SuppressLint("StaticFieldLeak")
    public static ListView shoppingListLV;
    private View view;
    //private ListAdapter adapter;
    @SuppressLint("StaticFieldLeak")
    public static ListViewAdapter adapter;
    // Constructor
    public ShoppingListFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        //adapter = new ArrayAdapter<>(view.getContext(), android.R.layout.simple_list_item_1,MainActivity.shoppingList);
        adapter = new ListViewAdapter(getContext(), MainActivity.shoppingList);
        shoppingListLV = view.findViewById(R.id.shoppingListLV);
        shoppingListLV.setAdapter(adapter);

        return view;
    }

    public static void removeItem (String selectedItem, final int position, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Remove " + selectedItem + "?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            if (selectedItem.trim().equals(MainActivity.shoppingList.get(position).trim())) {
                MainActivity.shoppingList.remove(position);
                MainActivity.storeArrayVal(MainActivity.shoppingList, context);
            }
            shoppingListLV.setAdapter(adapter);
        });
        builder.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
        builder.show();
    }

    public static void editItem (String selectedItem, final int position, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit " + selectedItem + "?");
        final EditText input = new EditText(context);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            if (selectedItem.trim().equals(MainActivity.shoppingList.get(position).trim())) {
                MainActivity.shoppingList.set(position, MainActivity.preferredCase(input.getText().toString()));
                MainActivity.storeArrayVal(MainActivity.shoppingList, context);
            }
            shoppingListLV.setAdapter(adapter);
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());
        builder.show();
    }

}
