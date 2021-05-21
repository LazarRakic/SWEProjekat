package com.example.projekatproba;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    //TODO :d

    TextView username;
    TextView email;
    ListView listView;
    FirebaseAuth baseAuth;
    private FirebaseFirestore docRef= FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        FirebaseUser user=baseAuth.getInstance().getCurrentUser();
        String userIdValue=user.getUid();
        username=  view.findViewById(R.id.userName);
        email= view.findViewById(R.id.eMail);


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

        /*String userId=user.getUid();
        String nameVal = name.getText().toString();
        String surnameVal = surname.getText().toString();
        String dateVal = date.getText().toString();
        String usernameVal = username.getText().toString();
        String passwordVal = password.getText().toString();
        String emailVal = email.getText().toString();
        String profileImageUrl=*/

        /*listView= view.findViewById(R.id.listview);
        ArrayList<String> arrayList= new ArrayList<>();

        arrayList.add("Jana");
        arrayList.add("Milan");
        arrayList.add("Jovan");
        arrayList.add("Lazar");

       ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ProfileFragment.this, "clicked item" + position + arrayList.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });*/


        return view;//TODO
    }
}
