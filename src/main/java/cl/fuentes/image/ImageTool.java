package cl.fuentes.image;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
//import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class ImageTool {

    /*
        Cargador de imagenes desde una carpeta o Url
    */
    public static Image imageLoader(String imgUrl){
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(imgUrl));
            //URL url = new URL(getCodeBase(), imgUrl);
            //img = ImageIO.read(url);
        } catch (IOException e) {
            System.out.println(e);
        }
        return img;
    }
    
    /*
        Escribir un archivo con la imagen
    */    
    public static void imageWrite(Image img){
        try {
            ImageIO.write((RenderedImage) img, "png",new File("imagen_"+(Math.random())*1000+".png"));
        } catch (IOException e) {
             System.out.println(e);
        }
    }
    
    /*
        Convertir Image a BufferedImage
    */        
    public static BufferedImage toBufferedImage(Image img){
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }
        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();
        // Return the buffered image
        return bimage;
    }
    
    /*
        Convertir un BufferedImage a Image
    */                
    public static Image toImage(BufferedImage bimage){
        // Casting is enough to convert from BufferedImage to Image
        Image img = (Image) bimage;
        return img;
    }
    
    /*
        Dividir una Imagen y convertirla en un arreglo de tipo BufferedImage
    */            
    public static BufferedImage[] splitImage(Image img, int rows, int cols){
        // Determine the width of each part
        int w = img.getWidth(null) / cols;
        // Determine the height of each part
        int h = img.getHeight(null) / rows;
        // Determine the number of BufferedImages to be created
        int num = rows * cols;
        // The count of images we'll use in looping
        int count = 0;
        // Create the BufferedImage array
        BufferedImage[] imgs = new BufferedImage[num];
        // Start looping and creating images [splitting]
        for (int x = 0; x < rows; x++) {
            for (int y = 0; y < cols; y++) {
                // The BITMASK type allows us to use bmp images with coloured
                // text and any background
                imgs[count] = new BufferedImage(w, h, BufferedImage.BITMASK);
                // Get the Graphics2D object of the split part of the image
                Graphics2D g = imgs[count++].createGraphics();
                // Draw only the required portion of the main image on to the
                // split image
                g.drawImage(img, 0, 0, w, h, w * y, h * x, w * y + w, h * x + h, null);
                // Now Dispose the Graphics2D class
                g.dispose();
            }
        }
        return imgs;
    }
    
    /*
        Cambiar el tamaño de la imagen
    */                
    public static Image rescale(Image img, int escala){
        // Create a null image
        Image image = null;
        int ancho = img.getWidth(null)*escala/100+img.getWidth(null);
        int alto = img.getHeight(null)*escala/100+img.getHeight(null);
        // Resize into a BufferedImage
        BufferedImage bimg = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimg.createGraphics();
        bGr.drawImage(img, 0, 0, ancho, alto, null);
        bGr.dispose();
        // Convert to Image and return it
        image = toImage(bimg);
        return image;
    }  

    /*
        Cambiar el tamaño de la imagen
    */                
    public static Image resize(Image img, int width, int height){
        // Create a null image
        Image image = null;
        // Resize into a BufferedImage
        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D bGr = bimg.createGraphics();
        bGr.drawImage(img, 0, 0, width, height, null);
        bGr.dispose();
        // Convert to Image and return it
        image = toImage(bimg);
        return image;
    }  
    /*
        Crear una imagen en mosaico con ancho y alto
    */                    
    public static Image createTiledImage(Image img, int width, int height){
        // Create a null image
        Image image = null;
        BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        // The width and height of the given image
        int imageWidth = img.getWidth(null);
        int imageHeight = img.getHeight(null);
        // Start the counting
        int numX = (width / imageWidth) + 2;
        int numY = (height / imageHeight) + 2;
        // Create the graphics context
        Graphics2D bGr = bimg.createGraphics();
        for (int y = 0; y < numY; y++) {
            for (int x = 0; x < numX; x++) {
                bGr.drawImage(img, x * imageWidth, y * imageHeight, null);
            }
        }
        // Convert and return the image
        image = toImage(bimg);
        return image;
    }
    
    /*
        Obtener una imagen vacía con ancho y alto
    */                    
    public static Image getEmptyImage(int width, int height){
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        return toImage(img);
    }    

    /*
        Obtener una imagen de color con ancho y alto
    */                        
    public static Image getColoredImage(Color color, int width, int height){
        BufferedImage img = toBufferedImage(getEmptyImage(width, height));
        Graphics2D g = img.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        g.dispose();
        return img;
    }    

    /*
        Voltear una imagen horizontalmente
    */                            
    public static Image flipImageHorizontally(Image img){
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        BufferedImage bimg = toBufferedImage(getEmptyImage(w, h));
        Graphics2D g = bimg.createGraphics();
        g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
        g.dispose();
        return toImage(bimg);
    }    

    /*
        Voltear una imagen verticalmente
    */                                
    public static Image flipImageVertically(Image img){
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        BufferedImage bimg = toBufferedImage(getEmptyImage(w, h));
        Graphics2D g = bimg.createGraphics();
        g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
        g.dispose();
        return toImage(bimg);
    }

    /*
        Clonar una imagen
    */                                    
    public static Image clone(Image img){
        BufferedImage bimg = toBufferedImage(getEmptyImage(img.getWidth(null), img.getHeight(null)));
        Graphics2D g = bimg.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        return toImage(bimg);
    }

    /*
        Rotar una imagen en un ángulo
    */                                    
    public static Image rotate(Image img, double angle){
        double sin = Math.abs(Math.sin(Math.toRadians(angle)));
        double cos = Math.abs(Math.cos(Math.toRadians(angle)));
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        int neww = (int) Math.floor(w * cos + h * sin);
        //int newh = (int) Math.floor(h * cos + w * sin);
        int newh = (int) Math.floor(h * cos + w * sin);
        
        BufferedImage bimg = toBufferedImage(getEmptyImage(neww, newh));
        //BufferedImage bimg = toBufferedImage(getColoredImage(Color.yellow, neww, newh));
        Graphics2D g = bimg.createGraphics();
        g.translate((neww - w) / 2, (newh - h) / 2);
        g.rotate(Math.toRadians(angle), w / 2, h / 2);
        g.drawRenderedImage(toBufferedImage(img), null);
        g.dispose();
        //System.out.println("x: " + img.getWidth(null) + " - y: " + img.getHeight(null));
        return toImage(bimg);
    }    
    /*
        Rota 90 grados un bufferedimage
    */                                    
    public static BufferedImage rotate90DX(BufferedImage bi) {
        int width = bi.getWidth();
        int height = bi.getHeight();
        BufferedImage biFlip = new BufferedImage(height, width, bi.getType());
        for(int i=0; i<width; i++)
            for(int j=0; j<height; j++)
                biFlip.setRGB(height-1-j, width-1-i, bi.getRGB(i, j));
        return biFlip;
    }    
    /*
        Hacer un color transparente en una imagen
    */                                    
    public static Image mask(Image img, Color color){
        BufferedImage bimg = toBufferedImage(getEmptyImage(img.getWidth(null), img.getHeight(null)));
        Graphics2D g = bimg.createGraphics();
        g.drawImage(img, 0, 0, null);
        g.dispose();
        for (int y=0; y<bimg.getHeight(); y++){
            for (int x=0; x<bimg.getWidth(); x++){
                int col = bimg.getRGB(x, y);
                if (col==color.getRGB()){
                    bimg.setRGB(x, y, col & 0x00ffffff);
                }
            }
        }
        return toImage(bimg);
    }

    /*
        Totar una imagen
    */                                    
    /*
    public static BufferedImage rotateImage(BufferedImage img, double degrees) {
	//Let's get the minimum and maximum point that our rotation will get
	Point[] xd = getMinMaxPointRotation(img.getWidth(), img.getHeight(), Math.toRadians(degrees));
	//As a consequence make the image that big
	BufferedImage rotated = new BufferedImage(xd[1].x-xd[0].x, xd[1].y-xd[0].y, img.getType());
	//We create a Graphics2D object...
	Graphics2D f = rotated.createGraphics();
	f.translate(rotated.getWidth()/2, rotated.getHeight()/2); //translate half the width and height
	f.rotate(Math.toRadians(degrees)); //rotate the image...
	f.translate(-img.getWidth()/2, -img.getHeight()/2); //translate-back to half the original width and height
	f.drawImage(img, 0, 0, null); //draw image
	f.dispose();
	return rotated;
    }
    */
    
}
