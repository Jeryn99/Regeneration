package me.sub.proxy;

import me.sub.client.RKeyBinds;

/**
 * Created by Sub
 * on 17/09/2018.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void init() {
        super.init();
        RKeyBinds.init();
    }

    @Override
    public void postInit() {
        super.postInit();
    }
}
