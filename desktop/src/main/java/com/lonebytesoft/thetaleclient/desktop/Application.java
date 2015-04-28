package com.lonebytesoft.thetaleclient.desktop;

public class Application {

    public static void main(String[] args) throws Throwable {
        Utils.consoleWrite("Select module:");
        final int count = Module.values().length;
        for(int i = 0; i < count; i++) {
            Utils.consoleWrite(String.format("%d - %s", i + 1, Module.values()[i].name));
        }

        final int index = Integer.decode(Utils.consoleReadLine()) - 1;
        Module.values()[index].execute();
    }

}
