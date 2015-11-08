package com.jfsiot.hsgallery.util.data.db;

import android.content.Context;
import android.database.Cursor;
import android.text.format.DateUtils;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jfsiot.hsgallery.R;
import com.jfsiot.hsgallery.app.AppConst;
import com.jfsiot.hsgallery.app.model.ImageData;
import com.jfsiot.hsgallery.app.model.UseLog;
import com.jfsiot.hsgallery.util.data.db.table.DBOpenHelper;
import com.jfsiot.hsgallery.util.data.db.table.Tables;
import com.jfsiot.hsgallery.util.data.image.FileController;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

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


    public String exportLog() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat(AppConst.Format.DATE_LINEAR, Locale.KOREAN);
        Timber.d("format : %s", format.format(date));
        String filePath = String.format("siot_backup_%s.csv", format.format(date));
        this.backupDatabaseCSV(filePath);
        return filePath;
    }

    public void deleteLog(Context context) {
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

    private Boolean backupDatabaseCSV(String outFileName) {
        Timber.d("backupDatabaseCSV");
        Boolean returnCode = false;
        String csvHeader = "";
        String csvValues = "";
        for (int i = 0; i < Tables.UseLog.COLUMN.values().length; i++) {
            if (csvHeader.length() > 0) {
                csvHeader += ",";
            }
            csvHeader += "\"" + Tables.UseLog.COLUMN.values()[i] + "\"";
        }

        csvHeader += "\n";
        Timber.d("header=" + csvHeader);
        DBOpenHelper dbAdapter = new DBOpenHelper(context);
        new FileController().makeDir(AppConst.BACKUP_DIR_PATH);

        dbAdapter.open();
        try {
            File outFile = new File(AppConst.BACKUP_DIR_PATH, outFileName);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(outFile), AppConst.Format.UTF);
            FileWriter fileWriter = new FileWriter(outFile, false);
            BufferedWriter out = new BufferedWriter(fileWriter);
            Cursor cursor = dbAdapter.getAllColumnsUseLog();
            if (cursor != null) {
                out.write(csvHeader);
                while (cursor.moveToNext()) {
                    csvValues = "";
                    for(int j = 0; j < Tables.UseLog.COLUMN.values().length; j++){
                        csvValues += cursor.getString(j) + ",";
                    }
                    csvValues += "\n";
                    Timber.d("output : %s", new String(csvValues.getBytes(), "UTF-8"));
                    out.write(csvValues);
                }
                cursor.close();
            }
            out.close();
            returnCode = true;
            Toast.makeText(context, R.string.success_export, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            returnCode = false;
            Timber.d("IOException: " + e.getMessage());
        }
        dbAdapter.close();
        return returnCode;
    }
}
