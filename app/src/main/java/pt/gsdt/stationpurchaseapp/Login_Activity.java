package pt.gsdt.stationpurchaseapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class Login_Activity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);

        etUsername = (EditText) findViewById(R.id.etUser);
        etPassword = (EditText) findViewById(R.id.etPass);
        btnLog = (Button) findViewById(R.id.btnLogin);

        btnLog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                sendMessage();


            }

        });

    }

    private void sendMessage() {

        String user_text = etUsername.getText().toString();
        String pass_text = etPassword.getText().toString();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("User", user_text);
        i.putExtra("Pass", pass_text);
        startActivity(i);

    }

    private void signInUser() {

        String user = etUsername.getText().toString();
        String pass = etPassword.getText().toString();

        RequestQueue rq;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network nw = new BasicNetwork(new HurlStack());
        rq = new RequestQueue(cache, nw);
        rq.start();

        String appID = "b6948414a6f42be3075eee653436b668";
        JSONObject usersignInCred = new JSONObject();
        String reqDate = "2019-04-10T15:14:00";
        String reqID = "1";

        JSONObject reqAuthenticator = new JSONObject();

        try {


            reqAuthenticator.put("ApplicationID", appID);
            usersignInCred.put("User", user);
            usersignInCred.put("Password", pass);
            reqAuthenticator.put("Credentials", usersignInCred);
            reqAuthenticator.put("RequestDate", reqDate);
            reqAuthenticator.put("RequestID", reqID);

            JsonObjectRequest jOR = new JsonObjectRequest(Request.Method.POST, getString(R.string.sign_in_url), reqAuthenticator, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject resAuthentication) {
                    Log.d("Resposta", resAuthentication.toString());
                    try {

                        if (resAuthentication.get("Message").equals("")) {
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            /*i.putExtra("User", etUsername.getText().toString());
                            i.putExtra("Pass", etPassword.getText().toString());*/
                            startActivity(i);
                            finish();
                            Toast.makeText(Login_Activity.this, R.string.welcome, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Login_Activity.this, R.string.error_login, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Erro", error.toString());
                }
            });
            rq.add(jOR);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}