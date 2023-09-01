package Threads;

import com.example.projectpetcentar.HelloApplication;
import entiteti.ChangesLog;

import java.util.List;

public class ThreadLastChange implements Runnable{
    @Override
    public void run() {

        List<ChangesLog> list = ChangesLog.getChangesList();

        int lastChangeIndex = list.size() - 1;

        String lastChange = list.get(lastChangeIndex).getNewData();

        HelloApplication.setMainStageTitle("Zadnja promjena: " + lastChange );

    }
}
