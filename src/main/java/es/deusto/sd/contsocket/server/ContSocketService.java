package es.deusto.sd.contsocket.server;

import java.util.HashMap;
import java.util.Map;

public class ContSocketService {

    private Map<String, Integer> capacity = new HashMap<>();

    public ContSocketService() {
        capacity.put("28112025", 120);
        capacity.put("29112025", 100);
        capacity.put("30112025", 140);
    }

    public int getCapacity(String date) {
        return capacity.getOrDefault(date, -1);
    }

    public String notifyAssignment(int dumpsters, int containers) {
        System.out.println("Assignment received → dumpsters=" + dumpsters
                + " containers=" + containers);
        return "OK";
    }
}
