package pro.anton.averin.networking.testrest.rx.events;

public class NaviManagerScreenEvent {
    public boolean save = false;

    private NaviManagerScreenEvent(boolean save) {
        this.save = save;
    }

    public static NaviManagerScreenEvent withSave() {
        return new NaviManagerScreenEvent(true);
    }

    public static NaviManagerScreenEvent withoutSave() {
        return new NaviManagerScreenEvent(false);
    }
}
