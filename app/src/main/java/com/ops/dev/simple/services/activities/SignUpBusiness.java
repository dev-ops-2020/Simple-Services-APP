package com.ops.dev.simple.services.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.onesignal.OneSignal;
import com.ops.dev.simple.services.Network;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.adapters.CategoriesListAdapter;
import com.ops.dev.simple.services.adapters.GlideAdapter;
import com.ops.dev.simple.services.adapters.IntentAdapter;
import com.ops.dev.simple.services.adapters.PreferencesAdapter;
import com.ops.dev.simple.services.adapters.ToastAdapter;
import com.ops.dev.simple.services.models.CategoriesModelListIcon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.ops.dev.simple.services.Network.REQUEST_CODE_IMAGE;
import static com.ops.dev.simple.services.Network.REQUEST_CODE_PICTURE_1;
import static com.ops.dev.simple.services.Network.REQUEST_CODE_PICTURE_2;
import static com.ops.dev.simple.services.Network.REQUEST_CODE_PICTURE_3;

public class SignUpBusiness extends AppCompatActivity implements OnMapReadyCallback {

    //Vars
	String __message, __deviceId;
	StorageReference storageReference;
	String _url, _url1, _url2, _url3;

	String type;

	String _owner, _email, _pass;
	TextInputLayout owner, email, pass;

	boolean logo = true;
	boolean delivery = false;
	Uri path;
	ImageView businessLogo;
	String _businessName, _businessDesc, _businessSlogan, _businessPhone, _businessAddress;
	TextInputLayout businessName, businessDesc, businessSlogan, businessPhone, businessAddress;

	GoogleMap map;
	Geocoder geocoder;
	String location, locality = "", country = "";
	Location mLocation;
	FusedLocationProviderClient fusedLocationProviderClient;
	LatLng businessLocation;
	Double lat, lng;
	String _lat, _lng;

	Uri path1, path2, path3;
	ImageView picture1, picture2, picture3;
	ArrayList<String> selectedPictures;

	String _day1, _day2, _day3, _day4, _day5, _day6, _day7;
	TextInputLayout day1, day2, day3, day4, day5, day6, day7;

	String _fb, _ig, _wa;
	TextInputLayout fb, ig, wa;

	RecyclerView rvCategories;
	List<CategoriesModelListIcon> listCategories;
	CategoriesListAdapter categoriesListAdapter;
	ArrayList<String> selectedCategories;

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
	GlideAdapter glideAdapter;
	PreferencesAdapter preferencesAdapter;

