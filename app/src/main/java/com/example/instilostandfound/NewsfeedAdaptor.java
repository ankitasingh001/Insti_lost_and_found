package com.example.instilostandfound;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter class for news feed
 */
public class NewsfeedAdaptor extends RecyclerView.Adapter<NewsfeedAdaptor.myViewHolder> {
    private Context mcontext;
    private List<CreateFoundObject> mUploads;
    private OnItemClickListener mListener;

    public NewsfeedAdaptor(Context context, List<CreateFoundObject> uploads){
        mcontext = context;
        mUploads=uploads;
    }
    /**
     * Creates view holder for adapter class
     * @param parent default parent
     * @param viewType type
     * @return
     */
    @NonNull
    @Override
    public NewsfeedAdaptor.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.list_item, parent, false);
        return  new myViewHolder(v);
    }
    /**
     * Sets the image uri onto the view
     * @param holder image holder view layout
     * @param position position of image
     */
    @Override
    public void onBindViewHolder(@NonNull NewsfeedAdaptor.myViewHolder holder, int position) {
        CreateFoundObject current_post = mUploads.get(position);
        holder.textViewname.setText(current_post.getmTitle());
        Picasso.with(mcontext).load(current_post.getImageUrl())
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }
    /**
     * Gets the count of total data /objects
     * @return count value
     */
    @Override
    public int getItemCount() {
        return mUploads.size();
    }
    /**
     * Creates recycler view for viewing item image and the title
     */
    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView textViewname;
        public ImageView imageView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewname = itemView.findViewById(R.id.list_item_textview);
            imageView = itemView.findViewById(R.id.list_item_imageview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }
            }

        }

    }

    /**
     * Implements item on click methods on the recycler view
     */
    public interface OnItemClickListener {
        /**
         * Executed when item is clicked once
         * @param position position of item on the screen
         */
        void onItemClick(int position);

        /**
         * Executed when user clicks edit option
         * @param position position of item on the screen
         */
    }
    /**
     *  Declaration for on click listener
     * @param listener listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
}
