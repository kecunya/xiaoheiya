package com.example.listviewtest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;


public class PlayerAdapter extends ArrayAdapter<Player> {
    private int resourceId;
    public PlayerAdapter(Context context, int textViewresourceId,
                         List<Player> objects) {
        super(context, textViewresourceId,objects);
        resourceId=textViewresourceId;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        Player player=getItem(position);
        View view;
        if (convertView==null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        }else {
            view=convertView;
        }
        ImageView playerImage=(ImageView) view.findViewById(R.id.playerimage);
        TextView playerName=(TextView) view.findViewById(R.id.playername);
        playerImage.setImageResource(player.getImageId());
        playerName.setText(player.getName());
        return view;
    }
}
