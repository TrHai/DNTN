package com.example.bruce.myapp.View.Information_And_Comments;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.bruce.myapp.Adapter.SectionsPageAdapter;
import com.example.bruce.myapp.Data.Tourist_Location;
import com.example.bruce.myapp.R;
import com.example.bruce.myapp.Server;
import com.example.bruce.myapp.View.Comment_Fragment.Comment_Fragment;
import com.example.bruce.myapp.View.Information_Fragment.Information_Fragment;
import com.google.firebase.auth.FirebaseAuth;
import com.hsalf.smilerating.SmileRating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InformationAndCommentsActivity extends AppCompatActivity {

    //passing data
    ArrayList<Tourist_Location> tls = new ArrayList<>();
    public String TAG ="TAG";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPage;
    private TextView txtTitle;
    private RatingBar ratingBar;
    private FloatingActionButton fabComment;
    private TabLayout tabLayout;
    private Button btnRate;
    public int costRate=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_and_comments);

        initialize();
        tls = getIntent().getParcelableArrayListExtra("tourist_location");
        setupTitle(txtTitle);
        setupViewPager(mViewPage);
        setupTabLayout(tabLayout,mViewPage);
        setDialogRate(btnRate,InformationAndCommentsActivity.this);
    }

    private void initialize(){

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPage = findViewById(R.id.container);
        tabLayout = findViewById(R.id.tabs);
        txtTitle = findViewById(R.id.title);
        ratingBar = findViewById(R.id.ratingBar_Main);
        fabComment = findViewById(R.id.floatingActionButton_Comment);
        btnRate=findViewById(R.id.btnrated);
    }
    public void setDialogRate(Button btnrate, Context context){
        btnrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=new Dialog(InformationAndCommentsActivity.this);
                dialog.setContentView(R.layout.dialog_rating);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                SmileRating smileRating=dialog.findViewById(R.id.smile_rating);
                Button btnRating=dialog.findViewById(R.id.btnrate);
                Button btncancel=dialog.findViewById(R.id.btncancel);

                smileRating.setOnSmileySelectionListener(new SmileRating.OnSmileySelectionListener() {
                    @Override
                    public void onSmileySelected(int smiley, boolean reselected) {
                        switch (smiley) {
                            case SmileRating.BAD:
                                Log.i(TAG, "Bad");
                                costRate =smileRating.getRating();
                                Log.i(TAG,String.valueOf(costRate));
                                break;
                            case SmileRating.GOOD:
                                Log.i(TAG, "Good");
                                costRate =smileRating.getRating();
                                Log.i(TAG,String.valueOf(costRate));
                                break;
                            case SmileRating.GREAT:
                                Log.i(TAG, "Great");
                                costRate =smileRating.getRating();
                                Log.i(TAG,String.valueOf(costRate));
                                break;
                            case SmileRating.OKAY:
                                Log.i(TAG, "Okay");
                                costRate =smileRating.getRating();
                                Log.i(TAG,String.valueOf(costRate));
                                break;
                            case SmileRating.TERRIBLE:
                                Log.i(TAG, "Terrible");
                                costRate =smileRating.getRating();
                                Log.i(TAG,String.valueOf(costRate));
                                break;
                        }
                    }
                });
                btnRating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(InformationAndCommentsActivity.this, "Đáng giá", Toast.LENGTH_SHORT).show();
                        RequestQueue requestQueue= Volley.newRequestQueue(context);
                        StringRequest stringRequest=new StringRequest(Request.Method.POST, Server.url_PostRating, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                    Log.i(TAG,"rated success");
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i(TAG,"rated failed");
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String>param=new HashMap<>();
                                param.put("idUser",FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                param.put("idLocation",String.valueOf(tls.get(0).getLocation_ID()));
                                param.put("rate",String.valueOf(costRate));
                                return super.getParams();
                            }
                        };
                        requestQueue.add(stringRequest);
                    }
                });

                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

            }
        });
    }
    private void setupTitle(TextView txtTitle){
        txtTitle.setMaxLines(1);
        //chấm 3 chấm nếu tiêu đề quá dài
        txtTitle.setEllipsize(TextUtils.TruncateAt.END);
        txtTitle.setText(tls.get(0).getLocationName());
    }

    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new Information_Fragment(),"Infomation");
        adapter.addFragment(new Comment_Fragment(),"Comments");
        viewPager.setAdapter(adapter);
    }

    private void setupTabLayout(TabLayout tabLayout, ViewPager viewPager){
        tabLayout.setupWithViewPager(mViewPage);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
