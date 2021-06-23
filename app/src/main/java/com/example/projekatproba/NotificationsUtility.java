package com.example.projekatproba;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

public class NotificationsUtility {
    public static final String CHANNEL_ID = "1998882";
    private final FirebaseFirestore docRef= FirebaseFirestore.getInstance();
    private String document;
    ListenerRegistration registration;

    public NotificationsUtility(String document) {
        this.document = document;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public void listenFirestore(String currentUser,android.content.Context context){
        final DocumentReference documentReference = docRef.collection("recepti").document(document);
        registration = documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("TAG", "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                   if(snapshot.getString("idPublishera").equals(currentUser) )
                   {
                       String[] nizReviewera = snapshot.getString("reviewers").split(",");
                       String onajKojiJeOcenio = nizReviewera[nizReviewera.length-1];
                       Log.d("TAG", "onajKojiJeOcenio: " + onajKojiJeOcenio);


                       if(!onajKojiJeOcenio.equals("")) {

                       DocumentReference documentReference=docRef.collection("korisnici").document(onajKojiJeOcenio);
                       documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                           @Override
                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                               if (task.isSuccessful()) {
                                   DocumentSnapshot document = task.getResult();
                                   if (document.exists()) {
                                       //TODO Notification Code

                                       NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
                                       builder.setContentTitle("Korisnik je ocenio recept: "+snapshot.getString("naziv"));
                                       builder.setContentText("Korisnik sa imenom: " + document.get("username").toString());
                                       builder.setSmallIcon(R.drawable.notifications_icon);
                                       builder.setAutoCancel(true);

                                       NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
                                       managerCompat.notify(1, builder.build());


                                       Log.d("TAG:", "DocumentSnapshot data: " + document.getData());
                                   } else {
                                       Log.d("TAG:", "No such document");
                                   }
                               } else {
                                   Log.d("TAG", "get failed with ", task.getException());
                               }
                           }
                       });


                       }
                   }
                    Log.d("TAG", "Current data: " + snapshot.getData());
                } else {
                    Log.d("TAG", "Current data: null");
                }
            }
        });
    }
}
