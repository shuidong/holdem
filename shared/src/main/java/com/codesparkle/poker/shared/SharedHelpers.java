package com.codesparkle.poker.shared;

import com.codesparkle.poker.shared.net.NetException;

import javax.swing.*;
import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public final class SharedHelpers {

    public static void close(Closeable... closeables) {
        for (Closeable c : closeables) {
            try {
                c.close();
            } catch (IOException e) {
                throw new NetException(e);
            }
        }
    }

    public static void close(ServerSocket serverSocket) {
        close(closeableFrom(serverSocket));
    }
    
    /*
     * In Java 7, java.com.codesparkle.poker.shared.net.Socket will implement Closeable, making this helper method obsolete.
     */
    public static Closeable closeableFrom(final Socket socket) {
        return new Closeable() {

            @Override
            public void close() throws IOException {
                socket.close();
            }
        };
    }

    /*
     * In Java 7, java.com.codesparkle.poker.shared.net.ServerSocket will implement Closeable, making this helper method obsolete.
     */
    private static Closeable closeableFrom(final ServerSocket socket) {
        return new Closeable() {

            @Override
            public void close() throws IOException {
                socket.close();
            }
        };
    }

    public static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // who cares, in this case java loads default L&F anyway
        }
    }

    public static boolean isPositiveNumber(String string) {
        if (string.length() == 0)
            return false;
        for (char c : string.toCharArray()) {
            if (c <= '/' || c >= ':') {
                return false;
            }
        }
        return true;
    }

    public static <T> String join(T[] source, String delimiter) {
        List<T> s = Arrays.asList(source);
        Iterator<T> iter = s.iterator();
        StringBuilder builder = new StringBuilder(iter.next().toString());
        while (iter.hasNext()) {
            builder.append(delimiter).append(iter.next());
        }
        return builder.toString();
    }

    public static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
