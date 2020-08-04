package com.unclesamtech.cocacapely;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;

public class AcapelyForgotPasswordAdapter extends DialogFragment implements TextInputEditText.OnEditorActionListener   {

    AlertDialog.Builder builder;
    TextInputEditText appEdt;
    private AcapelyForgotPasswordAdapter.RhemaHiveForgotPasswordListener rhemaHiveForgotPasswordListener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v  = inflater.inflate(R.layout.forgot_password,null);
        appEdt = v.findViewById(R.id.email_app_edt_forg_pass);

        appEdt.setOnEditorActionListener(this);


        builder.setView(v).setPositiveButton("Reset Password", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rhemaHiveForgotPasswordListener.onDialogPositiveClick(appEdt.getText().toString().trim());
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rhemaHiveForgotPasswordListener.onDialogNegativeClick(AcapelyForgotPasswordAdapter.this);

            }
        });
        return builder.create();
    }




    @Override
    public void onAttach(@NonNull Context context) {
        try{
            rhemaHiveForgotPasswordListener = (AcapelyForgotPasswordAdapter.RhemaHiveForgotPasswordListener) context;
        }catch(ClassCastException e){

            throw new ClassCastException(context.toString()
                    + " must implement Forgot Password Listener");
        }
        super.onAttach(context);
    }

    public Button getBtn(int id){
        View v = new View(getActivity());
        Button btn  =  v.findViewById(id);
        return btn;
    }

    public AppCompatEditText getAppEdt(int id){
        View v = new View(getActivity());
        AppCompatEditText appEdt = v.findViewById(id);
        return appEdt;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return false;
    }

    public interface RhemaHiveForgotPasswordListener{
        void onDialogPositiveClick(String email);
        void onDialogNegativeClick(DialogFragment dialogFragment);
    }
}
