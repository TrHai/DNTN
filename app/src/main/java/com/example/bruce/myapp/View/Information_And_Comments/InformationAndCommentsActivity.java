package com.example.bruce.myapp.View.Information_And_Comments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.bruce.myapp.Adapter.SectionsPageAdapter;
import com.example.bruce.myapp.Data.Tourist_Location;
import com.example.bruce.myapp.R;
import com.example.bruce.myapp.View.Comment_Fragment.Comment_Fragment;
import com.example.bruce.myapp.View.Information_Fragment.Information_Fragment;

import java.util.ArrayList;

public class InformationAndCommentsActivity extends AppCompatActivity {

    //passing data
    ArrayList<Tourist_Location> tls = new ArrayList<>();

    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPage;
    private TextView txtTitle;
    private RatingBar ratingBar;
    private FloatingActionButton fabComment;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_and_comments);

        initialize();
        tls = getIntent().getParcelableArrayListExtra("tourist_location");
        setupTitle(txtTitle);
        setupViewPager(mViewPage);
        setupTabLayout(tabLayout,mViewPage);
    }

    private void initialize(){

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        mViewPage = findViewById(R.id.container);
        tabLayout = findViewById(R.id.tabs);
        txtTitle = findViewById(R.id.title);
        ratingBar = findViewById(R.id.ratingBar_Main);
        fabComment = findViewById(R.id.floatingActionButton_Comment);
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
}
