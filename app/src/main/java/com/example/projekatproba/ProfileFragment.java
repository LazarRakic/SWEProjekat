package com.example.projekatproba;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    //TODO :d

    ImageView image;
    Button changeProfile;
    Button changeData;
    String imageUrl;
    Uri imageUri;
    TextView username;
    TextView email;
    ListView listView;
    FirebaseAuth baseAuth;
    private FirebaseFirestore docRef= FirebaseFirestore.getInstance();
    StorageReference storage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        FirebaseUser user=baseAuth.getInstance().getCurrentUser();
        storage= FirebaseStorage.getInstance().getReference();
        String userIdValue=user.getUid();
        username=  view.findViewById(R.id.userName);
        email= view.findViewById(R.id.eMail);
        image=view.findViewById(R.id.profile_image);
        changeProfile=view.findViewById(R.id.changeProfile);
        changeData= view.findViewById(R.id.changeData);


        CollectionReference usersRef= docRef.collection("korisnici");
        usersRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getId().equals(userIdValue))
                                {
                                    username.setText(document.get("username").toString());
                                    email.setText(document.get("email").toString());
                                    Picasso.get().load(document.get("profileImageUrl").toString()).into(image);


                                    Log.d("TAG", document.getId() + " => " + document.get("username"));
                                    break;
                                }
                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
        Log.d("TAG",usersRef.getId().toString()+" AAAAAAAAAAAAAAAAAAAAAA");



        changeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Idemo na galeriju i biranje slika
                //Kreiramo novi intent

                Intent openGallery=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery,1000);
            }
        });

        changeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ChangeProfileDataActivity.class));
            }
        });

        return view;//TODO
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if (resultCode == Activity.RESULT_OK) {
                imageUri=data.getData();
                image.setImageURI(imageUri);
                //mora se pamtiti u storage
                upload(imageUri);

            }
        }
    }

    public void upload(Uri imageUri)
    {
        ProgressDialog progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Uploading");
        progressDialog.show();
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
        StorageReference fileref=storage.child(uid+".jpg");
        StorageTask task=fileref.putFile(imageUri);
        task.continueWithTask(new Continuation() {
            @Override
            public Object then(@NonNull  Task task) throws Exception {
                if(!task.isSuccessful())
                    throw task.getException();
                return fileref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull  Task<Uri> task) {
                Uri downloaduri=task.getResult();
                imageUrl=downloaduri.toString();

                DocumentReference documentReference= docRef.collection("korisnici").document(baseAuth.getInstance().getCurrentUser().getUid());

                documentReference.update(
                        "profileImageUrl",imageUrl
                ).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                            Toast.makeText(getContext(), "Slika uspešno ažurirana.", Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(getContext(), "Greška prilikom ažuriranja slike.", Toast.LENGTH_SHORT).show();
                        }

                        progressDialog.dismiss();

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {

            }
        });
    }


}
