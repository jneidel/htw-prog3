package util;

public interface Observable {
    public void attachObserver( Observer o );
    public void detachObserver( Observer o );
    public void notifyObservers( String status );
}
