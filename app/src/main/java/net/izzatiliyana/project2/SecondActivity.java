package net.izzatiliyana.project2;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class SecondActivity extends AppCompatActivity {

    ImageView imageView;
    TextView name, aboutus;
    Button signOut;
    Button gmap;
    Button btncl;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        imageView = findViewById(R.id.imageView);
        name = findViewById(R.id.textName);
        signOut = findViewById(R.id.button);
        gmap = findViewById(R.id.buttonmap);
        aboutus = findViewById(R.id.about);
        btncl = findViewById(R.id.buttoncl);



        gmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
            }
        });

        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this,about.class);
                startActivity(intent);

            }
        });

        btncl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SecondActivity.this,currentloc.class);
                startActivity(intent);

            }
        });


        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button:
                        signOut();
                        break;
                }
            }
        });


        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String email = acct.getEmail();
            String GivenName = acct.getGivenName();
            Uri personPhoto = acct.getPhotoUrl();


            name.setText(personName);
            Glide.with(this).load(String.valueOf(personPhoto)).into(imageView);

            BackgroundTask backgroundTask = new BackgroundTask();
            backgroundTask.execute(personName,email,GivenName);


        }


    }
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SecondActivity.this, "Signed out successfully!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }

    class BackgroundTask extends AsyncTask<String,Void,String>
    {
        String add_info_url;

        @Override
        protected void onPreExecute() {
            add_info_url = "http://192.168.1.14/locmap/add_info.php";
        }

        @Override
        protected String doInBackground(String... args) {

            String personName, email, GivenName;
            personName = args[0];
            email = args[1];
            GivenName = args [2];
            try {
                URL url = new URL(add_info_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

       String data_string = URLEncoder.encode("personName","UTF-8")+"="+URLEncoder.encode(personName,"UTF-8")+"&"+
               URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
               URLEncoder.encode("GivenName","UTF-8")+"="+URLEncoder.encode(GivenName,"UTF-8");
               bufferedWriter.write(data_string);
               bufferedWriter.flush();
               bufferedWriter.close();
               outputStream.close();
               InputStream inputStream = httpURLConnection.getInputStream();
               inputStream.close();
               httpURLConnection.disconnect();
               return  "Success";



            } catch (MalformedURLException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();

        }


    }

}