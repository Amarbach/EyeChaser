/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package structure.calibration;

import com.theeyetribe.client.GazeManager;
import com.theeyetribe.client.ICalibrationProcessHandler;
import com.theeyetribe.client.data.CalibrationResult;
import com.theeyetribe.client.data.Point2D;
import components.view.ColoredEllipse;
import components.view.ColoredShape;
import components.view.Drawable;
import java.awt.Color;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import structure.Model;

/**
 *
 * @author Artur
 */
public class CalibrationModel extends Model implements ICalibrationProcessHandler{
        
    private ArrayList<Point2D> calibrationPoints;
    private int currentPointIndex;
    private int timeElapsed;
    private boolean isCalibrating;
    private boolean hasEnded;
    private boolean hasStarted;
    private boolean isConnected;
    
    private final GazeManager gazeManager;
    
    private String infoText;

    public CalibrationModel(int width, int height) {
        gazeManager  = GazeManager.getInstance();
        isConnected = gazeManager.isConnected();
        hasStarted = false;
        hasEnded = false;
        infoText = "";
        timeElapsed = 0;
        isCalibrating = false;
        calibrationPoints = new ArrayList<>();
        calibrationPoints.add(new Point2D(width * 0.2f, height * 0.2f));
        calibrationPoints.add(new Point2D(width * 0.5f, height * 0.2f));
        calibrationPoints.add(new Point2D(width * 0.8f, height * 0.2f));
        calibrationPoints.add(new Point2D(width * 0.2f, height * 0.5f));
        calibrationPoints.add(new Point2D(width * 0.5f, height * 0.5f));
        calibrationPoints.add(new Point2D(width * 0.8f, height * 0.5f));
        calibrationPoints.add(new Point2D(width * 0.2f, height * 0.8f));
        calibrationPoints.add(new Point2D(width * 0.5f, height * 0.8f));
        calibrationPoints.add(new Point2D(width * 0.8f, height * 0.8f));
        currentPointIndex = 0;
    }

    @Override
    public void update() {
        if (hasStarted && isConnected && !hasEnded) {
            for (int i = 0; i < calibrationPoints.size(); i++) {
                if (timeElapsed == 60 + 120 * i) {
                    calibratePoint();
                } else if (timeElapsed == 120 + 120 * i) {
                    nextPoint();
                } 
            }
            timeElapsed += 1;
        }
    }

    @Override
    public ArrayList<Drawable> getDrawableObjects() {
        ArrayList<Drawable> ret = new ArrayList<>();
        Color shapeColor;
        if (!isCalibrating) shapeColor = Color.YELLOW;
        else shapeColor = Color.GREEN;
        Point2D curPoint = calibrationPoints.get(currentPointIndex);
        if (!hasEnded && hasStarted) ret.add(new ColoredEllipse(shapeColor, new Ellipse2D.Double(curPoint.x - 20, curPoint.y - 20, 40, 40)));
        return ret;
    }

    @Override
    public void onCalibrationStarted() {
        
    }

    @Override
    public void onCalibrationProgress(double d) {
    }

    @Override
    public void onCalibrationProcessing() {
        infoText = "Processing, please wait.";
    }

    @Override
    public void onCalibrationResult(CalibrationResult cr) {
        hasEnded = true;
        if(cr.result) infoText = "Success.";
        else infoText = "Failed. Please try again.";
    }
    
    public String getInfoText(){
        return infoText;
    }
    
    private void nextPoint(){
        gazeManager.calibrationPointEnd();
        if(currentPointIndex < 8) currentPointIndex += 1;
        else currentPointIndex = 0;
        isCalibrating = false;
    }
    
    private void calibratePoint(){
        isCalibrating = true;
        Point2D curPoint = calibrationPoints.get(currentPointIndex);
        gazeManager.calibrationPointStart((int)curPoint.x, (int)curPoint.y);
    }
    
    public void startCalibration(){
        hasStarted = true;
        gazeManager.calibrationStart(9, this);
    }

    public boolean hasEnded() {
        return hasEnded;
    }
    
    public void reset(){
        hasEnded = false;
        hasStarted = false;
        timeElapsed = 0;
        isCalibrating = false;
    }
}
