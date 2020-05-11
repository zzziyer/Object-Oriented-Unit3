import com.oocourse.specs3.models.NodeIdNotFoundException;
import com.oocourse.specs3.models.NodeNotConnectedException;
import com.oocourse.specs3.models.Path;
import com.oocourse.specs3.models.PathIdNotFoundException;
import com.oocourse.specs3.models.PathNotFoundException;
import com.oocourse.specs3.models.RailwaySystem;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

//lehgth 1
//ticket 2
//transfer 3
//pleasant 4

public class MyRailwaySystem implements RailwaySystem {

    private HashMap<Integer, MyPath> pathHashMap = new HashMap<>();
    private HashMap<Integer, Integer> nodes = new HashMap<>();
    private HashMap<Pair<Integer, Integer>, Integer> edges
            = new HashMap<>();
    private ArrayList<HashSet<Integer>> blocks = new ArrayList<>();
    private int id = 1;
    private Weight weight = new Weight();
    private boolean rootAgain = true;

    public MyRailwaySystem() {
        //none
    }

    //@Override
    public int getUnpleasantValue(Path path, int i, int i1) {
        return 0;
    }

    @Override
    public int getLeastTicketPrice(int i, int i1)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!nodes.containsKey(i)) {
            throw new NodeIdNotFoundException(i);
        } else if (!nodes.containsKey(i1)) {
            throw new NodeIdNotFoundException(i1);
        } else if (weight.inquire(i, i1, 1) == Integer.MAX_VALUE) {
            throw new NodeNotConnectedException(i, i1);
        } else {
            if (i == i1) {
                return 0;
            }
            return weight.inquire(i, i1, 2) - 2;
        }
    }

    @Override
    public int getLeastTransferCount(int i, int i1)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!nodes.containsKey(i)) {
            throw new NodeIdNotFoundException(i);
        } else if (!nodes.containsKey(i1)) {
            throw new NodeIdNotFoundException(i1);
        } else if (weight.inquire(i, i1, 1) == Integer.MAX_VALUE) {
            throw new NodeNotConnectedException(i, i1);
        } else {
            if (i == i1) {
                return 0;
            }
            return weight.inquire(i, i1, 3) - 1;
        }
    }

    @Override
    public int getLeastUnpleasantValue(int i, int i1)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!nodes.containsKey(i)) {
            throw new NodeIdNotFoundException(i);
        } else if (!nodes.containsKey(i1)) {
            throw new NodeIdNotFoundException(i1);
        } else if (weight.inquire(i, i1, 1) == Integer.MAX_VALUE) {
            throw new NodeNotConnectedException(i, i1);
        } else {
            if (i == i1) {
                return 0;
            }
            return weight.inquire(i, i1, 4) - 32;
        }

    }

    @Override
    public int getConnectedBlockCount() {
        if (rootAgain) {
            blocks.clear();
            Collection<MyPath> paths = pathHashMap.values();
            for (Path p :
                    paths) {
                MyPath pp = (MyPath) p;
                ArrayList<Integer> n = pp.getPath();
                if (blocks.size() == 0) {
                    blocks.add(new HashSet<>(n));
                    continue;
                }
                ArrayList<Integer> merge = new ArrayList<>();
                for (int k :
                        n) {
                    for (int i = 0; i < blocks.size(); i++) {
                        if (blocks.get(i).contains(k)) {
                            if (!new HashSet<Integer>(merge).contains(i)) {
                                merge.add(i);
                            }
                        }
                    }
                }
                //System.out.println(merge);
                HashSet<Integer> present = new HashSet<>(n);
                for (int i = 0; i < merge.size(); i++) {
                    if (merge.get(i) >= blocks.size() || merge.get(i) < 0) {
                        break;
                    }
                    present.addAll(blocks.get(merge.get(i)));
                    int remo = merge.get(i);
                    blocks.remove(remo);
                    for (int j = i + 1; j < merge.size(); j++) {
                        if (merge.get(j) > merge.get(i)) {
                            merge.set(j, merge.get(j) - 1);
                        }
                    }
                }
                blocks.add(present);
            }
            rootAgain = false;
        }
        return blocks.size();
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
        if (!nodes.containsKey(i)) {
            throw new NodeIdNotFoundException(i);
        } else if (!nodes.containsKey(i1)) {
            throw new NodeIdNotFoundException(i1);
        } else {
            if (i == i1) {
                return true;
            }
            if (weight.inquire(i, i1, 1) < Integer.MAX_VALUE) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public int getShortestPathLength(int i, int i1)
            throws NodeIdNotFoundException, NodeNotConnectedException {
        if (!nodes.containsKey(i)) {
            throw new NodeIdNotFoundException(i);
        } else if (!nodes.containsKey(i1)) {
            throw new NodeIdNotFoundException(i1);
        } else if (weight.inquire(i, i1, 1) == Integer.MAX_VALUE) {
            throw new NodeNotConnectedException(i, i1);
        } else {
            if (i == i1) {
                return 0;
            }
            return weight.inquire(i, i1, 1);
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
        ArrayList<Integer> newnode = new ArrayList<>();
        if (path == null || !path.isValid()) {
            return 0;
        }
        if (pathHashMap.containsValue(path)) {
            return getPathId(path);
        } else {
            rootAgain = true;
            pathHashMap.put(id, (MyPath) path);
            for (int k :
                    path) {
                if (nodes.containsKey(k)) {
                    nodes.put(k, nodes.get(k) + 1);
                } else {
                    newnode.add(k);
                    nodes.put(k, 1);
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
            weight.add((MyPath) path, newnode);
            return (id - 1);
        }
    }

    @Override
    public int removePath(Path path) throws Exception {
        if (path == null || !path.isValid() ||
                !pathHashMap.containsValue(path)) {
            throw new PathNotFoundException(path);
        }
        int id = getPathId(path);
        pathHashMap.remove(id);
        ArrayList<Integer> denodes = remove(path);
        rootAgain = true;
        weight.remove((MyPath) path, denodes);
        return id;
    }

    @Override
    public void removePathById(int i) throws PathIdNotFoundException {
        if (!pathHashMap.containsKey(i)) {
            throw new PathIdNotFoundException(i);
        }
        Path temp = pathHashMap.remove(i);
        ArrayList<Integer> denodes = remove(temp);
        weight.remove((MyPath) temp, denodes);
        rootAgain = true;
    }

    public ArrayList<Integer> remove(Path path) {
        ArrayList<Integer> re = new ArrayList<>();
        for (int k :
                path) {
            if (nodes.get(k) != 1) {
                nodes.put(k, nodes.get(k) - 1);
            } else {
                nodes.remove(k);
                re.add(k);
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
        return re;
    }

    @Override
    public int getDistinctNodeCount() {
        return nodes.size();
    }
}
