package com.piemicrosystems.hoodcop.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.piemicrosystems.hoodcop.R;
import com.piemicrosystems.hoodcop.object.User;

/**
 * Created by aangjnr on 15/11/2017.
 */

public class EditMyProfileActivity extends BaseActivity {

    User user;
    TextView rank;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_profile);


    }
}
