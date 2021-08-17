package com.example.axestore.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.axestore.R;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

public class EmailTokenPopupActivity {
    boolean isValid = false;

    public boolean tokenValidation(final View view){
        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        final View popupView = inflater.inflate(R.layout.activity_email_token_popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        EditText tfToken = popupView.findViewById(R.id.tf_token);
        Button btSubmitToken = popupView.findViewById(R.id.bt_submit_token);
        btSubmitToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isValid = doValidate();
                popupWindow.dismiss();

            }
        });
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //Close the window when clicked
                popupWindow.dismiss();
                return true;
            }
        });
        return isValid;
    }

    private boolean doValidate(){
        return true;
    }

}