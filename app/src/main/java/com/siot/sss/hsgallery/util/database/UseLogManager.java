package com.siot.sss.hsgallery.util.database;

import android.content.Context;

import com.siot.sss.hsgallery.app.model.UseLog;
import com.siot.sss.hsgallery.app.model.unique.ImageShow;
import com.siot.sss.hsgallery.util.database.table.DBOpenHelper;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by SSS on 2015-08-09.
 */
public class UseLogManager {
    private static UseLogManager instance;

    public static synchronized UseLogManager getInstance(){
        if(instance == null) return UseLogManager.instance  = new UseLogManager();
        return UseLogManager.instance;
    }

    public void addReadLog(Context context){
            DBOpenHelper helper = new DBOpenHelper(context);
            helper.open();
            Calendar c = Calendar.getInstance();
            Date date = new Date(c.get(Calendar.YEAR)-1900, c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE));
            helper.insertColumnUseLog(
                new UseLog(
                    android.text.format.DateFormat.format("yyyy/MM/dd hh:mm", date).toString(),
                    ImageShow.getInstance().getImageData().displayName,
                    ImageShow.getInstance().getImageData().id,
                    UseLog.getType(UseLog.Type.READ)
                ));
            helper.close();
    }
}
