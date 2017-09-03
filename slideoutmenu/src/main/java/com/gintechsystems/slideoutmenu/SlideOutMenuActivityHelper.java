package com.gintechsystems.slideoutmenu;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by joeginley on 9/3/17.
 */

class SlideOutMenuActivityHelper {

    private Activity mActivity;

    private SlideOutMenu mSlideOutMenu;

    private View mViewAbove;

    private View mViewBehind;

    private boolean mBroadcasting = false;

    private boolean mOnPostCreateCalled = false;

    private boolean mEnableSlide = true;

    /**
     * Instantiates a new SlideOutMenuActivityHelper.
     *
     * @param activity the associated activity
     */
    SlideOutMenuActivityHelper(Activity activity) {
        mActivity = activity;
    }

    /**
     * Sets mSlideOutMenu as a newly inflated SlideOutMenu. Should be called within the activitiy's onCreate()
     *
     * @param savedInstanceState the saved instance state (unused)
     */
    void onCreate(Bundle savedInstanceState) {
        mSlideOutMenu = (SlideOutMenu) LayoutInflater.from(mActivity).inflate(R.layout.slide_out_menu, null);
    }

    /**
     * Further SlideOutMenu initialization. Should be called within the activitiy's onPostCreate()
     *
     * @param savedInstanceState the saved instance state (unused)
     */
    void onPostCreate(Bundle savedInstanceState) {
        if (mViewBehind == null || mViewAbove == null) {
            throw new IllegalStateException("Both setBehindContentView must be called " +
                    "in onCreate in addition to setContentView.");
        }

        mOnPostCreateCalled = true;

        mSlideOutMenu.attachToActivity(mActivity,
                mEnableSlide ? SlideOutMenu.SLIDING_WINDOW : SlideOutMenu.SLIDING_CONTENT);

        final boolean open;
        final boolean secondary;
        if (savedInstanceState != null) {
            open = savedInstanceState.getBoolean("SlidingActivityHelper.open");
            secondary = savedInstanceState.getBoolean("SlidingActivityHelper.secondary");
        } else {
            open = false;
            secondary = false;
        }
        new Handler().post(new Runnable() {
            public void run() {
                if (open) {
                    if (secondary) {
                        mSlideOutMenu.showSecondaryMenu(false);
                    } else {
                        mSlideOutMenu.showMenu(false);
                    }
                } else {
                    mSlideOutMenu.showContent(false);
                }
            }
        });
    }

    /**
     * Controls whether the ActionBar slides along with the above view when the menu is opened,
     * or if it stays in place.
     *
     * @param SlideOutActionBarEnabled True if you want the ActionBar to slide along with the SlideOutMenu,
     * false if you want the ActionBar to stay in place
     */
    void setSlideOutActionBarEnabled(boolean SlideOutActionBarEnabled) {
        if (mOnPostCreateCalled)
            throw new IllegalStateException("enableSlideOutActionBar must be called in onCreate.");
        mEnableSlide = SlideOutActionBarEnabled;
    }

    /**
     * Finds a view that was identified by the id attribute from the XML that was processed in onCreate(Bundle).
     *
     * @param id the resource id of the desired view
     * @return The view if found or null otherwise.
     */
    View findViewById(int id) {
        View v;
        if (mSlideOutMenu != null) {
            v = mSlideOutMenu.findViewById(id);
            if (v != null)
                return v;
        }
        return null;
    }

    /**
     * Called to retrieve per-instance state from an activity before being killed so that the state can be
     * restored in onCreate(Bundle) or onRestoreInstanceState(Bundle) (the Bundle populated by this method
     * will be passed to both).
     *
     * @param outState Bundle in which to place your saved state.
     */
    void onSaveInstanceState(Bundle outState) {
        outState.putBoolean("SlidingActivityHelper.open", mSlideOutMenu.isMenuShowing());
        outState.putBoolean("SlidingActivityHelper.secondary", mSlideOutMenu.isSecondaryMenuShowing());
    }

    /**
     * Register the above content view.
     *
     * @param v the above content view to register
     * @param params LayoutParams for that view (unused)
     */
    void registerAboveContentView(View v, ViewGroup.LayoutParams params) {
        if (!mBroadcasting)
            mViewAbove = v;
    }

    /**
     * Set the activity content to an explicit view. This view is placed directly into the activity's view
     * hierarchy. It can itself be a complex view hierarchy. When calling this method, the layout parameters
     * of the specified view are ignored. Both the width and the height of the view are set by default to
     * MATCH_PARENT. To use your own layout parameters, invoke setContentView(android.view.View,
     * android.view.ViewGroup.LayoutParams) instead.
     *
     * @param v The desired content to display.
     */
    public void setContentView(View v) {
        mBroadcasting = true;
        mActivity.setContentView(v);
    }

    /**
     * Set the behind view content to an explicit view. This view is placed directly into the behind view 's view hierarchy.
     * It can itself be a complex view hierarchy.
     *
     * @param view The desired content to display.
     * @param layoutParams Layout parameters for the view. (unused)
     */
    void setBehindContentView(View view, ViewGroup.LayoutParams layoutParams) {
        mViewBehind = view;
        mSlideOutMenu.setMenu(mViewBehind);
    }

    /**
     * Gets the SlideOutMenu associated with this activity.
     *
     * @return the SlideOutMenu associated with this activity.
     */
    SlideOutMenu getSlideOutMenu() {
        return mSlideOutMenu;
    }

    /**
     * Toggle the SlideOutMenu. If it is open, it will be closed, and vice versa.
     */
    void toggle() {
        mSlideOutMenu.toggle();
    }

    /**
     * Close the SlideOutMenu and show the content view.
     */
    void showContent() {
        mSlideOutMenu.showContent();
    }

    /**
     * Open the SlideOutMenu and show the menu view.
     */
    void showMenu() {
        mSlideOutMenu.showMenu();
    }

    /**
     * Open the SlideOutMenu and show the secondary menu view. Will default to the regular menu
     * if there is only one.
     */
    void showSecondaryMenu() {
        mSlideOutMenu.showSecondaryMenu();
    }

    /**
     * On key up.
     *
     * @param keyCode the key code
     * @param event the event
     * @return true, if successful
     */
    boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mSlideOutMenu.isMenuShowing()) {
            showContent();
            return true;
        }

        return false;
    }

}
