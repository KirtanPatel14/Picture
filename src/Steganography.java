import java.awt.Color;
import java.util.ArrayList;

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

    public static boolean canHide(Picture source, Picture secret){
        if(source.getHeight() != secret.getHeight()){
            return false;
        }
        if(source.getWidth() != source.getWidth()){
            return false;
        }
        return true;
    }

    public static Picture hidePicture(Picture source, Picture secret){

        Picture s = new Picture(source);

        Pixel[][] p1 = s.getPixels2D();
        Pixel[][] p2 = secret.getPixels2D();

        for(int r = 0; r < p1.length; r++){
            for (int c = 0; c < p1[0].length; c++){
               setLow(p1[r][c], p2[r][c].getColor());
            }
        }
        return s;
    }

    public static Picture hidePicture(Picture source, Picture secret, int startRow, int startCol){

        Picture copy = new Picture(source);
        Pixel[][] pixels = copy.getPixels2D();
        Pixel[][] secretPicture = secret.getPixels2D();

        int col = secretPicture.length;
        int row = secretPicture[0].length;

        for(int r = 0; r < col; r++) {
            for (int c = 0; c < row; c++) {
                Color color = secretPicture[r][c].getColor();
                setLow(pixels[r+startRow][c+startCol], color);
            }
        }
        return copy;
    }

    public static boolean isSame(Picture pict1, Picture pict2){

        Pixel[][] pixels1 = pict1.getPixels2D();
        Pixel[][] pixels2 = pict2.getPixels2D();

        if (pixels1.length != pixels2.length ||
                pixels1[0].length != pixels2[0].length){
            return false;
        }


        for(int r = 0; r < pixels1.length; r++) {
            for (int c = 0; c < pixels1[0].length; c++) {
                Pixel p1 = pixels1[r][c];
                Pixel p2 = pixels2[r][c];
                if (p1.getRed() != p2.getRed() || p1.getGreen() != p2.getGreen() || p1.getBlue() != p2.getBlue()) {
                    return false;
                }
            }
        }
        return true;
    }

    public static ArrayList<Point> findDifferences(Picture pict1, Picture pict2){
        ArrayList<Point> plist = new ArrayList<Point>();
        Pixel[][] pixels1 = pict1.getPixels2D();
        Pixel[][] pixels2 = pict2.getPixels2D();
        if (pixels1.length != pixels2.length) return plist;
        if (pixels1[0].length != pixels2[0].length) return plist;

        for(int r = 0; r < pixels1.length; r++) {
            for (int c = 0; c < pixels1[0].length; c++) {
                Pixel p1 = pixels1[r][c];
                Pixel p2 = pixels2[r][c];
                if(p1.getRed()!= p2.getRed() || p1.getGreen() != p2.getGreen() || p1.getBlue() != p2.getBlue()) plist.add(new Point(r,c));
            }
        }
        return plist;
    }

    public static Picture showDifferentArea(Picture picture, ArrayList<Point> points){

        Picture newpict = new Picture(picture);
        Pixel[][] pixels = newpict.getPixels2D();

        int maxr = 0;
        int maxc = 0;
        int minr = picture.getHeight();
        int minc = picture.getWidth();

        for(Point p: points){
            int r = p.getRow();
            if(r > maxr) maxr = r;
            if(r < minr) minr = r;
            int c = p.getCol();
            if(c > maxc) maxc = c;
            if(c < minc) minc = c;
        }

        for(int r = minr; r <= maxr; r++){
            pixels[r][minc].setColor(Color.YELLOW);
            pixels[r][maxc].setColor(Color.yellow);
        }

        for(int c = minc; c <= maxc; c++){
            pixels[minr][c].setColor(Color.YELLOW);
            pixels[maxr][c].setColor(Color.yellow);
        }
        return newpict;
    }

    public static ArrayList<Integer> encodeString(String s){
        s = s.toUpperCase();
        String alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
        ArrayList<Integer> result=new ArrayList<Integer>();
        for(int i=0; i<s.length(); i++){
            result.add(alpha.indexOf(s.charAt(i))+1);
        }
        result.add(0);
        return result;
    }

    public static String decodeString(ArrayList<Integer> codes){
        String result="";
        String alpha="ABCDEFGHIJKLMNOPQRSTUVWXYZ ";
        for(int i=0; i<codes.size(); i++){
            if(codes.get(i)==0)return result;
            result+= alpha.charAt(codes.get(i)-1);
        }
        return result;
    }

    private static int[] getBitPairs(int num){
        int[] arr= new int[3];
        arr[2]=num/16;
        arr[1]=num/4%4;
        arr[0]=num%4;
        return arr;
    }

    public static Picture hideText(Picture source, String s) {
        Picture copy= new Picture(source);
        Pixel[][] pixels= copy.getPixels2D();
        ArrayList<Integer> arrayList= encodeString(s);
        int[][]arr= new int[encodeString(s).size()][3];
        int x=0;


        for(int i=0; i<encodeString(s).size(); i++){
            arr[i] = getBitPairs(arrayList.get(i));
        }

        for (int row=0; row< source.getHeight(); row++){
            for (int col=0; col<source.getWidth(); col++){
                pixels[row][col].setRed(arr[x][0]+ pixels[row][col].getRed()/4*4);
                pixels[row][col].setGreen(arr[x][1]+ pixels[row][col].getRed()/4*4);
                pixels[row][col].setBlue(arr[x][2]+ pixels[row][col].getRed()/4*4);
                x++;


                if(x>s.length())return copy;
            }
        }
        return copy;

    }
    //
    public static String revealText(Picture source){
        Picture copy= new Picture(source);
        Pixel[][] pixels= copy.getPixels2D();
        ArrayList<Integer>encoded= new ArrayList<Integer>();



        for (int row=0; row< pixels.length; row++){
            for (int col=0; col<pixels[0].length; col++){
                Pixel p=pixels[row][col];
                int x=p.getRed()%4+p.getGreen()%4*4+p.getBlue()%4*16;
                encoded.add(x);
                if(x==0){return decodeString(encoded);}

            }
        }
        return decodeString(encoded);
    }

    public static void main(String[] args) {
        //PART 1

//        Picture beach = new Picture("beach.jpg");
//        Picture robot = new Picture("robot.jpg");
//        Picture flower1 = new Picture("flower1.jpg");
//        beach.explore();
//
//        Picture hidden1 = hidePicture(beach, robot, 65, 208);
//        Picture hidden2 = hidePicture(hidden1, flower1, 280, 110);
//        hidden2.explore();
//
//        Picture unhidden = revealPicture(hidden2);
//        unhidden.explore();
//
//        //PART 2
//
//        Picture swan = new Picture("swan.jpg");
//        Picture swan2 = new Picture("swan.jpg");
//        System.out.println("Swan and Swan 2 are the same: " + isSame(swan, swan2));
//        swan = testClearLow(swan);
//        System.out.println("Swan and swan2 are the same (after clearLow run on swan): " + isSame(swan, swan2));
//
//        //PART 3
//
//        Picture arch = new Picture("arch.jpg");
//        Picture koala = new Picture("koala.jpg");
//        Picture robot1 = new Picture("robot.jpg");
//        ArrayList<Point> pointlist = findDifferences(arch, arch);
//        System.out.println("PointList after comparing two identical pictures has a size of " + pointlist.size());
//        pointlist = findDifferences(arch, koala);
//        System.out.println("PointList after comparing two different sized pictures has a size of " + pointlist.size());
//        Picture arch2 = hidePicture(arch, robot1, 65,102);
//        pointlist = findDifferences(arch, arch2);
//        System.out.println("PointList after hiding a picture has a size of " + pointlist.size());
//        arch.explore();
//        arch2.explore();
//
//        //PART 4
//
//        Picture hall  = new Picture("femaleLionAndHall.jpg");
//        Picture robot2 = new Picture("robot.jpg");
//        Picture flower2 = new Picture("flower1.jpg");
//
//        //hide pictures
//        Picture hall2 = hidePicture(hall, robot2, 50,300);
//        Picture hall3 = hidePicture(hall2, flower2, 115, 275);
//        hall3.explore();
//
//        if(!isSame(hall, hall3)){
//            Picture hall4 = showDifferentArea(hall, findDifferences(hall,hall3));
//            hall4.explore();
//            Picture unhiddenHall3 = revealPicture(hall3);
//            unhiddenHall3.explore();
//        }
        String preamble = "We the People of the United States in Order to form a more perfect Union establish Justice insure domestic Tranquility provide for the common defence promote the general Welfare and secure the Blessings of Liberty to ourselves and our Posterity do ordain and establish this Constitution for the United States of America";

        Picture beach = new Picture ("beach.jpg");
        beach.explore();

        Picture hide= hideText(beach, preamble);
        hide.explore();
        System.out.println(revealText(hide));

    }

}
