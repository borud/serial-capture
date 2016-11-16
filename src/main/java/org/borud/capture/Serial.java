package org.borud.capture;

import java.io.InputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.util.Enumeration;

/**
 * Utility class to take care of reading from the serial port.
 *
 * RXTX reports that someone is already using the port if /var/lock
 * does not exist -- which it generally doesn't on macs.
 *
 *   sudo mkdir /var/lock
 *   sudo chmod 777 /var/lock
 *
 * @author borud
 */
public class Serial implements SerialPortEventListener {
    private static final int TIMEOUT = 2000;

    private SerialPort serialPort;

    private InputStream input;

    private String portName;
    private int dataRate;

    /**
     * Create Serial interface.
     *
     * @param portName the name of the port
     * @param dataRate number of bits per second
     */
    public Serial(String portName, int dataRate) {
        this.portName = portName;
        this.dataRate = dataRate;
    }

    /**
     * Open serial interface.  Connect to the port and set up communication.
     */
    public void open() {
        // Find the port
        CommPortIdentifier cpi = null;
        Enumeration<CommPortIdentifier> ports = CommPortIdentifier.getPortIdentifiers();
        while (ports.hasMoreElements()) {
            CommPortIdentifier tmp = ports.nextElement();
            if (tmp.getName().equals(portName)) {
                cpi = tmp;
                break;
            }
        }

        // If we do not find the port we throw an exception.
        if (cpi == null) {
            throw new RuntimeException("Couldn't find serial port '" + portName + "'");
        }

        try {
            // Open the actual port
            serialPort = (SerialPort) cpi.open("capture", TIMEOUT);

            // set port parameters
            serialPort.setSerialPortParams(dataRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // open the streams
            input = serialPort.getInputStream();            

            // add event listeners
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Event handler for serial events.
     */
    @Override
    public void serialEvent(SerialPortEvent ev) {
        try {
            switch (ev.getEventType()) {
            case SerialPortEvent.DATA_AVAILABLE:
                int available = input.available();
                byte chunk[] = new byte[available];
                input.read(chunk, 0, available);

                // Displayed results are codepage dependent
                System.out.print(new String(chunk));
                break;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
