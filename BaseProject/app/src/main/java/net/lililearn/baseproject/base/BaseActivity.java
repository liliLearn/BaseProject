package net.lililearn.baseproject.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import net.lililearn.baseproject.R;
import net.lililearn.baseproject.utils.ActivityStackManager;
import net.lililearn.baseproject.utils.ScreenManager;

import java.util.ArrayList;


public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    /**
     * 是否沉浸状态栏
     **/
    private boolean isStatusBar = true;
    /**
     * 是否允许全屏
     **/
    private boolean isFullScreen = true;
    /**
     * 是否禁止旋转屏幕
     **/
    private boolean isScreenRoate = false;

    /**
     * context
     **/
    protected Context ctx;

    /**
     * 是否输出日志信息
     **/
    private boolean isDebug;

    private final int container = R.id.container;

    /**
     * 初始化界面
     **/
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 绑定事件
     */
    protected abstract void setEvent();

    private ScreenManager screenManager;
    private ArrayList<BaseFragment> fragments;// back fragment list.
    private BaseFragment fragment;// current fragment.

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "--->onCreate()");
        initView();
        initData();
        setEvent();
        ctx = this;
        ActivityStackManager.getInstance().pushActivity(this);
        screenManager = ScreenManager.getInstance();
        screenManager.setStatusBar(isStatusBar, this);
//        screenManager.setScreenRoate(isScreenRoate, this);
        screenManager.setFullScreen(isFullScreen, this);
    }

    public ArrayList<BaseFragment> getFragments() {
        return fragments;
    }

    /**
     * replace the current fragment.
     *
     * @param fragment       the new fragment to shown.
     * @param addToBackStack if it can back.
     */
    public void addContent(BaseFragment fragment, boolean addToBackStack) {
        initFragments();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(container, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        } else {
            removePrevious();
        }


        ft.commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();

        fragments.add(fragment);
        setFragment();
    }

    // use replace method to show fragment.
    public void replaceContent(BaseFragment fragment, boolean addToBackStack) {
        initFragments();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(container, fragment);
        if (addToBackStack) {
            ft.addToBackStack(null);
        } else {
            removePrevious();
        }
        ft.commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();

        fragments.add(fragment);
        setFragment();
    }

    public void backTopFragment() {
        if (fragments != null && fragments.size() > 1) {
            removeContent();
            backTopFragment();
        }
    }

    /**
     * set current fragment.
     */
    private void setFragment() {
        if (fragments != null && fragments.size() > 0) {
            fragment = fragments.get(fragments.size() - 1);
        } else {
            fragment = null;
        }
    }

    /**
     * get the current fragment.
     *
     * @return current fragment
     */
    public BaseFragment getFirstFragment() {
        return fragment;
    }

    /**
     * get amount of fragment.
     *
     * @return amount of fragment
     */
    public int getFragmentNum() {
        return fragments != null ? fragments.size() : 0;
    }

    /**
     * clear fragment list
     */
    protected void clearFragments() {
        if (fragments != null) {
            fragments.clear();
        }
    }

    /**
     * remove previous fragment
     */
    private void removePrevious() {
        if (fragments != null && fragments.size() > 0) {
            fragments.remove(fragments.size() - 1);
        }
    }

    /**
     * init fragment list.
     */
    private void initFragments() {
        if (fragments == null) {
            fragments = new ArrayList<>();
        }
    }

    /**
     * remove current fragment and back to front fragment.
     */
    public void removeContent() {
        removePrevious();
        setFragment();

        getSupportFragmentManager().popBackStackImmediate();
    }

    /**
     * remove all fragment from back stack.
     */
    protected void removeAllStackFragment() {
        clearFragments();
        setFragment();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    /**
     * 跳转Activity
     * skip Another Activity
     *
     * @param activity
     * @param cls
     */
    public static void skipAnotherActivity(Activity activity,
                                           Class<? extends Activity> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
        activity.finish();
    }


    /**
     * 退出应用
     * called while exit app.
     */
    public void exitLogic() {
        ActivityStackManager.getInstance().popAllActivity();//remove all activity.
        removeAllStackFragment();
        System.exit(0);//system exit.
    }

    /**
     * [是否设置沉浸状态栏]
     *
     * @param statusBar
     */
    public void setStatusBar(boolean statusBar) {
        isStatusBar = statusBar;
    }

    /**
     * [是否设置全屏]
     *
     * @param fullScreen
     */
    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
    }

    /**
     * [是否设置旋转屏幕]
     *
     * @param screenRoate
     */
    public void setScreenRoate(boolean screenRoate) {
        isScreenRoate = screenRoate;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "--->onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "--->onResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(TAG, "--->onRestart()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "--->onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "--->onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "--->onDestroy()");
        ActivityStackManager.getInstance().popActivity(this);
    }

    //返回键返回事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}

