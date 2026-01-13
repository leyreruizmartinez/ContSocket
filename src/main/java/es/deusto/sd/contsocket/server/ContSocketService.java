package es.deusto.sd.contsocket.server;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ContSocketService {

    private Map<String, Integer> capacity = new HashMap<>();

    public ContSocketService() {
        capacity.put("28112025", 120);
        capacity.put("29112025", 100);
        capacity.put("30112025", 140);

        // Ensure today's date has a reasonable default capacity so assignments using LocalDate.now() work
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        capacity.putIfAbsent(today, 200); // default capacity for today
    }

    public int getCapacity(String date) {
        return capacity.getOrDefault(date, -1);
    }

    public String notifyAssignment(int dumpsters, int containers) {
        System.out.println("Assignment received → dumpsters=" + dumpsters
                + " containers=" + containers);
        
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        int currentCapacity = capacity.get(today);
        capacity.put(today, currentCapacity-containers); // default capacity for today
        return "OK";
    }
}