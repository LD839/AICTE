package com.aamrtu.aicte;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class TeachersLogin extends AppCompatActivity {

    Intent intent = getIntent();
    private static final String RIGHTS = "Rights";
    private static final String TAG = "TeachersLogin";


    private EditText user_id, user_pass;
    private Button login_btn;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseauthlistener;
    private ListenerRegistration dataListener;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;
    private TextView user_right;
    

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseauthlistener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_login);

//        user_id = (EditText) findViewById(R.id.user_id);
//        user_pass = (EditText) findViewById(R.id.user_pass);
//        login_btn = (Button) findViewById(R.id.login_btn);

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseauthlistener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Toast.makeText(TeachersLogin.this, "You are Logged In", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TeachersLogin.this, TeachersHome.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(TeachersLogin.this, "Please Login", Toast.LENGTH_SHORT).show();
                }

            }
        };

    }
    public void teacherHome(View view) {

        user_id = (EditText) findViewById(R.id.user_id);
        user_pass = (EditText) findViewById(R.id.user_pass);
        login_btn = (Button) findViewById(R.id.login_btn);
        user_right = (TextView) findViewById(R.id.user_right_txt);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        //documentReference = firebaseFirestore.collection("Teacher").document();

        String t_id = user_id.getText().toString();
        String email = user_id.getText().toString() + "@gmail.com";
        String pass = user_pass.getText().toString();

        if (TextUtils.isEmpty(t_id)) {
            Toast.makeText(TeachersLogin.this, "Please enter your Email", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(pass)) {
            Toast.makeText(TeachersLogin.this, "Please Enter your password", Toast.LENGTH_SHORT).show();
        }
        else if (!(TextUtils.isEmpty(t_id)) && !(TextUtils.isEmpty(pass))){

            firebaseFirestore.collection("Teachers")
                    .document(t_id)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String right = (String) documentSnapshot.get(RIGHTS);
                                Log.d(TAG, String.valueOf(right));
                                user_right.setText(right);
                                Toast.makeText(TeachersLogin.this, "Success", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(TeachersLogin.this, "User Does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TeachersLogin.this, "Error!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        }
                    });

            //---------------------------------------------email and password----------------------------------------------------------------------------------------

            firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(TeachersLogin.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        String u = user_right.getText().toString();
                        Log.d(TAG, String.valueOf(u));
                        if (u.equals("Admin") || u.equals(null )){
                            Toast.makeText(TeachersLogin.this, "Welcome Admin", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(TeachersLogin.this, TeachersHome.class);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(TeachersLogin.this, "Welcome Regular Teacher", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(TeachersLogin.this, RTeacherHome.class);
                            startActivity(intent);
                        }


                    } else {
                        Toast.makeText(TeachersLogin.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
}