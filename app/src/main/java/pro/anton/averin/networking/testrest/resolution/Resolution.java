package pro.anton.averin.networking.testrest.resolution;

public interface Resolution {
    void handle(Throwable t);

    void success();
}
