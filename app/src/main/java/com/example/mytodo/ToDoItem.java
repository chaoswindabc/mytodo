package com.example.mytodo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ToDoItem {
    private long id;
    private String title;
//    private String content;
    private Date time;
//    private String importance; // 重要程度，例如 "重要" 或 "不重要"
    private boolean isCompleted; // 标记事项是否已完成

    // 构造函数
//    public ToDoItem(String title, String content, Date time, String importance) {
//        this.title = title;
////        this.content = content;
//        this.time = time;
////        this.importance = importance;
//        this.isCompleted = false;
//    }

    public ToDoItem(String title, Date time) {
        this.title = title;
//        this.content = content;
        this.time = time;
//        this.importance = importance;
        this.isCompleted = false;
    }

    public ToDoItem() {

    }

    // Getters 和 Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

//    public String getContent() {
//        return content;
//    }

//    public void setContent(String content) {
//        this.content = content;
//    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

//    public String getImportance() {
//        return importance;
//    }

//    public void setImportance(String importance) {
//        this.importance = importance;
//    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    // 重写 toString 方法以在日志中更友好地显示 ToDoItem 对象
    @Override
    public String toString() {
        return "ToDoItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
//                ", content='" + content + '\'' +
                ", time=" + time +
//                ", importance='" + importance + '\'' +
                ", isCompleted=" + isCompleted +
                '}';
    }

//    public static List<ToDoItem> ITEMS = new ArrayList<>();
//
//    // 返回所有待办事项的列表
//    public static List<ToDoItem> getItems() {
//        return ITEMS;
//    }
}
