/*
 * Copyright 2012 Project UNKONW.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package jp.techie.plain.framework.util;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.FileConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

/**
 * プロパティ保持クラス。
 * 
 * @author ykhr
 */
public class PropertyUtils {

    /** アプリケーション設定 */
    private static FileConfiguration config;
    static {
        try {
            String propertyName = System.getProperty("applicationProperty", "application.properties");
            config = new PropertiesConfiguration(propertyName);
            config.setReloadingStrategy(new FileChangedReloadingStrategy());
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * private constructor
     */
    private PropertyUtils() {
    }

    /**
     * boolean値でプロパティを取得する
     * 
     * @param key
     *            キー
     * @return 値
     */
    public static boolean getBoolean(String key) {
        return config.getBoolean(key);
    }

    /**
     * boolean値でプロパティを取得する
     * 
     * @param key
     *            キー
     * @param defaultValue
     *            デフォルト値
     * @return 値
     */
    public static boolean getBoolean(String key, boolean defaultValue) {
        return config.getBoolean(key, defaultValue);
    }

    /**
     * boolean値でプロパティを取得する
     * 
     * @param key
     *            キー
     * @param defaultValue
     *            デフォルト値
     * @return 値
     */
    public static Boolean getBoolean(String key, Boolean defaultValue) {
        return config.getBoolean(key, defaultValue);
    }

    /**
     * double値でプロパティ値を取得する
     * 
     * @param key
     *            キー
     * @return 値
     */
    public static double getDouble(String key) {
        return config.getDouble(key);
    }

    /**
     * double値でプロパティ値を取得する
     * 
     * @param key
     *            キー
     * @param defaultValue
     *            デフォルト値
     * @return 値
     */
    public static double getDouble(String key, double defaultValue) {
        return config.getDouble(key, defaultValue);
    }

    /**
     * double値でプロパティ値を取得する
     * 
     * @param key
     *            キー
     * @param defaultValue
     *            デフォルト値
     * @return 値
     */
    public static Double getDouble(String key, Double defaultValue) {
        return config.getDouble(key, defaultValue);
    }

    /**
     * int値でプロパティ値を取得する
     * 
     * @param key
     *            キー
     * @return 値
     */
    public static int getInt(String key) {
        return config.getInt(key);
    }

    /**
     * int値でプロパティ値を取得する
     * 
     * @param key
     *            キー
     * @param defaultValue
     * @return 値
     */
    public static int getInt(String key, int defaultValue) {
        return config.getInt(key, defaultValue);
    }

    /**
     * int値でプロパティ値を取得する
     * 
     * @param key
     *            キー
     * @param defaultValue
     * @return 値
     */
    public static Integer getInteger(String key, Integer defaultValue) {
        return config.getInt(key, defaultValue);
    }

    /**
     * long値でプロパティ値を取得する
     * 
     * @param key
     *            キー
     * @return 値
     */
    public static long getLong(String key) {
        return config.getLong(key);
    }

    /**
     * long値でプロパティ値を取得する
     * 
     * @param key
     *            キー
     * @param defaultValue
     * @return 値
     */
    public static long getLong(String key, long defaultValue) {
        return config.getLong(key, defaultValue);
    }

    /**
     * long値でプロパティ値を取得する
     * 
     * @param key
     *            キー
     * @param defaultValue
     * @return 値
     */
    public static Long getLong(String key, Long defaultValue) {
        return config.getLong(key, defaultValue);
    }

    /**
     * String値でプロパティ値を取得する
     * 
     * @param key
     *            キー
     * @return 値
     */
    public static String getString(String key) {
        return config.getString(key);
    }

    /**
     * String値でプロパティ値を取得する
     * 
     * @param key
     *            キー
     * @param defaultValue
     * @return 値
     */
    public static String getString(String key, String defaultValue) {
        return config.getString(key, defaultValue);
    }

    /**
     * String[]値でプロパティ値を取得する
     * 
     * @param key
     *            キー
     * @param defaultValue
     * @return 値
     */
    public static String[] getStringArray(String key) {
        return config.getStringArray(key);
    }

}
