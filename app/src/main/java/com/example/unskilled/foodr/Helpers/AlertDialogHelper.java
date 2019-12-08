package com.example.unskilled.foodr.Helpers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertDialogHelper {

    public static void errorDialog(Context context, String errorTitle, String errorMessage){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setCancelable(true);

        builder.setTitle(errorTitle);
        builder.setMessage(errorMessage);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.show();
    }

}
