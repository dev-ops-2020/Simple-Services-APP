package com.ops.dev.simple.services.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.ops.dev.simple.services.Network;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.adapters.GlideAdapter;
import com.ops.dev.simple.services.adapters.IntentAdapter;
import com.ops.dev.simple.services.adapters.PreferencesAdapter;
import com.ops.dev.simple.services.adapters.ToastAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.Random;

public class SignIn extends AppCompatActivity {

    //Vars
	String __message;
	String _email, _pass;
	TextInputLayout email, pass;
	Button signIn;
	TextView signUp;
	RelativeLayout container;
	LinearLayout main;
	RadioButton isUser, isBusiness;
	Context context;
	RequestQueue queue;

	ProgressDialog progressDialog;
	IntentAdapter intentAdapter;
	ToastAdapter toastAdapter;
	GlideAdapter glideAdapter;
	PreferencesAdapter preferencesAdapter;

	Location userLocation;
	FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		View layout = findViewById(android.R.id.content);
		context = SignIn.this;
		intentAdapter = new IntentAdapter(this, context);
		toastAdapter = new ToastAdapter(context);
		glideAdapter = new GlideAdapter(context);
		preferencesAdapter = new PreferencesAdapter(context);
		queue = Volley.newRequestQueue(context);

		Glide.with(context).load(R.drawable.______icon_gif).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)).transform(new RoundedCorners(R.dimen.med_margin)).into(new DrawableImageViewTarget((ImageView) findViewById(R.id.gif)));

		fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
		getLocation();

		container = findViewById(R.id.container);
		email = findViewById(R.id.email);
		pass = findViewById(R.id.pass);
		main = findViewById(R.id.main);
		signIn = findViewById(R.id.signIn);
		signUp = findViewById(R.id.signUp);
		isUser = findViewById(R.id.isUser);
		isBusiness = findViewById(R.id.isBusiness);

		if (preferencesAdapter.getIsUser())
			isUser.setChecked(true);
		else if (preferencesAdapter.getIsBusiness())
			isBusiness.setChecked(true);

		signIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isUser.isChecked())
					signIn(Network.SignInUser, true);
				else if (isBusiness.isChecked())
					signIn(Network.SignInBusiness, false);
				else
					toastAdapter.makeToast(R.drawable.__info, "Selecciona si eres usuario o negocio 😄");
			}
		});
		signUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isUser.isChecked())
					intentAdapter.goActivityDefault(SignUpUser.class);
				else if (isBusiness.isChecked())
					intentAdapter.goActivityDefault(SignUpBusiness.class);
				else
				toastAdapter.makeToast(R.drawable.__info, "Selecciona si eres usuario o negocio 😄");
			}
		});

		Objects.requireNonNull(email.getEditText()).setText(preferencesAdapter.getEmail());
		Objects.requireNonNull(pass.getEditText()).setText(preferencesAdapter.getPass());

		// Auto LogIn
		if (preferencesAdapter.getIsLoggedIn()) {
			int[] backgrounds = {R.drawable.gradient_red, R.drawable.gradient_green, R.drawable.gradient_blue};
			int randomBackground = new Random().nextInt(backgrounds.length);
			container.setBackgroundResource(backgrounds[randomBackground]);
			findViewById(R.id.tittle).setVisibility(View.VISIBLE);
			findViewById(R.id.tittle).setAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.animation));
			if (preferencesAdapter.getIsUser())
				intentAdapter.goActivityAnimated(MainMenuUser.class, Network.DURATION_LONG);
			else if (preferencesAdapter.getIsBusiness())
				intentAdapter.goActivityAnimated(MainMenuBusiness.class, Network.DURATION_LONG);
		} else {
			main.setVisibility(View.VISIBLE);
		}
	}

	private void signIn(String url, final boolean isUser) {
    	_email = String.valueOf(Objects.requireNonNull(email.getEditText()).getText());
		_pass = String.valueOf(Objects.requireNonNull(pass.getEditText()).getText());

		if (_email.length() == 0 || _pass.length() == 0) {
			toastAdapter.makeToast(R.drawable.__warning, "Correo y contraseña requeridos");
		} else {
			JSONObject jsonParams = new JSONObject();
			try {
				jsonParams.put("email", _email);
				jsonParams.put("pass", _pass);
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
								preferencesAdapter.setIsLoggedIn();
								JSONObject jsonObject = response.getJSONObject("object");
								preferencesAdapter.setId(jsonObject.getString("_id"));
								preferencesAdapter.setEmail(_email);
								preferencesAdapter.setPass(_pass);
								preferencesAdapter.setToken(jsonObject.getString("token"));
								preferencesAdapter.setDeviceId(jsonObject.getString("deviceId"));
								if (isUser) {
									preferencesAdapter.setIsUser();
									preferencesAdapter.setAlias(jsonObject.getString("alias"));
									toastAdapter.makeToast(R.drawable.__ok, "Bienvenido " + preferencesAdapter.getAlias() + " 😎");
									intentAdapter.goActivityAnimated(MainMenuUser.class, Network.DURATION_NORMAL);
								} else  {
									preferencesAdapter.setIsBusiness();
									preferencesAdapter.setAlias(jsonObject.getString("name"));
									preferencesAdapter.setMembershipId(jsonObject.getString("membershipId"));
									toastAdapter.makeToast(R.drawable.__ok, "Bienvenido " + preferencesAdapter.getAlias() + " 😎");
									intentAdapter.goActivityAnimated(MainMenuBusiness.class, Network.DURATION_NORMAL);
								}
								progressDialog.dismiss();
								break;
							case "Passwords do not match":
								progressDialog.dismiss();
								toastAdapter.makeToast(R.drawable.__warning, "Revisa los datos de ingreso 😉");
								break;
							case "Object not found":
								progressDialog.dismiss();
								toastAdapter.makeToast(R.drawable.__error, "!Vaya! No encontramos ninguna cuenta registrada con estos datos ☹");
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

	private void getLocation() {
		Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
		locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
			@Override
			public void onSuccess(Location location) {
				userLocation = location;
				preferencesAdapter.setLat((float) userLocation.getLatitude());
				preferencesAdapter.setLng((float) userLocation.getLongitude());
			}
		});
	}
}