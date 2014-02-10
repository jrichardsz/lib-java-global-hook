lib-java-global-hook
====================

Java – Global (low level) Keyboard / Mouse Hook by Kristian Kraljic 
From
http://kra.lc/blog/2011/07/java-global-system-hook/

Kristian Kraljić

You may compare programming with poetry,
a harmonic script is a piece of creativity which will last forever.
- Kristian Kraljic
Java – Global (low level) Keyboard / Mouse Hook
by Kristian Kraljic

I’d like to start my small blog with a neat little Java snippet. As I wanted to have a global key/mouse hook for my Java programs, I found a great little script using the JNI at Jotschi’s Blog. Unfortuately Jotschi doesn’t develop on any windows binaries anymore, so he can not provide enhancements. Therefore I took Jotschi’s code, fixed some bugs and enhanced especially the Java part. Here is what I did:

    Compiled and testet the native libaries on Windows Vista & 7 (x86 and x64, 32 & 64 bit JVM)
    Split the keyboard / mouse hook into two different classes and native libraries, so they can be used separately
    Refactored the code in general
        Added KeyEvent & KeyListener, MouseEvent & MouseListener classes. So the overall concept is now more likely to the standard Java AWT keyboard & mouse events
        Fixed some bugs, especially time consuming processes in the event handlers kept crashing the JNI (added a buffer mechansim)
        Added support for all keyboard events (such as key-up and also the control keys [ALT, CTRL and SHIFT] are now easier to access)
        Added support for all low level mouse events (such as mouse buttons)
        It is now guranteed that the mouse coordinate is inside the screen bounds (before -1 or 1026 have been valid values at a 1024×800 pixel resolution)
    The hook’s are now non-blocking, which means, as the last (non-deamon) thread ends, also the hook-threads end
    The native libraries are now bundled into the jar files. So it is possible, but not required to specify them as a virtual machine parameter
    Last but not least, I will try to enhance the Global Keyboard / Mouse Hook, if you have any ideas / problems with it. Feel free to write it into the comments below!

The general description: Global Keyboard / Mouse Hook for Java applications. Normally keyboard and mouse listeners in Java only work, if the registered component has the focus. If, for example, any window looses it’s focus (minimized) it isn’t possible to track any keyboard / mouse events anymore. Therefore we will have to use the JNI (Java Nativ Interface), to register a low level keyboard / mouse hook to the system. This is done using a native library (compiled on Windows 7 x64 using 32 & 64 bit JVM).

Download Source / Binaries: (Distributed under the Simplified BSD License)
KeyboardHook (Version 0.3)
