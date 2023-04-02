package com.sortscript.serfix.modela;

public class AppliedJob {

    String AdminPhone, UserKey, JobKey, Key;

    public AppliedJob(){

    }

    public AppliedJob(String adminPhone, String userKey, String jobKey, String key) {
        AdminPhone = adminPhone;
        UserKey = userKey;
        JobKey = jobKey;
        Key = key;
    }

    public String getAdminPhone() {
        return AdminPhone;
    }

    public void setAdminPhone(String adminPhone) {
        AdminPhone = adminPhone;
    }

    public String getUserKey() {
        return UserKey;
    }

    public void setUserKey(String userKey) {
        UserKey = userKey;
    }

    public String getJobKey() {
        return JobKey;
    }

    public void setJobKey(String jobKey) {
        JobKey = jobKey;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
