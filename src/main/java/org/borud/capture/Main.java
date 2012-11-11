package org.borud.capture;

/**
 * Main class for data capture.
 *
 * @author borud
 */
public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("give me a port");
            return;
        }

        System.out.println("Hello");
        Serial serial = new Serial(args[0], 19200);
        serial.open();
    }
}
