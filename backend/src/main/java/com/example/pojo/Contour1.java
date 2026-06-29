package com.example.pojo;

public class Contour1 {
    private Integer id; // ID
    private String name; // 轮廓图名称
    private String img; // 轮廓图图片

    // 无参构造函数
    public Contour1() {
    }

    // 带所有参数的构造函数
    public Contour1(Integer id, String name, String category, String img) {
        this.id = id;
        this.name = name;

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



    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    // toString方法，用于输出实体类信息
    @Override
    public String toString() {
        return "Contour1{" +
                "id=" + id +
                ", name='" + name + '\'' +

                ", img='" + img + '\'' +
                '}';
    }
}