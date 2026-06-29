package com.example.Listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.util.StringUtils;
import com.example.DTO.SampleInfoExcelDTO;
import com.example.pojo.SampleInfo;
import com.example.pojo.SubtypeDetail;
import com.example.service.SampleInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class SampleInfoExcelListener implements ReadListener<SampleInfoExcelDTO> {

    // 每次批量插入的行数
    private static final int BATCH_COUNT = 10;

    // 存储解析后的数据
    private List<SampleInfo> sampleInfos = new ArrayList<>();

    // 存储表头中的亚型字段名（从第28列开始）
    private Map<Integer, String> subtypeHeaders;

    // 标记表头是否已解析
    private boolean isHeaderParsed = false;

    // 构造方法注入 Bean
    private SampleInfoService sampleInfoService;

    public SampleInfoExcelListener(SampleInfoService sampleInfoService,Map<Integer, String> subtypeHeaders) {
        this.sampleInfoService = sampleInfoService;
        this.subtypeHeaders = subtypeHeaders;
    }



    @Override
    public void invoke(SampleInfoExcelDTO excelDTO, AnalysisContext analysisContext) {
        try {
            // 获取当前行号
            int currentRowIndex = analysisContext.readRowHolder().getRowIndex();


            // 数据行：解析亚型字段的数量
            Map<String, Integer> dynamicFields = new LinkedHashMap<>();

            for (Map.Entry<Integer, String> entry : subtypeHeaders.entrySet()) {
                int columnIndex = entry.getKey();
                String subtypeName = entry.getValue();

                // 获取当前行的亚型数量
                Object countValueObj = analysisContext.readRowHolder().getCellMap().get(columnIndex);
                // 使用工具方法提取整数值
                Integer countValue = extractIntegerValue(countValueObj);
                //
                if ( countValue!=null && countValue>0) {
                    try {
                        dynamicFields.put(subtypeName, countValue);
                    } catch (NumberFormatException e) {
                        log.error("字段 {} 的数量无法转换为整数: {}", subtypeName, countValueObj);
                    }
                }
            }

            // 校验 sampleId 是否为空
            if (StringUtils.isEmpty(excelDTO.getSampleId())) {
                log.error("第 {} 行数据缺少 sampleId", currentRowIndex);
                return;
            }

            // 转换为 SampleInfo 对象
            SampleInfo sampleInfo = new SampleInfo();
            BeanUtils.copyProperties(excelDTO, sampleInfo);

            // 动态提取亚型字段
            //List<SubtypeDetail> subtypeDetails = new ArrayList<>();
            for (Map.Entry<String, Integer> entry : dynamicFields.entrySet()) {
                String subtypeName = entry.getKey();
                Integer count = entry.getValue();

                SubtypeDetail subtypeDetail = new SubtypeDetail();
                subtypeDetail.setSubTypeName(subtypeName);
                subtypeDetail.setSubtypePositiveCases(count);

                sampleInfo.getSubtypeDetails().add(subtypeDetail);
            }

            sampleInfos.add(sampleInfo);

            // 打印完整的 SampleInfo 对象信息
            //log.info("第 {} 行完整的 SampleInfo 对象信息: {}", currentRowIndex, sampleInfo);

            // 如果达到批量插入的阈值，执行批量插入
            if (sampleInfos.size() >= BATCH_COUNT) {
                saveData();
            }

        } catch (Exception e) {
            log.error("解析第 {} 行数据失败: {}", analysisContext.readRowHolder().getRowIndex(), e.getMessage());
        }
    }


    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("所有数据解析完成");
        if (!sampleInfos.isEmpty()) {
            saveData();
        }
    }

    /**
     * 批量保存数据到数据库
     */
    private void saveData() {
        try {
            if (!sampleInfos.isEmpty()) {
                // 再次去重：按 SampleId 去重
                List<SampleInfo> uniqueSampleInfos = sampleInfos.stream()
                        .collect(Collectors.toMap(
                                SampleInfo::getSampleId, // 以 SampleId 作为键
                                sampleInfo -> sampleInfo, // 值为 SampleInfo 对象
                                (existing, replacement) -> existing // 如果有重复，保留第一个
                        ))
                        .values()
                        .stream()
                        .collect(Collectors.toList());

                log.info("开始批量插入 {} 条数据（去重后）", uniqueSampleInfos.size());
                sampleInfoService.batchAddSampleInfos(uniqueSampleInfos);
                log.info("成功插入 {} 条数据", uniqueSampleInfos.size());

                // 清空列表
                sampleInfos.clear();
            }
            sampleInfos.clear(); // 清空列表
        } catch (Exception e) {
            log.error("批量插入数据失败: {}", e.getMessage());
        }
    }

    public static Integer extractIntegerValue(Object cellDataObj) {
        if (cellDataObj instanceof ReadCellData<?>) {
            ReadCellData<?> cellData = (ReadCellData<?>) cellDataObj;

            // 优先尝试 getNumberValue()
            Number numberValue = cellData.getNumberValue();
            if (numberValue != null) {
                return numberValue.intValue();
            }

            // 回退到 getStringValue()
            String stringValue = cellData.getStringValue();
            if (StringUtils.isNotBlank(stringValue)) {
                try {
                    return Integer.parseInt(stringValue.trim());
                } catch (NumberFormatException e) {
                    log.error("单元格值无法转换为整数: {}", stringValue);
                }
            }
        }
        return null;
    }
}