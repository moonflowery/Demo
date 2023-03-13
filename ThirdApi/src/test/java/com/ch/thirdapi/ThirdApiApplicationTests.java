package com.ch.thirdapi;

import com.ch.thirdapi.mapper.first.FirstDao;
import com.ch.thirdapi.poi.ExcelUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Vector;

@SpringBootTest
class ThirdApiApplicationTests {

	@Autowired
	FirstDao firstDao;

	@Test
	void contextLoads() {
		Integer integer = firstDao.selectCount();
		System.out.println(integer);
	}
	@Test
	void test(){
		List<Map<String, String>> list = ExcelUtil.parseExcelToListMap("/Users/chenghao/Desktop/；/下行电平质量都有异常2021-1-21.xlsx");
	}
	@Test
	void test01(){
		List<Map<String, String>> lis = ExcelUtil.parseExcelToListMap("/Users/chenghao/Desktop/excel/KMPOST2LATLON.xlsx");
		System.out.println(lis.size());
	}
	@Test
	void test02() throws IOException {
		ExcelUtil.parseExcelToList("/Users/chenghao/Desktop/；/下行电平质量都有异常2021-1-21.xlsx");
	}

	@Test
	void test88(){
		Vector vector = new Vector<>();
		System.out.println(vector.size());

	}


}
