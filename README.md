# Intro

This is just a quick hack I am using to read some data from an
Arduino.  Communicating over the serial port in Java is one of those
things Sun never managed to get right and as a consequence serial
communication in Java means you have to mess around with native
libraries and whatnot.

I'd like to take this opportunity to tell whomever was responsible for
the serial port API at Sun: **fuck you**.  From the bottom of my
heart, fuck you for being too lazy to provide an actual
implementation.

# Installing on OSX

First put the native library somewhere on your home directory.
Whenever you want to talk to the serial port, make sure the
`DYLD_LIBRARY_PATH` is set to wherever you copied the native library.

    mkdir $HOME/lib-crap
    cp lib/librxtxSerial.jnilib $HOME/lib-crap/.
    export DYLD_LIBRARY_PATH=$HOME/lib-crap

Yeah, I know.  This is stupid.

# Building and running

To build just execute:

    mvn clean package

To run just execute:

    java -jar target/serial-capture-1.0-SNAPSHOT-jar-with-dependencies.jar <insert_serial_port_device>

For instance on my machine that would be

    java -jar target/serial-capture-1.0-SNAPSHOT-jar-with-dependencies.jar /dev/tty.usbmodemfd131 

Usually just type in the command line above up until "/dev/tty." and
then just use TAB completion to see what devices you have that look
like serial ports.

