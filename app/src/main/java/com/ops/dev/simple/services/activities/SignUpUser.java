package com.ops.dev.simple.services.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.ops.dev.simple.services.adapters.IntentAdapter;
import com.ops.dev.simple.services.adapters.ToastAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class SignUpUser extends AppCompatActivity {

    //Vars
	String __message, __deviceId;
	String _name, _alias, _phone, _email, _pass;
	TextInputLayout name, alias, phone, email, pass;
	CheckBox accept;
	TextView terms;
	TextView signIn;
	Button signUp;
	Context context;
	RequestQueue queue;

	AlertDialog.Builder builder;
	AlertDialog alertDialog;
	IntentAdapter intentAdapter;
	ToastAdapter toastAdapter;

	static int lTerms = R.layout.__modal_terms;
	String _header, _terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_user);
		View layout = findViewById(android.R.id.content);
		context = SignUpUser.this;
		intentAdapter = new IntentAdapter(this, context);
		toastAdapter = new ToastAdapter(context);
		queue = Volley.newRequestQueue(context);
		getTerms();

		name = findViewById(R.id.name);
		alias = findViewById(R.id.alias);
		phone = findViewById(R.id.phone);
		email = findViewById(R.id.email);
		pass = findViewById(R.id.pass);

		// Controls
		accept = findViewById(R.id.accept);
		terms = findViewById(R.id.terms);
		signUp = findViewById(R.id.signUp);
		signIn = findViewById(R.id.signIn);

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
				intentAdapter.goActivityDefault(SignIn.class);
			}
		});

		OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
			@Override
			public void idsAvailable(String signalId, String registrationId) {
				if (registrationId != null) {
					__deviceId = signalId;
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
		_pass = String.valueOf(Objects.requireNonNull(pass.getEditText()).getText());

		if (_name.length() == 0 || _alias.length() == 0 || _phone.length() == 0 || _email.length() == 0 || _pass.length() == 0) {
			toastAdapter.makeToast(R.drawable.__warning, "Todos los campos son requeridos");
		} else {
			JSONObject jsonParams = new JSONObject();
			try {
				jsonParams.put("name", _name);
				jsonParams.put("alias", _alias);
				jsonParams.put("phone", _phone);
				jsonParams.put("email", _email);
				jsonParams.put("pass", _pass);
				jsonParams.put("deviceId", __deviceId);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			builder = new AlertDialog.Builder(context);
			builder.setTitle("Cargando...");
			builder.setMessage("Estamos creando tu cuenta");
			alertDialog.setCancelable(false);
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.show();

			JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						__message = response.getString("message");
						if ("Ok".equals(__message)) {
							JSONObject jsonObject = response.getJSONObject("user");

							alertDialog.dismiss();
							toastAdapter.makeToast(R.drawable.__ok, jsonObject.getString("alias") + " registrado correctamente");
							intentAdapter.goActivityDefault(SignIn.class);
						} else {
							alertDialog.dismiss();
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