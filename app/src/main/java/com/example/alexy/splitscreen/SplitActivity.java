package com.example.alexy.splitscreen;

import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class SplitActivity extends AppCompatActivity {

    private final int REQUEST = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("SplitActivity", "onCreate");

        super.onCreate(savedInstanceState);

        if (isInMultiWindowMode()) {
            Log.d("SplitActivity", "multi, start activities");
            launchActivities();
        } else {
            startService(new Intent(this, SplitScreenService.class));

            setContentView(R.layout.activity_split);
            Log.d("SplitActivity", "not multi");

            findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent accessibilityIntent = new Intent();
                    accessibilityIntent.setAction(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivityForResult(accessibilityIntent, REQUEST);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST) {
            startService(new Intent(this, SplitScreenService.class));
        }
    }

    private void launchActivities() {
        Intent app = getPackageManager().getLaunchIntentForPackage("com.google.android.apps.maps");

        Intent main = new Intent(getApplicationContext(), MainActivity.class);
        main.setFlags(Intent.FLAG_ACTIVITY_LAUNCH_ADJACENT);
        main.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(app);
        startActivity(main);
        finishAndRemoveTask();
    }

    @Override
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
        super.onMultiWindowModeChanged(isInMultiWindowMode);

        if (isInMultiWindowMode) {
            Log.d("SplitActivity", "multi, start activities");
            launchActivities();
        }
    }
}
