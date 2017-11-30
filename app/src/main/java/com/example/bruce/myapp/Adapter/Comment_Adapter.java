package com.example.bruce.myapp.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruce.myapp.R;
import com.example.bruce.myapp.Data.Comment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by BRUCE on 8/31/2017.
 */

public class Comment_Adapter extends RecyclerView.Adapter<Comment_Adapter.ViewHolder> implements Comment_Image_Adapter.RecyclerViewClicklistener {

    ArrayList<Comment> comment_contructors = new ArrayList<>();
    Context context;
    RecyclerViewClicklistener itemClickListener;
    DatabaseReference mDataLike= FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth= FirebaseAuth.getInstance();

    public Comment_Adapter(ArrayList<Comment> comment_contructors, Context context) {
        this.comment_contructors = comment_contructors;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View mView = layoutInflater.inflate(R.layout.comment_adapter,parent,false);

        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Comment cC = comment_contructors.get(position);

        ClickButtonLike(holder.txtBtnLike,holder.txtLikeNumber,context,cC);

        holder.txtComment.setText(cC.comment);
        holder.txtDateOfComment.setText(cC.date);
        holder.txtUsername.setText(cC.userName);
        holder.txtLikeNumber.setText(String.valueOf(cC.like));


        Picasso.with(context).load(cC.userImage).into(holder.userImage);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);

        holder.recyclerView_Comment_Image.setLayoutManager(layoutManager);
        holder.adapter_comment_image = new Comment_Image_Adapter(context,cC.commentImages);
        holder.adapter_comment_image.setClickListener(this);
        holder.recyclerView_Comment_Image.setAdapter(holder.adapter_comment_image);
        holder.adapter_comment_image.notifyDataSetChanged();
    }

    public void ClickButtonLike(TextView txtBtn, final TextView likeNumber, final Context context , final Comment cC){

        txtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Like_Adapter like_adapter=new Like_Adapter(cC.userID,cC.locationID);
                mDataLike.child("Likes").child(mAuth.getCurrentUser().getUid()).push().setValue(like_adapter);
                likeNumber.setText(String.valueOf(cC.like + 1));
                mDataLike.child("Comments").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Comment comment_contructor = dataSnapshot.getValue(Comment.class);
                        if(comment_contructor.date.equals(cC.date)&&comment_contructor.userID.equals(cC.userID)){
                            mDataLike.child("Comments").child(dataSnapshot.getKey()).child("like").setValue(cC.like + 1);
                        }
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    @Override
    public int getItemCount() {
        return comment_contructors.size();
    }


    @Override
    public void itemClickCommentImage(View view, int position) {
        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtUsername,txtComment,txtDateOfComment,txtBtnLike,txtLikeNumber;
        RecyclerView recyclerView_Comment_Image;
        Comment_Image_Adapter adapter_comment_image;
        ImageView userImage;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            txtUsername = itemView.findViewById(R.id.userName);
            txtComment = itemView.findViewById(R.id.Comment);
            txtDateOfComment =  itemView.findViewById(R.id.dateofComment);
            txtBtnLike = itemView.findViewById(R.id.like);
            txtLikeNumber = itemView.findViewById(R.id.likeNumber);
            userImage= itemView.findViewById(R.id.userImage);
            recyclerView_Comment_Image = itemView.findViewById(R.id.recyclerView_Comment_Image);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null){
                itemClickListener.itemClickComment(v,getPosition());
            }

        }
    }
    public void setClickListener(RecyclerViewClicklistener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface RecyclerViewClicklistener {
         void itemClickComment(View view, int position);
    }
}
