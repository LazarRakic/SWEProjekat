package com.example.projekatproba;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth baseAuth;
    private DrawerLayout drawer;
    private FirebaseFirestore docRef= FirebaseFirestore.getInstance();

    FirebaseUser user;
    TextView username;
    TextView email;
    ImageView srch;
    ImageView image;
    ImageView userProfiles;

    List<String> datum;
    List<String> mail;
    List<String> ime;
    List<String> sifra;
    List<String> slikeUrl;
    List<String> prezime;
    List<String> korime;
    
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
        userProfiles=findViewById(R.id.users);
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

        userProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText pretraga = new EditText (v.getContext());
                AlertDialog.Builder pretragaDijalog = new AlertDialog.Builder(v.getContext());
                pretragaDijalog.setTitle("Pretraga profila");
                pretragaDijalog.setMessage("Unesite username za profil za koji želite da vršite pretragu");
                pretragaDijalog.setView(pretraga);

                pretragaDijalog.setPositiveButton("Pretraga", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(HomeActivity.this, "Uspesno! ", Toast.LENGTH_SHORT).show();
                        Log.d("TAG:","MAJKOOOOOOOOOOOOOOOOOOO " + pretraga.getText().toString());

                        CollectionReference usersRef= docRef.collection("korisnici");
                        Query query=usersRef.whereEqualTo("username",pretraga.getText().toString());

                           query.get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                String pom = document.getString("username");


                                                if(pom.equals(pretraga.getText().toString()))
                                                {
                                                    //startActivity(new Intent(getApplicationContext(), SearchActivity.class));
                                                    Toast.makeText(HomeActivity.this, "Uspesno uspesno majko :( ! ", Toast.LENGTH_SHORT).show();
                                                    break;

                                                }

                                                Log.d("TAG","user exist");
                                                /*datum.add(document.get("date").toString());
                                                mail.add(document.get("email").toString());
                                                ime.add(document.get("name").toString());
                                                sifra.add(document.get("password").toString());
                                                slikeUrl.add(document.get("profileImageUrl").toString());
                                                prezime.add(document.get("surname").toString());
                                                korime.add(document.get("username").toString());*/



                                            }



                                        } else {
                                            Log.d("TAG", "Error getting documents: ", task.getException());
                                        }

                                    }
                                });

                        //startActivity(new Intent(getApplicationContext(), ProfileFragment.class));

                    }
                });

                pretragaDijalog.setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(HomeActivity.this, "Neuspesno! ", Toast.LENGTH_SHORT).show();
                    }
                });
                pretragaDijalog.create().show();
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