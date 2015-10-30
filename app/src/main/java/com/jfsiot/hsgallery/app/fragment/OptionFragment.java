package com.jfsiot.hsgallery.app.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jfsiot.hsgallery.R;
import com.jfsiot.hsgallery.app.AppConfig;
import com.jfsiot.hsgallery.util.data.db.UseLogManager;

import butterknife.ButterKnife;
import butterknife.InjectView;
import timber.log.Timber;

public class OptionFragment extends Fragment implements View.OnClickListener{

    @InjectView(R.id.auth) protected RelativeLayout authLayout;
    @InjectView(R.id.password_edit) protected EditText password;
    @InjectView(R.id.pass_conrifm) protected Button confirmButton;

    @InjectView(R.id.user_controller) protected LinearLayout userController;
    @InjectView(R.id.delete_log) protected Button deleteBtn;
    @InjectView(R.id.export_log) protected Button exportBtn;
    @InjectView(R.id.exit) protected Button exitBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_option, container, false);

        ButterKnife.inject(this, view);
        confirmButton.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        exportBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == deleteBtn.getId()){
            UseLogManager.getInstance().deleteLog();
        }else if(v.getId() == exitBtn.getId()){
            AppConfig.Option.SUPER_USER = false;
            authLayout.setVisibility(View.VISIBLE);
            userController.setVisibility(View.GONE);
        }else if(v.getId() == exportBtn.getId()){
            UseLogManager.getInstance().exportLog();
        }else if(v.getId() == confirmButton.getId()){
            Timber.d("pass : %s type : %s", password.getText(), password.getText().getClass());
            if(password.getText().toString().equals("4571")){
                AppConfig.Option.SUPER_USER = true;
                authLayout.setVisibility(View.GONE);
                userController.setVisibility(View.VISIBLE);
                password.setText("");
            }else{
                Toast.makeText(this.getActivity(), "Wrong password", Toast.LENGTH_LONG).show();
            }
        }
    }
}
