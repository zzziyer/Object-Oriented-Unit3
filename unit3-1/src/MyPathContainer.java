import com.oocourse.specs1.models.Path;
import com.oocourse.specs1.models.PathContainer;
import com.oocourse.specs1.models.PathIdNotFoundException;
import com.oocourse.specs1.models.PathNotFoundException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class MyPathContainer implements PathContainer {

    private HashMap<Integer, MyPath> pathHashMap = new HashMap<>();
    private HashMap<Integer, Integer> nodes = new HashMap<>();
    private int id;

    public MyPathContainer() {
        id = 1;
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
        HashSet<Integer> set = new HashSet<>(pathHashMap.keySet());
        if (set.contains(i)) {
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
        //TODO 此处传入的为什么不是mypath类
        //TODO comtains 是否使用重写的比较函数
        if (path == null || !path.isValid()) {
            return 0;
        }
        if (pathHashMap.containsValue(path)) {
            return getPathId(path);
        } else {
            pathHashMap.put(id, (MyPath) path);
            for (int k :
                    path) {
                if (nodes.containsKey(k)) {
                    nodes.put(k, nodes.get(k) + 1);
                } else {
                    nodes.put(k, 1);
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
        int id = getPathId(path);
        pathHashMap.remove(id);
        for (int k :
                path) {
            if (nodes.get(k) != 1) {
                nodes.put(k, nodes.get(k) - 1);
            } else {
                nodes.remove(k);
            }
        }
        return id;
    }

    @Override
    public void removePathById(int i) throws PathIdNotFoundException {
        if (!pathHashMap.containsKey(i)) {
            throw new PathIdNotFoundException(i);
        }
        Path temp = pathHashMap.remove(i);
        for (int k :
                temp) {
            if (nodes.get(k) != 1) {
                nodes.put(k, nodes.get(k) - 1);
            } else {
                nodes.remove(k);
            }
        }
    }

    @Override
    public int getDistinctNodeCount() {
        return nodes.size();
    }
}