	static int lTerms = R.layout.__modal_terms;
	String _header, _terms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up_business);
		final View layout = findViewById(android.R.id.content);
		context = SignUpBusiness.this;
		intentAdapter = new IntentAdapter(this, context);
		toastAdapter = new ToastAdapter(context);
		glideAdapter = new GlideAdapter(context);
		preferencesAdapter = new PreferencesAdapter(context);
		queue = Volley.newRequestQueue(context);
		getTerms();

		owner = findViewById(R.id.owner);
		email = findViewById(R.id.email);
		pass = findViewById(R.id.pass);

		builder = new AlertDialog.Builder(context);
		builder.setTitle("!Empezemos con el registro de tu negocio!");
		builder.setMessage("Primero que nada necesitamos saber si tu negocio ofrece productos o servicios... \n\n¡Psss! De esto dependerá la interfaz que verán los clientes 🤗");
		builder.setNegativeButton("Ofrecemos servicios", null);
		builder.setPositiveButton("Ofrecemos productos", null);
		alertDialog = builder.create();
		alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {
				Button btnCancel = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
				Button btnOk = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
				btnCancel.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						type = "Services";
						findViewById(R.id.lay1).setVisibility(View.VISIBLE);
						toastAdapter.makeToast(R.drawable._fav, "Continuemos con tus datos personales");
						alertDialog.dismiss();
					}
				});
				btnOk.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						type = "Products";
						findViewById(R.id.lay1).setVisibility(View.VISIBLE);
						toastAdapter.makeToast(R.drawable._fav, "Continuemos con tus datos personales");
						alertDialog.dismiss();
					}
				});
			}
		});
		alertDialog.setCancelable(false);
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();

		findViewById(R.id.next1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_owner = String.valueOf(Objects.requireNonNull(owner.getEditText()).getText());
				_email = String.valueOf(Objects.requireNonNull(email.getEditText()).getText());
				_pass = String.valueOf(Objects.requireNonNull(pass.getEditText()).getText());

				if (_owner.length() == 0 || _email.length() == 0 || _pass.length() == 0) {
					toastAdapter.makeToast(R.drawable.__warning, "Rellena los campos con tus datos personales");
				} else {
					findViewById(R.id.lay1).setVisibility(View.GONE);
					findViewById(R.id.lay2).setVisibility(View.VISIBLE);
					builder = new AlertDialog.Builder(context);
					builder.setTitle("El logo es un distintivo muy importante para tu negocio");
					builder.setMessage("Si aún no tienes un logo, puedes contactar con nuestro diseñador.\n\nSi decides dejar esto en sus manos, siéntete libre, tu logo se actualizará dentro de la aplicación en tanto lleguen a un acuerdo con él y tu logo esté listo 😊");
					builder.setNegativeButton("Seleccionar logo de mi galería", null);
					builder.setPositiveButton("Contactar al diseñador", null);
					alertDialog = builder.create();
					alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
						@Override
						public void onShow(DialogInterface dialogInterface) {
							Button btnCancel = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
							Button btnOk = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
							btnCancel.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									selectImage(REQUEST_CODE_IMAGE);
									alertDialog.dismiss();
								}
							});
							btnOk.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									goDesigner();
									logo = false;
									glideAdapter.setImageCircle(businessLogo, Network.NO_LOGO);
									alertDialog.dismiss();
								}
							});
						}
					});
					alertDialog.setCancelable(false);
					alertDialog.setCanceledOnTouchOutside(false);
					alertDialog.show();
				}
			}
		});

		path = null;
		storageReference = FirebaseStorage.getInstance().getReference();
		businessLogo = findViewById(R.id.businessLogo);
		businessName = findViewById(R.id.businessName);
		businessDesc = findViewById(R.id.businessDesc);
		businessSlogan = findViewById(R.id.businessSlogan);
		businessPhone = findViewById(R.id.businessPhone);
		businessAddress = findViewById(R.id.businessAddress);

		businessLogo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectImage(Network.REQUEST_CODE_IMAGE);
			}
		});

		findViewById(R.id.next2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_businessName = String.valueOf(Objects.requireNonNull(businessName.getEditText()).getText());
				_businessDesc = String.valueOf(Objects.requireNonNull(businessDesc.getEditText()).getText());
				_businessSlogan = String.valueOf(Objects.requireNonNull(businessSlogan.getEditText()).getText());
				_businessPhone = String.valueOf(Objects.requireNonNull(businessPhone.getEditText()).getText());
				_businessAddress = String.valueOf(Objects.requireNonNull(businessAddress.getEditText()).getText());

				if (_businessName.length() == 0 || _businessDesc.length() == 0 || _businessSlogan.length() == 0 || _businessPhone.length() == 0 || _businessAddress.length() == 0) {
					toastAdapter.makeToast(R.drawable.__warning, "Rellena los campos con los datos de tu negocio");
				} else {
					findViewById(R.id.lay2).setVisibility(View.GONE);
					findViewById(R.id.lay3).setVisibility(View.VISIBLE);
					toastAdapter.makeToast(R.drawable.__info, "Toca sobre el mapa para seleccionar la ubicación de tu negocio");
				}
			}
		});

		geocoder = new Geocoder(context);
		fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
		getLocation();

		findViewById(R.id.next3).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					lat = businessLocation.latitude;
					lng = businessLocation.longitude;
				} catch (Exception ex) {
					toastAdapter.makeToast(R.drawable.__warning, "Hay un fallo con tu ubicación...");
					return;
				}

				_lat = String.valueOf(lat);
				_lng = String.valueOf(lng);

				if (_lat.length() == 0 || _lng.length() == 0) {
					toastAdapter.makeToast(R.drawable.__warning, "Selecciona tu ubicación en el mapa");
				} else {
					findViewById(R.id.lay3).setVisibility(View.GONE);
					findViewById(R.id.lay4).setVisibility(View.VISIBLE);
				}
			}
		});

		path1 = null;
		path2 = null;
		path3 = null;
		picture1 = findViewById(R.id.picture1);
		picture2 = findViewById(R.id.picture2);
		picture3 = findViewById(R.id.picture3);

		picture1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectImage(REQUEST_CODE_PICTURE_1);
			}
		});
		picture2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectImage(REQUEST_CODE_PICTURE_2);
			}
		});
		picture3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectImage(REQUEST_CODE_PICTURE_3);
			}
		});

		selectedPictures = new ArrayList<>();

		findViewById(R.id.next4).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (path1 == null || path2 == null || path3 == null) {
					toastAdapter.makeToast(R.drawable.__warning, "Selecciona las fotos del local");
				} else {
					findViewById(R.id.lay4).setVisibility(View.GONE);
					findViewById(R.id.lay5).setVisibility(View.VISIBLE);
				}
			}
		});

		day1 = findViewById(R.id.day1);
		day2 = findViewById(R.id.day2);
		day3 = findViewById(R.id.day3);
		day4 = findViewById(R.id.day4);
		day5 = findViewById(R.id.day5);
		day6 = findViewById(R.id.day6);
		day7 = findViewById(R.id.day7);

		findViewById(R.id.next5).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_day1 = String.valueOf(Objects.requireNonNull(day1.getEditText()).getText());
				_day2 = String.valueOf(Objects.requireNonNull(day2.getEditText()).getText());
				_day3 = String.valueOf(Objects.requireNonNull(day3.getEditText()).getText());
				_day4 = String.valueOf(Objects.requireNonNull(day4.getEditText()).getText());
				_day5 = String.valueOf(Objects.requireNonNull(day5.getEditText()).getText());
				_day6 = String.valueOf(Objects.requireNonNull(day6.getEditText()).getText());
				_day7 = String.valueOf(Objects.requireNonNull(day7.getEditText()).getText());

				if (_day1.length() == 0 || _day2.length() == 0 || _day3.length() == 0 || _day4.length() == 0 || _day5.length() == 0 || _day6.length() == 0 || _day7.length() == 0) {
					toastAdapter.makeToast(R.drawable.__warning, "Todos los horarios son requeridos");
				} else {
					findViewById(R.id.lay5).setVisibility(View.GONE);
					findViewById(R.id.lay6).setVisibility(View.VISIBLE);
				}
			}
		});

		fb = findViewById(R.id.fb);
		ig = findViewById(R.id.ig);
		wa = findViewById(R.id.wa);

		findViewById(R.id.next6).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				_fb = String.valueOf(Objects.requireNonNull(fb.getEditText()).getText());
				_ig = String.valueOf(Objects.requireNonNull(ig.getEditText()).getText());
				_wa = String.valueOf(Objects.requireNonNull(wa.getEditText()).getText());
				if (_fb.length() == 0 || _ig.length() == 0 || _wa.length() == 0) {
					builder = new AlertDialog.Builder(context);
					builder.setTitle("Estás dejando de lado una de las mejores funciones 😌");
					builder.setMessage("Si no vinculas tus cuentas, los usuarios no podrán contactarte 😬");
					builder.setNegativeButton("Dejar así", null);
					builder.setPositiveButton("Aprovecharé esta función", null);
					alertDialog = builder.create();
					alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
						@Override
						public void onShow(DialogInterface dialogInterface) {
							Button btnCancel = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
							Button btnOk = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
							btnCancel.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									findViewById(R.id.lay6).setVisibility(View.GONE);
									findViewById(R.id.lay7).setVisibility(View.VISIBLE);
									alertDialog.dismiss();
								}
							});
							btnOk.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									alertDialog.dismiss();
								}
							});
						}
					});
					alertDialog.setCancelable(false);
					alertDialog.setCanceledOnTouchOutside(false);
					alertDialog.show();
				} else {
					findViewById(R.id.lay6).setVisibility(View.GONE);
					findViewById(R.id.lay7).setVisibility(View.VISIBLE);
				}
			}
		});

		rvCategories = findViewById(R.id.rvCategories);
		listCategories = new ArrayList<>();
		getCategories();
		selectedCategories = new ArrayList<>();

		findViewById(R.id.next7).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				for (int i = 0; i < listCategories.size(); i++) {
					CategoriesModelListIcon category = listCategories.get(i);
					if (category.getChecked()) {
						selectedCategories.add(category.getId());
					}
				}
				if (selectedCategories.size() == 0) {
					toastAdapter.makeToast(R.drawable.__warning, "Selecciona al menos una categoría");
				} else {
					findViewById(R.id.lay7).setVisibility(View.GONE);
					builder = new AlertDialog.Builder(context);
					builder.setTitle("Ya casi terminamos 😁");
					builder.setMessage("Una última pregunta, ¿Como negocio tienes delivery? \n\n(Ya sea de los servicios o productos que ofrecezcas)");
					builder.setNegativeButton("NO", null);
					builder.setPositiveButton("Sí 🤩", null);
					alertDialog = builder.create();
					alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
						@Override
						public void onShow(DialogInterface dialogInterface) {
							Button btnCancel = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
							Button btnOk = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
							btnCancel.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									findViewById(R.id.lay8).setVisibility(View.VISIBLE);
									alertDialog.dismiss();
								}
							});
							btnOk.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View view) {
									findViewById(R.id.lay8).setVisibility(View.VISIBLE);
									delivery = true;
									alertDialog.dismiss();
								}
							});
						}
					});
					alertDialog.setCancelable(false);
					alertDialog.setCanceledOnTouchOutside(false);
					alertDialog.show();
				}
			}
		});

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
				uploadImages();
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

	@Override
	public void onBackPressed() {

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

	private void selectImage(int request_code) {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_image)), request_code);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && data != null && data.getData() != null) {
			switch (requestCode) {
				case REQUEST_CODE_IMAGE:
					path = data.getData();
					glideAdapter.setImageCircle(businessLogo, String.valueOf(path));
					break;
				case REQUEST_CODE_PICTURE_1:
					path1 = data.getData();
					glideAdapter.setImageDefault(picture1, String.valueOf(path1));
					break;
				case REQUEST_CODE_PICTURE_2:
					path2 = data.getData();
					glideAdapter.setImageDefault(picture2, String.valueOf(path2));
					break;
				case REQUEST_CODE_PICTURE_3:
					path3 = data.getData();
					glideAdapter.setImageDefault(picture3, String.valueOf(path3));
					break;
			}
		}
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

	private void getCategories() {
		String url = Network.ListCategories;
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					JSONArray jsonArray = response.getJSONArray("categories");
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						CategoriesModelListIcon category = new CategoriesModelListIcon();
						category.setId(jsonObject.getString("_id"));
						category.setName(jsonObject.getString("name"));
						int icon;
						if (jsonObject.has("icon")) {
							icon = context.getResources().getIdentifier(jsonObject.getString("icon"), "drawable", context.getPackageName());
						} else {
							icon = context.getResources().getIdentifier("_fav", "drawable", context.getPackageName());
						}
						category.setIcon(icon);
						category.setChecked(false);
						listCategories.add(category);
					}
					LinearLayoutManager layoutManager = new LinearLayoutManager(context);
					layoutManager.setOrientation(RecyclerView.VERTICAL);
					rvCategories.setLayoutManager(layoutManager);
					rvCategories.setHasFixedSize(true);
					categoriesListAdapter = new CategoriesListAdapter(context, listCategories);
					rvCategories.setAdapter(categoriesListAdapter);
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

	public void goDesigner() {
		try {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Network.DESIGNER_URL)));
		} catch (ActivityNotFoundException ex) {
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com")));
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case Network.REQUEST_CODE_LOCATION:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					getLocation();
				}
				break;
		}
	}

	private void getLocation() {
		Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
		locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
			@Override
			public void onSuccess(Location location) {
				if (location != null) {
					mLocation = location;
					SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.businessMap);
					supportMapFragment.getMapAsync(SignUpBusiness.this);
				}
			}
		});
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map = googleMap;
		LatLng latLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
		try {
			List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
			if (addressList != null && addressList.size() > 0) {
				locality = addressList.get(0).getAddressLine(0);
				country = addressList.get(0).getSubLocality();
				//country = addressList.get(0).getCountryName();
				location = locality + " " + country;
				businessLocation = latLng;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		map.addMarker(new MarkerOptions()
				.position(latLng)
				.title(location)
				.snippet("Toca sobre el mapa para seleccionar la ubicación de tu negocio")
				//.flat(true)
				.draggable(true));
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, Network.ZOOM_LEVEL));
		googleMap.getUiSettings().setZoomControlsEnabled(true);

		map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				try {
					List<Address> addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
					if (addressList != null && addressList.size() > 0) {
						locality = addressList.get(0).getAddressLine(0);
						country = addressList.get(0).getSubLocality();
						//country = addressList.get(0).getCountryName();
						location = locality + " " + country;
						businessLocation = latLng;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				map.clear();
				map.addMarker(new MarkerOptions()
						.position(latLng)
						.title(location)
						.snippet("Si la ubicación es la correcta toca SIGUIENTE")
						//.flat(true)
						.draggable(true));
				map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
			}
		});
	}

	private void uploadImages() {
    	findViewById(R.id.signUp).setEnabled(false);
    	findViewById(R.id.signUp).setBackgroundResource(R.drawable.border_buttons_disable);
		builder = new AlertDialog.Builder(context);
		builder.setTitle("Espera un momento");
		builder.setMessage("Estamos validando los datos...");
		alertDialog = builder.create();
		alertDialog.setCancelable(false);
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
		if (logo &&  path != null) {
			UploadTask uploadTask = (UploadTask) storageReference.child("images/businesses/___logo_" + _email + ".png").putFile(path)
					.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
						@Override
						public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
							taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
								@Override
								public void onComplete(@NonNull Task<Uri> task) {
									_url = task.getResult().toString();
									alertDialog.dismiss();
								}
							});
						}
					})
					.addOnFailureListener(new OnFailureListener() {
						@Override
						public void onFailure(@NonNull Exception e) {

						}
					})
					.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
						@Override
						public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

						}
					});
		} else {
			_url = "No Logo";
			alertDialog.dismiss();
		}
		// PICTURES
		if (path1 != null) {
			UploadTask uploadTask = (UploadTask) storageReference.child("images/businesses/" + _email + "_1.png").putFile(path1)
					.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
						@Override
						public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
							taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
								@Override
								public void onComplete(@NonNull Task<Uri> task) {
									_url1 = task.getResult().toString();
									selectedPictures.add(_url1);
									// PICTURE 2
									if (path2 != null) {
										UploadTask uploadTask = (UploadTask) storageReference.child("images/businesses/" + _email + "_2.png").putFile(path2)
												.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
													@Override
													public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
														taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
															@Override
															public void onComplete(@NonNull Task<Uri> task) {
																_url2 = task.getResult().toString();
																selectedPictures.add(_url2);
																// PICTURE 3
																if (path3 != null) {
																	UploadTask uploadTask = (UploadTask) storageReference.child("images/businesses/" + _email + "_3.png").putFile(path3)
																			.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
																				@Override
																				public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
																					taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
																						@Override
																						public void onComplete(@NonNull Task<Uri> task) {
																							_url3 = task.getResult().toString();
																							selectedPictures.add(_url3);
																							alertDialog.dismiss();
																							signUp();
																						}
																					});
																				}
																			})
																			.addOnFailureListener(new OnFailureListener() {
																				@Override
																				public void onFailure(@NonNull Exception e) {

																				}
																			})
																			.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
																				@Override
																				public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

																				}
																			});
																} else {
																	toastAdapter.makeToast(R.drawable.__error, "No pudimos almacenar los datos en nuestros servidores, revisa tu conexión a Internet 😥");
																}
															}
														});
													}
												})
												.addOnFailureListener(new OnFailureListener() {
													@Override
													public void onFailure(@NonNull Exception e) {

													}
												})
												.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
													@Override
													public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

													}
												});
									} else {
										toastAdapter.makeToast(R.drawable.__error, "No pudimos almacenar los datos en nuestros servidores, revisa tu conexión a Internet 😥");
									}
								}
							});
						}
					})
					.addOnFailureListener(new OnFailureListener() {
						@Override
						public void onFailure(@NonNull Exception e) {

						}
					})
					.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
						@Override
						public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

						}
					});
		} else {
			toastAdapter.makeToast(R.drawable.__error, "No pudimos almacenar los datos en nuestros servidores, revisa tu conexión a Internet 😥");
		}
	}

	private void signUp() {
		String url = Network.Business;
		JSONObject jsonParams = new JSONObject();

		JSONArray scheduleArray = new JSONArray();
		JSONArray categoriesArray = new JSONArray();
		JSONArray picturesArray = new JSONArray();

		for (int i = 0; i < 7; i++) {
			JSONObject obj = new JSONObject();
			try {
				switch (i) {
					case 0:
						obj.put("Lunes:", _day1);
						break;
					case 1:
						obj.put("Martes:", _day2);
						break;
					case 2:
						obj.put("Miércoles:", _day3);
						break;
					case 3:
						obj.put("Jueves:", _day4);
						break;
					case 4:
						obj.put("Viernes:", _day5);
						break;
					case 5:
						obj.put("Sábado:", _day6);
						break;
					case 6:
						obj.put("Domingo:", _day7);
						break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			scheduleArray.put(obj);
		}

		for (int i = 0; i < selectedCategories.size(); i++) {
			JSONObject obj = new JSONObject();
			try {
				obj.put("category", selectedCategories.get(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			categoriesArray.put(obj);
		}

		for (int i = 0; i < selectedPictures.size(); i++) {
			JSONObject obj = new JSONObject();
			try {
				obj.put("picture", selectedPictures.get(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			picturesArray.put(obj);
		}

		try {
			jsonParams.put("owner", _owner);
			jsonParams.put("email", _email);
			jsonParams.put("pass", _pass);
			jsonParams.put("deviceId", __deviceId);
			jsonParams.put("type", type);
			jsonParams.put("logo", _url);
			jsonParams.put("name", _businessName);
			jsonParams.put("desc", _businessDesc);
			jsonParams.put("slogan", _businessSlogan);
			jsonParams.put("phone", _businessPhone);
			jsonParams.put("address", _businessAddress);
			jsonParams.put("lat", _lat);
			jsonParams.put("lng", _lng);
			jsonParams.put("fb", _fb);
			jsonParams.put("ig", _ig);
			jsonParams.put("wa", _wa);
			jsonParams.put("delivery", delivery);
			jsonParams.put("schedule", scheduleArray);
			jsonParams.put("categories", categoriesArray);
			jsonParams.put("pictures", picturesArray);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					__message = response.getString("message");
					if ("Ok".equals(__message)) {
						preferencesAdapter.setEmail(_email);
						preferencesAdapter.setPass(_pass);
						JSONObject jsonObject = response.getJSONObject("business");
						builder = new AlertDialog.Builder(context);
						builder.setTitle("Todo listo");
						builder.setMessage("Nuestro equipo verificará los datos, tu cuenta para " + jsonObject.getString("name") + " estará activa en un lapso de 48 horas 😋");
						builder.setPositiveButton("Genial Gracias 🥰", null);
						alertDialog = builder.create();
						alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
							@Override
							public void onShow(DialogInterface dialogInterface) {
								Button btnOk = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
								btnOk.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View view) {
										alertDialog.dismiss();
										intentAdapter.goActivityDefault(SignIn.class);
									}
								});
							}
						});
						alertDialog.setCancelable(false);
						alertDialog.setCanceledOnTouchOutside(false);
						alertDialog.show();
					} else {
						toastAdapter.makeToast(R.drawable.__error, "!Oh no! Algo salió mal 😭");
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