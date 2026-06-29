package com.example.pojo;

public class Contour {
    private Integer id; // ID
    private String name; // 轮廓图名称
    private String category; // 轮廓图分类
    private String img; // 轮廓图图片

    // 无参构造函数
    public Contour() {
    }

    // 带所有参数的构造函数
    public Contour(Integer id, String name, String category, String img) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.img = img;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    // toString方法，用于输出实体类信息
    @Override
    public String toString() {
        return "Contour{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", img='" + img + '\'' +
                '}';
    }
}