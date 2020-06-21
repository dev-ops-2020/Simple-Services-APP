package com.ops.dev.simple.services.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.onesignal.OneSignal;
import com.ops.dev.simple.services.Network;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.adapters.PreferencesAdapter;
import com.ops.dev.simple.services.adapters.ToastAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class SignIn extends AppCompatActivity {

    //Vars
	String __message, __id, __alias, __password, __idDevice, __token;
	String  _alias, _password;
	TextInputLayout alias, password;
	Button signIn;
	LinearLayout signUp;
	Context context;
	RequestQueue queue;

	ProgressDialog alertDialog;
	ToastAdapter toastAdapter;
	PreferencesAdapter preferencesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		View layout = findViewById(android.R.id.content);
		context = SignIn.this;

		alias = findViewById(R.id.alias);
		password = findViewById(R.id.password);
		signIn = findViewById(R.id.signIn);
		signUp = findViewById(R.id.signUp);

		signIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				signIn();
			}
		});
		signUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, SignUp.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		toastAdapter = new ToastAdapter(context);
		preferencesAdapter = new PreferencesAdapter(context);
		Objects.requireNonNull(alias.getEditText()).setText(preferencesAdapter.getAlias());
		Objects.requireNonNull(password.getEditText()).setText(preferencesAdapter.getPassword());
		queue = Volley.newRequestQueue(context);

		OneSignal.startInit(context).init();
		OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
			@Override
			public void idsAvailable(String userId, String registrationId) {
				if (registrationId != null) {
					__idDevice = userId;
				}
			}
		});
    }

	private void signIn() {
		String url = Network.SignIn;
		_alias = String.valueOf(Objects.requireNonNull(alias.getEditText()).getText());
		_password = String.valueOf(Objects.requireNonNull(password.getEditText()).getText());

		if (_alias.length() == 0 || _password.length() == 0) {
			toastAdapter.makeToast("Alias y contraseñas requeridos", R.drawable.__warning);
		} else {
			JSONObject jsonParams = new JSONObject();
			try {
				jsonParams.put("alias", _alias);
				jsonParams.put("password", _password);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			alertDialog = new ProgressDialog(context);
			alertDialog.setMessage(getString(R.string.loading));
			alertDialog.setIndeterminate(false);
			alertDialog.setCancelable(false);
			alertDialog.show();
			JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						__message = response.getString("message");
						if (__message.equals("Ok")) {JSONObject jsonObject = response.getJSONObject("user");
							__id = jsonObject.getString("_id");
							__alias = jsonObject.getString("alias");
							__token = jsonObject.getString("token");

							preferencesAdapter.deletePreferences();
							preferencesAdapter.savePreferences(__id, __alias, _password, "", __token, true);
							alertDialog.dismiss();
							toastAdapter.makeToast("Bienvenido " + __alias, R.drawable._check);

							Handler h = new Handler();
							h.postDelayed(new Runnable() {
								@Override
								public void run() {
									finish();
									Intent intent = new Intent(context, MainMenu.class);
									startActivity(intent);
								}
							}, 3000);
						} else if (__message.equals("User not found")) {
							alertDialog.dismiss();
							toastAdapter.makeToast("!Vaya! No encontramos ninguna cuenta registrada con estos datos", R.drawable.__error);
						} else {
							alertDialog.dismiss();
							toastAdapter.makeToast("Revisa los datos de ingreso", R.drawable.__warning);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {

				}
			});
			queue.add(request);
		}
	}

	@Override
	public void onBackPressed() {

	}
}