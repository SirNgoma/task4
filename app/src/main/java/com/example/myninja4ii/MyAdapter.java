package com.example.myninja4ii;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    private ShowActivity activity;

    private List<Model> mList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); //delete

public MyAdapter(ShowActivity activity, List<Model> mList){
    this.activity = activity;
    this.mList = mList;
}





//////////UPDAte///////

    //public void update
    public void updateData(int position){
    Model item = mList.get(position);
        Bundle bundle = new Bundle();
        bundle.putString("uId",item.getId());
        bundle.putString("uTitle",item.getTitle());
        bundle.putString("uDesc",item.getDesc());
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);

    }


    ///////////////

    //////////DELETE///////

    //public void update
    public void deleteData(int position){
        Model item = mList.get(position);
        db.collection("Documents").document(item.getId()).delete()
                .addOnCompleteListener(new OnCompleteListener<Void>(){


                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            notifyRemoved(position);
                            Toast.makeText(activity,"Data Deleted!",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(activity,"Error : "+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }


                });

    }


    ///////////////

    private void notifyRemoved(int position){
    mList.remove(position);
    notifyItemRemoved(position);
    activity.showData();

    }








    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(R.layout.item,parent,false);
          return new MyViewHolder(v);

    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.title.setText(mList.get(position).getTitle());
        holder.desc.setText(mList.get(position).getDesc());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title, desc;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.title_text);
            desc = itemView.findViewById(R.id.desc_text);
        }
    }
}
