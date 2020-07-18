package com.ops.dev.simple.services.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.ops.dev.simple.services.R;

public class CustomNumberPicker extends ConstraintLayout {

    private static final int DEFAULT_VALUE = 1;
    private static final int DEFAULT_MIN = 1;
    private static final int DEFAULT_MAX = 20;
    private int value = DEFAULT_VALUE;
    private int min = DEFAULT_MIN;
    private int max = DEFAULT_MAX;

    private TextView textView;
    private OnValueChangedListener valueChangedListener;

    public CustomNumberPicker(Context context) {
        super(context);
        init(context, null);
    }

    public CustomNumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomNumberPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.______widget_number_picker, this);
        findViewById(R.id.picker_button_down).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomNumberPicker.this.newVal(value - 1);
            }
        });

        findViewById(R.id.picker_button_up).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomNumberPicker.this.newVal(value + 1);
            }
        });

        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomNumberPicker,0,0);

        value = typedArray.getInteger(R.styleable.CustomNumberPicker_value, DEFAULT_VALUE);
        min = typedArray.getInteger(R.styleable.CustomNumberPicker_min, DEFAULT_MIN);
        max = typedArray.getInteger(R.styleable.CustomNumberPicker_max, DEFAULT_MAX);

        textView = findViewById(R.id.picker_text_number);
        textView.setText(String.valueOf(value));
    }

    private void newVal(int newValue) {
        if (newValue > max || newValue < min) {
            return;
        }

        this.value = newValue;
        textView.setText(String.valueOf(value));

        if (valueChangedListener != null) {
            valueChangedListener.onValueChanged(value);
        }
    }

    public int getValue() {
        return value;
    }

    public void setOnValueChangedListener(OnValueChangedListener valueChangedListener) {
        this.valueChangedListener = valueChangedListener;
    }

    public interface OnValueChangedListener {
        void onValueChanged(int newValue);
    }
}