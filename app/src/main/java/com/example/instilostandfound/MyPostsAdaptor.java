package com.example.instilostandfound;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adaptor class for my posts
 */
public class MyPostsAdaptor extends RecyclerView.Adapter<MyPostsAdaptor.myViewHolder> {
    private Context mcontext;
    private List<CreateFoundObject> mUploads;
    private OnItemClickListener mListener;

    public MyPostsAdaptor(Context context, List<CreateFoundObject> uploads){
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
    public MyPostsAdaptor.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mcontext).inflate(R.layout.list_item, parent, false);
        return  new myViewHolder(v);
    }

    /**
     * Sets the image uri onto the view
     * @param holder image holder view layout
     * @param position position of image
     */
    @Override
    public void onBindViewHolder(@NonNull MyPostsAdaptor.myViewHolder holder, int position) {
        CreateFoundObject current_post = mUploads.get(position);
        holder.textViewname.setText(current_post.getmTitle());
        Picasso.with(mcontext).load(current_post.getImageUrl())
                .fit()
                .centerCrop()
                .into(holder.imageView);
   /*     if (current_post.getImageUrl() == null) {
            Picasso.with(mcontext).load(R.id.camera).fit()
                    .centerCrop()
                    .into(holder.imageView);}*/
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
    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener{

        public TextView textViewname;
        public ImageView imageView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewname = itemView.findViewById(R.id.list_item_textview);
            imageView = itemView.findViewById(R.id.list_item_imageview);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
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

        /**
         * Menu that comes up when user right clicks image
         * @param menu menu item default
         * @param v view default
         * @param menuInfo menu default
         */
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Select Action");
                MenuItem doedit = menu.add(Menu.NONE, 1, 1, "Edit post");
                MenuItem delete = menu.add(Menu.NONE, 2, 2, "Delete post");

                doedit.setOnMenuItemClickListener(this);
                delete.setOnMenuItemClickListener(this);
            }

        /**
         * Method executed when user long clicks item
         * @param item
         * @return
         */
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {

                        switch (item.getItemId()) {
                            case 1:
                                mListener.oneditClick(position);
                                return true;
                            case 2:
                                mListener.onDeleteClick(position);
                                return true;
                        }
                    }
                }
                return false;
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
            void oneditClick(int position);
        /**
         * Executed when user clicks delete option
         * @param position position of item on the screen
         */
            void onDeleteClick(int position);
        }

    /**
     *  Declaration for on click listener
     * @param listener listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
            mListener = listener;
        }
    }
