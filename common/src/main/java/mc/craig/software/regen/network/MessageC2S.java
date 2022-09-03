package mc.craig.software.regen.network;

public abstract class MessageC2S extends Message {

    public void send() {
        this.getType().getNetworkManager().sendToServer(this);
    }

}
