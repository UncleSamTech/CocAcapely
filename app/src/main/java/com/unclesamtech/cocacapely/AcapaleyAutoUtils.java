package com.unclesamtech.cocacapely;

import android.content.Context;
import android.widget.Toast;

public class AcapaleyAutoUtils {

    public Toast getToast(Context c, String message, int len){
        Toast t = Toast.makeText(c,message,len);
        return t;
    }
}
