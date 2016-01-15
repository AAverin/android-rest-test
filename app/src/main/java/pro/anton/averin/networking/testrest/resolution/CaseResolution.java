package pro.anton.averin.networking.testrest.resolution;

import java.net.ConnectException;

public interface CaseResolution extends Resolution {
    void handle(ConnectException connectException);
}
