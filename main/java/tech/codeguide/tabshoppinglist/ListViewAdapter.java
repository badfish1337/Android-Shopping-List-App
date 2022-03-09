package tech.codeguide.tabshoppinglist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ListViewAdapter extends ArrayAdapter<String> {

    ArrayList<String> currentList;
    ArrayList<String> otherList;
    Context context;

    public ListViewAdapter(Context context, ArrayList<String> items){
        super(context, R.layout.list_row, items);
        this.context = context;
        currentList = items;
        otherList = (currentList.equals(MainActivity.shoppingList)) ? MainActivity.cartList : MainActivity.shoppingList;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_row, null);

            TextView number = convertView.findViewById(R.id.number);
            number.setText(position + 1 + ".");

            TextView name = convertView.findViewById(R.id.name);
            name.setText(currentList.get(position));

            ImageView toList = convertView.findViewById(R.id.toList);

            toList.setImageResource((currentList.equals(MainActivity.shoppingList) ? R.drawable.baseline_add_shopping_cart_black_48:R.drawable.baseline_playlist_add_black_48) );

            toList.setOnClickListener(view -> {
                otherList.add(currentList.get(position));
                currentList.remove(position);

                if(currentList.equals(MainActivity.shoppingList)){
                    ShoppingListFragment.shoppingListLV.setAdapter(ShoppingListFragment.adapter);
                } else {
                    CartListFragment.cartListLV.setAdapter(CartListFragment.adapter);
                }

                MainActivity.storeArrayVal(currentList, getContext());
                MainActivity.storeArrayVal(otherList, getContext());
            });

            name.setOnLongClickListener(view -> {
                showItemMenu(position, currentList.get(position));
                return true;
            });
        }
        return convertView;
    }

    public void showItemMenu (final int position, String name){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setPositiveButton("Edit", (dialogInterface, i) -> {
            if(currentList.equals(MainActivity.shoppingList)){
                ShoppingListFragment.editItem(name,position,getContext());
                ShoppingListFragment.shoppingListLV.setAdapter(ShoppingListFragment.adapter);
            } else {
                CartListFragment.editItem(name,position,getContext());
                CartListFragment.cartListLV.setAdapter(CartListFragment.adapter);
            }
        });

        builder.setNeutralButton("Remove" , (dialogInterface, i) -> {
            if(currentList.equals(MainActivity.shoppingList)){
                ShoppingListFragment.removeItem(name,position,getContext());
                ShoppingListFragment.shoppingListLV.setAdapter(ShoppingListFragment.adapter);
                MainActivity.storeArrayVal(MainActivity.shoppingList,getContext());
            } else {
                CartListFragment.removeItem(name,position,getContext());
                CartListFragment.cartListLV.setAdapter(CartListFragment.adapter);
                MainActivity.storeArrayVal(MainActivity.cartList,getContext());
            }

        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel());

        builder.show();
    }
}
