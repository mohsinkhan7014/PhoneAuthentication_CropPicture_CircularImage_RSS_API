package com.example.haqdarshak_assignment.rssfeed.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.haqdarshak_assignment.ItemClickListener;
import com.example.haqdarshak_assignment.R;
import com.example.haqdarshak_assignment.rssfeed.module.RSSOBJECT;




class FeedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener ,View.OnLongClickListener{

    TextView txttitle,txtpub,txtcontent;
    ItemClickListener itemClickListener;

    public FeedViewHolder(@NonNull View itemView) {
        super(itemView);
        txttitle=itemView.findViewById(R.id.txtTiile);
        txtpub=itemView.findViewById(R.id.txtpub);
        txtcontent=itemView.findViewById(R.id.textConten);


        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);

    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;

    }

    @Override
    public void onClick(View v) {

     itemClickListener.onClick(v,getAdapterPosition(),false);

    }

    @Override
    public boolean onLongClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),true);
        return true;
    }
}

public class FeedAdapter extends  RecyclerView.Adapter<FeedViewHolder> {


   private RSSOBJECT rssobject;
  private   Context mContext;
 private    LayoutInflater inflater;

    public FeedAdapter(RSSOBJECT rssobject, Context mContext) {
        this.rssobject = rssobject;
        this.mContext = mContext;
        inflater=LayoutInflater.from(mContext);
    }


    @NonNull
    @Override
    public FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View itemView=inflater.inflate(R.layout.row,parent,false);
       return new FeedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedViewHolder holder, int position) {
     holder.txttitle.setText(rssobject.getItems().get(position).getTitle());
        holder.txtpub.setText(rssobject.getItems().get(position).getPubDate());
        holder.txtcontent.setText(rssobject.getItems().get(position).getContent());


    }

    @Override
    public int getItemCount() {
        return rssobject.items.size();
    }
}
