package com.example.garbagereport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class MyReportActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ListView listView;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_report);
        drawerLayout = findViewById(R.id.drawerlayout);
        listView = findViewById(R.id.listview);
        ArrayList<String> list= new ArrayList<>();
        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.listitem,list);
        listView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Details");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot Datasnapshot : snapshot.getChildren())
                {
                    list.add(Datasnapshot.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        recreate();
    }
    public void ClickAboutUs(View view)
    {
        Navigation.redirectActivity(this,AboutUsActivity.class);
    }
    public void ClickShare(View view)
    {
        Intent intent =new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"Check out this cool app");
        intent.putExtra(Intent.EXTRA_TEXT,"Your Application link here");
        startActivity(Intent.createChooser(intent,"Share via+"));
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