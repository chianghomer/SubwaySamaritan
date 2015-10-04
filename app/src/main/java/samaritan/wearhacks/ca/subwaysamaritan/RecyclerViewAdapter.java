package samaritan.wearhacks.ca.subwaysamaritan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HenryChiang on 15-10-03.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public static ArrayList<Message> mData;
    private static Context context;


    public RecyclerViewAdapter(){

    }
    public RecyclerViewAdapter(Context context,ArrayList<Message> mData) {
        this.mData = mData;
        this.context=context;

    }

    public RecyclerViewAdapter(ArrayList<Message> mData) {
        this.mData = mData;
    }

    @Override
    public int getItemViewType(int position) {
        return (mData==null ? 0 : mData.get(position).getViewType());
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v=null;
        ViewHolder vh;
        switch (viewType) {
            case 1:
                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.message_left, viewGroup, false);
                break;
            case 2:
                v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.messgae_right, viewGroup, false);
                break;
        }

        vh = new ViewHolder(v, viewType);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder,int i) {

        final Message message = mData.get(i);
        final int viewType=getItemViewType(i);
        viewHolder.bind(message, viewType);
    }

    @Override
    public int getItemCount() {
        if (mData != null)
            return mData.size();
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;


        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            switch (viewType){
                case 1:
                    mTextView = (TextView)itemView.findViewById(R.id.message_left_txt);
                    break;
                case 2:
                    mTextView = (TextView)itemView.findViewById(R.id.message_right_txt);
                    break;
            }
            mTextView.setTypeface(FontManager.getTypeface(context));

        }

        public void bind(final Message data, int viewType) {
            switch (viewType) {
                case 1:
                    mTextView.setText(data.getText());
                case 2:
                    mTextView.setText(data.getText());

            }
        }
    }

    public void swap(ArrayList<Message> data){
        mData=data;
        notifyDataSetChanged();
    }
}


