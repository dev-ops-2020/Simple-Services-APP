package com.ops.dev.simple.services.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
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
import com.ops.dev.simple.services.adapters.PreferencesAdapter;
import com.ops.dev.simple.services.adapters.ToastAdapter;
import com.ops.dev.simple.services.models.BusinessesModel;
import com.ops.dev.simple.services.models.UsersModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class EditProfile extends AppCompatActivity {

    //Vars
	String __message, __id, __alias;
	String _name, _alias, _phone, _email, _password;
	TextView alias;
	TextInputLayout name, phone, email, password;
	Button update;
	Context context;
	RequestQueue queue;

	ProgressDialog alertDialog;
	ToastAdapter toastAdapter;

	UsersModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		View layout = findViewById(android.R.id.content);
		context = EditProfile.this;

		name = findViewById(R.id.name);
		alias = findViewById(R.id.alias);
		phone = findViewById(R.id.phone);
		email = findViewById(R.id.email);
		update = findViewById(R.id.update);

		user = (UsersModel) getIntent().getSerializableExtra("user");
		assert user != null;
		alias.setText(String.format("Editar Perfil '%s'", user.getAlias()));
		Objects.requireNonNull(name.getEditText()).setText(user.getName());
		Objects.requireNonNull(phone.getEditText()).setText(user.getPhone());
		Objects.requireNonNull(email.getEditText()).setText(user.getEmail());

		update.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//update();
				toastAdapter.makeToast("Al tocar este botón se actualizarán los datos 😎", R.drawable._fav);
			}
		});
		toastAdapter = new ToastAdapter(context);
		queue = Volley.newRequestQueue(context);
    }

    private void update() {
		String url = Network.Users+user.getId();
		_name = String.valueOf(Objects.requireNonNull(name.getEditText()).getText());
		_phone = String.valueOf(Objects.requireNonNull(phone.getEditText()).getText());
		_email = String.valueOf(Objects.requireNonNull(email.getEditText()).getText());
		//_password = String.valueOf(Objects.requireNonNull(password.getEditText()).getText());

		if (_name.length() == 0 || _phone.length() == 0 || _email.length() == 0) {
			toastAdapter.makeToast("Todos los campos son requeridos", R.drawable.__warning);
		} else {
			JSONObject jsonParams = new JSONObject();
			try {
				jsonParams.put("name", _name);
				jsonParams.put("phone", _phone);
				jsonParams.put("email", _email);
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

							alertDialog.dismiss();
							toastAdapter.makeToast(__alias + " actualizado correctamente", R.drawable.__ok);

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
							alertDialog.dismiss();
							toastAdapter.makeToast("!Vaya! Al parecer tenemos una cuenta registrada con estos datos", R.drawable.__error);
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
}