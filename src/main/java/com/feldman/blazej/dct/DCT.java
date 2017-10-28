package com.feldman.blazej.dct;


import com.feldman.blazej.presenter.WatermarkPresenter;
import com.feldman.blazej.util.JpegWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * Created by Błażej on 05.09.2017.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DCT{

    private int N = 16;
    private double[] c = new double[N];
    private BufferedImage bufImgs;
    private double[][] F;
    private int[][] f= new int[N][N];
    private BufferedImage bufOut;
    private int weigth = 5;
    private int height= 7;

    @Autowired
    public WatermarkPresenter watermarkPresenter;

    public DCT() throws IOException {
        initializeCoefficients();

    }
    public void setChange(){

        F[weigth][height] = F[weigth][height] + (watermarkPresenter.getIndexNumber()*3);

    }

    public double getDctParam(File file){

        loadPicture(file);
        F = applyDCT(f);
        return F[weigth][height];
    }
    public void createWatermarkWithDct(File input, File output){
        loadPicture(input);
        F = applyDCT(f);
        setChange();
        goneIDCT();
        JpegWriter.createPicture(output,"jpg",bufOut);

    }
    private void initializeCoefficients() {
        for (int i=1;i<N;i++) {
            c[i]=1;
        }
        c[0]=1/Math.sqrt(2.0);
    }

    public double[][] applyDCT(int[][] f) {
        double[][] F = new double[N][N];
        for (int u=0;u<N;u++) {
            for (int v=0;v<N;v++) {
                double sum = 0.0;
                for (int i=0;i<N;i++) {
                    for (int j=0;j<N;j++) {
                        sum+=Math.cos(((2*i+1)/(2.0*N))*u*Math.PI)*
                                Math.cos(((2*j+1)/(2.0*N))*v*Math.PI)*f[i][j];
                    }
                }
                sum*=((c[u]*c[v])/4.0);
                F[u][v]=sum;
            }
        }return F;
    }
    public int[][] applyIDCT(double[][] F) {
        int[][] f = new int[N][N];
        for (int i=0;i<N;i++) {
            for (int j=0;j<N;j++) {
                double sum = 0.0;
                for (int u=0;u<N;u++) {
                    for (int v=0;v<N;v++) {
                        sum+=(2*c[u]*c[v]/N)*Math.cos(((2*i+1)/(2.0*N))*u*Math.PI)
                                *Math.cos(((2*j+1)/(2.0*N))*v*Math.PI)*F[u][v];
                    }
                }
                f[i][j]=(int)Math.round(sum);
            }
        }return f;
    }
    public BufferedImage loadPicture(File file){
        try {
            bufImgs = ImageIO.read(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        for( int w=0;w<N;w++){
            for(int k =0;k<N;k++){
                f[w][k] = bufImgs.getRGB(w, k);
            }
        }
        return bufImgs;
    }

    public void goneIDCT(){
        f = applyIDCT(F);
        bufOut = new BufferedImage(bufImgs.getWidth(),bufImgs.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y=0;y < bufImgs.getWidth();y++) {
            for (int z=0;z<bufImgs.getHeight();z++) {
                if((y<N)&&(z<N)) {
                    bufOut.setRGB(y, z, f[y][z]*8/N);
                }
                else{
                    bufOut.setRGB(y,z,bufImgs.getRGB(y,z));
                }
            }
        }
    }
}