package com.jfsiot.hsgallery.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by SSS on 2015-09-10.
 */
@Data
@AllArgsConstructor(suppressConstructorProperties = true)
public class SimpleItem {
    public Integer icon;
    public String name;

}
