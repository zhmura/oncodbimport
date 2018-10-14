package com.sanda;

import com.sanda.pojo.Patient;

public class ExcelValuesImportTest {
    public static void main(String[] args) {
        String test1 = "";
        String test2 = "101";
        String test3 = "22.12.2018";
        String test4 = "1";
        String test5 = " 101 ";
        String test6 =  " 101 , 102 ";
        String test7 = " нский ";

        try {
            Patient patient = new Patient();
            String s = OncoExcelExportService.checkStringValue(test7, "gender", true, 1);
            switch (s.toLowerCase()) {
                case "мужской":
                    patient.setGender("male");
                    break;
                case "женский":
                    patient.setGender("female");
                    break;
                default:
                    System.out.println("Gender parsing failed with row: " + 1);
                    patient.setGender("male");

            }
            System.out.println("Test int array convert value = " + test6 + " result = " + patient.getGender());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
