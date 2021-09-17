package com.example.myninja4ii;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private EditText  mTitle, mDesc;
    private Button mSaveBtn, mShowBtn;

    private FirebaseFirestore db;

    private String uTitle, uDesc, uId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = findViewById(R.id.edit_title);
        mDesc = findViewById(R.id.edit_desc);
        mSaveBtn = findViewById(R.id.button1);
        mShowBtn = findViewById(R.id.button2);

        db = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            mSaveBtn.setText("Update");
             uTitle = bundle.getString("uTitle");
            uId = bundle.getString("uId");
            uDesc = bundle.getString("uDesc");
            mTitle.setText(uTitle);
            mDesc.setText(uDesc);

        }else{
            mSaveBtn.setText("Save");
        }





        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = mTitle.getText().toString();
                String desc = mDesc.getText().toString();

                Bundle bundle = getIntent().getExtras();
                if(bundle != null){
                    String id = uId;
                    updateToFirestore(id,title,desc);


                }else{

                    String  id = UUID.randomUUID().toString();

                    saveToFireStore(id,title,desc);
                }


            }
        });

        mShowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ShowActivity.class));

            }
        });

    }

    private void updateToFirestore(String id, String title, String desc) {
        db.collection("Documents").document(id).update("title", title, "desc",desc)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"Data Updated!",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this,"Error :  "+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void saveToFireStore(String id, String title, String desc) {

        if(!title.isEmpty() && !desc.isEmpty()){
            HashMap<String,Object> map = new HashMap<>();
            map.put("id", id);
            map.put("title", title);
            map.put("desc", desc);

            db.collection("Documents").document(id).set(map)
                    .addOnCompleteListener(new OnCompleteListener<Void>(){


                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"Data Saved!",Toast.LENGTH_SHORT).show();
                            }
                        }


                    }).addOnFailureListener(new OnFailureListener(){
                @Override
                public void onFailure(@NonNull @org.jetbrains.annotations.NotNull Exception e) {
                    Toast.makeText(MainActivity.this,"Failed to Save!",Toast.LENGTH_SHORT).show();
                }

            });

        }else {
            Toast.makeText(this,"Fields empty",Toast.LENGTH_SHORT).show();
        }
    }
}