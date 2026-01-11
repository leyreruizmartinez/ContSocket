package es.deusto.sd.contsocket.server;

import java.io.*;
import java.net.*;

public class ContSocketServer {

    public static void main(String[] args) {
        int port = 9500;
        ContSocketService service = new ContSocketService();

        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("ContSocket Server running on port " + port);

            while (true) {
                Socket socket = server.accept();
                new Thread(() -> handleClient(socket, service)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket socket, ContSocketService service) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
        	System.out.println("Client connected: " + socket.getInetAddress());
            String msg = in.readLine();
            if (msg == null) return;

            String[] p = msg.split(";");
            String cmd = p[0];
            System.out.println("Received command: " + cmd);
            switch (cmd) {
                case "GET_CAPACITY":
                	System.out.println("Processing GET_CAPACITY for date: " + p[1]);
                    out.println(service.getCapacity(p[1]));
                    break;

                case "NOTIFY":
                    // Support two formats for backwards compatibility:
                    // 1) NOTIFY;<dumpsters>;<containers>
                    // 2) NOTIFY;<plantName>;<dumpster>;<containers>  <-- format sent by Ecoembes gateway
                    try {
                        if (p.length == 3) {
                            // old format
                            out.println(service.notifyAssignment(
                                Integer.parseInt(p[1]),
                                Integer.parseInt(p[2])
                            ));
                        } else if (p.length == 4) {
                            String plantName = p[1];
                            int dumpster = Integer.parseInt(p[2]);
                            int containers = Integer.parseInt(p[3]);
                            System.out.println("NOTIFY for plant: " + plantName + " dumpster=" + dumpster + " containers=" + containers);
                            out.println(service.notifyAssignment(dumpster, containers));
                        } else {
                            out.println("ERROR");
                        }
                    } catch (NumberFormatException nfe) {
                        out.println("ERROR");
                    }
                    break;

                default:
                    out.println("ERROR");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}