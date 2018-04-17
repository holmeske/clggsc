//package com.sc.clgg.util;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.content.SharedPreferences.Editor;
//
//import com.sc.clgg.application.App;
//import com.sc.clgg.bean.UserInfoBean;
//import com.sc.clgg.config.Constants;
//
//public class ConfigUtil {
//    private SharedPreferences sp;
//    private Editor editor;
//
//    public ConfigUtil() {
//        super();
//        sp = App.getInstance().getSharedPreferences("user_info2", Context.MODE_PRIVATE);
//        editor = sp.edit();
//    }
//
//    public ConfigUtil(Context context) {
//        super();
//        sp = context.getSharedPreferences("user_info2", Context.MODE_PRIVATE);
//        editor = sp.edit();
//    }
//
//    public String getUserType() {
//        return sp.getString(Constants.USERTYPE, "");
//    }
//
//    public Editor getEditor() {
//        return editor;
//    }
//
//    public void setEditor(Editor editor) {
//        this.editor = editor;
//    }
//
//    public String getEmail() {
//        return sp.getString(Constants.EMALI, "");
//    }
//
//    public void setEmail(String email) {
//        editor.putString(Constants.EMALI, email);
//        editor.apply();
//    }
//
//    public String getGender() {
//        return sp.getString(Constants.GENDER, "");
//    }
//
//    public void setGender(String gender) {
//        editor.putString(Constants.GENDER, gender);
//        editor.apply();
//    }
//
//
//    public String getMobile() {
//        return sp.getString(Constants.MOBILE, "");
//    }
//
//    public void setMobile(String mobile) {
//        editor.putString(Constants.MOBILE, mobile);
//        editor.apply();
//    }
//
//    public String getPassword() {
//        return sp.getString(Constants.PASSWORD, "");
//    }
//
//    public void setPassword(String password) {
//        editor.putString(Constants.PASSWORD, password);
//        editor.apply();
//    }
//
//    public SharedPreferences getSp() {
//        return sp;
//    }
//
//    public void setSp(SharedPreferences sp) {
//        this.sp = sp;
//    }
//
//    public String getUserid() {
//        return sp.getString(Constants.USERID, "");
//    }
//
//    public void setUserid(String userid) {
//        editor.putString(Constants.USERID, userid);
//        editor.apply();
//    }
//
//    public String getUsername() {
//        return sp.getString(Constants.USERNAME, "");
//    }
//
//    public void setUsername(String username) {
//        editor.putString(Constants.USERNAME, username);
//        editor.apply();
//    }
//
//    public boolean setUserInfo(UserInfoBean bean) {
//        if (null != bean) {
//            editor.putString(Constants.USERID, bean.getUserCode());
//            editor.putString(Constants.USERNAME, bean.getUserName());
//            editor.putString(Constants.MOBILE, bean.getPersonalPhone());
//            editor.putString(Constants.USERTYPE, bean.getUserType());
//            editor.commit();
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public void reset() {
//        setUserid("");
//        setUsername("");
//        setMobile("");
//        setPassword("");
//    }
//
//}
