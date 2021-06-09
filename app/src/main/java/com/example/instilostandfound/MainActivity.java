package com.example.instilostandfound;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Login Activity for login using LDAP credentials
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    EditText ldap_id;
    EditText ldap_password;
    public static final String ID = "com.example.instilostandfound.id";
    ProgressDialog pd;

    /**
     * Sign in using valid credentials
     * @param savedInstanceState current state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        ldap_id = findViewById(R.id.LDAP_ID);
        ldap_password = findViewById(R.id.LDAP_PASSWORD);
        setSupportActionBar(toolbar);
        findViewById(R.id.login_btn).setOnClickListener(this);
        pd = new ProgressDialog(this);
        pd.setMessage("Signing in...");

       /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }

    /**
     * Check if interent is working or not
     */
    @Override
    public void onStart() {
        super.onStart();
        NetworkConnection networkConnection = new NetworkConnection();
        if (networkConnection.isConnectedToInternet(MainActivity.this)
                || networkConnection.isConnectedToMobileNetwork(MainActivity.this)
                || networkConnection.isConnectedToWifi(MainActivity.this)) {

        } else {
            networkConnection.showNoInternetAvailableErrorDialog(MainActivity.this);
            return;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Implementing LDAP login using firebase (dummy)
     */
    private void LDAPLogin()
    {
        final String ldap = ldap_id.getText().toString().trim();
        String id       = ldap_id.getText().toString().trim()+"@iitb.ac.in";
        String password = ldap_password.getText().toString().trim();
        if (id.isEmpty())
        {
            ldap_id.setError("Please enter your ldap id");
            ldap_id.requestFocus();
            return;
        }
        if (password.isEmpty())
        {
            ldap_password.setError("Please enter your ldap password ");
            ldap_password.requestFocus();
            return;
        }
        pd.show();
        mAuth.signInWithEmailAndPassword(id,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    pd.dismiss();
                    Toast.makeText(MainActivity.this,"LOGIN SUCCESSFUL",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this, Navigation.class);
                    intent.putExtra("username", ldap);
                    startActivity(intent);


                }
                else
                {
                    pd.dismiss();
                    ldap_password.setText("");
                    Toast.makeText(MainActivity.this,"INVALID CREDENTIALS",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * Login when clicked
     * @param view login button
     */
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.login_btn:
                LDAPLogin();
                break;
        }
    }
}
