package com.example.currenciesapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

/**
 * Error dialog that displays a message.
 */
public class ErrorDialogFragment extends DialogFragment {

    private final String TAG = "ErrorDialogFragment";
    private final String ERROR_MSG = "error msg";

    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_error_dialog, container, false);
        v.findViewById(R.id.button).setOnClickListener(v1 -> dismiss());
        textView = v.findViewById(R.id.text);
        textView.setText(getArguments().getString(ERROR_MSG));
        return v;
    }

    public void displayDialog(String errorText, FragmentManager fm) {
        Bundle bundle = new Bundle();
        bundle.putString(ERROR_MSG, errorText);
        setArguments(bundle);
        show(fm, TAG);
    }
}
