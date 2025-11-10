package dev.garlicbread;

import java.awt.*;

public class Main {
    private static Helper helper;
    private static Robot robot;

    // Example colors - adjust these based on your screen/game
    private static Color startButtonGreen = new Color(164, 209, 48);
    private static Color caughtFishColor = new Color(56, 255, 56);
    private static Color junkColor = new Color(255, 255, 255);

    private static Point startButtonLocation;

    public static void main(String[] args) throws AWTException, InterruptedException {
        if (args.length < 1) {
            System.out.println("Usage: java -jar BitHeroes.jar <number_of_bait>");
            System.exit(0);
        }

        int baitCount = Integer.parseInt(args[0]);

        robot = new Robot();
        helper = new Helper(robot);

        // Find the start button
        System.out.println("Searching for start button...");
        startButtonLocation = findColor(startButtonGreen);
        if (startButtonLocation == null) {
            System.out.println("Start button not found. Exiting.");
            System.exit(1);
        }
        System.out.println("Start button found at: " + startButtonLocation);

        // Start fishing loop
        startFishing(baitCount);
    }

    // Search screen for a color with tolerance
    private static Point findColor(Color targetColor) {
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        for (int x = 0; x < screenDim.width; x += 5) {
            for (int y = 0; y < screenDim.height; y += 5) {
                Color c = robot.getPixelColor(x, y);
                if (helper.areColorsSimilar(c, targetColor, 10)) {
                    return new Point(x, y);
                }
            }
        }
		System.out.println("Could not find the button");
        return null;
    }

    // Main fishing loop
    private static void startFishing(int baitCount) throws InterruptedException {
        for (int i = 0; i < baitCount; i++) {
            System.out.println("Casting bait " + (i + 1) + "/" + baitCount);

            // Click start button
            helper.clickAndWait(startButtonLocation, 500);

            // Wait for fish or junk with timeout
            boolean caught = false;
            long startTime = System.currentTimeMillis();
            Point checkPoint = new Point(startButtonLocation.x, startButtonLocation.y - 50); // adjust if needed

            while (!caught) {
                Color pixel = helper.getPixelColor(checkPoint);

                if (helper.areColorsSimilar(pixel, caughtFishColor, 10)) {
                    System.out.println("Fish caught!");
                    helper.pressSpaceWithDelay(500); // collect fish
                    caught = true;
                } else if (helper.areColorsSimilar(pixel, junkColor, 10)) {
                    System.out.println("Junk caught.");
                    helper.pressSpaceWithDelay(500);
                    caught = true;
                }

                // Timeout after 8 seconds to avoid infinite loop
                if (System.currentTimeMillis() - startTime > 8000) {
                    System.out.println("No fish detected. Retrying cast...");
                    break;
                }

                Thread.sleep(200);
            }

            Thread.sleep(500); // short delay before next bait
        }

        System.out.println("Fishing finished.");
    }
}
