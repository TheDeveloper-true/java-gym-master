package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final HashMap<DayOfWeek, TreeMap<TimeOfDay, TrainingSession[]>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        if ((0 <= trainingSession.getTimeOfDay().getHours() && trainingSession.getTimeOfDay().getHours() <= 24)
                && (trainingSession.getDayOfWeek() != null)) {
            TimeOfDayComparator comparator = new TimeOfDayComparator();
            TreeMap<TimeOfDay, TrainingSession[]> trainForDay = timetable.get(trainingSession.getDayOfWeek());
            if (trainForDay == null)
                trainForDay = new TreeMap<>(comparator);
            TrainingSession[] newSession;
            TrainingSession[] currentSession = trainForDay.get(trainingSession.getTimeOfDay());
            if (currentSession == null)
                newSession = new TrainingSession[]{trainingSession};
            else {
                for (TrainingSession existingSession : currentSession) {
                    if (existingSession != null && existingSession.getCoach().equals(trainingSession.getCoach())) {
                        return;
                    }
                }
                newSession = new TrainingSession[currentSession.length + 1];
                System.arraycopy(currentSession, 0, newSession, 0, currentSession.length);
                newSession[newSession.length - 1] = trainingSession;
            }
            trainForDay.put(trainingSession.getTimeOfDay(), newSession);
            timetable.put(trainingSession.getDayOfWeek(), trainForDay);
        }
    }

    public ArrayList<TrainingSession[]> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, TrainingSession[]> trainForDay = timetable.get(dayOfWeek);
        if (trainForDay != null)
            return new ArrayList<>(trainForDay.values());
        return null;
    }

    public TrainingSession[] getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, TrainingSession[]> trainForDay = timetable.get(dayOfWeek);
        if (trainForDay != null)
            return trainForDay.get(timeOfDay);
        return null;
    }

    public int getCountByCoaches(Coach coach) {
        int sessionsSum = 0;
        for (TreeMap<TimeOfDay, TrainingSession[]> day : timetable.values()) {
            for (TrainingSession[] session : day.values()) {
                if (session != null)
                    for (TrainingSession trainingSession : session) {
                        if (trainingSession.getCoach().equals(coach)) {
                            sessionsSum++;
                        }
                    }
            }
        }
        return sessionsSum;
    }
}
