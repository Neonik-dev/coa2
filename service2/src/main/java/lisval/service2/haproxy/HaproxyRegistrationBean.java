package lisval.service2.haproxy;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;

@Singleton
@Startup
public class HaproxyRegistrationBean {

    private static final String BACKEND = "my_backend";
    private static final String SERVER_NAME = "server-" + System.currentTimeMillis();
    private static final String SERVER_ADDRESS = "192.168.65.254";

    @PostConstruct
    public void register() {
        try {
            sendCommand("add server " + BACKEND + "/" + SERVER_NAME + " " + SERVER_ADDRESS + ":" + getHttpPort());
            sendCommand("set server " + BACKEND + "/" + SERVER_NAME + " state ready");
            System.out.println("Registered in HAProxy: " + SERVER_NAME);
        } catch (Exception e) {
            throw new IllegalStateException("HAProxy registration failed", e);
        }
    }

    @PreDestroy
    public void unregister() {
        try {
            sendCommand("set server " + BACKEND + "/" + SERVER_NAME + " state maint");
            sendCommand("del server " + BACKEND + "/" + SERVER_NAME);
            System.out.println("Unregistered from HAProxy: " + SERVER_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendCommand(String command) throws Exception {
        HaproxySocketClient.send(command);
    }

    private int getHttpPort() {
        try {
            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            ObjectName binding = new ObjectName("jboss.as:socket-binding-group=standard-sockets,socket-binding=http");
            return (Integer) server.getAttribute(binding, "boundPort");
        } catch (Exception e) {
            String portStr = System.getProperty("jboss.http.port");
            try {
                return Integer.parseInt(portStr);
            } catch (NumberFormatException ex) {
                throw new RuntimeException("Failed to determine HTTP port", e);
            }
        }
    }
}

