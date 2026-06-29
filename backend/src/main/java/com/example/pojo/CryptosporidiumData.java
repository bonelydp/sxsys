package com.example.pojo;



import jakarta.persistence.*;

@Entity
@Table(name = "sample")
public class CryptosporidiumData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String host;

    @Column(nullable = false, length = 100)
    private String province;

    @Column(length = 100)
    private String city;

    @Column(nullable = false)
    private int year;

    @Column(nullable = false)
    private int sampleSize;

    @Column(nullable = false)
    private int cryptosporidiumQuantity;

    @Column(nullable = false)
    private double infectionRate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSampleSize() {
        return sampleSize;
    }

    public void setSampleSize(int sampleSize) {
        this.sampleSize = sampleSize;
    }

    public int getCryptosporidiumQuantity() {
        return cryptosporidiumQuantity;
    }

    public void setCryptosporidiumQuantity(int cryptosporidiumQuantity) {
        this.cryptosporidiumQuantity = cryptosporidiumQuantity;
    }

    public double getInfectionRate() {
        return infectionRate;
    }

    public void setInfectionRate(double infectionRate) {
        this.infectionRate = infectionRate;
    }
}