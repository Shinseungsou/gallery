package com.jfsiot.hsgallery.app.model.unique;

import com.jfsiot.hsgallery.app.model.UseLog;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
  * Created by SSS on 2015-08-06.
  */
@NoArgsConstructor
@AllArgsConstructor(suppressConstructorProperties = true)
public class CurrentUseLog {
    @Getter @Setter private UseLog useLog;

    private static CurrentUseLog instance = null;
    public static synchronized CurrentUseLog getInstance(){
        if(CurrentUseLog.instance == null) CurrentUseLog.instance = new CurrentUseLog();
        return CurrentUseLog.instance;
    }
}
