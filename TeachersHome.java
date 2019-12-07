package com.aamrtu.aicte;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TeachersHome extends AppCompatActivity {

    private static final String TAG = "TeachersHome";
    private static final String RIGHTS = "Rights";
    private static final String NAME = "Name";
    private static final String ID = "User_Id";

    private EditText u_name, u_password, u_id;
    private Spinner u_rights;
    private Button create_user_btn;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private DocumentReference documentReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_home);

        u_name = (EditText) findViewById(R.id.user_name);
        u_password = (EditText) findViewById(R.id.user_password);
        u_id = (EditText) findViewById(R.id.user_id);
        u_rights = (Spinner) findViewById(R.id.user_rights);
        create_user_btn = (Button) findViewById(R.id.create_user_btn);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        //documentReference = firebaseFirestore.collection("Teachers").document("Teacher_1");

    }

    public void createUserFunc(View view) {

        String name = u_name.getText().toString();
        String userid = u_id.getText().toString();
        String rights = u_rights.getSelectedItem().toString();
        String pwd = u_password.getText().toString();

        //---------------------------------------------user email and password entry------------------------------------------------------------

        if (userid.isEmpty()) {
            Toast.makeText(TeachersHome.this, "Enter User Id", Toast.LENGTH_SHORT).show();
        }
        else if (pwd.isEmpty()) {
            Toast.makeText(TeachersHome.this, "Enter User Password", Toast.LENGTH_SHORT).show();
        }
        else if (pwd.length()<7) {
            Toast.makeText(TeachersHome.this, "Password minimum length 7 is required", Toast.LENGTH_SHORT).show();
        }
        else if (!(userid.isEmpty()) && !(pwd.isEmpty())){

            String useridmail = u_id.getText().toString() + "@gmail.com";

            documentReference = firebaseFirestore.collection("Teachers").document(userid);

            Map<String, Object> userMap = new HashMap<>();

            userMap.put(NAME, name);
            userMap.put(ID, userid);
            userMap.put(RIGHTS, rights);

            documentReference.set(userMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(TeachersHome.this, "Data Saved", Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TeachersHome.this, "Error!", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, e.toString());
                        }
                    });
            firebaseAuth.createUserWithEmailAndPassword(useridmail,pwd).addOnCompleteListener(TeachersHome.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(TeachersHome.this, "Successfully User Created", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(TeachersHome.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }

    }

    public void logOut(View view){
        FirebaseAuth.getInstance().signOut();
        Toast.makeText(TeachersHome.this, "Logged out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(TeachersHome.this, TeachersLogin.class);
        startActivity(intent);
    }

}
