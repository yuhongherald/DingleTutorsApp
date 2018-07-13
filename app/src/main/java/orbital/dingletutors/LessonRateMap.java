package orbital.dingletutors;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Muruges on 30/7/2017.
 */

public class LessonRateMap {
    public static ArrayList<LessonRate> lessonRates = new ArrayList<>();
    public static TreeMap<String, Double> lessonRatesMap = new TreeMap<>();
    public static DecimalFormat format = new DecimalFormat("0.00");
    public static class LessonRate {

        public String classLevel;
        public String subjectName;
        public double feesPerHour;
        public String displayFees;

        public LessonRate(String classLevel, String subjectName, double feesPerHour){
            this.classLevel = classLevel;
            this.subjectName = subjectName;
            this.feesPerHour = feesPerHour;
            this.displayFees = "$" + format.format(feesPerHour) + "/hr";
            lessonRatesMap.put(classLevel + subjectName, feesPerHour);
        }

    }
    public static boolean isSameRate(LessonRate rate1, String classLevel2, String subjectName2 ){
        return rate1.classLevel == classLevel2 && rate1.subjectName == subjectName2;
    }

    public static String getKey(LessonRate rate){
        return rate.classLevel + rate.subjectName;
    }
    public static int getPosInMap(LessonRate lessonRate){
        if (lessonRatesMap.get(lessonRate.classLevel+lessonRate.subjectName) == null) {
            return -1;
        } else {
            return lessonRatesMap.headMap(lessonRate.classLevel + lessonRate.subjectName).size();
        }
    }
    public static boolean isPresent(String classLevel, String subjectName){
        return lessonRatesMap.get(classLevel + subjectName) != null;
    }
}
