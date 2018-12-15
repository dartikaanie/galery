package com.example.dara.galery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    TextView nama;
    TextView deskripsi;
    TextView lat,lng, alamat, alamatLengkap;
    ImageView fotoView;
    Foto foto;
    Button unduhBtn, shareBtn;
    private RequestQueue requestQueue;

    // Uri for image path
    String imageUrl;
    private final int select_photo = 1; // request code fot gallery intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        nama = findViewById(R.id.namaDetail);
        deskripsi = findViewById(R.id.deskripsiDetail);
        lat = findViewById(R.id.latDetail);
        lng = findViewById(R.id.lngDetail);
        alamat = findViewById(R.id.alamatDetail);
        alamatLengkap =  findViewById(R.id.alamatlenkapDetail);
        fotoView  = findViewById(R.id.fotoDetail);

        Intent detailIntent = getIntent();
        if(null != detailIntent) {
            foto = detailIntent.getParcelableExtra("key_foto_parcelable");
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(foto != null) {
            getSupportActionBar().setTitle(foto.getNama());
            nama.setText(String.valueOf(foto.getNama()));
            deskripsi.setText(foto.getDeskripsi());
            lat.setText(String.valueOf(foto.getLat()));
            lng.setText(String.valueOf(foto.getLng()));
            imageUrl = "http://parit.store/galery/public/foto/" + foto.getPath_foto();
            Glide.with(this).load(imageUrl).into(fotoView);
            unduhBtn = findViewById(R.id.btn_download);
            shareBtn = findViewById(R.id.btn_share);
            alamat.setText( getAddress(foto.getLat(), foto.getLng(),1));
            alamatLengkap.setText( getAddress(foto.getLat(), foto.getLng(),2));


            unduhBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(DetailActivity.this, "download", Toast.LENGTH_SHORT).show();
                }
            });

            shareBtn = findViewById(R.id.btn_share);
            shareBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  share(v);
                }
            });
        }
        else{
            Toast.makeText(this, "no Data", Toast.LENGTH_SHORT).show();

        }
    }


    public void shareItem(String url) {
        Picasso.with(getApplicationContext()).load(url).into(new Target() {
            @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("image/*");
                i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(bitmap));
                startActivity(Intent.createChooser(i, "Share Image"));
            }
            @Override public void onBitmapFailed(Drawable errorDrawable) { }
            @Override public void onPrepareLoad(Drawable placeHolderDrawable) { }
        });
    }

    public Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        try {
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    public void share(View v){
//
        Intent share = new Intent(Intent.ACTION_SEND);
//        share.setType("text/plain");
//        share.putExtra(Intent.EXTRA_TEXT, imageUrl);
////
//        share.setType("image/jpg");
//        share.putExtra(Intent.EXTRA_TEXT,imageUrl);//your Image Url

//        Intent share = new Intent(Intent.ACTION_SEND);

        // If you want to share a png image only, you can do:
        // setType("image/png"); OR for jpeg: setType("image/jpeg");
        share.setType("image/*");

        // Make sure you put example png image named myImage.png in your
        // directory
        String imagePath =imageUrl;

        File imageFileToShare = new File(imagePath);

        Uri uri = Uri.fromFile(imageFileToShare);
        share.putExtra(Intent.EXTRA_STREAM, uri);

        startActivity(Intent.createChooser(share, "Share Image!"));
        if(share.resolveActivity(getPackageManager())!= null ){
            startActivity(share);
        }
    }


    //lokasi
//    public  String lokasi(){
//       JsonObjectRequest request = new JsonObjectRequest(
//            "https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&key=AIzaSyCPx2FI7E0Ag0OCExa-6Ih8VMdUNZbHm_U",
//            new Response.Listener<JSONObject>(){
//                @Override
//                public void onResponse(JSONObject response) {
//
//                }
//            })
//        requestQueue.add(request);
//}

    private String getAddress(double latitude, double longitude, int pilih) {
       if(konekkah()){
           StringBuilder result = new StringBuilder();
//        StringBuilder alamatR = new StringBuilder();
           try {
               Geocoder geocoder = new Geocoder(this, Locale.getDefault());
               List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
               if (addresses.size() > 0) {
                   android.location.Address address = addresses.get(0);
                   // 1 untuk kota saja
                   if (pilih == 1){
                       result.append(address.getLocality());
                   }else{
                       result.append(address.getAddressLine(0));
                   }
               }
           } catch (IOException e) {
               Log.e("tag", e.getMessage());
           }
           return result.toString();
       }else{
           return "no internet";
       }

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
