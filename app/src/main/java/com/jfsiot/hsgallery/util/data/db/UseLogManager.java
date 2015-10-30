package com.jfsiot.hsgallery.util.data.db;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jfsiot.hsgallery.R;
import com.jfsiot.hsgallery.app.model.ImageData;
import com.jfsiot.hsgallery.app.model.UseLog;
import com.jfsiot.hsgallery.util.data.db.table.DBOpenHelper;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by SSS on 2015-08-09.
 */
public class UseLogManager {
    private static UseLogManager instance;

    private Context context;

    public static synchronized UseLogManager getInstance(){
        if(instance == null) return UseLogManager.instance  = new UseLogManager();
        return UseLogManager.instance;
    }
    public String currentTime(){
        Calendar c = Calendar.getInstance();
        Date date = new Date(c.get(Calendar.YEAR)-1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));

        return android.text.format.DateFormat.format("yyyy/MM/dd hh:mm", date).toString();
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void addLog(ImageData imageData, UseLog.Type type){
        addLog(imageData, type, null);
    }
    public void addLog(ImageData imageData, String targetPath, UseLog.Type type){
        addLog(imageData, type, targetPath, null);
    }
    public void addLog(ImageData imageData, UseLog.Type type, String note){
        addLog(imageData, type, null, note);
    }
    public void addLog(ImageData imageData, UseLog.Type type, String targetPath, String note){
        this.addLog(
            UseLog.makeUseLog(imageData).setTypeByType(type).setTo_data(targetPath).setNote(note)
        );
    }
    public void addLog(ImageData imageData, String note, String share){
        this.addLog(imageData, UseLog.Type.SHARE, null, note, share);
    }

    public void addLog(ImageData imageData, UseLog.Type type, String targetPath, String note, String share){
        this.addLog(
            UseLog.makeUseLog(imageData).setTypeByType(type).setTo_data(targetPath).setNote(note).setShare(share)
        );
    }

    public void addLog(UseLog log){

        DBOpenHelper helper = new DBOpenHelper(context);
        helper.open();
        helper.insertColumnUseLog(
            log);
        helper.close();
    }

    public void addLogUpdate(ImageData imageData, UseLog.Type type, String targetPath){
        addLogUpdate(imageData, type, targetPath, null);
    }
    public void addLogUpdate(ImageData imageData, UseLog.Type type, String targetPath, String note){
        addLog(imageData, type, targetPath, note);
    }


    public void exportLog() {
    }

    public void deleteLog() {
        new MaterialDialog.Builder(context)
            .content(R.string.question_delete)
            .negativeText(R.string.cancel_normal)
            .positiveText(R.string.log_delete)
            .callback(new MaterialDialog.ButtonCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    super.onPositive(dialog);
                    DBOpenHelper helper = new DBOpenHelper(context);
                    helper.open();
                    helper.refreshUseLog();
                    helper.close();
                }

                @Override
                public void onNegative(MaterialDialog dialog) {
                    super.onNegative(dialog);
                }
            }).show();
    }
}
