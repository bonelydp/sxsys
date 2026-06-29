package com.example.Repository;

import com.example.pojo.SampleInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SampleInfoRepository extends JpaRepository<SampleInfo, Long> {
    Optional<SampleInfo> findBySampleId(String sampleId);

    @Query("SELECT DISTINCT s.collectionYear from SampleInfo s")
    List<Integer> getYear();

    @Query("SELECT DISTINCT s.hostSource from SampleInfo s")
    List<String> getHost();

    // 1.根据年份，宿主获取感染率
    @Query("SELECT s.province,SUM(s.cPositiveCases)*1.0/SUM(s.cTotalSamples)*100,SUM(s.cPositiveCases)*1.0/SUM(s.cTotalSamples)*100 FROM SampleInfo s WHERE s.collectionYear = :year AND s.hostSource = :host group by s.province")
    List<Object[]> findByYearAndHostWithInfectionRate(
            @Param("year") int year,
            @Param("host") String host);

    // 2.年份，宿主获取省份，样本，感染数，感染率
    @Query("SELECT s.province, SUM(s.cTotalSamples), SUM(s.cPositiveCases), SUM(s.cPositiveCases)*1.0/SUM(s.cTotalSamples)*100 FROM SampleInfo s WHERE s.collectionYear = :year AND s.hostSource = :host GROUP BY s.province")
    List<Object[]> findByYearAndHostWithDetails(@Param("year") int year, @Param("host") String host);
    // 3.根据省份、年份和宿主获取感染率
    @Query("SELECT s.province,SUM(s.cPositiveCases)*1.0/SUM(s.cTotalSamples)*100,SUM(s.cPositiveCases) FROM SampleInfo s WHERE s.collectionYear = :year AND s.hostSource = :host AND s.province = :province")
    List<Object[]> findByProvinceAndYearAndHostWithInfectionRate(
            @Param("province") String province,
            @Param("year") int year,
            @Param("host") String host);

    // 4.年份，宿主获取省份及其感染数
    @Query("SELECT s.province, SUM(s.cPositiveCases) FROM SampleInfo s WHERE s.collectionYear = :year AND s.hostSource = :host GROUP BY s.province")
    List<Object[]> findByYearAndHostWithProvinceAndInfectionCount(
            @Param("year") int year,
            @Param("host") String host);

    // 5.获取某省份某年的全部宿主感染数据
    @Query("SELECT s.hostSource, SUM(s.cTotalSamples), SUM(s.cPositiveCases), SUM(s.cPositiveCases)*1.0/SUM(s.cTotalSamples) FROM SampleInfo s WHERE s.collectionYear = :year AND s.province = :province GROUP BY s.hostSource")
    List<Object[]> findByProvinceAndYearWithDetails(
            @Param("province") String province,
            @Param("year") int year);

    // 6.获取某一省份特定年份某宿主所有市级单位的感染数
    @Query("SELECT s.city, s.cPositiveCases FROM SampleInfo s WHERE s.collectionYear = :year AND s.province = :province AND s.hostSource = :host")
    List<Object[]> findByProvinceAndYearAndHostWithCityAndInfectionCount(
            @Param("province") String province,
            @Param("year") int year,
            @Param("host") String host);


    // CURD
    // 查询所有信息
//    @Query("SELECT s FROM SampleInfo s LEFT JOIN FETCH s.subtypeDetails ORDER BY s.collectionYear DESC")
    @Query(
            value = "SELECT s FROM SampleInfo s LEFT JOIN FETCH s.subtypeDetails ORDER BY s.collectionYear DESC",
            countQuery = "SELECT COUNT(s) FROM SampleInfo s"
    )
    Page<SampleInfo> findAllWithSubtypeDetails(Pageable pageable);

    @Query("SELECT s FROM SampleInfo s LEFT JOIN FETCH s.subtypeDetails WHERE s.sampleId = :sampleId")
    List<SampleInfo> findOneWithSubtypeDetails(String sampleId);


    @Query("SELECT s.province, SUM(s.cPositiveCases) FROM SampleInfo s GROUP BY s.province")
    List<Object[]> findAllInfectionCount();

    @Query("SELECT  SUM(s.cTotalSamples),count(DISTINCT s.hostSource),count(DISTINCT s.province) FROM SampleInfo s")
    List<Object[]> findCountOfSampleAndHost();
}