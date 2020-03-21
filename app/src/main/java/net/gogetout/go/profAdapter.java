package net.gogetout.go;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by Maged on 6/14/2015.
 */
public class profAdapter extends RecyclerView.Adapter<profAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<profElements> data = Collections.emptyList();
    private Context context;

    public profAdapter(Context context, List<profElements> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }



    public void delete(int position) {

        data.remove(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_prof_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        profElements current = data.get(position);
        holder.valueText.setText(current.value);
        holder.infoText.setText(current.info);
        holder.profIcon.setImageResource(current.iconID);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder/* implements View.OnClickListener*/ {
        TextView valueText;
        TextView infoText;
        ImageView profIcon;


        public MyViewHolder(View itemView) {
            super(itemView);
            /*itemView.setOnClickListener(this);*/
            valueText = (TextView) itemView.findViewById(R.id.valueText);
            infoText = (TextView) itemView.findViewById(R.id.infoText);
            profIcon = (ImageView) itemView.findViewById(R.id.profIcon);
            /*icon.setOnClickListener(this);*/
        }/*

        @Override
        public void onClick(View v) {

            //getPosition & Navigate
            Toast.makeText(context, "Item #:" + getPosition(), Toast.LENGTH_SHORT).show();
        }*/

    }
}