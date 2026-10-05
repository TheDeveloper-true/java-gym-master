import java.util.Comparator;

public class TimeOfDayComparator implements Comparator<TimeOfDay> {

    @Override
    public int compare(TimeOfDay time1, TimeOfDay time2) {

        if (time1.getHours() > time2.getHours()) {
            return 1;

        } else if (time1.getHours() < time2.getHours()) {
            return -1;

        } else {
            if (time1.getMinutes() > time2.getMinutes())
                return 1;
            else if (time1.getMinutes() < time2.getMinutes())
                return -1;
            else
                return 0;
        }
    }
}
