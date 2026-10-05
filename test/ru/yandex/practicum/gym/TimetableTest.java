package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import ru.yandex.practicum.gym.Timetable;

import java.util.Arrays;
import java.util.List;

import java.util.ArrayList;

public class TimetableTest {

    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);
        TrainingSession[] trainingArray = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).getFirst();

        Assertions.assertEquals(singleTrainingSession, trainingArray[0]);//Проверить, что за понедельник вернулось одно занятие
        Assertions.assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY));//Проверить, что за вторник не вернулось занятий
    }

    @Test
    void testGetTrainingSessionsForTimeOutOf24Hours() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession trainingSessionOutOf24Hours = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(25, 0));

        timetable.addNewTrainingSession(trainingSessionOutOf24Hours);

        Assertions.assertNull(timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, new TimeOfDay(25, 0)));
    }

    @Test
    void testGetTrainingSessionsForNullDayOfWeek() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession trainingSessionNullDayOfWeek = new TrainingSession(group, coach,
                null, new TimeOfDay(24, 0));

        timetable.addNewTrainingSession(trainingSessionNullDayOfWeek);

        Assertions.assertNull(timetable.getTrainingSessionsForDay(null));
    }

    @Test
    void testGetTrainingSessionsForSameTime() {
        Timetable timetable = new Timetable();

        Group childGroup = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach childCoach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession trainingSessionChildGroup = new TrainingSession(childGroup, childCoach,
                DayOfWeek.MONDAY, new TimeOfDay(24, 0));

        Group adultGroup = new Group("Акробатика для взрослых", Age.ADULT, 60);
        Coach adultCoach = new Coach("Васильев", "Максим", "Сергеевич");
        TrainingSession trainingSessionAdultGroup = new TrainingSession(adultGroup, adultCoach,
                DayOfWeek.MONDAY, new TimeOfDay(24, 0));

        timetable.addNewTrainingSession(trainingSessionChildGroup);
        timetable.addNewTrainingSession(trainingSessionAdultGroup);

        ArrayList<TrainingSession> expected = new ArrayList<>();
        expected.add(trainingSessionChildGroup);
        expected.add(trainingSessionAdultGroup);
        ArrayList<TrainingSession> actual = new ArrayList<>();
        TrainingSession actualChildGroup = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(24, 0))[0];
        TrainingSession actualAdultGroup = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(24, 0))[1];
        actual.add(actualChildGroup);
        actual.add(actualAdultGroup);

        Assertions.assertIterableEquals(expected, actual);
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        List<TrainingSession> expected = new ArrayList<>();//Тут начинается код написанный при большой помощи нейросети
        expected.add(thursdayChildTrainingSession);//Где-то в ранних уроках писали об этом предупреждать ревьюера
        expected.add(thursdayAdultTrainingSession);
        ArrayList<TrainingSession[]> sessionsArray = timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);

        TrainingSession[] trainingArray = timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY).getFirst();
        List<TrainingSession> actual = new ArrayList<>();
        if (sessionsArray != null) {
            for (TrainingSession[] subArray : sessionsArray) {
                if (subArray != null) {
                    actual.addAll(Arrays.asList(subArray));
                }
            }
        }//Тут ощутимая помощь нейросети в коде заканчивается

        Assertions.assertIterableEquals(expected, actual);

        Assertions.assertEquals(mondayChildTrainingSession, trainingArray[0]);// Проверить, что за понедельник вернулось одно занятие
        Assertions.assertNull(timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY));// Проверить, что за вторник не вернулось занятий
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);
        TrainingSession[] expected = new TrainingSession[]{singleTrainingSession};
        TrainingSession[] actual = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(13, 0));
        TrainingSession[] actualForNull = timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY,
                new TimeOfDay(14, 0));

        Assertions.assertArrayEquals(expected, actual);//Проверить, что за понедельник в 13:00 вернулось одно занятие
        Assertions.assertNull(actualForNull);//Проверить, что за понедельник в 14:00 не вернулось занятий
    }

    @Test
    void testGetCoachWithNoSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coachWithoutSession = new Coach("Coach", "Without", "Session");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        timetable.addNewTrainingSession(singleTrainingSession);
        int sumForCoachWithoutSession = timetable.getCountByCoaches(coachWithoutSession);
        Assertions.assertEquals(0, sumForCoachWithoutSession);
    }

    @Test
    void testGetCoachWithThreeSessionInOneDay() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession firstTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession secondTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thirdTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(16, 0));
        timetable.addNewTrainingSession(firstTrainingSession);
        timetable.addNewTrainingSession(secondTrainingSession);
        timetable.addNewTrainingSession(thirdTrainingSession);
        int sumForCoachWith3Sessions = timetable.getCountByCoaches(coach);
        Assertions.assertEquals(3, sumForCoachWith3Sessions);
    }

    @Test
    void testGetCoachWithThreeSessionInOneTime() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession firstTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession secondTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        TrainingSession thirdTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(10, 0));
        timetable.addNewTrainingSession(firstTrainingSession);
        timetable.addNewTrainingSession(secondTrainingSession);
        timetable.addNewTrainingSession(thirdTrainingSession);
        int sumForCoachWith3SessionsInOneTime = timetable.getCountByCoaches(coach);
        Assertions.assertEquals(1, sumForCoachWith3SessionsInOneTime);
    }

}