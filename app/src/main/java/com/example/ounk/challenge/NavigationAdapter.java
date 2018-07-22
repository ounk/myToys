package com.example.ounk.challenge;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class NavigationAdapter extends ArrayAdapter<Navigation> {

    private Context context;
    private List<Navigation> naviList;

    NavigationAdapter(@NonNull Context context, ArrayList<Navigation> list) {
        super(context, 0, list);
        this.context=context;
        naviList=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.listview_item,parent,false);

        Navigation currentNavi=naviList.get(position);
        TextView name=listItem.findViewById(R.id.text);
        name.setText(currentNavi.getName());

        ImageView imageView=listItem.findViewById(R.id.arrow);
        LinearLayout linearLayout=listItem.findViewById(R.id.listview_item_main);

        switch (currentNavi.getType()) {
            case MainActivity.TYPE_Section:
                imageView.setVisibility(View.INVISIBLE);
                linearLayout.setBackgroundResource (R.color.colorGray);
                break;
            case MainActivity.TYPE_Node:
                imageView.setVisibility(View.VISIBLE);
                linearLayout.setBackgroundResource (R.color.colorWhite);
                break;
            case MainActivity.TYPE_Link:
                imageView.setVisibility(View.INVISIBLE);
                linearLayout.setBackgroundResource (R.color.colorWhite);
                break;
        }
        return listItem;
    }

}
