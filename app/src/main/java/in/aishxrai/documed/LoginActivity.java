package in.aishxrai.documed;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout tilEmail, tilPassword;
    EditText editTextEmail, editTextPassword;
    Button btn_login;
    TextView textView;

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        pref = getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = pref.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tilEmail = findViewById(R.id.textInputLayoutEmail);
        tilPassword = findViewById(R.id.textInputLayoutPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textView = findViewById(R.id.textView2);
        btn_login = findViewById(R.id.btn_login);
        firebaseAuth = FirebaseAuth.getInstance();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString().trim().toLowerCase();
                String password = editTextPassword.getText().toString().trim();

                // clearing error onTouch of editTexts
                editTextEmail.setOnTouchListener((v, event) -> {
                    tilEmail.setError(null);
                    return false;
                });
                editTextPassword.setOnTouchListener((v, event) -> {
                    tilPassword.setError(null);
                    return false;
                });

                // setting error if any field is empty
                if (email.isEmpty()) {
                    tilEmail.setError("Enter email");
                }
                else if (password.isEmpty()) {
                    tilEmail.setError(null);
                    tilPassword.setError("Enter password");
                }
                else {
                    tilPassword.setError(null);

                    // TODO: FIREBASE LOGIN
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        FirebaseUser user = firebaseAuth.getCurrentUser();
                                        Toast.makeText(LoginActivity.this, "Logged In", Toast.LENGTH_SHORT).show();

                                        // storing user info in SharedPreference

                                        editor.putString("user_email", user.getEmail());
                                        editor.putString("user_id", user.getUid());
                                        editor.putBoolean("isLoggedIn", true);
                                        editor.commit();

                                        startActivity(new Intent(LoginActivity.this, AddActivity.class));

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    // TODO: GOTO HOME PAGE

                    Toast.makeText(LoginActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // going to LoginActivity onClick of textView
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }
}