package com.example.android.project1;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.fragment.app.DialogFragment;

import java.util.Locale;

public class LanguageDialog extends DialogFragment {
    public LanguageDialog() {}

    private final CharSequence[] langs = {"Русский", "Английский"};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Выберите язык")
                .setItems(langs, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0) {
                            Resources res = getResources();
                            DisplayMetrics dm = res.getDisplayMetrics();
                            android.content.res.Configuration conf = res.getConfiguration();
                            conf.locale = new Locale("ru");
                            res.updateConfiguration(conf, dm);

                        } else if (which == 1) {
                            Resources res = getResources();
                            DisplayMetrics dm = res.getDisplayMetrics();
                            android.content.res.Configuration conf = res.getConfiguration();
                            conf.locale = new Locale("en");
                            res.updateConfiguration(conf, dm);
                        }
                    }
                });
        return builder.create();
    }

    @Override
    public void onStop() {
        super.onStop();
        Intent reload = new Intent(getContext(), SettingsActivity.class);
        startActivity(reload);
    }

}
