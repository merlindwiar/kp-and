package id.kotlin.labapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener stateListener;

    private EditText emailnya, passwordnya;
    private Button login, telpon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        emailnya = findViewById(R.id.emailnya);
        passwordnya = findViewById(R.id.password);
        login = findViewById(R.id.btn_loginya);
        telpon = findViewById(R.id.telpon);
        mAuth = FirebaseAuth.getInstance();
        stateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //Meneteksi Apakah Ada User Yang Sedang Login (Belum Logout)
                if (user != null) {
                    //Jika Ada, User Tidak perlu Login Lagi, dan Langsung Menuju Acivity Yang Dituju
                    startActivity(new Intent(Login.this, Home.class));
                    finish();
                }
            }
        };

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                lomgin();
            }
        });
        telpon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Login_SMS.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(stateListener);


    }

    @Override
    protected void onStop() {
        super.onStop();
        if(stateListener != null){
            //Menghapus Listener pada FirebaseAuth saat Activity Dihentikan
            mAuth.removeAuthStateListener(stateListener);
        }
    }


//    private void lomgin() {
//        String email = emailnya.getText().toString().trim();
//        String password = passwordnya.getText().toString().trim();
//
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d("sukses", "signInWithEmail:success");
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            Intent i = new Intent(Login.this, Home.class);
//                            startActivity(i);
//                            finish();
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w("gagal", "signInWithEmail:failure", task.getException());
//                            Toast.makeText(Login.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//
//                            // ...
//                        }
//
//                        // ...
//                    }
//                });
//    }
}