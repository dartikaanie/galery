package com.example.dara.galery.fragment;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dara.galery.DatabaseHandler;
import com.example.dara.galery.Foto;
import com.example.dara.galery.R;
import com.example.dara.galery.TmdbClient;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class addFragment extends Fragment {

    Intent intent;
    Uri fileUri;
    Button btn_choose_image;
    ImageView imageView;
    Bitmap bitmap, decoded;
    ProgressBar waiting;
    public final int REQUEST_CAMERA = 0;
    public final int SELECT_FILE = 1;

    int bitmap_size = 40; // image quality 1 - 100;
    int max_resolution_image = 800;

    //untuk location
    private static  final  int REQUEST_LOCATION =1;
    private EditText ETnama, ETpath, ETdeskripsi, ETlokasi;
    TextView TextView;
    private Button tambahBtn, button_location;
    LocationManager locationManager;
    String latitude, longtitude;

    DatabaseHandler handler;
    private Activity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.activity = (Activity) context;
        handler = new DatabaseHandler(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        btn_choose_image = view.findViewById(R.id.btn_choose_image);
        imageView = view.findViewById(R.id.imageView);


        btn_choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        return view;
    }

    private void selectImage() {
        imageView.setImageResource(0);
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("Add Photo!");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 0);
                } else if (items[item].equals("Choose from Library")) {
                    intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Bitmap bitmap = (Bitmap) data.getExtras().get("data");
        imageView.setImageBitmap(bitmap);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        ETnama = view.findViewById(R.id.edit_text_name);
        ETpath = view.findViewById(R.id.edit_text_path);
        ETdeskripsi = view.findViewById(R.id.edit_text_deskripsi);
        ETlokasi = view.findViewById(R.id.edit_text_lat);
        tambahBtn = view.findViewById(R.id.tambah_foto);
        button_location = view.findViewById(R.id.button_location);
//        TextView = view.findViewById(R.id.text_location);

        //klik button_location
        button_location.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    buildAlertMessageNoGPS();
                }
                else
                    {
                        getLocation();
                    }

            }
        });





        //insert

        // lakukan validasi terlebih dahulu, jika sudah benar, maka lakukan proses insert data
        tambahBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 String nama = ETnama.getText().toString();
                 String deskripsi = ETdeskripsi.getText().toString();
                 Double lat = Double.valueOf(latitude);
                 Double lng = Double.valueOf(longtitude);
                 String foto = getBase64String(imageView);

                if(TextUtils.isEmpty(ETnama.getText().toString())){
                    ETnama.setError(getResources().getString(R.string.msg_cannot_allow_empty_field));
                }else if(TextUtils.isEmpty(ETdeskripsi.getText().toString())) {
                    ETdeskripsi.setError(getResources().getString(R.string.msg_cannot_allow_empty_field));
                }
                else if(TextUtils.isEmpty(ETlokasi.getText().toString())) {
                    ETlokasi.setError(getResources().getString(R.string.msg_cannot_allow_empty_field));
                }else{

                    TmdbClient client = (new Retrofit.Builder()
                            .baseUrl("http://parit.store/galery/public/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build())
                            .create(TmdbClient.class);
                    Call<ResponseBody> call = client.newFoto(nama, deskripsi,lat, lng, foto);
                    Log.e("foto", foto);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            ResponseBody s = response.body();
                            Toast.makeText(activity, "berhasil disimpan", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(activity, t.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

//                    tambah(handler);
                }
            }
        });


    }


    @Override
    public void onPause() {
        super.onPause();
        ETnama.clearFocus();
    }

   public void getLocation(){
        if((ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED )
                &&
                (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)){
            ActivityCompat.requestPermissions(getActivity(), new String [] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
       }
       else{
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if(location!=null){
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                latitude = String.valueOf(lat);
                longtitude = String.valueOf(lng);
                ETlokasi.setText("Latitude" + latitude +", longitute"+ longtitude);
            }
            else{
                Toast.makeText(getActivity(),"TIDAK TAMPIL LOKASINYA YA", Toast.LENGTH_SHORT).show();
            }
        }
   }

   protected void buildAlertMessageNoGPS(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Hidupkan GPS mu")
                .setCancelable(false)
                .setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
   }

    private void tambah(final DatabaseHandler databaseHandler){
        String nama = ETnama.getText().toString();
        String deskripsi = ETdeskripsi.getText().toString();
        String path = ETpath.getText().toString();
        Double lat = Double.valueOf(latitude);
        Double lng = Double.valueOf(longtitude);


        Foto dataFoto = new Foto();
        dataFoto.setNama(nama);
        dataFoto.setDeskripsi(deskripsi);
        dataFoto.setPath_foto(path);
        dataFoto.setLat(lat);
        dataFoto.setLng(lng);

        databaseHandler.tambah_foto(dataFoto);

        ETnama.setText("");
        ETpath.setText("");
        ETdeskripsi.setText("");
    }

    //konvert gambar ke string
    private String getBase64String(ImageView imageFoto) {

        BitmapDrawable drawable = (BitmapDrawable) imageFoto.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}
