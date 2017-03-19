package me.astro.metis;

import android.Manifest;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private Bitmap bitmap;
    private boolean isPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save(view);
            }
        });

        if (!(isPermissionGranted = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isPermissionGranted = true;
                Snackbar.make(findViewById(R.id.fab), "Permission was granted.", Snackbar.LENGTH_SHORT).show();
            } else {
                isPermissionGranted = false;
                Snackbar.make(findViewById(R.id.fab), "Permission denied.", Snackbar.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void save(View view) {
        if (bitmap != null) {
            if (isPermissionGranted) {
                try {
                    String format = getSharedPreferences("settings", MODE_PRIVATE).getString("image_format", "jpeg");
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Metis/" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(System.currentTimeMillis())) + "." + format);
                    file.getParentFile().mkdirs();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    bitmap.compress(format.equals("jpeg") ? Bitmap.CompressFormat.JPEG : Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    Snackbar.make(view, "Save the wallpaper at " + file.getAbsolutePath() + ".", Snackbar.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                }
            } else {
                final Activity activity = this;
                Snackbar.make(view, "You have to allow the permission for saving the wallpaper.", Snackbar.LENGTH_LONG)
                        .setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            }
                        })
                        .show();
            }
        } else {
            Snackbar.make(view, "You have to get the wallpaper first.", Snackbar.LENGTH_SHORT).show();
        }
    }

    public void updatePreview(View view) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        bitmap = ((BitmapDrawable) wallpaperManager.getDrawable()).getBitmap();

        ImageView imageView = (ImageView) findViewById(R.id.preview);
        imageView.setImageBitmap(bitmap);

        Snackbar.make(findViewById(R.id.fab), "Preview updated.", Snackbar.LENGTH_SHORT).show();
    }
}
