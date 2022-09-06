package com.example.managein;

import android.content.Intent;

public class SearchInvoice implements Command {
    public void execute(android.content.Context a){
        Intent intent = new Intent(a, SearchBy.class);
        a.startActivity(intent);
    }

}
