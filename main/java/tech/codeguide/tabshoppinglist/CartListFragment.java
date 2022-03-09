package tech.codeguide.tabshoppinglist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import androidx.fragment.app.Fragment;

// TODO: 3/5/2022 See ShoppingListFragment
public class CartListFragment extends Fragment {
    View view = null;
    public static ListView cartListLV = null;
    public static ListViewAdapter adapter = null;

    public CartListFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cart_list, container, false);
        adapter = new ListViewAdapter (getContext(), MainActivity.cartList);
        cartListLV = view.findViewById(R.id.cartListLV);
        cartListLV.setAdapter(adapter);

        return view;
    }

    public static void removeItem (String selectedItem, final int position, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Remove " + selectedItem + "?");
        builder.setPositiveButton("Yes", (dialogInterface, i) -> {
            if (selectedItem.trim().equals(MainActivity.cartList.get(position).trim())) {
                MainActivity.cartList.remove(position);
                MainActivity.storeArrayVal(MainActivity.cartList, context);
            }
            cartListLV.setAdapter(adapter);
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
            if (selectedItem.trim().equals(MainActivity.cartList.get(position).trim())) {
                MainActivity.cartList.set(position, MainActivity.preferredCase(input.getText().toString()));
                MainActivity.storeArrayVal(MainActivity.cartList, context);
            }
            cartListLV.setAdapter(adapter);
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());
        builder.show();
    }

}