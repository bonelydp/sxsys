package com.example.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Table;

@Table(name = "teamintroduction")
public class TeamIntroduction {
    private Integer id; // 团队成员ID
    private String name; // 团队成员姓名
//    @Column(name = "avatar_url")
    private String avatarUrl; // 团队成员头像URL
    private String introduction; // 团队成员具体介绍

    private Category category; // 新增的类别字段
    public enum Category {
        admin,
        worker
    }
    // 无参构造函数
    public TeamIntroduction() {
    }

    // 带所有参数的构造函数
    public TeamIntroduction(Integer id, String name, String avatarUrl, String introduction,Category category) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.introduction = introduction;
        this.category=category;
    }

    // Getter和Setter方法
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    // toString方法，用于输出实体类信息
    @Override
    public String toString() {
        return "TeamIntroduction{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", introduction='" + introduction + '\'' +
                ", category=" + category +
                '}';
    }
}