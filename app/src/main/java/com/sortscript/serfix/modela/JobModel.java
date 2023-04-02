package com.sortscript.serfix.modela;

public class JobModel {
    private String Title, Description, Salary,LastDate, DatePosted,
            Status, JobType , Location, PostedBy, ContactNo,Key, ImageUrl;

    public Boolean getApplied() {
        return isApplied;
    }

    public void setApplied(Boolean applied) {
        isApplied = applied;
    }

    private Boolean isApplied;

    public JobModel(){}

    public JobModel(String title, String description, String salary,
                    String lastDate, String datePosted, String status,
                    String jobType, String location, String postedBy,
                    String contactNo, String key, String imageUrl) {
        Title = title;
        Description = description;
        Salary = salary;
        LastDate = lastDate;
        DatePosted = datePosted;
        Status = status;
        JobType = jobType;
        Location = location;
        PostedBy = postedBy;
        ContactNo = contactNo;
        Key = key;
        ImageUrl = imageUrl;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getSalary() {
        return Salary;
    }

    public void setSalary(String salary) {
        Salary = salary;
    }

    public String getLastDate() {
        return LastDate;
    }

    public void setLastDate(String lastDate) {
        LastDate = lastDate;
    }

    public String getDatePosted() {
        return DatePosted;
    }

    public void setDatePosted(String datePosted) {
        DatePosted = datePosted;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getJobType() {
        return JobType;
    }

    public void setJobType(String jobType) {
        JobType = jobType;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getPostedBy() {
        return PostedBy;
    }

    public void setPostedBy(String postedBy) {
        PostedBy = postedBy;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "JobModel{" +
                "Title='" + Title + '\'' +
                ", Description='" + Description + '\'' +
                ", Salary='" + Salary + '\'' +
                ", LastDate='" + LastDate + '\'' +
                ", DatePosted='" + DatePosted + '\'' +
                ", Status='" + Status + '\'' +
                ", JobType='" + JobType + '\'' +
                ", Location='" + Location + '\'' +
                ", PostedBy='" + PostedBy + '\'' +
                ", ContactNo='" + ContactNo + '\'' +
                ", Key='" + Key + '\'' +
                ", ImageUrl='" + ImageUrl + '\'' +
                '}';
    }
}
