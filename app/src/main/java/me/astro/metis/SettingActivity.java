package me.astro.metis;

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
        ((RadioButton) radioGroup.getChildAt(0)).setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.jpeg:
                        Snackbar.make(radioGroup, "Format was set to JPEG.", Snackbar.LENGTH_SHORT).show();
                        break;
                    case R.id.png:
                        Snackbar.make(radioGroup, "Format was set to PNG.", Snackbar.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
