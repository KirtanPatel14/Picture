import java.awt.*;

public class Steganography {
    public static Picture testClearLow(Picture hidden){
        Picture copy = new Picture(hidden);
        Pixel[][] pixels = copy.getPixels2D();
        for(int r = 0; r<pixels.length; r++ ){
            for(int c = 0; c<pixels[0].length; c++) {
                Color col = pixels[r][c].getColor();
                clearLow();
            }
        }
        return copy;
    }
    public static void clearLow(Pixel p){
        int x = p.getRed();
        p.setRed(x/4*4);
        int y = p.getBlue();
        p.setBlue(y/4*4);
        int z = p.getGreen();
        p.setGreen(z/4*4);
    }

    public static void main(String[] args) {

    }
}
