package com.example.garbagereport;

import static android.service.controls.ControlsProviderService.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.garbagereport.Model.Data;
import com.example.garbagereport.Model.Users;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class CameraActivity extends AppCompatActivity {
    String TAG = "devyani";
    String[] TO_EMAILS;
    ImageView imageView;
    Uri uri1;
    ArrayList<String> list= new ArrayList<>();
    String locality,email;
    Button btnopen;
    ImageButton btn;
    EditText fetchlocation;
    EditText fetchcomplaint;
    FusedLocationProviderClient fusedLocationProviderClient;
    Button btndone;
    String currentPhotoPath;
    StorageReference storageReference;
    DatabaseReference databaseReference,IdatabaseReference,databaseRef ;
    public static final int CAMERA_PREM_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imageView = findViewById(R.id.image_view);
        btnopen = findViewById(R.id.bt_open);
        btn = findViewById(R.id.getLocation);
        fetchlocation = findViewById(R.id.fetchlocation);
        fetchcomplaint = findViewById(R.id.fetchcomplaint);
        btndone = findViewById(R.id.bt_done);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseRef = FirebaseDatabase.getInstance().getReference().child("Data");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Details");
        IdatabaseReference = FirebaseDatabase.getInstance().getReference().child("Image");
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(CameraActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                } else {
                    ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
                }
            }
        });
        btndone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sendemail();
            }
        });
    }
    @SuppressLint("MissingPermission")
    private void getLocation() {
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                if (location != null) {
                    try {
                        Geocoder geocoder = new Geocoder(CameraActivity.this, Locale.getDefault());
                        List<Address> addresses = null;
                        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        fetchlocation.setText(Html.fromHtml("<font color ='#6200EE'></font>" + addresses.get(0).getAddressLine(0)));
                        locality = addresses.get(0).getLocality();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        btnopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               askCameraPermission();
            }
        });
    }

    private void askCameraPermission()
    {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.CAMERA},CAMERA_PREM_CODE);
        }
        else
        {
            dispatchTakePictureIntent();
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PREM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "Camera Permission is Required to use camera", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void insertData()
    {
        String location = fetchlocation.getText().toString();
        String complaint = fetchcomplaint.getText().toString();
        Details details = new Details(location,complaint);
        databaseReference.push().setValue(details);
        Toast.makeText(this, "Data Inserted", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingSuperCall")
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                imageView.setImageURI(Uri.fromFile(f));
                Log.d("tag", "Absolute Url of Image is " + Uri.fromFile(f));
                Intent mediascanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediascanIntent.setData(contentUri);
                this.sendBroadcast(mediascanIntent);
                UploadImage(f.getName(), contentUri);
            }
        }
    }

    private void UploadImage(String name, Uri contentUri) {
        StorageReference image = storageReference.child("images/" + name);
        image.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                image.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        model model = new model(uri.toString());
                        String modelid = IdatabaseReference. push().getKey();
                        IdatabaseReference.child(modelid).setValue(model);
                        insertData();
                        Picasso.get().load(uri).into(imageView);
                        uri1 = uri;
                    }
                });
                Toast.makeText(CameraActivity.this, "Image is Uploaded", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CameraActivity.this, "Upload Failed!!!!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private File createImageFile() throws IOException {
        String imageFileName = "JPEG_";
        File storagedir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storagedir
        );
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null)
        { // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

// Error occurred while creating the File
            }
// Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "com.example.android.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 100);
            }
        }
    }
    private void Sendemail()
    {
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    list.add(snapshot1.getValue().toString());
                    if(snapshot1.getKey().toString().equals(locality)) {
                        TO_EMAILS = new String[]{snapshot1.child("email").getValue().toString()};
                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setType("image/jpeg");
                        intent.setData(Uri.parse("mailto:"));
                        intent.putExtra(Intent.EXTRA_EMAIL,TO_EMAILS);
                        intent.putExtra(Intent.EXTRA_SUBJECT,fetchcomplaint.getText().toString());
                        intent.putExtra(Intent.EXTRA_TEXT,fetchlocation.getText().toString());
                        intent.putExtra(Intent.EXTRA_MIME_TYPES,uri1);
                        startActivity(Intent.createChooser(intent,"Email Via"));
                        break;
                    }
                    else
                    {
                    }
                }
            }
            @Override
           public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}