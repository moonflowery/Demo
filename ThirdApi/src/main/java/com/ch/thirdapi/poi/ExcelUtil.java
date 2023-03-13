package com.ch.thirdapi.poi;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;



public class ExcelUtil {
    /*
     * @Description:  该方法将结果集为List<Map>的结果集转换为ecxel表格，文件标题title、文件路径path，数据mapList
     * @Author: chenghao
     * @Date: 2022/12/12 14:55
     * @param mapList
     * @param path
     * @param title
     * @return: boolean
     **/
    public static boolean  ListToExcelAsOrder(List<Map<String,Object>> list,String path,String title,String[] columns) {
        OutputStream outputStream;
        //生成期间，只在内存中维持100行，超时写临时文件到磁盘中，最后释放
        SXSSFWorkbook workbook = new SXSSFWorkbook(100);
        SXSSFSheet sheet = workbook.createSheet(title);
        workbook.setCompressTempFiles(true);
        //在这个表中创建第一行为表头，并赋值
        SXSSFRow row = sheet.createRow(0);
        for (int j = 0; j < columns.length; j++) {
            row.createCell(j).setCellValue(columns[j]);
        }
        //外层循环为每一条记录，内层循环遍历某条记录中的所有字段
        for (int i = 0; i < list.size(); i++) {
            Row rowOne = sheet.createRow(i + 1);
            Map<String, Object> map = list.get(i);

            for (int k = 0; k < columns.length; k++) {
                Cell cell = rowOne.createCell(k);
                Object o = map.get(columns[k]);
                if ( null != o){
                    cell.setCellValue(o.toString());
                }
            }
        }
        /*
        // Rows with rownum < 900 are flushed and not accessible
        for(int i = 0; i < 900; i++){
            Assert.assertNull(sheet.getRow(i));
        }
        // ther last 100 rows are still in memory
        for(int i = 900; i < 1000; i++){
            Assert.assertNotNull(sheet.getRow(i));
        }*/
        try {
            outputStream = new FileOutputStream(path + title + ".xlsx");
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //务必清除临时文件，否则消耗磁盘空间
            workbook.dispose();
        }
        return true;
    }
    /*
        将Excel文件解析为List<Map>集合
     */
    public static List<Map<String,String>> parseExcelToListMap(String path){
        //最后方法返回该集合
        List<Map<String,String>> list = new ArrayList<>();
        //获取第一行，存储表头
        List<String> keys = new ArrayList<>();

        XSSFWorkbook workbook = null;
        XSSFSheet sheetAt;
        //总行数
        int totalRowsNum = 0;
        //总列数
        int totalCellsNum = 0;
        try {
            workbook = new XSSFWorkbook(path);
            sheetAt = workbook.getSheetAt(0);
            //获取总行数
            totalRowsNum = sheetAt.getLastRowNum();
            XSSFRow firstRow = sheetAt.getRow(0);
            if(totalRowsNum > 1 && null != firstRow){
                totalCellsNum = sheetAt.getRow(0).getPhysicalNumberOfCells();
            }
            //将表头单独存储在一个集合中
            for (int i = 0; i < totalCellsNum; i++) {
                String value = firstRow.getCell(i).getStringCellValue();
                keys.add(value);
            }
            //遍历总行数，每次为一条记录创建一个map集合，并且在内层for循环中进行每一列数据的填充
            for (int j = 1; j < totalRowsNum + 1 ; j++) {
                Map<String,String> map = new HashMap<>();
                XSSFRow row = sheetAt.getRow(j);
                for (int k = 0; k < totalCellsNum; k++) {
                    //获取单元格值，而不定义类型。格式化为字符串
                    map.put(keys.get(k), new DataFormatter().formatCellValue(row.getCell(k)));
                }
                list.add(map);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (null != workbook) {
                try {
                    workbook.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }

        return list;
    }
    public static List<List<String>>parseExcelToList(String path) {
        XSSFWorkbook workbook = null;
        XSSFSheet sheet;
        XSSFRow row;
        int totalRowsNum = 0;
        int totalCellsNum = 0;
        List<List<String>> list = new ArrayList<>();
        List<String> listRow;
        try{
            workbook = new XSSFWorkbook(path);
            sheet = workbook.getSheetAt(0);
            totalRowsNum = sheet.getLastRowNum();
            XSSFRow firtRow = sheet.getRow(0);
            totalCellsNum = firtRow.getLastCellNum();
            for (int i = 1; i < totalRowsNum + 1; i++) {
                listRow = new ArrayList<>();
                XSSFRow everyRow = sheet.getRow(i);
                for (int j = 0; j < totalCellsNum; j++) {
                    listRow.add(new DataFormatter().formatCellValue(everyRow.getCell(j)));
                }
                list.add(listRow);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (null != workbook){
                try{
                    workbook.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    //TODO
   /* public static Vector publiv(int a){
        if (a == 1){
            return new Vector<>();
        }
        return 0;
    }*/
}
