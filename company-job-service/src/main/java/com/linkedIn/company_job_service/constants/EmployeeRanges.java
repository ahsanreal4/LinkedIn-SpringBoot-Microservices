package com.linkedIn.company_job_service.constants;

import java.util.ArrayList;

public class EmployeeRanges {
    public static String LOW = "1-10";
    public static String MEDIUM = "11-50";
    public static String HIGH = "50-500";
    public static String ENTERPRISE = "500+";

    public static boolean isValidEmployeeRange(String range) {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(LOW);
        arrayList.add(HIGH);
        arrayList.add(MEDIUM);
        arrayList.add(ENTERPRISE);

        return arrayList.contains(range);
    }
}
