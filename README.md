# Bixolon SDK Test

### Problem

I am unable to connect to a connected USB device using the Android SDK.

I can detect the printer over USB to retrieve the `Set<UsbDevice>` object, but I am unable to connect to the device.

Currently, when trying to connect to the found device, the `Handler` receives the message `STATE_CONNECTING` then immediately receives `STATE_NONE`. So there is a problem with connecting, but I'm not sure what it is.

This project is a representation of our implementation in our Point-of-Sale app.
