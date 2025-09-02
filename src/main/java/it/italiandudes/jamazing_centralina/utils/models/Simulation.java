package it.italiandudes.jamazing_centralina.utils.models;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
public final class Simulation {

    // Constants
    private final double FUEL_MM_CONSUMPTION = 0.044 * Math.pow(10, -6); // Fuel liters consumed per mm
    private final double FUEL_TANK_CAPACITY = 56.0; // Fuel liters the car's tank can contain.
    private final int TEMPERATURE_THRESHOLD = 30;
    private final double TIRE_LOW_THRESHOLD = 1000.0;
    private final double ABS_TERRAIN_SLOPE_THRESHOLD = 15.0;
    private final double MIN_FUEL_QUANTITY = 0.0;
    private final double FUEL_RESERVE_THRESHOLD = 0.15;
    private final int PROXIMITY_THRESHOLD = 20;

    private final LoadedDataHandler dataHandler;

    private int pastVelocity;
    private int velocity;
    private double fuelQuantity;
    private double fuelPercentage;

    private boolean isEngineOn;
    private boolean isParked;
    private boolean isTankEmpty;
    private boolean isReserveOn;
    private boolean isAccelerating;
    private boolean isEngineTempTooHigh;
    private boolean isTerrainSlope;
    private boolean isTireLow;
    private boolean isProximity;

    public Simulation(LoadedDataHandler dataHandler){
        this.isEngineOn = false;
        this.dataHandler = dataHandler;
        this.velocity = 0;
        this.isParked = true;
        this.pastVelocity = 0;
        this.isTankEmpty = false;
        this.isAccelerating = false;
        this.fuelQuantity = FUEL_TANK_CAPACITY;
        this.isReserveOn = false;
        this.fuelPercentage = fuelQuantity/FUEL_TANK_CAPACITY;
        this.isEngineTempTooHigh = false;
        this.isTerrainSlope = false;
        this.isTireLow = false;
        this.isProximity = false;
    }

    public void runSimCycle(){
        if(this.isEngineOn){
            if(!this.isTankEmpty){
                this.velocity = (int) this.dataHandler.getVelocityDataBatch().getLastElement();

                this.isAccelerating = this.velocity > this.pastVelocity;

                if (this.isParked && this.isAccelerating && this.pastVelocity==0) {
                    this.isParked = false;
                }else if(!this.isParked && !this.isAccelerating && this.velocity==0){
                    this.isParked = true;
                }

                this.fuelQuantity -= FUEL_MM_CONSUMPTION * this.velocity;
                if(this.fuelQuantity < MIN_FUEL_QUANTITY) {
                    this.fuelQuantity = MIN_FUEL_QUANTITY;
                    this.isTankEmpty = true;
                }

                this.fuelPercentage = this.fuelQuantity/FUEL_TANK_CAPACITY;
                if (this.fuelPercentage <= FUEL_RESERVE_THRESHOLD) this.isReserveOn = true;

                this.isEngineTempTooHigh = this.dataHandler.getTemperatureDataBatch().getLastElement() > TEMPERATURE_THRESHOLD;

                this.isTerrainSlope = (this.dataHandler.getDegreeRateBatch().getLastElement() > ABS_TERRAIN_SLOPE_THRESHOLD || this.dataHandler.getDegreeRateBatch().getLastElement() < -ABS_TERRAIN_SLOPE_THRESHOLD);

                this.isTireLow = this.dataHandler.getPressureDataBatch().getLastElement() < TIRE_LOW_THRESHOLD;

                this.pastVelocity = this.velocity;

                this.isProximity = this.dataHandler.getDistanceDataBatch().getLastElement() > 0 && this.dataHandler.getDistanceDataBatch().getLastElement() < PROXIMITY_THRESHOLD;
            }
        }
        //System.out.println(this);
    }

    public void setEngine(){
        this.isEngineOn = !this.isEngineOn;
        if(this.isEngineOn) isParked = true;
    }

    public boolean isEngineOn(){
        return this.isEngineOn;
    }

    public double getFuelPercentage() {
        return fuelPercentage;
    }

    public boolean isParked() {
        return isParked;
    }

    public boolean isTankEmpty() {
        return isTankEmpty;
    }

    public boolean isReserveOn() {
        return isReserveOn;
    }

    public boolean isAccelerating() {
        return isAccelerating;
    }

    public int getVelocity() {
        return velocity;
    }

    public boolean isEngineTempTooHigh() {
        return isEngineTempTooHigh;
    }

    public boolean isTerrainSlope() {
        return isTerrainSlope;
    }

    public boolean isTireLow() {
        return isTireLow;
    }

    public boolean isProximity() {
        return isProximity;
    }

    @Override
    public String toString() {
        return "\n\nSim data:" +
        "\n\tIs engine on: " + this.isEngineOn +
        "\n\tFuel %: " + this.fuelPercentage +
        "\n\tFuel quantity: " + this.fuelQuantity +
        "\n\tVelocity: " + this.velocity +
        "\n\tTire pressure: " + this.dataHandler.getPressureDataBatch().getLastElement();
    }
}
