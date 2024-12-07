package my.lab.GUIClasses;

import my.lab.model.MouseMovementData;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.lang.Math.abs;

public class MouseEventCollector {
    private final MouseDataHandler mouseDataHandler = new MouseDataHandler();

    private int previousX = -1;
    private int previousY = -1;

    public void start() {
        for (int i = 1; i <= 3; i++) {
            createWindow("Window " + i);
        }
    }

    private void createWindow(String windowName) {
        JFrame frame = new JFrame(windowName);
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.add(panel);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMouseEvent(e, windowName);
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                handleMouseEvent(e, windowName);
            }
        });

        frame.setVisible(true);
    }

    private void handleMouseEvent(MouseEvent e, String windowName) {

        int deltaX = abs(previousX == -1 ? 0 : e.getX() - previousX);
        int deltaY = abs(previousY == -1 ? 0 : e.getY() - previousY);
        MouseMovementData data = new MouseMovementData(
                e.getX(), e.getY(), deltaX, deltaY,
                System.currentTimeMillis() / 1000.0f,
                (byte) e.getButton(), windowName
        );
        mouseDataHandler.saveMouseMovement(data);

        previousX = e.getX();
        previousY = e.getY();
    }
}


