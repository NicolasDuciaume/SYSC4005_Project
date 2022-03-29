import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Workstations implements Runnable{
    private HashMap<ComponentType,Integer> ComponentVsHolding = new HashMap<>();
    private ArrayList<Double> TimeToProduce = new ArrayList<>();
    private Boolean Running = true;
    private int ProductProduced = 0;
    private double SumOfTimeToProduce = 0;

    public Workstations(ProductType ProductToProduce, ArrayList<Double> Times){
        //Initialize the variables to change which workstation it is
        TimeToProduce = Times; // the list of service times from the .dat files

        // These take in what kind of product is to be produce and
        // Sets the components / components needed to produce that product
        // While holding the amount of times that product is found within the buffer
        if(ProductToProduce.equals(ProductType.P1)){
            ComponentVsHolding.put(ComponentType.C1,0);
        }
        else if(ProductToProduce.equals(ProductType.P2)){
            ComponentVsHolding.put(ComponentType.C1,0);
            ComponentVsHolding.put(ComponentType.C2,0);
        }
        else if(ProductToProduce.equals(ProductType.P3)){
            ComponentVsHolding.put(ComponentType.C1,0);
            ComponentVsHolding.put(ComponentType.C3,0);
        }
    }

    // Assess if the workstation's buffer is already full to wait until the product has
    // been produced or else it add's that component to the buffer thus incrementing
    // The amount of times that component is found within the buffer by 1.
    public synchronized void put(ComponentType componentType){
        while (ComponentVsHolding.get(componentType) == 2){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        ComponentVsHolding.put(componentType, ComponentVsHolding.get(componentType) + 1);
        notifyAll();
    }

    //This method is used to produce the product once each component needed is found
    // The return type is an int for the simple reason of canceling the production
    // if the for loop at the start to check that all needed components are found
    // results in finding a missing component thus ends the function early
    private int Produce(){
        for(ComponentType componentType : ComponentVsHolding.keySet()){
            if(ComponentVsHolding.get(componentType) == 0){
                return 0;
            }
        }

        double time = System.currentTimeMillis();
        try{
            Thread.sleep((long) (TimeToProduce.remove(0) * 1L));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        SumOfTimeToProduce = SumOfTimeToProduce + (System.currentTimeMillis() - time);

        for(ComponentType componentType : ComponentVsHolding.keySet()){
            ComponentVsHolding.put(componentType, ComponentVsHolding.get(componentType) - 1);
        }
        ProductProduced++;
        return 0;
    }

    // The remainder are basic setters and getters
    public Boolean getRunning(){
        return Running;
    }

    public void setRunning(Boolean bool){
        Running = bool;
    }

    public ArrayList<Double> getTimeToProduce() {
        return TimeToProduce;
    }

    public void setTimeToProduce(ArrayList<Double> timeToProduce) {
        TimeToProduce = timeToProduce;
    }

    public HashMap<ComponentType, Integer> getComponentVsHolding() {
        return ComponentVsHolding;
    }

    public void setComponentVsHolding(HashMap<ComponentType, Integer> componentVsHolding) {
        ComponentVsHolding = componentVsHolding;
    }

    public double getSumOfTimeToProduce() {
        return SumOfTimeToProduce;
    }

    public int getProductProduced() {
        return ProductProduced;
    }

    public double getAverageTime(){
        return SumOfTimeToProduce/ProductProduced;
    }

    @Override
    public void run() {
        while(Running){
            Produce();
        }
    }
}
