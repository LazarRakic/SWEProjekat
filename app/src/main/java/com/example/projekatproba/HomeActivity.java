package com.example.projekatproba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth baseAuth;
    private DrawerLayout drawer;
    private FirebaseFirestore docRef= FirebaseFirestore.getInstance();

    FirebaseUser user;
    TextView username;
    TextView email;
    ImageView srch;
    ImageView image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        srch= findViewById(R.id.search);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        baseAuth=FirebaseAuth.getInstance();
        user=baseAuth.getCurrentUser();

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        View headerView=navigationView.getHeaderView(0);
        username=headerView.findViewById(R.id.usrnameNav);
        email=headerView.findViewById(R.id.emailNav);
        image= headerView.findViewById(R.id.profilePicture);
        String user1=user.getUid();
        Log.d("TAG", user1+" AAAAAAAAAAAAAAAAAAAAAA");
        DocumentReference documentReference=docRef.collection("korisnici").document(user1);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        username.setText(document.get("username").toString());
                        email.setText(document.get("email").toString());
                        if(!(document.get("profileImageUrl").toString().equals("default")))
                        Picasso.get().load(document.get("profileImageUrl").toString()).into(image);
                        Log.d("TAG:", "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d("TAG:", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

        srch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), SearchActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                break;
            case R.id.nav_notifications:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotificationsFragment()).commit();
                break;
            case R.id.nav_settings_username:
                startActivity(new Intent(getApplicationContext(), ChangeUsernameActivity.class));
                break;
            case R.id.nav_settings_password:
                startActivity(new Intent(getApplicationContext(), ChangePasswordActivity.class));
                break;
            case R.id.nav_settings_delete_account:
                startActivity(new Intent(getApplicationContext(), DeleteAccountActivity.class));
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}