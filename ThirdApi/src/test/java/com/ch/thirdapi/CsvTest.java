package com.ch.thirdapi;
import com.ch.thirdapi.mapper.first.FirstDao;
import com.ch.thirdapi.poi.CSVUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author chenghao
 * @purpose：
 * @备注：
 * @data 2023年01月30日 16:13
 */
@SpringBootTest
public class CsvTest {
    @Autowired
    FirstDao firstDao;
    //测试解析为List集合，测试方法读取第一行和最后一行
    @Test
    public void test(){
        CSVUtils.parseCsvToList("/Users/chenghao/Desktop/KMPOST2LATLON.csv",',');
    }
    @Test
    public void test01(){
        CSVUtils.parseCsvToListMap("/Users/chenghao/Desktop/KMPOST2LATLON.csv",',');
    }
    @Test
    public void test02() throws IOException {
        String [] colums = {"ID",
                "RAILLINE",
                "NAME",
                "SERIALNUM",
                "KMPOST",
                "TYPE",
                "USAGE",
                "LRBG",
                "DIRECTION"};
        List<Map<String, Object>> all = firstDao.getAll();
        System.out.println(all.size());
        CSVUtils.exportToCsv(all,"/Users/chenghao/Desktop/excel/bbs.csv",colums,',');
    }
}
