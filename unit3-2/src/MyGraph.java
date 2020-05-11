import com.oocourse.specs2.models.Graph;
import com.oocourse.specs2.models.NodeIdNotFoundException;
import com.oocourse.specs2.models.NodeNotConnectedException;
import com.oocourse.specs2.models.Path;
import com.oocourse.specs2.models.PathIdNotFoundException;
import com.oocourse.specs2.models.PathNotFoundException;

import java.util.HashMap;

import javafx.util.Pair;

import java.util.Iterator;
import java.util.Map;

public class MyGraph implements Graph {
    private HashMap<Integer, MyPath> pathHashMap = new HashMap<>();
    private HashMap<Integer, Integer> nodes = new HashMap<>();
    //private HashMap<Pair<Integer, Integer>, Integer> distance
    //       = new HashMap<>();
    private int[][] distance = new int[300][300];
    private HashMap<Pair<Integer, Integer>, Integer> edges
            = new HashMap<>();
    private int id = 1;
    private boolean again = true;
    private HashMap<Integer, Integer> idToFooter = new HashMap<>();
    private int footer = 0;//可以分配的脚标
    private boolean[] footerToId = new boolean[300];

    public int getFooter() {
        while (footerToId[footer]) {
            footer++;
            if (footer == 298) {
                footer = 0;
            }

        }
        int re = footer;
        footer++;
        if (footer == 298) {
            footer = 0;
        }
        return re;
    }

    public void floyd() {
        for (int i = 0; i < 298; i++) {
            for (int j = 0; j < 298; j++) {
                distance[i][j] = Integer.MAX_VALUE;
            }
        }
        for (Pair k :
                edges.keySet()) {
            int a1 = (int) k.getKey();
            int b1 = (int) k.getValue();
            int a = idToFooter.get(a1);
            int b = idToFooter.get(b1);
            if (a == b) {
                continue;
            }
            distance[a][b] = 1;
        }
        //System.out.println("nodes = " + nodes.keySet());
        for (int k :
                nodes.keySet()) {
            for (int i :
                    nodes.keySet()) {
                for (int j :
                        nodes.keySet()) {
                    int ii = idToFooter.get(i);
                    int jj = idToFooter.get(j);
                    int kk = idToFooter.get(k);
                    if ((distance[ii][kk] + distance[kk][jj] > 0)
                            && distance[ii][kk] + distance[kk][jj]
                            < distance[ii][jj]) {
                        distance[ii][jj] =
                                distance[ii][kk] + distance[kk][jj];
                        //System.out.println("ii = " + ii + " " + "i="+i);
                        //System.out.println("jj = " + jj + " " + "j="+j);
                    }
                }
            }
        }
        again = false;
    }

    @Override
    public boolean containsNode(int i) {
        if (nodes.containsKey(i)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean containsEdge(int i, int i1) {
        if (edges.containsKey(new Pair<>(i, i1))) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean isConnected(int i, int i1) throws NodeIdNotFoundException {
        if (again) {
            floyd();
        }
        if (!nodes.containsKey(i)) {
            throw new NodeIdNotFoundException(i);
        } else if (!nodes.containsKey(i1)) {
            throw new NodeIdNotFoundException(i1);
        } else {
            if (i == i1) {
                return true;
            }
            int a = idToFooter.get(i);
            int b = idToFooter.get(i1);
            if (distance[a][b] < Integer.MAX_VALUE) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public int getShortestPathLength(int i, int i1)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (again) {
            floyd();
        }
        if (!nodes.containsKey(i)) {
            throw new NodeIdNotFoundException(i);
        } else if (!nodes.containsKey(i1)) {
            throw new NodeIdNotFoundException(i1);
        } else if (distance[idToFooter.get(i)][idToFooter.get(i1)]
                == Integer.MAX_VALUE) {
            throw new NodeNotConnectedException(i, i1);
        } else {
            if (i == i1) {
                return 0;
            }
            int a = idToFooter.get(i);
            int b = idToFooter.get(i1);
            return distance[a][b];
        }
    }

    @Override
    public int size() {
        return pathHashMap.size();
    }

    @Override
    public boolean containsPath(Path path) {
        if (pathHashMap.containsValue(path)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean containsPathId(int i) {
        if (pathHashMap.containsKey(i)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Path getPathById(int i) throws Exception {
        //TODO 这里做了改动
        //HashSet<Integer> set = new HashSet<>(pathHashMap.keySet());
        if (pathHashMap.containsKey(i)) {
            return pathHashMap.get(i);
        } else {
            throw new PathIdNotFoundException(i);
        }
    }

    @Override
    public int getPathId(Path path) throws Exception {
        if (path == null || !path.isValid() ||
                !pathHashMap.containsValue(path)) {
            throw new PathNotFoundException(path);
        }
        Iterator iterator = pathHashMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry temp = (Map.Entry) iterator.next();
            if (temp.getValue().equals(path)) {
                return (int) temp.getKey();
            }
        }
        return 0;
    }

    @Override
    public int addPath(Path path) throws Exception {
        //System.out.println("add!");
        if (path == null || !path.isValid()) {
            return 0;
        }
        if (pathHashMap.containsValue(path)) {
            return getPathId(path);
        } else {
            again = true;
            pathHashMap.put(id, (MyPath) path);
            for (int k :
                    path) {
                if (nodes.containsKey(k)) {
                    nodes.put(k, nodes.get(k) + 1);
                } else {
                    nodes.put(k, 1);
                    int t = getFooter();
                    idToFooter.put(k, t);
                    //System.out.println("t = " + t);
                    footerToId[t] = true;
                }
            }
            for (int i = 0; i < path.size() - 1; i++) {
                Pair pair = new Pair<>(path.getNode(i),
                        path.getNode(i + 1));
                Pair pair1 = new Pair<>(path.getNode(i + 1),
                        path.getNode(i));
                if (edges.containsKey(pair)) {
                    edges.put(pair, edges.get(pair) + 1);
                    edges.put(pair1, edges.get(pair1) + 1);
                } else {
                    edges.put(pair, 1);
                    edges.put(pair1, 1);
                }
            }
            id += 1;
            return (id - 1);
        }
    }

    @Override
    public int removePath(Path path) throws Exception {
        if (path == null || !path.isValid() ||
                !pathHashMap.containsValue(path)) {
            throw new PathNotFoundException(path);
        }
        again = true;
        int id = getPathId(path);
        pathHashMap.remove(id);
        remove(path);
        return id;
    }

    public void remove(Path path) {
        for (int k :
                path) {
            if (nodes.get(k) != 1) {
                nodes.put(k, nodes.get(k) - 1);
            } else {
                nodes.remove(k);
                int t = idToFooter.get(k);
                idToFooter.remove(k);
                footerToId[t] = false;
            }
        }
        for (int i = 0; i < path.size() - 1; i++) {
            Pair pair = new Pair<>(path.getNode(i),
                    path.getNode(i + 1));
            Pair pair1 = new Pair<>(path.getNode(i + 1),
                    path.getNode(i));
            if (edges.get(pair) != 1) {
                edges.put(pair, edges.get(pair) - 1);
                edges.put(pair1, edges.get(pair1) - 1);
            } else {
                edges.remove(pair);
                edges.remove(pair1);
            }
        }
    }

    @Override
    public void removePathById(int i) throws PathIdNotFoundException {
        if (!pathHashMap.containsKey(i)) {
            throw new PathIdNotFoundException(i);
        }
        again = true;
        Path temp = pathHashMap.remove(i);
        remove(temp);
    }

    @Override
    public int getDistinctNodeCount() {
        return nodes.size();
    }

}
