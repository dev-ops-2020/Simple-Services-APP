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
import com.google.android.material.checkbox.MaterialCheckBox;
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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.ops.dev.simple.services.Network.REQUEST_CODE_IMAGE;
import static com.ops.dev.simple.services.Network.REQUEST_CODE_PICTURE_1;
import static com.ops.dev.simple.services.Network.REQUEST_CODE_PICTURE_2;
import static com.ops.dev.simple.services.Network.REQUEST_CODE_PICTURE_3;

public class AddProduct extends AppCompatActivity {

    //Vars
	TextView tittle;
	String __message;
	StorageReference storageReference;
	String _url1, _url2, _url3;

	String _type, type;

	String _name, _desc, _price, _tags;
	TextInputLayout name, desc, price, tags;
	ArrayList<String> selectedTags;

	MaterialCheckBox available;

	boolean _available = false;

	Uri path1, path2, path3;
	ImageView picture1, picture2, picture3;
	ArrayList<String> selectedPictures;

	Button add;
	Context context;
	RequestQueue queue;

	AlertDialog.Builder builder;
	AlertDialog alertDialog;
	IntentAdapter intentAdapter;
	ToastAdapter toastAdapter;
	GlideAdapter glideAdapter;
	PreferencesAdapter preferencesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_product);
		final View layout = findViewById(android.R.id.content);
		context = AddProduct.this;
		intentAdapter = new IntentAdapter(this, context);
		toastAdapter = new ToastAdapter(context);
		glideAdapter = new GlideAdapter(context);
		preferencesAdapter = new PreferencesAdapter(context);
		queue = Volley.newRequestQueue(context);

		tittle = findViewById(R.id.tittle);

		picture1 = findViewById(R.id.picture1);
		picture2 = findViewById(R.id.picture2);
		picture3 = findViewById(R.id.picture3);

		name = findViewById(R.id.name);
		desc = findViewById(R.id.desc);
		price = findViewById(R.id.price);
		tags = findViewById(R.id.tags);
		available = findViewById(R.id.available);
		add = findViewById(R.id.add);

		_type = getIntent().getStringExtra("type");

		assert _type != null;
		if (_type.equals("Product"))
			type = "producto";
		else if (_type.equals("Service"))
			type = "servicio";

		tittle.setText("Agregar " + type);
		storageReference = FirebaseStorage.getInstance().getReference();

		path1 = null;
		path2 = null;
		path3 = null;

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
		selectedTags = new ArrayList<>();

		add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (path1 == null || path2 == null || path3 == null) {
					toastAdapter.makeToast(R.drawable.__warning, "Selecciona las fotos del " + type);
				} else {
					_name = String.valueOf(Objects.requireNonNull(name.getEditText()).getText());
					_desc = String.valueOf(Objects.requireNonNull(desc.getEditText()).getText());
					_price = String.valueOf(Objects.requireNonNull(price.getEditText()).getText());

					if (_name.length() == 0 || _desc.length() == 0 || _price.length() == 0) {
						toastAdapter.makeToast(R.drawable.__warning, "Nombre, descripción y precio son requeridos");
					} else {
						_tags = String.valueOf(Objects.requireNonNull(tags.getEditText()).getText());

						if (_tags.length() == 0) {
							builder = new AlertDialog.Builder(context);
							builder.setTitle("Estás dejando de lado una de las mejores funciones 😌");
							builder.setMessage("Agregar etiquetas a tus " + type + "s permite que estos sean encontrados más fácilmente 😎  \n\n¡Psss! Aprovecha las etiquetas, son ilimitadas, solo asegurate de poner un '#' antes y separarlas por ','");
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
											uploadImages();
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
							uploadImages();
						}
					}
				}
			}
		});
    }

	@Override
	public void onBackPressed() {

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

	private void uploadImages() {
    	findViewById(R.id.add).setEnabled(false);
    	findViewById(R.id.add).setBackgroundResource(R.drawable.border_buttons_disable);
		builder = new AlertDialog.Builder(context);
		builder.setTitle("Agregando " + type);
		builder.setMessage("Estamos validando los datos...");
		alertDialog = builder.create();
		alertDialog.setCancelable(false);
		alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.show();
		// PICTURES
		if (path1 != null) {
			UploadTask uploadTask = (UploadTask) storageReference.child("images/businesses/products/" + UUID.randomUUID() + ".png").putFile(path1)
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
										UploadTask uploadTask = (UploadTask) storageReference.child("images/businesses/products/" + UUID.randomUUID() + ".png").putFile(path2)
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
																	UploadTask uploadTask = (UploadTask) storageReference.child("images/businesses/products/" + UUID.randomUUID() + ".png").putFile(path3)
																			.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
																				@Override
																				public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
																					taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
																						@Override
																						public void onComplete(@NonNull Task<Uri> task) {
																							_url3 = task.getResult().toString();
																							selectedPictures.add(_url3);
																							alertDialog.dismiss();
																							addProduct();
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

	private void addProduct() {
		String url = Network.Products;
		JSONObject jsonParams = new JSONObject();

		if (available.isChecked())
			_available = true;

		JSONArray picturesArray = new JSONArray();
		JSONArray tagsArray = new JSONArray();

		for (int i = 0; i < selectedPictures.size(); i++) {
			JSONObject obj = new JSONObject();
			try {
				obj.put("picture", selectedPictures.get(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			picturesArray.put(obj);
		}

		String[] tags = _tags.split(",");
		Collections.addAll(selectedTags, tags);

		for (int i = 0; i < selectedTags.size(); i++) {
			JSONObject obj = new JSONObject();
			try {
				obj.put("tag", selectedTags.get(i));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			tagsArray.put(obj);
		}

		try {
			jsonParams.put("type", _type);
			jsonParams.put("name", _name);
			jsonParams.put("desc", _desc);
			jsonParams.put("price", _price);
			jsonParams.put("available", _available);
			jsonParams.put("pictures", picturesArray);
			jsonParams.put("tags", tagsArray);
			jsonParams.put("businessId", preferencesAdapter.getId());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonParams, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				try {
					__message = response.getString("message");
					if ("Product stored".equals(__message)) {
						JSONObject jsonObject = response.getJSONObject("product");
						toastAdapter.makeToast(R.drawable.__ok, "Agregado " + type + " " + jsonObject.getString("name") + " correctamente");
						Intent intent = new Intent(context, MainMenuBusiness.class);
						intent.putExtra("screen", R.id.catalog);
						intent.putExtra("number", 0);
						startActivity(intent);
					} else {
						toastAdapter.makeToast(R.drawable.__error, "!Oh no! Algo salió mal 😭 \n\nIntenta agregar el " + type + " nuevamente");
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