/*
package com.example.syxm.mapper;

import com.example.syxm.entity.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SampleMapper extends JpaRepository<SampleEntity, Long> {
}*/
package com.example.mapper;


import com.example.pojo.SampleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface SampleMapper {

    // 查询所有
    @Select("SELECT * FROM sampledata")
    List<SampleEntity> selectAll();

    // 根据ID查询
    @Select("SELECT * FROM sampledata WHERE id = #{id}")
    SampleEntity selectById(Integer id);

    // 查询带有用户名的单条数据
    @Select("SELECT s.*, u.username FROM sampledata s LEFT JOIN user u ON s.user_id = u.id WHERE s.id = #{id}")
    SampleEntity selectByIdWithUsername(@Param("id") Integer id);

    // 查询所有带用户名的数据
    @Select("SELECT s.*, u.username FROM sampledata s LEFT JOIN user u ON s.user_id = u.id")
    List<SampleEntity> selectAllWithUsername();

    // 新增样本
    @Insert("INSERT INTO sampledata (sample_number, country, province, city, district, address_details, " +
            "sample_type, collection_time, storage_conditions, outbreak, start_time, end_time, report_time, " +
            "disease_count, death_count, host_source, gender, diarrhea, diarrhea_type, death, host_species, age, " +
            "other_symptoms, immunity, detection_method, detection_methodnew, insect_species, subtype, jingpicture, " +
            "status, file, selected_standard_sequence) VALUES " +
            "(#{sampleNumber}, #{country}, #{province}, #{city}, #{district}, #{addressDetails}, " +
            "#{sampleType}, #{collectionTime}, #{storageConditions}, #{outbreak}, #{startTime}, #{endTime}, " +
            "#{reportTime}, #{diseaseCount}, #{deathCount}, #{hostSource}, #{gender}, #{diarrhea}, #{diarrheaType}, " +
            "#{death}, #{hostSpecies}, #{age}, #{otherSymptoms}, #{immunity}, #{detectionMethod}, #{detectionMethodnew}, " +
            "#{insectSpecies}, #{subtype}, #{jingpicture}, #{status}, #{file}, #{selectedStandardSequence})")
    void save(SampleEntity sample);

    // 删除
    @Delete("DELETE FROM sampledata WHERE id = #{id}")
    void deleteById(Long id);

    // 更新状态
    @Update("UPDATE sampledata SET status = #{status} WHERE id = #{id}")
    void updateStatus(Long id, String status);

    // 【新增】按 status 查询总数
    @Select("<script>" +
            "SELECT COUNT(*) FROM sampledata" +
            "<where>" +
            "  <if test='status != null'> AND status = #{status} </if>" +
            "</where>" +
            "</script>")
    int countByStatus(@Param("status") String status);

    // 【新增】分页查询 + 获取用户名 + 按 status 筛选
    @Select("<script>" +
            "SELECT s.*, u.username FROM sampledata s LEFT JOIN user u ON s.user_id = u.id " +
            "WHERE 1=1 " +
            "<if test='status != null'> AND s.status = #{status} </if>" +
            "ORDER BY s.id DESC LIMIT #{offset}, #{pageSize}" +
            "</script>")
    List<SampleEntity> selectWithPaginationAndStatus(
            @Param("status") String status,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize);
}