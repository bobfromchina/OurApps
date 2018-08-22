/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lovely3x.common.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewDebug;
import android.widget.Checkable;
import android.widget.TextView;

/**
 * An extension to {@link TextView} that supports the {@link Checkable}
 * interface and displays.
 * <p/>
 * This is useful when used in a {@link android.widget.ListView ListView} where
 * the {@link android.widget.ListView#setChoiceMode(int) setChoiceMode} has
 * been set to something other than
 * {@link android.widget.ListView#CHOICE_MODE_NONE CHOICE_MODE_NONE}.
 *
 * @attr ref android.R.styleable#CheckedTextView_checked
 * @attr ref android.R.styleable#CheckedTextView_checkMark
 */
public class CheckedTextView extends TextView implements Checkable {

    private boolean mChecked;

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    public CheckedTextView(Context context) {
        this(context, null);
    }

    public CheckedTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckedTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArr = context.obtainStyledAttributes(attrs, new int[]{android.R.attr.checked});
            mChecked = typedArr.getBoolean(0, false);
            typedArr.recycle();
        }
        setChecked(mChecked);
    }


    public void toggle() {
        setChecked(!mChecked);
    }

    @ViewDebug.ExportedProperty
    public boolean isChecked() {
        return mChecked;
    }

    /**
     * Sets the checked state of this view.
     *
     * @param checked {@code true} set the state to checked, {@code false} to
     *                uncheck
     */
    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
        }
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }
    
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return CheckedTextView.class.getName();
    }

}
