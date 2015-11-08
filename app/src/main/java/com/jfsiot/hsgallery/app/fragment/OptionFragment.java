package com.jfsiot.hsgallery.app.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jfsiot.hsgallery.R;
import com.jfsiot.hsgallery.app.AppConfig;
import com.jfsiot.hsgallery.app.model.Mail;
import com.jfsiot.hsgallery.util.data.db.UseLogManager;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.view.ViewObservable;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class OptionFragment extends Fragment implements View.OnClickListener{

    @InjectView(R.id.auth) protected RelativeLayout authLayout;
    @InjectView(R.id.password_edit) protected EditText password;
    @InjectView(R.id.pass_conrifm) protected Button confirmButton;

    @InjectView(R.id.user_controller) protected LinearLayout userController;
    @InjectView(R.id.delete_log) protected Button deleteBtn;
    @InjectView(R.id.export_log) protected Button exportBtn;
    @InjectView(R.id.send_email) protected Button buttonEmail;
    @InjectView(R.id.exit) protected Button exitBtn;

    private CompositeSubscription subscription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_option, container, false);

        ButterKnife.inject(this, view);
        subscription = new CompositeSubscription();
        confirmButton.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);
        exportBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        password.setOnKeyListener((view, keycode, action) -> {
            Timber.d("key down %s", action);
            if(action.getAction() == KeyEvent.ACTION_DOWN && (action.getKeyCode() == KeyEvent.KEYCODE_ENTER || action.getKeyCode() == KeyEvent.KEYCODE_ENDCALL)) {
                this.passCheck();
                return true;
            }
            return false;
        });

        subscription.add(
            ViewObservable.clicks(this.buttonEmail)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    event -> {
                        this.sendEmail();
                    }, error ->
                        error.printStackTrace()
                )
        );
    }
    public void sendEmails() {
        try {
            Mail sender = new Mail("wwww43211@gmail.com", "password");
            sender.sendMail("This is Subject",
                "This is Body");
        } catch (Exception e) {
            Timber.e("SendMail", e.getMessage(), e);
        }
//        //
//        EditText childNameBox = (EditText) getAcfindViewById(R.id.childNameBox);
//        EditText parentNameBox = (EditText) findViewById(R.id.parentNameBox);
//        EditText phoneNumberBox = (EditText) findViewById(R.id.phoneNumberBox);
//        EditText ageBox = (EditText) findViewById(R.id.ageBox);
//        EditText notesBox = (EditText) findViewById(R.id.notesBox);
//        EditText colorsBox = (EditText) findViewById(R.id.colorsBox);
//        EditText dateBox = (EditText) findViewById(R.id.dateBox);
//        EditText timeBox = (EditText) findViewById(R.id.timeBox);
//
//        childName = childNameBox.toString();
//        parentName = parentNameBox.toString();
//        childAge = ageBox.toString();
//        phoneNumber = phoneNumberBox.toString();
//        colorChoice = colorsBox.toString();
//        notesText = notesBox.toString();
//        dateDay = dateBox.toString();
//        timeDay = timeBox.toString();
//
//        emailCombined = childName + bN + parentName + bN + childAge + bN + phoneNumber + bN + colorChoice + bN + notesText + bN + dateDay + bN + timeDay;
//        Mail m = new Mail("emailaddress", "password");
//
//        String[] toArr = {"cistoran@live.com", "ivan017@gmail.com"};
//        m.setTo(toArr);
//        m.setFrom("ivan017@gmail.com");
//        m.setSubject("Party Booked");
//        m.setBody(emailCombined);
//
//        try {
//            //m.addAttachment("/sdcard/filelocation");
//
//            if(m.send()) {
//                Toast.makeText(PartyPlannerActivity.this, "Sent Email.", Toast.LENGTH_LONG).show();
//            } else {
//                Toast.makeText(PartyPlannerActivity.this, "Email was not sent.", Toast.LENGTH_LONG).show();
//            }
//        } catch(Exception e) {
//            //Toast.makeText(MailApp.this, "There was a problem sending the email.", Toast.LENGTH_LONG).show();
//            Log.e("PartyPlannerActivity", "Could not send email.", e);
//        }
    }
    public void sendEmail(){
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL  , new String[]{"wwww43211@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT   , "Message Body");
//        String targetFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "tmp" + File.separator + "test.txt";
//        Uri attachmentUri = Uri.parse(targetFilePath);
//        emailIntent.putExtra(android.content.Intent.EXTRA_STREAM, Uri.parse("file://" + attachmentUri));
        startActivity(emailIntent);
    }

    public void passCheck(){
        if(password.getText().toString().equals("4571")){
            AppConfig.Option.SUPER_USER = true;
            authLayout.setVisibility(View.GONE);
            userController.setVisibility(View.VISIBLE);
            password.setText("");
            ((InputMethodManager) this.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(this.password.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        }else{
            Toast.makeText(this.getActivity(), "Wrong password", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == deleteBtn.getId()){
            UseLogManager.getInstance().deleteLog(this.getActivity());
        }else if(v.getId() == exitBtn.getId()){
            AppConfig.Option.SUPER_USER = false;
            authLayout.setVisibility(View.VISIBLE);
            userController.setVisibility(View.GONE);
        }else if(v.getId() == exportBtn.getId()){
            UseLogManager.getInstance().exportLog();
        }else if(v.getId() == confirmButton.getId()){
            Timber.d("pass : %s type : %s", password.getText(), password.getText().getClass());
            this.passCheck();
        }
    }
}
