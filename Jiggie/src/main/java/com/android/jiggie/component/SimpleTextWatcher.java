package com.android.jiggie.component;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by rangg on 21/12/2015.
 */
public class SimpleTextWatcher implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }
    @Override
    public void afterTextChanged(Editable s) { }
}
