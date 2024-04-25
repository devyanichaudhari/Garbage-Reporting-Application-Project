package com.example.garbagereport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AboutUsActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        drawerLayout = findViewById(R.id.drawerlayout);
    }
    public void ClickMenu(View view)
    {
        //open drawer
        Navigation.openDrawer(drawerLayout);

    }
    public void ClickLogo(View view)
    {
        //close drawer
        Navigation.closeDrawer(drawerLayout);
    }
    public void ClickHome(View view)
    {
        Navigation.redirectActivity(this,Navigation.class);
    }
    public void ClickMyReport(View view)
    {
        Navigation.redirectActivity(this,MyReportActivity.class);
    }
    public void ClickAboutUs(View view)
    {
        recreate();
    }
    public void ClickShare(View view)
    {
        Intent intent =new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"check out this cool app");
        intent.putExtra(Intent.EXTRA_TEXT,"your Application link here");
        startActivity(Intent.createChooser(intent,"share via+"));
    }
    public void ClickLogout(View view)
    {
        Navigation.logout(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //close drawer
        Navigation.closeDrawer(drawerLayout);
    }
}