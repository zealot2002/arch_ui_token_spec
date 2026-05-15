package com.joy.arch_ui_token_spec;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.TooltipCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_showcase);

        FloatingActionButton fab = findViewById(R.id.fab_theme_toggle);
        applyThemeFabIcon(fab);
        TooltipCompat.setTooltipText(fab, fab.getContentDescription());

        fab.setOnClickListener(v -> {
            int mask = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            boolean isNight = mask == Configuration.UI_MODE_NIGHT_YES;
            AppCompatDelegate.setDefaultNightMode(
                    isNight ? AppCompatDelegate.MODE_NIGHT_NO : AppCompatDelegate.MODE_NIGHT_YES);
            recreate();
        });
    }

    private static void applyThemeFabIcon(FloatingActionButton fab) {
        int mask = fab.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isNight = mask == Configuration.UI_MODE_NIGHT_YES;
        fab.setImageResource(isNight ? R.drawable.ic_theme_day : R.drawable.ic_theme_night);
    }
}
