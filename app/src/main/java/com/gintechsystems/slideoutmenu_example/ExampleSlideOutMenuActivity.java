package com.gintechsystems.slideoutmenu_example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gintechsystems.slideoutmenu.*;

/**
 * Created by joeginley on 9/3/17.
 */

public class ExampleSlideOutMenuActivity extends SlideOutMenuActivity {

    private static final String TAG = "ExampleActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_example_slideoutmenu);
        setBehindContentView(R.layout.side_menu);

        final SlideOutMenu som = getSlideOutMenu();

        som.setFadeEnabled(false);
        //som.setFadeDegree(0.35f);

        som.setBehindOffsetRes(R.dimen.menu_offset);

        som.setShadowDrawable(R.drawable.menu_shadow);
        som.setShadowWidthRes(R.dimen.menu_shadow_width);

        som.setOnClosedListener(new SlideOutMenu.OnClosedListener() {
            @Override
            public void onClosed() {
                Log.d(TAG, "Closed");
            }
        });

        som.setOnOpenedListener(new SlideOutMenu.OnOpenedListener() {
            @Override
            public void onOpened() {
                Log.d(TAG, "Opened");
            }
        });
    }

}
