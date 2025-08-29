package it.italiandudes.jamazing_centralina.utils.models;

public class Simulation {
    private boolean isEngineOn;
    private LoadedDataHandler dataHandler;

    private int pastVelocity;
    private int velocity;
    private double fuelQuantity;
    private final double FUEL_MM_CONSUMPTION = 0.044 * Math.pow(10, -6); //Fuel liters consumed per mm
    private final double FUEL_TANK_CAPACITY = 56.0; //Fuel liters the car's tank can contain.
    private double fuelPercentage;


    private boolean isParked;
    private boolean isTankEmpty;
    private boolean isReserveOn;
    private boolean isAccelerating;

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
        this.fuelPercentage = 1.0;
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
                if(this.fuelQuantity < 0.0) {
                    this.fuelQuantity = 0.0;
                    this.isTankEmpty = true;
                }

                fuelPercentage = fuelQuantity/FUEL_TANK_CAPACITY;
                if (fuelPercentage <= 0.15) this.isReserveOn = true;
            }
        }
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
}
