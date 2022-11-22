import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Steganography {
    public static void encodeText(String text, String key, String inputImage, String outputImage) {
        byte[] string = (text).getBytes();
        try {

            File file = new File(inputImage);
            BufferedImage source = ImageIO.read(file);

            char[] seedStr = key.toCharArray();
            long seed = 32;
            for (char c : seedStr) {
                seed *= (int) c;
                seed += (int) c / 2;
            }

            Random rand = new Random(seed);

            BufferedImage result = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());

            for (int x = 0; x < source.getWidth(); x++) {
                for (int y = 0; y < source.getHeight(); y++) {

                    Color color = new Color(source.getRGB(x, y));
                    result.setRGB(x, y, color.getRGB());
                }
            }
            int width = result.getWidth();

            ArrayList<Integer> poss = new ArrayList<>();
            rand.ints(0, result.getHeight() * result.getWidth()).distinct().limit(string.length + 1).forEach(poss::add);
            for (int i = 0; i <= string.length; i++) {
                int x = poss.get(i) % width, y = poss.get(i) / width;
                Color color = new Color(source.getRGB(x, y));
                int blue = color.getBlue();
                int red = color.getRed();
                int green = color.getGreen();

                int newRed = red - (red % 8);
                int newGreen = green - (green % 4);
                int newBlue = blue - (blue % 8);

                if (i != string.length) {
                    newRed += (string[i]) / 32;
                    newGreen += (string[i]) / 8 % 4;
                    newBlue += (string[i]) % 8;
                }
                Color newColor = new Color(newRed, newGreen, newBlue);

                result.setRGB(x, y, newColor.getRGB());
            }
            File output = new File(outputImage);
            ImageIO.write(result, "bmp", output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String decodeText(String inputImage, String key) {
        try {
            File file = new File(inputImage);
            BufferedImage source = ImageIO.read(file);
            StringBuilder string = new StringBuilder();
            boolean flag = true;

            char[] seedStr = key.toCharArray();
            long seed = 32;
            for (char c : seedStr) {
                seed *= (int) c;
                seed += (int) c / 2;
            }

            Random rand = new Random(seed);

            int height = source.getHeight();
            int width = source.getWidth();

            ArrayList<Integer> arr = new ArrayList<>();
            rand.ints(0, height * width).distinct().limit((long) height * width).forEach(arr::add);
            int i = 0;
            while (flag && i < arr.size()) {
                int x = arr.get(i) % width, y = arr.get(i) / width;
                Color color = new Color(source.getRGB(x, y));
                int blue = color.getBlue();
                int red = color.getRed();
                int green = color.getGreen();
                int chr = (red % 8) * 32 + (green % 4) * 8 + (blue % 8);
                if (chr == 0) {
                    flag = false;
                    break;
                }
                string.append((char) chr);
                i += 1;
            }
            return string.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void encodeImage(String inputImage, String secretImage, String outputImage) {
        try {
            File picOut = new File(inputImage);
            BufferedImage source = ImageIO.read(picOut);
            File picIn = new File(secretImage);
            BufferedImage secretSource = ImageIO.read(picIn);

            BufferedImage result = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());

            BufferedImage resized = new BufferedImage(secretSource.getWidth(), secretSource.getHeight(), secretSource.getType());
            Graphics2D g = resized.createGraphics();
            g.drawImage(secretSource, 0, 0, source.getWidth(), source.getHeight(), 0, 0, secretSource.getWidth(),
                    secretSource.getHeight(), null);
            g.dispose();
            secretSource = resized;

            for (int x = 0; x < source.getWidth(); x++) {
                for (int y = 0; y < source.getHeight(); y++) {

                    Color color = new Color(source.getRGB(x, y));
                    result.setRGB(x, y, color.getRGB());
                }
            }

            for (int i = 0; i < result.getHeight(); i++) {
                for (int j = 0; j < result.getWidth(); j++) {
                    Color color = new Color(source.getRGB(i, j));
                    int blue = color.getBlue() / 4;
                    int red = color.getRed() / 4;
                    int green = color.getGreen() / 4;

                    Color colorIn = new Color(secretSource.getRGB(i, j));
                    int blueIn = colorIn.getBlue() / 64;
                    int redIn = colorIn.getRed() / 64;
                    int greenIn = colorIn.getGreen() / 64;

                    Color newColor = new Color(red * 4 + redIn, green * 4 + greenIn, blue * 4 + blueIn);

                    result.setRGB(i, j, newColor.getRGB());
                }
            }
            File output = new File(outputImage);
            ImageIO.write(result, "bmp", output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage decodeImage(String inputImage, String outputImage, boolean saving) {
        try {
            File picOut = new File(inputImage);
            BufferedImage source = ImageIO.read(picOut);

            BufferedImage result = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());

            for (int x = 0; x < source.getWidth(); x++) {
                for (int y = 0; y < source.getHeight(); y++) {

                    Color color = new Color(source.getRGB(x, y));
                    result.setRGB(x, y, color.getRGB());
                }
            }

            for (int i = 0; i < result.getHeight(); i++) {
                for (int j = 0; j < result.getWidth(); j++) {
                    Color color = new Color(source.getRGB(i, j));
                    int blue = color.getBlue() % 4;
                    int red = color.getRed() % 4;
                    int green = color.getGreen() % 4;

                    Color newColor = new Color(red * 64, green * 64, blue * 64);

                    result.setRGB(i, j, newColor.getRGB());
                }
            }
            if (saving) {
                File output = new File(outputImage);
                ImageIO.write(result, "bmp", output);
            }
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
