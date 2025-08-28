package it.italiandudes.jamazing_centralina.utils.models;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class LoadedDataHandler {

    private final double G = 9.81;

    private ParsedSerialData parsedSerialData;
    private IntArrayPile distanceDataBatch;
    private IntArrayPile humidityDataBatch;
    private IntArrayPile temperatureDataBatch;
    private IntArrayPile pressureDataBatch;
    private DoubleArrayPile accelerationDataBatch;
    private DoubleArrayPile velocityDataBatch;
    private DoubleArrayPile degreeRateBatch;
    private double[] pastAcc;
    private double alpha;

    private double rollGyro;
    private double pitchGyro;
    private double roll;
    private double pitch;

    private boolean isInitFilter;

    public void initData(ParsedSerialData parsedSerialData, int maxDistanceBatchSize, int maxHumidityBatchSize,
                            int maxTemperatureBatchSize, int maxPressureBatchSize, int maxAccelerationBatchSize,
                            int maxDegreeRateBatchSize, double alpha){
        this.parsedSerialData = new ParsedSerialData(parsedSerialData);
        this.distanceDataBatch = new IntArrayPile(maxDistanceBatchSize);
        this.humidityDataBatch = new IntArrayPile(maxHumidityBatchSize);
        this.temperatureDataBatch = new IntArrayPile(maxTemperatureBatchSize);
        this.pressureDataBatch = new IntArrayPile(maxPressureBatchSize);
        this.accelerationDataBatch = new DoubleArrayPile(maxAccelerationBatchSize);
        this.velocityDataBatch = new DoubleArrayPile(maxAccelerationBatchSize);
        this.degreeRateBatch = new DoubleArrayPile(maxDegreeRateBatchSize);
        this.alpha = alpha;

        distanceDataBatch.addElement(parsedSerialData.getDistance());
        humidityDataBatch.addElement(parsedSerialData.getHumidity());
        temperatureDataBatch.addElement(parsedSerialData.getTemperature());
        pressureDataBatch.addElement(parsedSerialData.getPressure());
        accelerationDataBatch.addElement(0.0);
        velocityDataBatch.addElement(0.0);

        int[] accelerations = parsedSerialData.getAcceleration();
        double[] presentAcc = new double[3];
        presentAcc[0] = Math.pow(accelerations[0], -3);
        presentAcc[1] = Math.pow(accelerations[1], -3);
        presentAcc[2] = Math.pow(accelerations[2], -3);
        degreeRateBatch.addElement(getPitchAngle(presentAcc[0], presentAcc[1], presentAcc[2],
                parsedSerialData.getDegreeRates()[0], parsedSerialData.getTimePeriod()));
        pastAcc = presentAcc.clone();

        this.isInitFilter = true;
    }

    public void updateData(ParsedSerialData parsedSerialData){
        this.parsedSerialData = new ParsedSerialData(parsedSerialData);

        distanceDataBatch.addElement(parsedSerialData.getDistance());
        humidityDataBatch.addElement(parsedSerialData.getHumidity());
        temperatureDataBatch.addElement(parsedSerialData.getTemperature());
        pressureDataBatch.addElement(parsedSerialData.getPressure());

        int[] accelerations = parsedSerialData.getAcceleration();
        double[] presentAcc = new double[3];
        presentAcc[0] = accelerations[0];
        presentAcc[1] = accelerations[1];
        presentAcc[2] = accelerations[2];

        //System.out.println("Present acc: \n" + Arrays.toString(presentAcc));
        accelerationDataBatch.addElement(getAccModule(presentAcc[0], presentAcc[1], presentAcc[2]));
        velocityDataBatch.addElement(getVelocityModule(presentAcc, parsedSerialData.getTimePeriod(),
                parsedSerialData.getDegreeRates()[0], parsedSerialData.getDegreeRates()[1], this.isInitFilter));
        if(this.isInitFilter){
            this.isInitFilter = false;
        }
        System.out.println("Velocity batch: \n" + velocityDataBatch);
        degreeRateBatch.addElement(getPitchAngle(presentAcc[0], presentAcc[1], presentAcc[2],
                Math.pow(parsedSerialData.getDegreeRates()[0], -3), parsedSerialData.getTimePeriod()));
    }

    private double getPitchAngle(double accX, double accY, double accZ, double gyroX, int dt) {
        double accelPitch = Math.atan2(-accX, Math.sqrt(accY * accY + accZ * accZ)) * (180.0 / Math.PI);

        double pitchAngle = gyroX * dt * Math.pow(10, -6);

        pitchAngle = this.alpha * pitchAngle + (1.0 - this.alpha) * accelPitch;

        return pitchAngle;
    }

    // This method takes into consideration the fact that the velocity is actually a three-dimensional
    // vector. It returns a double only containing the actual velocity module after using the past and present
    // accelerations to obtain the velocity between 2 points in a 3D space.
    private double getVelocityModule(double @NotNull [] presentAcc, double gyroX, double gyroY, int dt, boolean isInit){
        double[] actualPresentAcc = new double[3];
        actualPresentAcc[0] = presentAcc[0] * G;
        actualPresentAcc[1] = presentAcc[1] * G;
        actualPresentAcc[2] = presentAcc[2] * G;
        double actualDt = dt * Math.pow(10, -6);

        //System.out.println("ActualPresentAcc: \n[" + actualPresentAccX + ", " + actualPresentAccY + ", " + actualPresentAccZ + "]");

        //Complementary filter: used to delete the G component from all axes
        double rollAcc = Math.atan2(actualPresentAcc[1], Math.sqrt(actualPresentAcc[0] * actualPresentAcc[0] + actualPresentAcc[2] * actualPresentAcc[2]));
        double pitchAcc = Math.atan2(-actualPresentAcc[0], Math.sqrt(actualPresentAcc[1] * actualPresentAcc[1] + actualPresentAcc[2] * actualPresentAcc[2]));


        if(isInit){
            this.rollGyro = rollAcc;
            this.roll = rollAcc;
            this.pitchGyro = pitchAcc;
            this.pitch = pitchAcc;
        }else{
            this.rollGyro += gyroX * (Math.PI/180.0) * actualDt;
            this.pitchGyro += gyroY * (Math.PI/180.0) * actualDt;

            roll = this.alpha * this.rollGyro + (1 - this.alpha) * rollAcc;
            pitch = this.alpha * this.pitchGyro + (1 - this.alpha) * pitchAcc;
        }

        double gX = G * Math.pow(10, 3) * Math.sin(pitch);
        double gY = -G * Math.pow(10, 3) * Math.sin(roll);
        double gZ = G * Math.cos(pitch) * Math.cos(roll);
        
        double[] filteredAcc = new double[3];
        filteredAcc[0] = actualPresentAcc[0] - gX;
        filteredAcc[1] = actualPresentAcc[1] - gY;
        filteredAcc[2] = actualPresentAcc[2] - gZ;

        double[] accAvg = new double[3];
        accAvg[0] = (filteredAcc[0] + (this.pastAcc[0]))/2.0;
        accAvg[1] = (filteredAcc[1] + (this.pastAcc[1]))/2.0;
        accAvg[2] = (filteredAcc[2] + (this.pastAcc[2]))/2.0;

        System.out.println("AccAvg: \n" + Arrays.toString(accAvg));

        double[] finalVectorialVel = new double[3];
        finalVectorialVel[0] = accAvg[0] * actualDt;
        finalVectorialVel[1] = accAvg[1] * actualDt;
        finalVectorialVel[2] = accAvg[2] * actualDt;

        //System.out.println("FinalVectorialVel: \n" + Arrays.toString(finalVectorialVel));

        this.pastAcc = finalVectorialVel.clone();
        double finalVelocityModule = Math.sqrt(finalVectorialVel[0] * finalVectorialVel[0] + finalVectorialVel[1] * finalVectorialVel[1] +
                finalVectorialVel[2] * finalVectorialVel[2]);
        if(finalVelocityModule > 100.0){
            return finalVelocityModule;
        }else{
            return 0.0;
        }
    }

    // This method takes into consideration the fact that the acceleration is actually a three-dimensional
    // vector. It returns a double only containing the actual acceleration module.
    private double getAccModule(double accX, double accY, double accZ){
        return Math.sqrt(accX * accX + accY * accY + accZ * accZ);
    }

    public IntArrayPile getDistanceDataBatch() {
        return distanceDataBatch;
    }

    public IntArrayPile getHumidityDataBatch() {
        return humidityDataBatch;
    }

    public IntArrayPile getTemperatureDataBatch() {
        return temperatureDataBatch;
    }

    public IntArrayPile getPressureDataBatch() {
        return pressureDataBatch;
    }

    public DoubleArrayPile getAccelerationDataBatch() {
        return accelerationDataBatch;
    }

    public DoubleArrayPile getVelocityDataBatch() {
        return velocityDataBatch;
    }

    public DoubleArrayPile getDegreeRateBatch() {
        return degreeRateBatch;
    }
}
