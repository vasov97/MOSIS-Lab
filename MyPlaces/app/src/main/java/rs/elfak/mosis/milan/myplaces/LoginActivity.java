package rs.elfak.mosis.milan.myplaces;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import com.google.firebase.auth.FirebaseAuthException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener
{
    boolean LOGIN_MODE = true;
    Button loginButton;
    Button registerButton;
    EditText email;
    EditText password;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginButton= findViewById(R.id.login_activity_loginbtn);
        registerButton = findViewById(R.id.login_activity_registerbtn);
        email = findViewById(R.id.login_activity_email);
        password = findViewById(R.id.login_activity_password);
        firebaseAuth = FirebaseAuth.getInstance();
        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);

    }
    @Override
    public void onClick(View v)
    {
            if(v.getId() == R.id.login_activity_loginbtn)
                loginUser();
            else if(v.getId() == R.id.login_activity_registerbtn)
                registerUser();
    }

    private void registerUser()
    {

        if(LOGIN_MODE)
        {
            LOGIN_MODE = false;
            loginButton.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "RegisterMode", Toast.LENGTH_SHORT).show();
        }
        else
        {
            LOGIN_MODE = true;
            loginButton.setVisibility(View.VISIBLE);
            createUserProfile();
            Toast.makeText(this, "RegisterModeDisabled", Toast.LENGTH_SHORT).show();
        }

    }

    private void createUserProfile()
    {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        if(validateInfo(emailText,passwordText))
        {
            firebaseAuth.createUserWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(LoginActivity.this,

                    new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(!task.isSuccessful())
                            {
                                FirebaseAuthException e=(FirebaseAuthException)task.getException();
                                Toast.makeText(LoginActivity.this,"Registration failed", Toast.LENGTH_SHORT).show();
                                Log.e("LoginActivity","Failed Registration",e);
                            }
                            else
                            {

                                finish();
                                startActivity(getIntent());


                            }

                        }
                    }
            );
        }

    }

    private boolean validateInfo(String emailText, String passwordText) {
        boolean valid=true;
        if(emailText.isEmpty())
        {
            email.setError("Please enter email");
            email.requestFocus();
            valid=false;
        }
        else if(passwordText.isEmpty())
        {
            password.setError("Please enter password");
            password.requestFocus();
            valid=false;
        }
        else if(emailText.isEmpty() && passwordText.isEmpty())
        {
            valid=false;
            Toast.makeText(this, "Fields are empty", Toast.LENGTH_SHORT).show();
        }
        else if(passwordText.length()<6)
        {
            valid=false;
            Toast.makeText(this, "Password must be longer than 6 characters", Toast.LENGTH_SHORT).show();
        }
       return valid;
    }

    private void loginUser()
    {
        String emailText = email.getText().toString();
        String passwordText = password.getText().toString();
        if(validateInfo(emailText,passwordText))
        {
            firebaseAuth.signInWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(LoginActivity.this,

                    new OnCompleteListener<AuthResult>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if(!task.isSuccessful())
                            {
                                FirebaseAuthException e=(FirebaseAuthException)task.getException();
                                Toast.makeText(LoginActivity.this,"Login failed", Toast.LENGTH_SHORT).show();
                                Log.e("LoginActivity","Login Registration",e);
                            }
                            else
                            {


                                startActivity(new Intent(LoginActivity.this,MainActivity.class));


                            }

                        }
                    }
            );
        }

    }
}
