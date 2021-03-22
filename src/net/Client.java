package net;

public interface Client {
    public void sendRegister();
    public void sendUnregister();

    public void sendUploadItem();
    public void sendDeleteItem();

    public void sendCreateProd();
    public void sendDeleteProd();
}
