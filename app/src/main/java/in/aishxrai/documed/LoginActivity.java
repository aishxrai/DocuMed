package in.aishxrai.documed;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    TextInputLayout tilEmail, tilPassword;
    EditText editTextEmail, editTextPassword;
    Button btn_login;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tilEmail = findViewById(R.id.textInputLayoutEmail);
        tilPassword = findViewById(R.id.textInputLayoutPassword);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        textView = findViewById(R.id.textView2);
        btn_login = findViewById(R.id.btn_login);

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