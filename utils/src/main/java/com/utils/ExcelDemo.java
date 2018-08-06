package com.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * Created by cynthia on 2018/8/6.
 */
public class ExcelDemo {
    /**
     * 创建一个简单的 excel demo
     *
     * @param file 输出位置
     * @throws IOException
     */
    public static void createExcel(String file) throws IOException {
        //创建新工作簿
        XSSFWorkbook workBook = new XSSFWorkbook();
        //创建工作表
        XSSFSheet sheet = workBook.createSheet("sheet1");
        //设置列宽 第一列 16个字符宽
        sheet.setColumnWidth(0, 16 * 256);
        //创建第一行
        XSSFRow row0 = sheet.createRow(0);
        XSSFRow row1 = sheet.createRow(1);
        //创建第一行第一个单元格 并赋值
        row0.createCell(0, CellType.STRING).setCellValue("序号");
        row0.createCell(1, CellType.STRING).setCellValue("姓名");
        row1.createCell(0, CellType.STRING).setCellValue("1");
        row1.createCell(1, CellType.STRING).setCellValue("张三");

        FileOutputStream outputStream = new FileOutputStream(new File(file));
        workBook.write(outputStream);

        workBook.close();//记得关闭工作簿
    }

    /**
     * 读取excel 文件 此方法多为前端传入的文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static String importExcel(MultipartFile file) throws IOException {
        checkFile(file);
        Workbook workBook = null;
        try {
            workBook = getWorkBook(file.getOriginalFilename());
            Sheet sheet = workBook.getSheetAt(0);
            int cellNum = sheet.getRow(sheet.getFirstRowNum()).getPhysicalNumberOfCells();
            for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
                for (int j = 0; j < cellNum; j++) {
                    Cell cell = sheet.getRow(i).getCell(j);
                    String cellValue = cell.getStringCellValue();
                    System.out.print(i + "," + j + "==> " + cellValue + "\t\t\t");
                }
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 检查文件
     *
     * @param file
     * @throws IOException
     */
    public static String checkFile(MultipartFile file) throws IOException {
        //判断文件是否存在
        if (null == file) {
            return "文件不存在！";
        }
        //获得文件名
        String fileName = file.getOriginalFilename();
        //判断文件是否是excel文件
        if (!fileName.endsWith("xls") && !fileName.endsWith("xlsx")) {
            return fileName + "不是excel文件";
        }
        return "";
    }

    /**
     * 用于判断 excel 版本
     *
     * @param fileName
     * @return
     */
    public static Workbook getWorkBook(String fileName) {
        //获得文件名

        //创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            //获取excel文件的io流
            File excelFile = new File(fileName);
            InputStream is = new FileInputStream(excelFile);

            //根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            if (fileName.endsWith("xls")) {
                //2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith("xlsx")) {
                //2007 及2007以上
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return workbook;
    }

    /**
     * 传入文件地址读取
     * @param file
     */
    public static void readFile(String file) {
        Workbook workBook = getWorkBook(file);
        Sheet sheet = workBook.getSheetAt(0);
        int cellNum = sheet.getRow(sheet.getFirstRowNum()).getPhysicalNumberOfCells();
        for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
            for (int j = 0; j < cellNum; j++) {
                Cell cell = sheet.getRow(i).getCell(j);
                String cellValue = cell.getStringCellValue();
                System.out.print(i + "," + j + "==> " + cellValue + "\t\t\t");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws IOException {
//        生成测试
//        createExcel("/Users/trthi/Desktop/test.xlsx");
//        读取测试
//        File file = new File("/Users/trthi/Desktop/test.xlsx");
//        FileInputStream input = new FileInputStream(file);
//        MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(input));
//        importExcel(multipartFile);
        //读取测试
        readFile("/Users/trthi/Desktop/test.xlsx");
    }

}
