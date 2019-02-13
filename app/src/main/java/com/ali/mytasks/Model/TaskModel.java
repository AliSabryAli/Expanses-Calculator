package com.ali.mytasks.Model;

public class TaskModel {
    private String name;
    private String no;
    private String dateItemAdded;
    private int id;

    public TaskModel() {
    }

    public TaskModel(String name, String no, String dateItemAdded, int id) {
        this.name = name;
        this.no = no;
        this.dateItemAdded = dateItemAdded;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDateItemAdded() {
        return dateItemAdded;
    }

    public void setDateItemAdded(String dateItemAdded) {
        this.dateItemAdded = dateItemAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
