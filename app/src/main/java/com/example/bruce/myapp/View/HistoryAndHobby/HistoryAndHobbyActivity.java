package com.example.bruce.myapp.View.HistoryAndHobby;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruce.myapp.Adapter.HistoryAdapter;
import com.example.bruce.myapp.CircleTransform;
import com.example.bruce.myapp.Data.Tourist_Location;
import com.example.bruce.myapp.Data.UserProfile;
import com.example.bruce.myapp.GPSTracker;
import com.example.bruce.myapp.Presenter.HistoryAndHobby.PHistoryAndHobby;
import com.example.bruce.myapp.Presenter.MenuFragment.PMenuFragment;
import com.example.bruce.myapp.R;
import com.example.bruce.myapp.TeamBroadcastReceiver;
import com.example.bruce.myapp.View.BigMap.BigMapsActivity;
import com.example.bruce.myapp.View.Information_And_Comments.InformationAndCommentsActivity;
import com.example.bruce.myapp.View.Login.LoginActivity;
import com.example.bruce.myapp.View.MenuFragment.IViewMenuFragment;
import com.example.bruce.myapp.View.Team.TeamActivity;
import com.example.bruce.myapp.View.User.UserProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HistoryAndHobbyActivity extends AppCompatActivity implements IViewHistoryAndHobby,IViewMenuFragment,HistoryAdapter.RecyclerViewClicklistener {


    private PHistoryAndHobby presenterHistoryAndHobby = new PHistoryAndHobby(this);
    private PMenuFragment pMenuFragment = new PMenuFragment(this);

    ImageView imgFriendProfilePicture;
    TextView txtGreeting, txtEmail;

    ActionBarDrawerToggle mToggle;
    DrawerLayout mDrawerLayout;

    RecyclerView recyclerView,recyclerViewRecommended;
    StringBuilder hashCodeListHistory = new StringBuilder();
    StringBuilder hashCodeListRecommeded = new StringBuilder();
    ArrayList<Tourist_Location> listHistory;
    ArrayList<Tourist_Location> listRecommended;
    HistoryAdapter adapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 99;
    ListView listView;
    //gps
    GPSTracker gps = new GPSTracker(HistoryAndHobbyActivity.this);

    //biến lưu lịch sử
    String history = "";
    ArrayList<Tourist_Location> allLocation;
    UserProfile userProfile;

    String[] menuItem = {};
    private Dialog dialogCreateTeam ;
    private TextView txtTeamName;
    private Button btnOk,btnCancel;
    BroadcastReceiver broadcastReceiver;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_and_hobby);


//---------------------------------------------------------------------------------------------------------------------------
        //kiểm tra GPS có bật hay chưa trong Presenter
        presenterHistoryAndHobby.receivedEnableGPS(getApplicationContext(), this);
        //lấy dữ thông tin user từ firebase
        presenterHistoryAndHobby.receivedGetUserData(FirebaseAuth.getInstance().getCurrentUser().getUid());
