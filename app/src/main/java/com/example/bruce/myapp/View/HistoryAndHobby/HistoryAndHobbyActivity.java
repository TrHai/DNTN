package com.example.bruce.myapp.View.HistoryAndHobby;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bruce.myapp.Adapter.HistoryAdapter;
import com.example.bruce.myapp.CircleTransform;
import com.example.bruce.myapp.Data.Tourist_Location;
import com.example.bruce.myapp.GPSTracker;
import com.example.bruce.myapp.Presenter.HistoryAndHobby.PHistoryAndHobby;
import com.example.bruce.myapp.Presenter.MenuFragment.PMenuFragment;
import com.example.bruce.myapp.R;
import com.example.bruce.myapp.View.BigMap.BigMapsActivity;
import com.example.bruce.myapp.View.Login.LoginActivity;
import com.example.bruce.myapp.View.MenuFragment.IViewMenuFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    HistoryAdapter adapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    public static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 99;
    ListView listView;
    //gps
    GPSTracker gps = new GPSTracker(HistoryAndHobbyActivity.this);

    //biến lưu lịch sử
    String history = "";
    ArrayList<Tourist_Location> allLocation;
    ProgressDialog progressDialog;
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

    }
    private void interfaceFacebookUser(FirebaseUser user,ImageView imgFriendProfilePicture,TextView txtEmail,TextView txtGreeting){

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
        String[] menuItem = {"My profile",
                "Map",
                "Log out",
                "Help"};

        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                menuItem);
        listView.setAdapter(listViewAdapter);

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
//                    getActivity().finish();
//                    Intent target = new Intent(getActivity(), User_Profile.class);
//                    startActivity(target);
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
        return null;
    }

    @Override
    public ArrayList<Tourist_Location> returnRecommendedList(ArrayList<Tourist_Location> tourist_locations) {
        setUpRecyclerView(recyclerViewRecommended,adapter,tourist_locations);

        return null;
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

    @Override
    public void onClickItemRecyclerView(View view, int position) {
        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
    }
    public void startLoading(Context context){
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Đang tải thông tin.....");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(true);
    }
    public void dismisLoading(){
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

}


