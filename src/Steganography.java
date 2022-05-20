import java.awt.Color;

public class Steganography {
    public static void clearLow(Pixel p){
        p.setRed((p.getRed()/4)*4);
        p.setGreen((p.getGreen()/4)*4);
        p.setBlue((p.getBlue()/4)*4);
    }

    public static void setLow(Pixel p, Color c){
        p.setRed( (p.getRed()/4)*4 + (c.getRed()/64) );
        p.setGreen( (p.getGreen()/4)*4 + (c.getGreen()/64) );
        p.setBlue( (p.getBlue()/4)*4 + (c.getBlue()/64) );
    }

    public static Picture testClearLow(Picture p){
        Pixel[][] p2= p.getPixels2D();
        for(int i=0; i< p2.length; i++){
            for(int x=0; x<p2[0].length; x++){
                clearLow(p2[i][x]);
            }
        }
        return p;
    }

    public static Picture testSetLow(Picture p, Color co){
        Pixel[][] p2= p.getPixels2D();
        for(int i=0; i< p2.length; i++){
            for(int x=0; x<p2[0].length; x++){
                setLow(p2[i][x], co);
            }
        }
        return p;
    }

    public static Picture revealPicture(Picture hidden){
        Picture copy= new Picture(hidden);
        Pixel[][]pixels= copy.getPixels2D();
        Pixel[][]source= hidden.getPixels2D();
        for(int i=0; i<pixels.length; i++){
            for(int x=0; x<pixels[0].length; x++){
                Color col = source[i][x].getColor();
                int red= col.getRed()%4*64;
                int blue= col.getBlue()%4*64;
                int  green=col.getGreen()%4*64;
                pixels[i][x].setRed(red);
                pixels[i][x].setBlue(blue);
                pixels[i][x].setGreen(green);
            }

        }
        return copy;
    }


    public static void main(String[] args) {
        Picture beach = new Picture ("beach.jpg");
        beach.explore();
        Picture copy= testClearLow(beach);
        copy.explore();
        Picture beach2 = new Picture ("beach.jpg");
        beach2.explore();
        Picture copy2= testSetLow(beach,Color.PINK);
        copy2.explore();
        Picture copy3= revealPicture(copy2);
        copy3.explore();
    }

}