package com.ops.dev.simple.services.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.ops.dev.simple.services.adapters.SharedPreferencesAdapter;
import com.ops.dev.simple.services.adapters.ToastAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class SignUp extends AppCompatActivity {


    //Vars
	String __message, __id, __alias, __password, __idDevice, __token;
	String _name, _alias, _phone, _email, _password;
	TextInputLayout name, alias, phone, email, password;
	Button signUp;
	LinearLayout signIn;
	Context context;
	RequestQueue queue;

	ProgressDialog progressDialog;
	ToastAdapter toastAdapter;
	SharedPreferencesAdapter sharedPreferencesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		View layout = findViewById(android.R.id.content);
		context = SignUp.this;

		name = findViewById(R.id.name);
		alias = findViewById(R.id.alias);
		phone = findViewById(R.id.phone);
		email = findViewById(R.id.email);
		password = findViewById(R.id.password);
		signUp = findViewById(R.id.signUp);
		signIn = findViewById(R.id.signIn);

		signUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				signUp();
			}
		});
		signIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, SignIn.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		});
		toastAdapter = new ToastAdapter(context);
		sharedPreferencesAdapter = new SharedPreferencesAdapter(context);
		queue = Volley.newRequestQueue(context);

		OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
			@Override
			public void idsAvailable(String userId, String registrationId) {
				if (registrationId != null) {
					__idDevice = userId;
				}
			}
		});
    }

    private void signUp() {
		String url = Network.SignUp;
		_name = String.valueOf(Objects.requireNonNull(name.getEditText()).getText());
		_alias = String.valueOf(Objects.requireNonNull(alias.getEditText()).getText());
		_phone = String.valueOf(Objects.requireNonNull(phone.getEditText()).getText());
		_email = String.valueOf(Objects.requireNonNull(email.getEditText()).getText());
		_password = String.valueOf(Objects.requireNonNull(password.getEditText()).getText());

		if (_name.length() == 0 || _alias.length() == 0 || _phone.length() == 0 || _email.length() == 0 || _password.length() == 0) {
			toastAdapter.makeToast(R.drawable.__warning, "Todos los campos son requeridos");
		} else {
			JSONObject jsonParams = new JSONObject();
			try {
				jsonParams.put("name", _name);
				jsonParams.put("alias", _alias);
				jsonParams.put("phone", _phone);
				jsonParams.put("email", _email);
				jsonParams.put("password", _password);
				jsonParams.put("idDevice", __idDevice);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage(getString(R.string.loading));
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
			JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						__message = response.getString("message");
						if (__message.equals("Ok")) {JSONObject jsonObject = response.getJSONObject("user");
							__id = jsonObject.getString("_id");
							__alias = jsonObject.getString("alias");
							__token = jsonObject.getString("token");

							sharedPreferencesAdapter.deletePreferences();
							sharedPreferencesAdapter.savePreferences(__id, __alias, __password, __id, __token, false);
							progressDialog.dismiss();
							toastAdapter.makeToast(R.drawable.__ok, __alias + " registrado correctamente");

							Handler h = new Handler();
							h.postDelayed(new Runnable() {
								@Override
								public void run() {
									finish();
									Intent intent = new Intent(context, SignIn.class);
									startActivity(intent);
								}
							}, 3000);
						} else {
							progressDialog.dismiss();
							toastAdapter.makeToast(R.drawable.__error, "!Vaya! Al parecer tenemos una cuenta registrada con estos datos");
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