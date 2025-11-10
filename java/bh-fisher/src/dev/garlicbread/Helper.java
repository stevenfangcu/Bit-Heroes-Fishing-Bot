package dev.garlicbread;

import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class Helper {
    private Robot robot;

    public Helper(Robot r) {
        this.robot = r;
    }

    // Move mouse
    public void moveCursor(Point p) {
        robot.mouseMove(p.x, p.y);
    }

    // Left-click
    public void mouseClick() {
        int mask = InputEvent.BUTTON1_DOWN_MASK;
        robot.mousePress(mask);
        robot.mouseRelease(mask);
    }

    // Press a key
    public void sendKey(int key) {
        robot.keyPress(key);
        robot.keyRelease(key);
    }

    // Get pixel color
    public Color getPixelColor(Point p) {
        return robot.getPixelColor(p.x, p.y);
    }

    // Compare colors with tolerance
    public boolean areColorsSimilar(Color c1, Color c2, int tolerance) {
        return Math.abs(c1.getRed() - c2.getRed()) <= tolerance &&
               Math.abs(c1.getGreen() - c2.getGreen()) <= tolerance &&
               Math.abs(c1.getBlue() - c2.getBlue()) <= tolerance;
    }

    // Click on a point and wait a short delay
    public void clickAndWait(Point p, int delayMs) throws InterruptedException {
        moveCursor(p);
        mouseClick();
        Thread.sleep(delayMs);
    }

    // Press space bar with delay
    public void pressSpaceWithDelay(int delayMs) throws InterruptedException {
        sendKey(KeyEvent.VK_SPACE);
        Thread.sleep(delayMs);
    }
}
