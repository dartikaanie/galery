package com.example.dara.galery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dara.galery.fragment.addFragment;
import com.example.dara.galery.fragment.galeryFragment;
import com.example.dara.galery.fragment.homeFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FloatingActionButton fab;
    public int mode;

    @Override
    protected void onCreate(Bundle outState) {
        super.onCreate(outState);
        setContentView(R.layout.activity_main);


//            // kita set default nya Home Fragment
        loadFragment(new galeryFragment());
//        // inisialisasi BottomNavigaionView
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
//        // beri listener pada saat item/menu bottomnavigation terpilih
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


    }


    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        //Jika tombol refresh diklik
        switch (item.getItemId()){
            case   R.id.menu_refresh :
                Toast.makeText(this, "Refreshing data . . .", Toast.LENGTH_SHORT).show();
                galeryFragment.getFoto();
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("status_halaman",mode);

    }



    // method untuk load fragment yang sesuai
    private boolean loadFragment(android.support.v4.app.Fragment fragment){
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        android.support.v4.app.Fragment fragment = null;
        switch (menuItem.getItemId()){
            case R.id.navigation_galery:
                fragment = new galeryFragment();
                break;
            case R.id.navigation_add:
                fragment = new addFragment();
                break;

        }
        return loadFragment(fragment);
    }

    public Boolean konekkah(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        @SuppressLint("MissingPermission") NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean konek = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return konek;
    }

}

