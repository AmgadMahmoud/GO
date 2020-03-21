package net.gogetout.go;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

/**
 * Created by Maged on 6/1/2015.
 */
public class navAdapter extends RecyclerView.Adapter<navAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<navElements> data = Collections.emptyList();
    private Context context;

    public navAdapter(Context context, List<navElements> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    public void delete(int position) {

        data.remove(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_nav_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        navElements current = data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.iconID);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder/* implements View.OnClickListener*/ {
        TextView title;
        ImageView icon;

        public MyViewHolder(View itemView) {
            super(itemView);
            /*itemView.setOnClickListener(this);*/
            title = (TextView) itemView.findViewById(R.id.navText);
            icon = (ImageView) itemView.findViewById(R.id.navIcon);
            /*icon.setOnClickListener(this);*/
        }/*

        @Override
        public void onClick(View v) {

            //getPosition & Navigate
            Toast.makeText(context, "Item #:" + getPosition(), Toast.LENGTH_SHORT).show();
        }*/

    }
}
