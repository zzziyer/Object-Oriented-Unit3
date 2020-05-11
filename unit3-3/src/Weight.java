import java.util.ArrayList;
import java.util.HashMap;

public class Weight {
    private int[][] lehgth = new int[130][130];
    private int[][] ticket = new int[130][130];
    private int[][] transfer = new int[130][130];
    private int[][] pleasant = new int[130][130];

    private int[][] preLehgth = new int[130][130];
    private int[][] preTicket = new int[130][130];
    private int[][] preTransfer = new int[130][130];
    private int[][] prePleasant = new int[130][130];

    private int[][] tempLehgth = new int[130][130];
    private int[][] tempTicket = new int[130][130];
    private int[][] tempTransfer = new int[130][130];
    private int[][] tempPleasant = new int[130][130];

    private boolean[] footerToId = new boolean[130];
    private HashMap<Integer, Integer> idToFooter = new HashMap<>();
    private ArrayList<MyPath> paths = new ArrayList<>();
    private int footer = 0;//可以分配的脚标
    private boolean againRes = true;
    private boolean againPre = true;

    public Weight() {
        preMax();
    }

    public int inquire(int from, int to, int code) {
        int re;
        int i = idToFooter.get(from);
        int j = idToFooter.get(to);
        if (againPre) {
            buildGraph();
        }
        if (againRes) {
            floyd();
        }
        if (code == 1) {
            re = lehgth[i][j];
        } else if (code == 2) {
            re = ticket[i][j];
        } else if (code == 3) {
            re = transfer[i][j];
        } else {
            re = pleasant[i][j];
        }
        return re;
    }

    public void preMax() {
        for (int i = 0; i < 129; i++) {
            for (int j = 0; j < 129; j++) {
                preLehgth[i][j] = Integer.MAX_VALUE;
                preTicket[i][j] = Integer.MAX_VALUE;
                preTransfer[i][j] = Integer.MAX_VALUE;
                prePleasant[i][j] = Integer.MAX_VALUE;
            }
        }
    }

    public void tempMax() {
        for (int i = 0; i < 129; i++) {
            for (int j = 0; j < 129; j++) {
                tempLehgth[i][j] = Integer.MAX_VALUE;
                tempTicket[i][j] = Integer.MAX_VALUE;
                tempTransfer[i][j] = Integer.MAX_VALUE;
                tempPleasant[i][j] = Integer.MAX_VALUE;
            }
        }
    }

    public void floyd() {
        lehgth = preLehgth;
        ticket = preTicket;
        transfer = preTransfer;
        pleasant = prePleasant;
        for (int k : idToFooter.keySet()) {
            for (int i : idToFooter.keySet()) {
                for (int j : idToFooter.keySet()) {
                    int ii = idToFooter.get(i);
                    int jj = idToFooter.get(j);
                    int kk = idToFooter.get(k);
                    if ((lehgth[ii][kk] + lehgth[kk][jj] > 0)
                            && lehgth[ii][kk] + lehgth[kk][jj]
                            < lehgth[ii][jj]) {
                        lehgth[ii][jj] =
                                lehgth[ii][kk] + lehgth[kk][jj];
                    }
                    if (ticket[ii][kk] + ticket[kk][jj] > 0 &&
                            ticket[ii][kk] + ticket[kk][jj]
                                    < ticket[ii][jj]) {
                        ticket[ii][jj] =
                                ticket[ii][kk] + ticket[kk][jj];
                    }
                    if (transfer[ii][kk] + transfer[kk][jj] > 0 &&
                            transfer[ii][kk] + transfer[kk][jj]
                                    < transfer[ii][jj]) {
                        transfer[ii][jj] =
                                transfer[ii][kk] + transfer[kk][jj];
                    }
                    if (pleasant[ii][kk] + pleasant[kk][jj] > 0 &&
                            pleasant[ii][kk] + pleasant[kk][jj]
                                    < pleasant[ii][jj]) {
                        pleasant[ii][jj] =
                                pleasant[ii][kk] + pleasant[kk][jj];
                    }
                }
            }
        }
        againRes = false;
    }

    public ArrayList<Integer> removeSelf(ArrayList<Integer> a) {
        ArrayList<Integer> re = new ArrayList<>();
        re.add(a.get(0));
        for (int i = 1; i < a.size(); i++) {
            if (a.get(i) == a.get(i - 1)) {
                continue;
            }
            re.add(a.get(i));
        }
        return re;
    }

