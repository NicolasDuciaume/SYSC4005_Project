import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Inspectors implements Runnable{
    private HashMap<ComponentType, ArrayList<Double>> ComponentVsTime;
    private ArrayList<Workstations> workstations;
    private boolean Running = true;

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


    @Override
    public void run() {
        while(Running){
            ComponentType send = getNewComponent();
            /*
                Here the inspection time would be placed per component
             */

            int priority = getWorkstationPriority(send);
            if(priority == -1){
                while (priority == -1){
                    // The inspector is blocked in this case
                    priority = getWorkstationPriority(send);
                }
            }

            workstations.get(priority).put(send);
        }
    }
}
