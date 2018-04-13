package edu.brown.cs.rmerzbacgajith.tree;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import edu.brown.cs.rmerzbacgajith.maps.MapCommand;

public class NearestTest {

  @Test
  public void testDistance() throws IOException {
    double[] coords1 = { 1 };
    double[] coords2 = { 2 };
    assert (Nearest.getDistance(coords1, coords2) == 1);

    double[] coords3 = { 1, 1 };
    double[] coords4 = { 2, 2 };
    assert (Nearest.getDistance(coords3, coords4) == Math.sqrt(2));
    assert (Nearest.getDistance(coords3, coords3) == 0);
    assert (Nearest.getDistance(coords4, coords3) == Math.sqrt(2));

    double[] coords5 = { 1, 1, 0 };
    double[] coords6 = { 1, 1, -2 };
    assert (Nearest.getDistance(coords5, coords6) == 2);
  }

  @Test
  public void testArrayContains() {
    double[] arr1 = { 1, 2, 3 };
    Point p1 = new Point(arr1);
    Point p2 = new Point(arr1);
    ArrayList<Point> arr2 = new ArrayList<Point>(1);
    arr2.add(p1);
    assertTrue(Nearest.arrayContains(arr2, p1));
    assertFalse(Nearest.arrayContains(null, p1));
    assertFalse(Nearest.arrayContains(arr2, p2));
  }

  @Test
  public void testSingleFind() throws IOException {
    MapCommand mc = new MapCommand();
    mc.mapCommand("data/maps/smallMaps.sqlite3");
    KDTree<Point> tree = mc.getBuilder().getTree();
    double[] coords1 = { 0, 0 };
    double[] coords2 = { 100, 100 };
    assert (mc.nearestCommand(coords1)
        .equals(Nearest.findNumNeighborsTrivial(1, coords1, tree).get(0)));
    assert (mc.nearestCommand(coords2)
        .equals(Nearest.findNumNeighborsTrivial(1, coords2, tree).get(0)));
  }

  @Test
  public void testBigSingleFind() throws IOException {
    MapCommand mc = new MapCommand();
    mc.mapCommand("data/maps/maps.sqlite3");
    double[] coords1 = { 500, -50 };
    assert (mc.nearestCommand(coords1)
        .equals(Nearest
            .findNumNeighborsTrivial(1, coords1, mc.getBuilder().getTree())
            .get(0)));
  }

  @Test
  public void testNoInputData() {
    double[] coords = { 1, 2, 3 };
    assert (Nearest.handleNeighborsCommandWithCoords(coords, null) == null);
  }
}
