package com.example.dara.galery.helper;

import android.content.Intent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.example.dara.galery.MainActivity;
import com.example.dara.galery.R;

public class SplashScreen extends AppCompatActivity {

        PermissionHelper permissionHelper;
        Intent intent;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.splashscreen);
            permissionHelper = new PermissionHelper(this);

            checkAndRequestPermissions();
        }

        private boolean checkAndRequestPermissions() {
            permissionHelper.permissionListener(new PermissionHelper.PermissionListener() {
                @Override
                public void onPermissionCheckDone() {
                    intent = new Intent(com.example.dara.galery.helper.SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            permissionHelper.checkAndRequestPermissions();

            return true;
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            permissionHelper.onRequestCallBack(requestCode, permissions, grantResults);
        }
}
