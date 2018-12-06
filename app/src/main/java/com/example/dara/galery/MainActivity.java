package com.example.dara.galery;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.dara.galery.fragment.addFragment;
import com.example.dara.galery.fragment.galeryFragment;
import com.example.dara.galery.fragment.homeFragment;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // kita set default nya Home Fragment
        loadFragment(new homeFragment());
        // inisialisasi BottomNavigaionView
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
        // beri listener pada saat item/menu bottomnavigation terpilih
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
            case R.id.navigation_home:
                fragment = new homeFragment();
                break;
        }
        return loadFragment(fragment);
    }
}
