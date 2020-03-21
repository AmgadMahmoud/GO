package net.gogetout.go;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

/**
 * Created by Maged on 6/20/2015.
 */
public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    List<PeopleElements> data = Collections.emptyList();
    private Context context;
    public PeopleAdapter(Context context, List<PeopleElements> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    public void delete(int position) {

        data.remove(position);
    }

    @Override
    public PeopleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_ppl_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PeopleAdapter.MyViewHolder holder, int position) {
        PeopleElements current = data.get(position);
        holder.personName.setText(current.name);
        //Picasso.with(context).load(current.imageURL).into(holder.personImage);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView personName;
        ImageView personImage;

        public MyViewHolder(View itemView) {
            super(itemView);
            personName = (TextView) itemView.findViewById(R.id.personName);
            personImage = (ImageView) itemView.findViewById(R.id.personImage);
        }

    }

}
