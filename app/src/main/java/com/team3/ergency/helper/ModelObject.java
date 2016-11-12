package com.team3.ergency.helper;

import com.team3.ergency.R;

/**
 * Created by knnguy on 11/12/16.
 */

public enum ModelObject {
    introOne(R.string.introOne, R.layout.intro_page_one),
    introTwo(R.string.introTwo, R.layout.intro_page_two),
    introThree(R.string.introThree, R.layout.intro_page_three);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }
}
