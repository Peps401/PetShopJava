package Threads;

import com.example.projectpetcentar.HelloApplication;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ThreadTodaysDateNTime implements Runnable{
    @Override
    public void run() {
        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = LocalDateTime.now().getDayOfWeek();
        String day ="";

        switch(dayOfWeek)
        {
            case MONDAY:
                day = "PONEDJELJAK";
                break;
            case TUESDAY:
                day = "UTORAK";
                break;
            case WEDNESDAY:
                day = "SRIJEDA";
                break;
            case THURSDAY:
                day = "CETVRTAK";
                break;
            case FRIDAY:
                day = "PETAK";
                break;
            default:
                day = "VIKEND";
                break;
        }

        HelloApplication.setMainStageTitle("Danas je : " + day + " "
                + now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy. HH:mm")));
    }
}
