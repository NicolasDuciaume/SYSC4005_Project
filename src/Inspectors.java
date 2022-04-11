import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Inspectors implements Runnable{
    private HashMap<ComponentType, ArrayList<Double>> ComponentVsTime;
    private ArrayList<Workstations> workstations;
    private boolean Running = true;
    private int Component1_inspected = 0;
    private int Component2_inspected = 0;
    private int Component3_inspected = 0;
    private double BlockedTime = 0;
    private double inspectionTime1 = 0;
    private double inspectionTime2 = 0;
    private double inspectionTime3 = 0;


    //The only reason for a hashmap here is that inspector 2 produces two components with separate data times between them
    public Inspectors(HashMap<ComponentType, ArrayList<Double>> ComponentVsTime, ArrayList<Workstations> workstations){
        this.ComponentVsTime = ComponentVsTime;
        this.workstations = workstations;
    }

    private int getWorkstationPriority(ComponentType componentType){
        //goes from priority level to find which workstation to give the component
        for(Workstations workstation : workstations)
        {

            if(workstation.getComponentVsHolding().containsKey(componentType)){
                if(workstation.getComponentVsHolding().get(componentType) == 0){
                    return workstations.indexOf(workstation);
                }
            }
        }

        //if all workstations already had 1 of that type of component we
        //search through which doesn't have two in the buffer yet
        for(Workstations workstation : workstations)
        {
            if(workstation.getComponentVsHolding().containsKey(componentType)){
                if(workstation.getComponentVsHolding().get(componentType) == 1){
                    return workstations.indexOf(workstation);
                }
            }
        }

        //returns a negative meaning that all workstations have full buffers for that
        //component
        return -1;
    }

    //Gets a new random component to give to the workstation from the list of components that inspector can pick from
    private ComponentType getNewComponent(){
        Random random = new Random();
        int rand = random.nextInt(ComponentVsTime.keySet().size());
        Object[] temp = ComponentVsTime.keySet().toArray();
        return (ComponentType) temp[rand];
    }


    //Setters and getters

    public void setRunning(boolean running) {
        Running = running;
    }

    public boolean getRunning() {
        return Running;
    }

    public void setComponentVsTime(HashMap<ComponentType, ArrayList<Double>> componentVsTime) {
        ComponentVsTime = componentVsTime;
    }

    public HashMap<ComponentType, ArrayList<Double>> getComponentVsTime() {
        return ComponentVsTime;
    }

    public void setWorkstations(ArrayList<Workstations> workstations) {
        this.workstations = workstations;
    }

    public ArrayList<Workstations> getWorkstations() {
        return workstations;
    }

    public int getComponent1_inspected() {
        return Component1_inspected;
    }

    public int getComponent2_inspected() {
        return Component2_inspected;
    }

    public int getComponent3_inspected() {
        return Component3_inspected;
    }

    public double getBlockedTime() {
        return BlockedTime;
    }

    public double getAverageBlockedTime(){
        int inspect = Component1_inspected + Component2_inspected + Component3_inspected;
        return BlockedTime/inspect;
    }

    public double getInspectionTime1() {
        return inspectionTime1;
    }

    public double getInspectionTime2() {
        return inspectionTime2;
    }

    public double getInspectionTime3() {
        return inspectionTime3;
    }

    public double getAverageInspectionTime1(){
        return inspectionTime1/getComponent1_inspected();
    }

    public double getAverageInspectionTime2(){
        return inspectionTime2/getComponent2_inspected();
    }

    public double getAverageInspectionTime3(){
        return inspectionTime3/getComponent3_inspected();
    }

    @Override
    public void run() {
        while(Running){
            ComponentType send = getNewComponent();

            double time = System.currentTimeMillis();
            try{
                Thread.sleep((long) (ComponentVsTime.get(send).remove(0)*1L));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(send == ComponentType.C1){
                inspectionTime1 = inspectionTime1 + (System.currentTimeMillis() - time);
            }
            else if(send == ComponentType.C2){
                inspectionTime2 = inspectionTime2 + (System.currentTimeMillis() - time);
            }
            else if(send == ComponentType.C3){
                inspectionTime3 = inspectionTime3 + (System.currentTimeMillis() - time);
            }

            long blocked = System.currentTimeMillis();
            int priority = getWorkstationPriority(send);
            if(priority == -1){
                while (priority == -1){
                    priority = getWorkstationPriority(send);
                }
            }
            BlockedTime = BlockedTime + (System.currentTimeMillis() - blocked);

            workstations.get(priority).put(send);
            if(send == ComponentType.C1){
                Component1_inspected++;
            }
            else if(send == ComponentType.C2){
                Component2_inspected++;
            }
            else if(send == ComponentType.C3){
                Component3_inspected++;
            }

            if(ComponentVsTime.containsKey(ComponentType.C1)){
                if(ComponentVsTime.get(ComponentType.C1).isEmpty()){
                    setRunning(false);
                }
            }
            else{
                if((ComponentVsTime.get(ComponentType.C2).isEmpty()) && !(ComponentVsTime.get(ComponentType.C3).isEmpty())){
                    ComponentVsTime.remove(ComponentType.C2);
                }
                else if(!(ComponentVsTime.get(ComponentType.C2).isEmpty()) && (ComponentVsTime.get(ComponentType.C3).isEmpty())){
                    ComponentVsTime.remove(ComponentType.C3);
                }
                else if((ComponentVsTime.get(ComponentType.C2).isEmpty()) && (ComponentVsTime.get(ComponentType.C3).isEmpty())){
                    setRunning(false);
                }
            }
        }
    }
}