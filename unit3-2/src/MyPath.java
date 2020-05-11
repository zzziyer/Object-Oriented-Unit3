import com.oocourse.specs2.models.Path;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

public class MyPath implements Path {
    private ArrayList<Integer> path = new ArrayList<>();

    public MyPath(int... nodeList) {
        int size = nodeList.length;
        int i;
        for (i = 0; i < size; i++) {
            path.add(nodeList[i]);
        }
    }

    public ArrayList<Integer> getPath() {
        return this.path;
    }

    @Override
    public int size() {
        return path.size();
    }

    @Override
    public int getNode(int i) {
        return path.get(i);
    }

    @Override
    public boolean containsNode(int i) {
        HashSet<Integer> set = new HashSet<>(path);
        if (set.contains(i)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getDistinctNodeCount() {
        HashSet<Integer> set = new HashSet<>(path);
        return set.size();
    }

    @Override
    public boolean isValid() {
        if (path.size() >= 2) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Path o) {
        int minsize;
        if (path.size() < o.size()) {
            minsize = path.size();
        } else {
            minsize = o.size();
        }
        for (int i = 0; i < minsize; i++) {
            if (path.get(i) > o.getNode(i)) {
                return 1;
            }
            if (path.get(i) < o.getNode(i)) {
                return -1;
            }
        }
        if (path.size() == o.size()) {
            return 0;
        } else if (path.size() > o.size()) {
            return 1;
        } else {
            return -1;
        }

    }

    @Override
    public Iterator<Integer> iterator() {
        return path.iterator();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MyPath myPath = (MyPath) o;
        if (!(myPath.size() == path.size())) {
            return false;
        } else {
            boolean tag = true;
            for (int i = 0; i < path.size(); i++) {
                if (!path.get(i).equals(myPath.getNode(i))) {
                    tag = false;
                    break;
                }
            }
            return tag;
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

}
