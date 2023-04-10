import java.util.*;

class Process1 {
    int pid;  // process ID
    int priority;  // priority value
    int arrivalTime;  // arrival time
    int burstTime;  // CPU burst time
    int remainingTime;  // remaining CPU time

    public Process1(int pid,  int arrivalTime,int priority, int burstTime) {
        this.pid = pid;
        this.priority = priority;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.remainingTime = burstTime;
    }
}
public class PreMp {
    public static void displayData(List<Process1> processes) {
        System.out.println("PID" + " " + "Arrival" + " " + "Priority" + " BurstTime");
        for (Process1 p : processes) {
            System.out.println(p.pid + "\t\t" + p.arrivalTime + "\t\t" + p.priority + "\t\t" + p.burstTime);
        }
        System.out.println("------------------------------------------------------");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Number of Processes: ");
        int n = sc.nextInt();
        // create a list of processes
        List<Process1> processes = new ArrayList<Process1>();

        for (int i = 0; i < n; i++) {
            System.out.print("Enter the Process ID,  Arrival Time, Priority and Burst Time of process " + (i + 1) + ": ");

            int p1 = sc.nextInt();
            int p2 = sc.nextInt();
            int p3 = sc.nextInt();
            int p4 = sc.nextInt();

            Process1 p = new Process1(p1, p2, p3, p4);
            processes.add(p);
        }
        System.out.println("Your Input is: ");
        displayData(processes);

        // sort the processes by arrival time
        Collections.sort(processes, Comparator.comparing(p -> p.arrivalTime));
        System.out.println("Sorted by Arrival time: ");
        displayData(processes);

        // create a priority queue to hold the processes
        PriorityQueue<Process1> queue = new PriorityQueue<Process1>(Comparator.comparing(p -> p.priority));

        // create a queue to hold preempted processes
        Queue<Process1> queue2 = new LinkedList<Process1>();

        int time = 0;
        boolean flag = true;
        while (true) {
            while (!queue.isEmpty() || !processes.isEmpty()) {
                // add any arriving processes to the queue
                while (!processes.isEmpty() && processes.get(0).arrivalTime <= time) {
                    queue.add(processes.remove(0));
                }

                if (!queue.isEmpty()) {
                    // get the process with the highest priority
                    Process1 p = queue.peek();

                    // execute the process for one time unit
                    p.remainingTime--;
                    System.out.println("Time " + time + ": Process " + p.pid + " is running.");

                    // add any new arriving processes with higher priority than the current process
                    while (!processes.isEmpty() && processes.get(0).arrivalTime <= time && processes.get(0).priority < p.priority) {
                        queue.add(processes.remove(0));
                    }

                    // check if a higher priority process has arrived
                    if (!processes.isEmpty() && processes.get(0).priority < p.priority) {
                        // preempt the current process and add it to queue2
                        queue2.add(queue.poll());
                        // add the higher priority process to queue
                        queue.add(processes.remove(0));
                        flag = false;
                        System.out.println("Time " + time + ": Process " + p.pid + " has been preempted.");

                    } else if (p.remainingTime == 0) {
                        // remove the current process from the queue if it has finished
                        queue.poll();
                        System.out.println("Time " + time + ": Process " + p.pid + " has ended.");
                    }
                } else {
                    // no processes are currently running
                    System.out.println("Time " + time + ": No process is running.");
                }

                // advance the time by one unit
                time++;
            }


            if (flag) {
                break;
            }
            System.out.println("Handling queue2 at Level 2, and total processes left are:  " + queue2.size());
            while (!queue2.isEmpty()) {
                int quantum = 2;  // time quantum
                Process1 p = queue2.poll();
                // execute the process for one time quantum
                p.remainingTime -= quantum;
                System.out.println("Time " + time + ": Process " + p.pid + " is running.");

                // check if the process has finished
                if (p.remainingTime <= 0) {

                    System.out.println("Time " + time + ": Process " + p.pid + " has ended.");
                } else {
                    // add the process back to the queue
                    queue2.add(p);
                }
                time += quantum;

            }
            break;
        }
    }
}