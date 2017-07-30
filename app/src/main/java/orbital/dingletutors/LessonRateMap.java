package orbital.dingletutors;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Muruges on 30/7/2017.
 */

public class LessonRateMap {
    public static ArrayList<LessonRate> lessonRates = new ArrayList<>();
    public static TreeMap<String, Integer> lessonRatesMap = new TreeMap<>();
    public static DecimalFormat format = new DecimalFormat("0.00");
    public static class LessonRate {

        public String classLevel;
        public String subjectName;
        public int feesPerHour;
        public String displayFees;

        public LessonRate(String classLevel, String subjectName, int feesPerHour){
            this.classLevel = classLevel;
            this.subjectName = subjectName;
            this.feesPerHour = feesPerHour;
            this.displayFees = "$" + format.format(feesPerHour) + "/hr";
            lessonRatesMap.put(classLevel + subjectName, feesPerHour);
        }

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
