package net.lililearn.baseproject.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import net.lililearn.baseproject.R;
import net.lililearn.baseproject.base.BaseActivity;
import net.lililearn.baseproject.ui.fragment.HomeFragment;

public class MainActivity extends BaseActivity {
    private HomeFragment mHomeFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        removeAllStackFragment();
        mHomeFragment = new HomeFragment();
        replaceContent(mHomeFragment, false);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setEvent() {

    }
}
