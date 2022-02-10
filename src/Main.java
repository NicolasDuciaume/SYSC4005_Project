import java.io.File;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main{
    private Workstations workstation1;
    private Workstations workstation2;
    private Workstations workstation3;
    private Thread WorkThread1;
    private Thread WorkThread2;
    private Thread WorkThread3;
    private Inspectors inspector1;
    private Inspectors inspector2;
    private Thread InspectThread1;
    private Thread InspectThread2;

    public Main(){
        ArrayList<Double> temp = new ArrayList<>();
        //as we aren't implementing the timing yet
        //I will be giving a dummy array of time
        workstation1 = new Workstations(ProductType.P1,temp);
        workstation2 = new Workstations(ProductType.P2,temp);
        workstation3 = new Workstations(ProductType.P3,temp);

        HashMap<ComponentType, ArrayList<Double>> HashforInspect1 = new HashMap<>();
        HashforInspect1.put(ComponentType.C1,temp);
        HashMap<ComponentType, ArrayList<Double>> HashforInspect2 = new HashMap<>();
        HashforInspect2.put(ComponentType.C2,temp);
        HashforInspect2.put(ComponentType.C3,temp);

        ArrayList<Workstations> ArrayforInspect1 = new ArrayList<>();
        ArrayforInspect1.add(workstation1);
        ArrayforInspect1.add(workstation2);
        ArrayforInspect1.add(workstation3);
        ArrayList<Workstations> ArrayforInspect2 = new ArrayList<>();
        ArrayforInspect2.add(workstation2);
        ArrayforInspect2.add(workstation3);

        inspector1 = new Inspectors(HashforInspect1, ArrayforInspect1);
        inspector2 = new Inspectors(HashforInspect2, ArrayforInspect2);

        WorkThread1 = new Thread(workstation1);
        WorkThread2 = new Thread(workstation2);
        WorkThread3 = new Thread(workstation3);
        InspectThread1 = new Thread(inspector1);
        InspectThread2 = new Thread(inspector2);
    }

    private void running(){
        WorkThread1.start();
        WorkThread2.start();
        WorkThread3.start();
        InspectThread1.start();
        InspectThread2.start();

        //No stop implemented as of yet as we are not including the times/data thus no indication of when to stop the process
    }

    public static void main(String[]  args){
        Main main = new Main();
        main.running();
    }
}
