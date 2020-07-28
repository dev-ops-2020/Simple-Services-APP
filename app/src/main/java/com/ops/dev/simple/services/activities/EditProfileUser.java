package com.ops.dev.simple.services.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ops.dev.simple.services.Network;
import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.adapters.GlideAdapter;
import com.ops.dev.simple.services.adapters.ToastAdapter;
import com.ops.dev.simple.services.models.UsersModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import static com.ops.dev.simple.services.Network.REQUEST_CODE_IMAGE;

public class EditProfileUser extends AppCompatActivity {

	//Vars
	String __message, __id, __alias;
	String _name, _alias, _phone, _email, _pass, _picture;
	TextView alias;
	TextInputLayout name, phone, email, pass;
	ImageView picture;
	Button update;
	Context context;
	RequestQueue queue;

	ProgressDialog progressDialog;
	ToastAdapter toastAdapter;
	GlideAdapter glideAdapter;

	UsersModel user;

	Uri path;
	StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_profile);
		View layout = findViewById(android.R.id.content);
		context = EditProfileUser.this;

		toastAdapter = new ToastAdapter(context);
		glideAdapter = new GlideAdapter(context);
		queue = Volley.newRequestQueue(context);

		storageReference = FirebaseStorage.getInstance().getReference();

		name = findViewById(R.id.name);
		alias = findViewById(R.id.alias);
		phone = findViewById(R.id.phone);
		email = findViewById(R.id.email);
		update = findViewById(R.id.update);
		picture = findViewById(R.id.picture);

		user = (UsersModel) getIntent().getSerializableExtra("user");
		assert user != null;
		alias.setText(String.format("Editar Perfil  de: '%s'", user.getAlias()));
		Objects.requireNonNull(name.getEditText()).setText(user.getName());
		Objects.requireNonNull(phone.getEditText()).setText(user.getPhone());
		Objects.requireNonNull(email.getEditText()).setText(user.getEmail());
		glideAdapter.setImageCircle(picture, user.getPicture());

		picture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				selectPicture();
			}
		});

		update.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				uploadImage();
			}
		});
    }

	private void selectPicture() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, getResources().getString(R.string.select_image)), Network.REQUEST_CODE_IMAGE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
			path = data.getData();
			glideAdapter.setImageCircle(picture, String.valueOf(path));
		}
	}

	private void uploadImage() {
		if (path != null) {
			UploadTask uploadTask = (UploadTask) storageReference.child("images/users/" + user.getAlias() + ".png").putFile(path)
				.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
					@Override
						public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
							taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
								@Override
								public void onComplete(@NonNull Task<Uri> task) {
									_picture = task.getResult().toString().substring(0,task.getResult().toString().indexOf("&token"));
									updateProfile();
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
						//double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
						//toastAdapter.makeToast(R.drawable._fav, "Transfiriendo datos... " + (int)progress + "%");
					}
				});
		} else {
			_picture = user.getPicture();
			updateProfile();
		}
	}

	private void updateProfile() {
		String url = Network.Users+user.getId();
		_name = String.valueOf(Objects.requireNonNull(name.getEditText()).getText());
		_phone = String.valueOf(Objects.requireNonNull(phone.getEditText()).getText());
		_email = String.valueOf(Objects.requireNonNull(email.getEditText()).getText());

		if (_name.length() == 0 || _phone.length() == 0 || _email.length() == 0) {
			toastAdapter.makeToast(R.drawable.__warning, "Todos los campos son requeridos");
		} else {
			JSONObject jsonParams = new JSONObject();
			try {
				jsonParams.put("name", _name);
				jsonParams.put("phone", _phone);
				jsonParams.put("email", _email);
				jsonParams.put("picture", _picture);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage(getString(R.string.loading));
			progressDialog.setIndeterminate(false);
			progressDialog.setCancelable(false);
			progressDialog.show();
			JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonParams, new Response.Listener<JSONObject>() {
				@Override
				public void onResponse(JSONObject response) {
					try {
						__message = response.getString("message");
						if (__message.equals("User updated")) {
							progressDialog.dismiss();

							toastAdapter.makeToast(R.drawable.__ok, String.format("Datos de: '%s' actualizados correctamente", user.getAlias()));

							Handler h = new Handler();
							h.postDelayed(new Runnable() {
								@Override
								public void run() {
									finish();
									Intent intent = new Intent(context, MainMenuUser.class);
									intent.putExtra("screen", R.id.profile);
									intent.putExtra("number", 4);
									startActivity(intent);
								}
							}, 3000);
						} else {
							progressDialog.dismiss();
							toastAdapter.makeToast(R.drawable.__error, "!Vaya! Algo salió mal, prueba revisar los datos ingresados");
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