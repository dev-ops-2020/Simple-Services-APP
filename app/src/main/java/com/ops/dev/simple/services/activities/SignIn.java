package com.ops.dev.simple.services.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import java.util.Random;

public class SignIn extends AppCompatActivity {

    //Vars
	String __message, __id, __alias, __password, __idDevice, __token;
	String  _alias, _password;
	TextInputLayout alias, password;
	TextView tittle;
	Button signIn;
	RelativeLayout container;
	LinearLayout main, signUp;
	Context context;
	RequestQueue queue;

	ProgressDialog progressDialog;
	ToastAdapter toastAdapter;
	SharedPreferencesAdapter sharedPreferencesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		View layout = findViewById(android.R.id.content);
		context = SignIn.this;
		toastAdapter = new ToastAdapter(context);
		sharedPreferencesAdapter = new SharedPreferencesAdapter(context);
		queue = Volley.newRequestQueue(context);

		container = findViewById(R.id.container);
		tittle = findViewById(R.id.tittle);
		alias = findViewById(R.id.alias);
		password = findViewById(R.id.password);
		main = findViewById(R.id.main);
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

		OneSignal.startInit(context).init();
		OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
			@Override
			public void idsAvailable(String signalId, String registrationId) {
				if (registrationId != null) {
					__idDevice = signalId;
				}
			}
		});

		Objects.requireNonNull(alias.getEditText()).setText(sharedPreferencesAdapter.getAlias());
		Objects.requireNonNull(password.getEditText()).setText(sharedPreferencesAdapter.getPassword());

		if (sharedPreferencesAdapter.getIsLoggedIn()) {
			int[] backgrounds = {R.drawable.gradient_blue, R.drawable.gradient_red, R.drawable.gradient_green};
			int randomBackground = new Random().nextInt(backgrounds.length);
			container.setBackgroundResource(backgrounds[randomBackground]);
			tittle.setVisibility(View.VISIBLE);
			tittle.setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation));
			findViewById(R.id.image).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation));
			goMain();
		} else {
			main.setVisibility(View.VISIBLE);
		}
    }

    public boolean isValidEmail(String email) {
		boolean isValid = false;
		if (email.contains("@") && email.contains(".com"))
			isValid = true;
		return isValid;
	}

	private void signIn() {
		String url = Network.SignIn;
		_alias = String.valueOf(Objects.requireNonNull(alias.getEditText()).getText());
		_password = String.valueOf(Objects.requireNonNull(password.getEditText()).getText());

		if (_alias.length() == 0 || _password.length() == 0) {
			toastAdapter.makeToast(R.drawable.__warning, "Alias y contraseñas requeridos");
		} else {
			JSONObject jsonParams = new JSONObject();
			try {
				jsonParams.put("alias", _alias);
				jsonParams.put("password", _password);
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
						switch (__message) {
							case "Ok":
								JSONObject jsonObject = response.getJSONObject("user");
								__id = jsonObject.getString("_id");
								__alias = jsonObject.getString("alias");
								__token = jsonObject.getString("token");

								sharedPreferencesAdapter.setUserId(__id);
								sharedPreferencesAdapter.setAlias(_alias);
								sharedPreferencesAdapter.setPassword(_password);
								sharedPreferencesAdapter.setToken(__token);
								sharedPreferencesAdapter.setIsLoggedIn(true);

								progressDialog.dismiss();
								toastAdapter.makeToast(R.drawable.__ok, R.string.welcome + " " + __alias);
								goMain();
								break;
							case "User not found":
								progressDialog.dismiss();
								toastAdapter.makeToast(R.drawable.__error, "!Vaya! No encontramos ninguna cuenta registrada con estos datos");
								break;
							default:
								progressDialog.dismiss();
								toastAdapter.makeToast(R.drawable.__warning, "Revisa los datos de ingreso");
								break;
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

	private void goMain() {
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			@Override
			public void run() {
				finish();
				Intent intent = new Intent(context, MainMenu.class);
				startActivity(intent);
				overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
			}
		}, 5000);
	}

	@Override
	public void onBackPressed() {

	}
}