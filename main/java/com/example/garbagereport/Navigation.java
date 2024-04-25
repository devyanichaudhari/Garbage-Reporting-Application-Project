package com.example.garbagereport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class Navigation extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ViewPager viewpager;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        drawerLayout = findViewById(R.id.drawerlayout);
        viewpager = findViewById(R.id.viewpager);
        linearLayout = findViewById(R.id.linearlayout2);
        List<Integer> imagelist = new ArrayList<>();
        imagelist.add(R.drawable.image5);
        imagelist.add(R.drawable.image6);
        imagelist.add(R.drawable.image7);
        imagelist.add(R.drawable.image10);
        imagelist.add(R.drawable.image11);
        MyAdapter myAdapter = new MyAdapter(imagelist);
        viewpager.setAdapter(myAdapter);
    }
    public  void ClickCenter(View view)
    {
        redirectActivity(this,CenterActivity.class);
    }
    public void ClickMenu(View view)
    {
        //open drawer
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout)
    {
        //open drawerLayout
        drawerLayout.openDrawer(GravityCompat.START);
    }
    public void ClickLogo(View view)
    {
        //close drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout)
    {
        //close drawer layout
        //check condition
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            //when drawer is open
            //Close Drawer
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    public void ClickHome(View view)
    {
        //recreate activity
        recreate();
    }
    public void ClickMyReport(View view)
    {
        //redirect activity
        redirectActivity(this,MyReportActivity.class);
    }
    public void ClickAboutUs(View view)
    {
        //redirect activity
        redirectActivity(this,AboutUsActivity.class);
    }
    public void ClickShare(View view)
    {
        Intent intent =new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Check out this cool Application");
        intent.putExtra(Intent.EXTRA_TEXT,"Your Application link here");
        startActivity(Intent.createChooser(intent,"Share Via"));
    }
    public void ClickCamera(View view)
    {
        //redirect activity
        redirectActivity(this,CameraActivity.class);
    }
    public void ClickReport(View view)
    {
        redirectActivity(this,MyReportActivity.class);
    }
    public void ClickLogout(View view)
    {
        logout(this);
    }
    public static void logout(Activity activity) {
        //Initialize alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        //set Title
        builder.setTitle("LogOut");
        builder.setMessage("Are you sure you want to Log Out?");
        //positive yes Button
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //finish activity
                activity.finishAffinity();
                //exit app
                System.exit(0);
            }
        });
        //negative No button
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // dismiss dialog
                dialog.dismiss();
            }
        });
        // show dialog
        builder.show();
    }

    public static void redirectActivity(Activity activity,Class aClass)
    {
        //Initialize intent
        Intent intent = new Intent(activity,aClass);
        //set Flag
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }
    @Override
    protected void onPause() {
        super.onPause();
        //close drawer
        closeDrawer(drawerLayout);
    }
}