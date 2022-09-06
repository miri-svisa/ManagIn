package com.example.managein;

import android.content.Intent;

public class NewInvoice implements Command{
    public void execute(android.content.Context a){
        HelperUtil.isCameraActivityBackPressed = false;
        Intent intent = new Intent(a,Camera.class);
        a.startActivity(intent);
    }
}
