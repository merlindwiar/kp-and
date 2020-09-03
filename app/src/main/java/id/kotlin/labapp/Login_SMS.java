package id.kotlin.labapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Login_SMS extends AppCompatActivity {

    private EditText no_telpon, kode_verif;
    private Button send, verif;
    private LinearLayout layout_verif;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener stateListener;
    private PhoneAuthProvider.ForceResendingToken resendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String VerifikasiID;
    private String No_Telepon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__s_m_s);
        no_telpon = findViewById(R.id.no_telpon);
        kode_verif = findViewById(R.id.verif);
        send = findViewById(R.id.btn_send_sms);
        verif = findViewById(R.id.btn_verif);
        layout_verif = findViewById(R.id.layout_verif);
        auth = FirebaseAuth.getInstance();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                daftar();
                layout_verif.setBackgroundColor(R.drawable.rounded);
            }
        });

        verif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verifiCode = kode_verif.getText().toString();
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerifikasiID, verifiCode);
                signInWithPhoneAuthCredential(credential);
            }
        });
    }


    private void setupVerificationCallback() {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
                // Callback didalam sini akan dipanggil/dieksekusi saat terjadi proses pengiriman kode
                // Dan User Diminta untuk memasukan kode verifikasi

                // Untuk Menyimpan ID verifikasi dan kirim ulang token
                VerifikasiID = verificationId;
                resendToken = token;
//                send.setEnabled(true);
                Toast.makeText(getApplicationContext(), "Mendapatkan Kode Verifikasi", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential Credential) {
                // Callback disini akan dipanggil saat Verifikasi Selseai atau Berhasil
                Toast.makeText(getApplicationContext(), "Verifikasi Selesai", Toast.LENGTH_SHORT).show();
                signInWithPhoneAuthCredential(Credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // Callback disini akan dipanggil saat permintaan tidak valid atau terdapat kesalahan
                Toast.makeText(getApplicationContext(), "Verifikasi Gagal, Silakan Coba Lagi", Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //Sign In Berhasil
                            startActivity(new Intent(Login_SMS.this, Home.class));
                            finish();
                        } else {
                            //Sign In Gagal
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // Kode Yang Dimasukan tidal Valid.
                                Toast.makeText(getApplicationContext(), "Kode yang dimasukkan tidak valid", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void login() {
        No_Telepon = no_telpon.getText().toString().trim();
        setupVerificationCallback();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                No_Telepon, //NO Telepon Untuk Di Verifikasi
                60, //Durasi Waktu Habis
                TimeUnit.SECONDS, //Unit Timeout
                this, //Activity
                callbacks); // OnVerificationStateChangedCallbacks
        Toast.makeText(getApplicationContext(), "Memverifikasi, Mohon Tunggu", Toast.LENGTH_SHORT).show();
        no_telpon.setText("");


    }

    private void daftar() {
        No_Telepon = no_telpon.getText().toString();
        setupVerificationCallback();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                No_Telepon, //NO Telepon Untuk Di Vertifikai
                60, //Durasi Waktu Habis
                TimeUnit.SECONDS, //Unit Timeout
                this, //Activity
                callbacks, // OnVerificationStateChangedCallbacks
                resendToken); // Digunakan untuk mengirim ulang kembali kode verifikasi
        Toast.makeText(getApplicationContext(), "Mengirim Ulang Kode Verifikasi", Toast.LENGTH_SHORT).show();
    }

}