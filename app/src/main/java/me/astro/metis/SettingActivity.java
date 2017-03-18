package me.astro.metis;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.format);

        final SharedPreferences preferences = getSharedPreferences("settings", MODE_PRIVATE);
        switch (preferences.getString("image_format", "jpeg")) {
            case "jpeg":
                ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
                break;
            case "png":
                ((RadioButton) radioGroup.getChildAt(1)).setChecked(true);
                break;
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.jpeg:
                        preferences.edit().putString("image_format", "jpeg").apply();
                        Snackbar.make(radioGroup, "Format was set to JPEG.", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.png:
                        preferences.edit().putString("image_format", "png").apply();
                        Snackbar.make(radioGroup, "Format was set to PNG.", Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
