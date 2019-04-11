package pt.gsdt.stationpurchaseapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

public class Login_Activity extends AppCompatActivity {

    private static final String PREFS_NAME = "preferences";
    private static final String PREF_UNAME = "Username";
    private static final String PREF_PASSWORD = "Password";

    private final String DefaultUnameValue = "";
    private String UnameValue;

    private final String DefaultPasswordValue = "";
    private String PasswordValue;

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

                //sendMessage();
                signInUser();

            }

        });

    }

    @Override
    public void onPause() {
        super.onPause();
        savePreferences();

    }

    @Override
    public void onResume() {
        super.onResume();
        loadPreferences();
    }

    private void savePreferences() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        // Edit and commit
        UnameValue = etUsername.getText().toString();
        PasswordValue = etPassword.getText().toString();
        System.out.println("onPause save name: " + UnameValue);
        System.out.println("onPause save password: " + PasswordValue);
        editor.putString(PREF_UNAME, UnameValue);
        editor.putString(PREF_PASSWORD, PasswordValue);
        editor.commit();
    }

    private void loadPreferences() {

        SharedPreferences settings = getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        // Get value
        UnameValue = settings.getString(PREF_UNAME, DefaultUnameValue);
        PasswordValue = settings.getString(PREF_PASSWORD, DefaultPasswordValue);
        etUsername.setText(UnameValue);
        etPassword.setText(PasswordValue);
        System.out.println("onResume load name: " + UnameValue);
        System.out.println("onResume load password: " + PasswordValue);
    }

    /*private void sendMessage() {

        String user_text = etUsername.getText().toString();
        String pass_text = etPassword.getText().toString();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.putExtra("User", user_text);
        i.putExtra("Pass", pass_text);
        startActivity(i);

    }*/

    private void signInUser() {

        String user = etUsername.getText().toString();
        String pass = etPassword.getText().toString();

        CookieManager cookieManager = new CookieManager(new PersistentCookieStore(mContext), CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        CookieHandler.setDefault(cookieManager);

        RequestQueue rq;
        Cache cache = new DiskBasedCache(getCacheDir(), 1024 * 1024);
        Network nw = new BasicNetwork(new HurlStack());
        rq = new RequestQueue(cache, nw);
        rq.start();

        String ApplicationID = "b6948414a6f42be3075eee653436b668";
        JSONObject Credentials = new JSONObject();
        String RequestDate = "2019-04-10T15:14:00";
        String RequestID = "1";
        JSONObject RequestAuthentication = new JSONObject();

        try {

            RequestAuthentication.put("ApplicationID", ApplicationID);
            Credentials.put("User", user);
            Credentials.put("Password", pass);
            RequestAuthentication.put("Credentials", Credentials);
            RequestAuthentication.put("RequestDate", RequestDate);
            RequestAuthentication.put("RequestID", RequestID);

            JsonObjectRequest jOR = new JsonObjectRequest(Request.Method.POST, getString(R.string.sign_in_url), RequestAuthentication, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject ResponseAuthentication) {
                    Log.d("Resposta", ResponseAuthentication.toString());

                    try {

                        if (!ResponseAuthentication.get("AuthorizationToken").equals("")) {

                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
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