//----------------------------------------------------------------------------------------------------------------------------
        initialize();
        //giao dien profile của ng đăng nhập bằng facebook
        interfaceFacebookUser(user,imgFriendProfilePicture,txtEmail,txtGreeting);
        //hien thi nut memu (toggle)
        setUpToggle(mToggle,mDrawerLayout);
        //set up menu
        setUpListViewMenu(listView);

        checkInternetActivity();
    }

    private void checkInternetActivity(){
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int[] type = {ConnectivityManager.TYPE_WIFI, ConnectivityManager.TYPE_MOBILE};
                //It's like the first way
                if(TeamBroadcastReceiver.isNetWorkAvailable(context,type) == true){
                    FirebaseDatabase.getInstance().getReference("User").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Toast.makeText(context, "Change.....", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    Toast.makeText(HistoryAndHobbyActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                }
            }
        };
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }

    private void interfaceFacebookUser(FirebaseUser user, ImageView imgFriendProfilePicture, TextView txtEmail, TextView txtGreeting){

        if (user != null) {
            //neu ko dang nhap bang facebook thi` ...
            Picasso.with(this).load(user.getPhotoUrl()).transform(new CircleTransform()).into(imgFriendProfilePicture);
            txtGreeting.setText(user.getDisplayName());
            txtEmail.setText(user.getEmail());

        } else {
            Toast.makeText(getApplicationContext(), "chua co user", Toast.LENGTH_SHORT).show();
        }
    }

    private void setUpToggle(ActionBarDrawerToggle mToggle,DrawerLayout mDrawerLayout){
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.mToggle = mToggle;
    }

    private void setUpListViewMenu(final ListView listView){
        presenterHistoryAndHobby.receivedTeamChecker(user.getUid(),menuItem,listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String value = (String) listView.getItemAtPosition(position);
                if(value == "Log out")
                {
                    pMenuFragment.receivedLogout();
                }
                if(value == "Map"){

                    finish();
                    Intent target = new Intent(HistoryAndHobbyActivity.this, BigMapsActivity.class);
                    target.putParcelableArrayListExtra("allLocation",allLocation);

                    startActivity(target);
                }
                if(value == "My profile"){
                    finish();
                    Intent target = new Intent(HistoryAndHobbyActivity.this, UserProfileActivity.class);
                    ArrayList<UserProfile> listUser=new ArrayList<>();
                    listUser.add(userProfile);
                    target.putParcelableArrayListExtra("user",listUser);
                    startActivity(target);
                }
                if(value=="Team")
                {
                    finish();
                    Intent target = new Intent(HistoryAndHobbyActivity.this, TeamActivity.class);
                    startActivity(target);
                }
                if(value=="Create Team")
                {
                    dialogCreateTeam = new Dialog(HistoryAndHobbyActivity.this);
                    dialogCreateTeam.setContentView(R.layout.dialog_create_team);
                    txtTeamName=dialogCreateTeam.findViewById(R.id.txtTeamName);
                    btnOk=dialogCreateTeam.findViewById(R.id.btnOk);
                    btnCancel=dialogCreateTeam.findViewById(R.id.btnCancel);
                    btnOk.setOnClickListener(v -> {
                        String TeamName=txtTeamName.getText().toString();
                        FirebaseDatabase.getInstance().getReference("CheckTeam").child(user.getUid()).child("Captain").setValue(user.getUid());
                        FirebaseDatabase.getInstance().getReference("TeamUser").child(user.getUid()).child("TeamName").setValue(TeamName);
                        FirebaseDatabase.getInstance().getReference("TeamUser").child(user.getUid()).child("Member").setValue(user.getUid());
                        dialogCreateTeam.dismiss();
                    });
                    dialogCreateTeam.show();
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogCreateTeam.dismiss();
                        }
                    });
                }
            }
        });
            }

    private void initialize() {
        imgFriendProfilePicture = findViewById(R.id.imgUser);
        txtGreeting = findViewById(R.id.txtDisplayName);
        txtEmail = findViewById(R.id.txtEmail);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        recyclerView = findViewById(R.id.recyclerView_History);
        recyclerViewRecommended = findViewById(R.id.recyclerView_Recommended);
        listView = findViewById(R.id.Menu);
    }

    private void setUpRecyclerView(RecyclerView recyclerView, HistoryAdapter adapter, ArrayList<Tourist_Location> listHistoryLocation) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new HistoryAdapter(listHistoryLocation, this);
        this.adapter = adapter;
        adapter.setClickListenerRecyclerView(this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void EnableGPS_API22() {
        gps.getLocation();
        if (!gps.canGetLocation()) {
            gps.showSettingAlert();
        }
    }

    @Override
    public void EnableGPS_APILater22() {
        gps.getLocation();
        if (!gps.canGetLocation()) {
            gps.showSettingAlert();
        }

        int accessCoarsePermission
                = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);
        int accessFinePermission
                = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED
                || accessFinePermission != PackageManager.PERMISSION_GRANTED) {

            // Các quyền cần người dùng cho phép.
            String[] permissions = new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.ACCESS_FINE_LOCATION};

            // Hiển thị một Dialog hỏi người dùng cho phép các quyền trên.
            ActivityCompat.requestPermissions(this, permissions, REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);
        }
    }
    @Override
    public String GetUserHistory(String history) {
        //lấy dữ liệu các địa điểm
        this.history = history;
        presenterHistoryAndHobby.receivedGetLocationData( HistoryAndHobbyActivity.this);
        return null;

    }

    @Override
    public String getUserBehavior(String behavior) {
        presenterHistoryAndHobby.receivedRecommendeToUser(behavior,history,allLocation);
        return null;
    }

    @Override
    public UserProfile UserProfile(UserProfile User) {
        return this.userProfile = User;
    }

    @Override
    public ArrayList<Tourist_Location> GetLocationData(ArrayList<Tourist_Location> tourist_locations) {

        //lấy dữ liệu của tất cả các địa điểm để truyền qua activity khác
        allLocation = tourist_locations;
//---------------------------------------------------------------------------------------------------------
        presenterHistoryAndHobby.receivedGetUserHistoryLocation(history, tourist_locations);
        //lấy dữ liệu thông tin hanh vi của user
        presenterHistoryAndHobby.receivedGetUsetBehavior(FirebaseAuth.getInstance().getCurrentUser().getUid());
        return null;
    }

    @Override
    public ArrayList<Tourist_Location> GetUserHistoryLocation(ArrayList<Tourist_Location> tourist_locations) {
        setUpRecyclerView(recyclerView,adapter,tourist_locations);
        hashCodeListHistory.append(tourist_locations.hashCode());
        listHistory = tourist_locations;
        return null;
    }

    @Override
    public ArrayList<Tourist_Location> returnRecommendedList(ArrayList<Tourist_Location> tourist_locations) {
        setUpRecyclerView(recyclerViewRecommended,adapter,tourist_locations);
        hashCodeListRecommeded.append(tourist_locations.hashCode());
        listRecommended = tourist_locations;
        return null;
    }

    @Override
    public void HasTeam(String[] menuItem, ListView listView) {
        menuItem = new String[]{"My profile",
                "Map",
                "Log out",
                "Team"};
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                menuItem);
        listView.setAdapter(listViewAdapter);
    }

    @Override
    public void HasNoTeam(String[] menuItem, ListView listView) {
        menuItem = new String[]{"My profile",
                "Map",
                "Log out",
                "Create Team"};
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                menuItem);
        listView.setAdapter(listViewAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(getIntent());
    }


    @Override
    public void LogoutSuccess() {
        Intent target = new Intent(this, LoginActivity.class);
        finish();
        startActivity(target);
    }
    private void setUpDialog(Dialog info ,Tourist_Location tl){
        info = new Dialog(HistoryAndHobbyActivity.this);
        //info.requestWindowFeature(Window.FEATURE_NO_TITLE); -- bo title cua dialog
        info.setContentView(R.layout.dialog_bigmap);
        info.setTitle("Choose what you want !");
        info.show();
        Button btnDirection = info.findViewById(R.id.btnDirection);
        Button btnInformation = info.findViewById(R.id.btnInformation);

        btnDirection.setOnClickListener( v -> {
            finish();
            Intent target = new Intent(HistoryAndHobbyActivity.this, BigMapsActivity.class);
            target.putParcelableArrayListExtra("allLocation",allLocation);
            target.putExtra("destination",tl.getLatitude() + ", " + tl.getLongtitude());
            startActivity(target);
        });

        btnInformation.setOnClickListener(v->{
            ArrayList<Tourist_Location> tls = new ArrayList<>();
            tls.add(tl);
            finish();
            Intent target = new Intent(HistoryAndHobbyActivity.this, InformationAndCommentsActivity.class);
            target.putParcelableArrayListExtra("tourist_location",tls);
            startActivity(target);
        });
    }
    @Override
    public void onClickItemRecyclerView(View view, int position, String listId) {
        if(listId.equals(hashCodeListHistory.toString())){
            Tourist_Location tl = listHistory.get(position);
            final Dialog info = new Dialog(HistoryAndHobbyActivity.this);
            setUpDialog(info,tl);
        }
        else{
            Tourist_Location tl = listRecommended.get(position);
            final Dialog info = new Dialog(HistoryAndHobbyActivity.this);
            setUpDialog(info,tl);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}


