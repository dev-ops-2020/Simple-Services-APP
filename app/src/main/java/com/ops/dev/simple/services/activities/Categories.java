package com.ops.dev.simple.services.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ops.dev.simple.services.R;
import com.ops.dev.simple.services.adapters.CategoriesAdapter;
import com.ops.dev.simple.services.models.CategoriesModel;

import java.util.ArrayList;
import java.util.List;

public class Categories extends AppCompatActivity {

    //Vars
    RecyclerView rvCategories;
    List<CategoriesModel> listCategories;
    CategoriesAdapter categoriesAdapter;
    Context context ;
    String idCat, categoryName, tittle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);

	setContentView(R.layout.activity_categories);
	View layout = findViewById(android.R.id.content);

	try {
	    CategoriesModel category = (CategoriesModel) getIntent().getSerializableExtra("category");
	    idCat = category.getId();
	    categoryName = category.getName();
	} catch (Exception ex) {
	    //CategoriesIconModel categoryIcon = (CategoriesIconModel) getIntent().getSerializableExtra("category");
	    //categoryName = categoryIcon.getName();
	}

	final TextView txtTittle = findViewById(R.id.tittle);
	if (savedInstanceState == null) {
	    Bundle extras = getIntent().getExtras();
	    if(extras == null) {
		tittle = null;
	    } else {
		tittle = extras.getString("tittle");
	    }
	} else {
	    tittle = (String) savedInstanceState.getSerializable("tittle");
	}
	txtTittle.setText(tittle);

	context = Categories.this;

	rvCategories = findViewById(R.id.rvCategories);
	listCategories = new ArrayList<>();

	for(int i = 1; i <= 9 ; i++)  {
	    listCategories.add(new CategoriesModel(String.valueOf(i), categoryName + " " +String.valueOf(i) , "",R.drawable._fav));
	};

	RecyclerView.LayoutManager layoutManager = new GridLayoutManager( context,3);
	rvCategories.setLayoutManager(layoutManager);
	categoriesAdapter = new CategoriesAdapter(context, listCategories);
	rvCategories.setAdapter(categoriesAdapter);
    }
}