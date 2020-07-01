package com.ops.dev.simple.services.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
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

public class SignUp extends AppCompatActivity {

    //Vars
	String __message, __id, __alias, __password, __idDevice, __token;
	String _name, _alias, _phone, _email, _password;
	TextInputLayout name, alias, phone, email, password;
	CheckBox accept;
	TextView terms;
	Button signUp;
	LinearLayout signIn;
	Context context;
	RequestQueue queue;

	ProgressDialog progressDialog;
	ToastAdapter toastAdapter;
	SharedPreferencesAdapter sharedPreferencesAdapter;

	AlertDialog.Builder builder;
	AlertDialog alertDialog;

	static int lTerms = R.layout.__modal_terms;
	String _header, _terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		View layout = findViewById(android.R.id.content);
		context = SignUp.this;
		toastAdapter = new ToastAdapter(context);
		sharedPreferencesAdapter = new SharedPreferencesAdapter(context);
		queue = Volley.newRequestQueue(context);

		name = findViewById(R.id.name);
		alias = findViewById(R.id.alias);
		phone = findViewById(R.id.phone);
		email = findViewById(R.id.email);
		password = findViewById(R.id.password);
		accept = findViewById(R.id.accept);
		terms = findViewById(R.id.terms);
		signUp = findViewById(R.id.signUp);
		signIn = findViewById(R.id.signIn);

		getTerms();
		signUp.setEnabled(false);
		signUp.setBackgroundResource(R.drawable.border_buttons_disable);

		accept.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (accept.isChecked()) {
					signUp.setEnabled(true);
					signUp.setBackgroundResource(R.drawable.border_buttons_enable);
				} else {
					signUp.setEnabled(false);
					signUp.setBackgroundResource(R.drawable.border_buttons_disable);
				}
			}
		});

		terms.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showModal(lTerms);
			}
		});

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

		OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
			@Override
			public void idsAvailable(String signalId, String registrationId) {
				if (registrationId != null) {
					__idDevice = signalId;
				}
			}
		});
    }

	private void showModal(final int layout) {
		final View layoutView = getLayoutInflater().inflate(layout, null);
		builder = new AlertDialog.Builder(context);
		builder.setView(layoutView);

		if (layout == lTerms) {
			//Vars
			final TextView header = layoutView.findViewById(R.id.header);
			final TextView terms = layoutView.findViewById(R.id.terms_conditions);

			header.setText(_header);
			terms.setText(_terms);
		}
		alertDialog = builder.create();
		alertDialog.setCancelable(true);
		alertDialog.show();
	}

	public boolean isValidEmail(String email) {
		boolean isValid = false;
		if (email.contains("@") && email.contains(".com"))
			isValid = true;
		return isValid;
	}

	private void getTerms() {
		String url = Network.Terms;
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					_header = response.getString("header");
					_terms = response.getString("message");
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
						switch (__message) {
							case "Ok":
								JSONObject jsonObject = response.getJSONObject("user");
								__id = jsonObject.getString("_id");
								__alias = jsonObject.getString("alias");
								__token = jsonObject.getString("token");

								sharedPreferencesAdapter.setUserId(__id);
								sharedPreferencesAdapter.setAlias(__alias);
								sharedPreferencesAdapter.setPassword(__password);
								sharedPreferencesAdapter.setIdDevice(__idDevice);
								sharedPreferencesAdapter.setToken(__token);
								sharedPreferencesAdapter.setIsFirstTime(false);
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
								break;
							default:
								progressDialog.dismiss();
								toastAdapter.makeToast(R.drawable.__error, "!Vaya! Al parecer tenemos una cuenta registrada con estos datos");
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

	@Override
	public void onBackPressed() {

	}
}