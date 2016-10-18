package pl.cyrzan.e3future.models;

/**
 * Created by Patryk on 14.10.2016.
 */

public class ReadStateResponse {

    private String id;
    private String name;
    private String hardware;
    private boolean connected;
    private int return_value;

    public int getReturn_value() {
        return return_value;
    }

    public void setReturn_value(int return_value) {
        this.return_value = return_value;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
