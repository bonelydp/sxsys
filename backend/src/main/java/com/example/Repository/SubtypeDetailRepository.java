package com.example.Repository;

import com.example.pojo.SampleInfo;
import com.example.pojo.SubtypeDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubtypeDetailRepository extends JpaRepository<SubtypeDetail, Long> {
    List<SubtypeDetail> findAllBySampleInfo_SampleId(String sampleId);

    void deleteBySampleInfo(SampleInfo sampleInfo);
}