import java.io.File;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main implements Runnable{
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
    private double time;

    public Main(){
        //as we aren't implementing the timing yet
        //I will be giving a dummy array of time
        workstation1 = new Workstations(ProductType.P1, (ArrayList<Double>) getData(new  File("D:/SYSC4005_Project/ws1.dat")));
        workstation2 = new Workstations(ProductType.P2,(ArrayList<Double>) getData(new  File("D:/SYSC4005_Project/ws2.dat")));
        workstation3 = new Workstations(ProductType.P3,(ArrayList<Double>) getData(new  File("D:/SYSC4005_Project/ws3.dat")));

        HashMap<ComponentType, ArrayList<Double>> HashforInspect1 = new HashMap<>();
        HashforInspect1.put(ComponentType.C1,(ArrayList<Double>) getData(new  File("D:/SYSC4005_Project/servinsp1.dat")));
        HashMap<ComponentType, ArrayList<Double>> HashforInspect2 = new HashMap<>();
        HashforInspect2.put(ComponentType.C2,(ArrayList<Double>) getData(new  File("D:/SYSC4005_Project/servinsp22.dat")));
        HashforInspect2.put(ComponentType.C3,(ArrayList<Double>) getData(new  File("D:/SYSC4005_Project/servinsp23.dat")));

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


    public static void main(String[]  args){
        Thread mainThread = new Thread(new Main());
        mainThread.start();
    }

    private static List<Double> getData(File file){
        Scanner scnr = null;
        List<Double> data = new ArrayList<>();

        try {
            scnr = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        while(scnr.hasNextDouble()){
            Double time = scnr.nextDouble();
            data.add(time);
        }
        return data;
    }

    @Override
    public void run() {
        WorkThread1.start();
        WorkThread2.start();
        WorkThread3.start();
        InspectThread1.start();
        InspectThread2.start();

        time = System.currentTimeMillis();
        while(InspectThread1.isAlive() && InspectThread2.isAlive()){

        }

        time = System.currentTimeMillis() - time;

        inspector1.setRunning(false);
        inspector2.setRunning(false);
        workstation1.setRunning(false);
        workstation2.setRunning(false);
        workstation3.setRunning(false);

        System.out.println("Inspector 1 inspected:" + inspector1.getComponent1_inspected() + " of component 1");
        System.out.println("Inspector 2 inspected:" + inspector2.getComponent2_inspected() + " of component 2");
        System.out.println("Inspector 2 inspected:" + inspector2.getComponent3_inspected() + " of component 3");

        System.out.println("Inspector 1 was blocked for " + inspector1.getBlockedTime() + " With the average being " + inspector1.getAverageBlockedTime());
        System.out.println("Inspector 2 was blocked for " + inspector2.getBlockedTime() + " With the average being " + inspector2.getAverageBlockedTime());

        System.out.println("WorkStation1 produced " + workstation1.getProductProduced() + " of P1");
        System.out.println("WorkStation2 produced " + workstation2.getProductProduced() + " of P2");
        System.out.println("WorkStation3 produced " + workstation3.getProductProduced() + " of P3");

        System.out.println("WorkStation1 took " + workstation1.getSumOfTimeToProduce() + " to Produce P1 with an average of " + workstation1.getAverageTime());
        System.out.println("WorkStation2 took " + workstation2.getSumOfTimeToProduce() + " to Produce P2 with an average of " + workstation2.getAverageTime());
        System.out.println("WorkStation3 took " + workstation3.getSumOfTimeToProduce() + " to Produce P3 with an average of " + workstation3.getAverageTime());
    }
}
