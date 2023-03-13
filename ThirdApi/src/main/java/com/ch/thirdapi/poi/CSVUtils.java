package com.ch.thirdapi.poi;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.*;
import java.util.*;

/**
 * @author chenghao
 * @purpose：对CSV文件的相关操作
 * @备注：使用第三方openCSV
 * @data 2023年01月30日 15:37
 */
public class CSVUtils {

    /*
        path : 待解析文件的路径
        split : 设置CSV文件的分隔符
     */
    public static void parseCsvToList(String path,char split) {
        List<List<String>> list = new ArrayList<>();
        List<String> subList;
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(path),split);
            while (true) {
                subList = new ArrayList<>();
                String[] rowEvery = csvReader.readNext();
                if (null == rowEvery) {
                    break;
                }
                for (int i = 0; i < rowEvery.length; i++) {
                    subList.add(rowEvery[i]);
                }
                list.add(subList);
            }
            System.out.println(list.get(0));
            System.out.println(list.get(list.size()-1));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != csvReader) {
                try {
                    csvReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
    /*
        path : 待解析文件的路径
        split : 设置CSV文件的分隔符
     */
    public static void parseCsvToListMap(String path,char split) {
        List<Map<String,String>> list = new ArrayList<>();
        Map<String,String> map;
        CSVReader csvReader = null;
        try {
            csvReader = new CSVReader(new FileReader(path),split);
            String[] title = csvReader.readNext();
            while (true) {
                map = new HashMap<>();
                String[] rowEvery = csvReader.readNext();
                if (null == rowEvery) {
                    break;
                }
                for (int i = 0; i < rowEvery.length; i++) {
                    map.put(title[i],rowEvery[i]);
                }
                list.add(map);
            }
            System.out.println(list.get(1));
            System.out.println(list.get(list.size()-1));
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != csvReader){
                try {
                    csvReader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /*
        listData：需要导出的数据
        outPath：导出的路径
        colums：指定列按照顺序
        split：设置分隔符号
     */
    public static void exportToCsv(List<Map<String,Object>> listData,String outPath,String [] colums,char split){
        OutputStream outputStream = null;
        CSVWriter writer = null;
        try {
            outputStream = new FileOutputStream(new File(outPath));
            writer = new CSVWriter(new OutputStreamWriter(outputStream,"utf-8"),split);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //填充表头
        if (writer != null) {
            writer.writeNext(colums);
        }
        for (int i = 0; i < listData.size(); i++) { //遍历每一行
            Map<String, Object> map = listData.get(i);
            String [] rowCell = new String[colums.length];
            for (int j = 0; j < colums.length; j++) {
                if (map.get(colums[j]) != null){
                rowCell[j] = map.get(colums[j]).toString(); //列
                }
            }
            writer.writeNext(rowCell); //写入每一列
            try {
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (rowCell != null) {
                    try {
                        writer.close();
                        outputStream.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}