    public void update(MyPath pathin) {
        ArrayList<Integer> path = pathin.getPath();
        path = removeSelf(path);
        tempMax();
        tempOri(pathin);
        tempfloyd(path);
        int i;
        int j;
        int size = path.size();
        for (i = 0; i < size - 1; i++) {
            int ii = idToFooter.get(path.get(i));
            int ii1 = idToFooter.get(path.get(i + 1));
            if (i < size - 1) {
                if (preLehgth[ii][ii1] > 1) {
                    preLehgth[ii][ii1] = 1;
                    preLehgth[ii1][ii] = 1;
                }
            }
            for (j = i + 1; j < size; j++) {
                int jj = idToFooter.get(path.get(j));
                if (1 < preTransfer[ii][jj]) {
                    preTransfer[ii][jj] = 1;
                    preTransfer[jj][ii] = 1;
                }
                if (tempTicket[ii][jj] + 2 < preTicket[ii][jj]) {
                    preTicket[ii][jj] = tempTicket[ii][jj] + 2; //
                    preTicket[jj][ii] = tempTicket[ii][jj] + 2; //
                }
                if (tempPleasant[ii][jj] + 32 < prePleasant[ii][jj]) {
                    prePleasant[ii][jj] = tempPleasant[ii][jj] + 32;
                    prePleasant[jj][ii] = tempPleasant[ii][jj] + 32;
                }
            }
        }
    }

    public void tempOri(MyPath pathin) {
        ArrayList<Integer> path = pathin.getPath();
        path = removeSelf(path);
        int size = path.size();
        //System.out.println("path = " + path);
        for (int i = 0; i < size - 1; i++) {
            int ii = idToFooter.get(path.get(i));
            int jj = idToFooter.get(path.get(i + 1));
            tempTicket[ii][jj] = 1;
            tempTicket[jj][ii] = 1;
            int temp = edgeUnple(path.get(i), path.get(i + 1));
            tempPleasant[ii][jj] = temp;
            tempPleasant[jj][ii] = temp;
            //System.out.println("tempTicket[ii][jj] = " + tempTicket[ii][jj]
            //        + " " + path.get(i) + path.get(i + 1));
        }
    }

    public void tempfloyd(ArrayList<Integer> a) {
        for (int k : a) {
            for (int i : a) {
                for (int j : a) {
                    int ii = idToFooter.get(i);
                    int jj = idToFooter.get(j);
                    int kk = idToFooter.get(k);
                    if (tempTicket[ii][kk] + tempTicket[kk][jj] > 0 &&
                            tempTicket[ii][kk] + tempTicket[kk][jj]
                                    < tempTicket[ii][jj]) {
                        tempTicket[ii][jj] =
                                tempTicket[ii][kk] + tempTicket[kk][jj];
                    }
                    if (tempPleasant[ii][kk] + tempPleasant[kk][jj] > 0 &&
                            tempPleasant[ii][kk] + tempPleasant[kk][jj]
                                    < tempPleasant[ii][jj]) {
                        tempPleasant[ii][jj] =
                                tempPleasant[ii][kk] + tempPleasant[kk][jj];
                    }
                }
            }
        }
        // System.out.println("tempTicket = " + tempTicket[0][5]);
    }

    public int edgeUnple(int from, int to) {
        int a = (from % 5 + 5) % 5;
        int b = (to % 5 + 5) % 5;
        int temp = Math.max(a, b);
        temp = (int) Math.pow(4, temp);
        return temp;
    }

    public void buildGraph() {
        preMax();
        for (MyPath p :
                paths) {
            update(p);
        }
        againPre = false;
    }

    public void remove(MyPath path, ArrayList<Integer> renodes) {
        for (int k :
                renodes) {
            int t = idToFooter.get(k);
            idToFooter.remove(k);
            footerToId[t] = false;
        }
        paths.remove(path);
        againPre = true;
        againRes = true;
        //for (int i = 0; i < 20; i++) {
        //    System.out.print(footerToId[i] + " ");
        //}
    }

    public void add(MyPath path, ArrayList<Integer> addnodes) {
        for (int k :
                addnodes) {
            int t = getFooter();
            idToFooter.put(k, t);
            footerToId[t] = true;
        }
        paths.add(path);
        update(path);
        againRes = true;
        //for (int i = 0; i < 20; i++) {
        //    System.out.print(footerToId[i] + " ");
        //}
    }

    public int getFooter() {
        while (footerToId[footer]) {
            footer++;
            if (footer == 129) {
                footer = 0;
            }
        }
        int re = footer;
        footer++;
        if (footer == 129) {
            footer = 0;
        }
        return re;
    }

}
