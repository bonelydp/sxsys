package com.example.Listener;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.util.StringUtils;


import java.util.Map;

public class HeaderReadListener extends AnalysisEventListener<Map<Integer, String>> {

    private final Map<Integer, String> subtypeHeaders;

    public HeaderReadListener(Map<Integer, String> subtypeHeaders) {
        this.subtypeHeaders = subtypeHeaders;
    }

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {

        if (context.readRowHolder().getRowIndex() != 0) {
            return; // 如果不是第 0 行，则跳过
        }

        // 解析表头中的亚型字段名（从第28列开始）
        for (int i = 28; i < data.size(); i++) {
            String headerValue = data.get(i);
            if (StringUtils.isNotBlank(headerValue)) {
                subtypeHeaders.put(i, headerValue); // 存储亚型字段名及其对应的列索引
            }
        }
//        for (Map.Entry<Integer, String> entry : subtypeHeaders.entrySet()) {
//            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
//        }
        //System.out.println("成功解析表头，亚型字段数量: " + subtypeHeaders.size());


    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 表头解析完成
    }
}