package byow.KdTree;

import java.util.List;

public class KDTree implements PointSet {

    private class Node {
        Point coordinates;
        Node left;
        Node right;
        boolean isXNode;

        private Node(Point a, boolean isXNode) {
            coordinates = a;
            this.left = null;
            this.right = null;
            this.isXNode = isXNode;
        }
    }

    private static boolean horizontal = true;
    private Node root;

    public KDTree(List<Point> points) {
        for (Point eachPoint : points) {
            root = insert(eachPoint, root, horizontal);
            horizontal = !horizontal;
        }
    }

    private Node insert(Point toInsert, Node current, boolean orientation) {
        if (current == null) {
            Node temp = new Node(toInsert, orientation);
            return temp;

        } else if (current.coordinates.getX() == toInsert.getX()
                    && current.coordinates.getY() == toInsert.getY()) {
            return current;
        } else if (current.isXNode) {
            if (Double.compare(current.coordinates.getX(), toInsert.getX()) > 0) {
                current.left = insert(toInsert, current.left, false);
            } else {
                current.right = insert(toInsert, current.right, false);
            }
        } else {
            if (Double.compare(current.coordinates.getY(), toInsert.getY()) > 0) {
                current.left = insert(toInsert, current.left, true);
            } else {
                current.right = insert(toInsert, current.right, true);
            }
        }
        return current;
    }


    public Point nearest(double x, double y) {
        Point goal = new Point(x, y);
        return nearestHelper(root, goal, root).coordinates;
    }

    private Node nearestHelper(Node curr, Point target, Node best) {
        if (curr == null) {
            return best;
        }
        //If the current node is already a better option, then set it as best
        if (Point.distance(curr.coordinates, target) < Point.distance(target, best.coordinates)) {
            best = curr;
        }

        //Decide which is the betterChild based on
        // whether the current Node's X/Y value is larger than the target
        boolean checkLeftFirst = false;
        if (curr.isXNode) {
            if (curr.coordinates.getX() > target.getX()) {
                checkLeftFirst = true;
            }
        } else {
            if (curr.coordinates.getY() > target.getY()) {
                checkLeftFirst = true;
            }
        }

        //Check the betterChild first
        if (checkLeftFirst) {
            best = nearestHelper(curr.left, target, best);
        } else {
            best = nearestHelper(curr.right, target, best);
        }

        //Find the hypothetical best case scenario on the worse side
        Point fakePoint;
        if (curr.isXNode) {
            fakePoint = new Point(curr.coordinates.getX(), target.getY());
        } else {
            fakePoint = new Point(target.getX(), curr.coordinates.getY());
        }
        //Check if this best-case scenario is better than our current best option.
        // If it is, then evaluate worseChild
        if (Point.distance(fakePoint, target) < Point.distance(best.coordinates, target)) {
            if (checkLeftFirst) {
                best = nearestHelper(curr.right, target, best);
            } else {
                best = nearestHelper(curr.left, target, best);
            }
        }
        return best;
    }
}






