package com.example.bruce.myapp.View.Team;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bruce.myapp.Adapter.TeamAdapter;
import com.example.bruce.myapp.Data.UserProfile;
import com.example.bruce.myapp.Model.MTeam;
import com.example.bruce.myapp.Presenter.Team.PTeam;
import com.example.bruce.myapp.R;
import com.example.bruce.myapp.View.HistoryAndHobby.HistoryAndHobbyActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TeamActivity extends AppCompatActivity implements IViewTeam,TeamAdapter.RecyclerViewClicklistener{
    RecyclerView recyclerViewTeam;
    PTeam pTeam=new PTeam(this);
    TeamAdapter teamAdapter;
    Button btnInvite;
    EditText edtInvite;
    ArrayList<UserProfile> listUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);
        initialize();
        listUser=new ArrayList<>();
        recyclerViewTeam.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        teamAdapter = new TeamAdapter(listUser, this);
        recyclerViewTeam.setAdapter(teamAdapter);
        teamAdapter.setClickListener(this);
        //Hiển thị Danh sách User lên recyclerView
        pTeam.receivedAddListUser(teamAdapter,listUser);

        btnInvite.setOnClickListener(v -> {
            String EmailInvited=edtInvite.getText().toString();
            FirebaseDatabase.getInstance().getReference("User").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    UserProfile userProfile=dataSnapshot.getValue(UserProfile.class);
                    if(userProfile.Email.equals(EmailInvited))
                    {
                        String IdUser=dataSnapshot.getKey();
                        FirebaseDatabase.getInstance().getReference("CheckTeam").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(IdUser).exists()) {
                                    Toast.makeText(TeamActivity.this, EmailInvited + "đã có nhóm rồi", Toast.LENGTH_SHORT).show();
                                } else  if (dataSnapshot.child(IdUser).exists()==false) {
                                    Log.d("ffff","wqeqwe");
                                    FirebaseDatabase.getInstance().getReference("ListInviting").child(IdUser).setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    Toast.makeText(TeamActivity.this, "Đã gởi lời mời đến " + EmailInvited, Toast.LENGTH_SHORT).show();
                                    edtInvite.setText("");

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

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
        });
        RemoveTeam();
    }

    private void RemoveTeam() {

    }

    private void initialize() {
        recyclerViewTeam=findViewById(R.id.recyclerViewTeam);
        btnInvite = findViewById(R.id.btnInvite);
        edtInvite=findViewById(R.id.edtInvite);
    }
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), HistoryAndHobbyActivity.class));
        super.onBackPressed();
    }

    @Override
    public void itemClickMember(View view, int position) {
        UserProfile userProfile=listUser.get(position);
        FirebaseDatabase.getInstance().getReference("User").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.child("Email").getValue().toString().equals(userProfile.Email))
                {
                    listUser.remove(position);
                    teamAdapter.notifyDataSetChanged();
                    FirebaseDatabase.getInstance().getReference("TeamUser").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("member")
                            .child(dataSnapshot.getKey()).removeValue();
                    FirebaseDatabase.getInstance().getReference("CheckTeam").child(dataSnapshot.getKey()).removeValue();

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
        Toast.makeText(this, userProfile.Email, Toast.LENGTH_SHORT).show();
    }
}
