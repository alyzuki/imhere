package com.here.zuki.imhere.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * Created by zuki on 3/8/17.
 */
public class Config {

    private Properties configuration;
    File path = null;
    private String configurationFile = null;

    public Config() {
        configuration = new Properties();
    }

    public boolean load() {
        boolean retval = false;

        try {
            configuration.load(new FileInputStream( new File(path, this.configurationFile)));
            retval = true;
        } catch (IOException e) {
            System.out.println("Configuration error: " + e.getMessage());
        }

        return retval;
    }

    public boolean store() {
        boolean retval = false;

        try {

            configuration.store(new FileOutputStream( new File(path, this.configurationFile)), null);
            retval = true;
        } catch (IOException e) {
            System.out.println("Configuration error: " + e.getMessage());
        }

        return retval;
    }

    public void setConfigFileName(String name, File path)
    {
        this.configurationFile = name;
        this.path = path;
    }

    public int configGetIntValue(String key, int defautValue)
    {
        String value;
        try
        {
            value = configuration.getProperty(key);
            return Integer.valueOf(value);
        }catch (Exception e)
        {
            return  defautValue;
        }

    }

    public boolean configGetBooleanValue(String key, boolean defautValue)
    {
        String value;
        try
        {
            value = configuration.getProperty(key);
            return Boolean.valueOf(value);
        }catch (Exception e)
        {
            return  defautValue;
        }

    }

    public float configGetFloatValue(String key, float defautValue)
    {
        String value;
        try
        {
            value = configuration.getProperty(key);
            return Float.valueOf(value);
        }catch (Exception e)
        {
            return  defautValue;
        }

    }

    public double configGetDoubleValue(String key, double defautValue)
    {
        String value;
        try
        {
            value = configuration.getProperty(key);
            return  Double.valueOf(value);
        }catch (Exception e)
        {
            return  defautValue;
        }

    }

    public String configGetStringValue(String key, String defautValue)
    {
        String value;
        try
        {
            value = configuration.getProperty(key);
            return  value;
        }catch (Exception e)
        {
            return  defautValue;
        }

    }

    public void configSetStringValue(String key, String value)
    {
        setValue(key,value);
    }

    public void configSetIntValue(String key, int value)
    {
        setValue(key,String.valueOf(value));
    }

    public void configSetFloatValue(String key, float value)
    {
        setValue(key,String.valueOf(value));
    }
    public void configSetBooleanValue(String key, boolean value)
    {
        setValue(key,String.valueOf(value));
    }

    public void configSetDoubleValue(String key, double value)
    {
        setValue(key,String.valueOf(value));
    }

    private void setValue(String key, String value)
    {
        configuration.setProperty(key, value);
    }
}

