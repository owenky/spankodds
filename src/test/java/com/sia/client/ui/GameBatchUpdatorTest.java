package com.sia.client.ui;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class GameBatchUpdatorTest {

    @Test
    public void testGetNewUpdateRegions1() {

        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(3);

        List<int[]> result = GameBatchUpdator.getNewUpdateRegions(0,1,set);
        assertEquals(1,result.size());
        assertEquals(0,result.get(0)[0]);
        assertEquals(0,result.get(0)[1]);
    }
    @Test
    public void testGetNewUpdateRegions2() {

        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(3);

        List<int[]> result = GameBatchUpdator.getNewUpdateRegions(0,5,set);
        assertEquals(3,result.size());


        assertEquals(0,result.get(0)[0]);
        assertEquals(0,result.get(0)[1]);

        assertEquals(2,result.get(1)[0]);
        assertEquals(2,result.get(1)[1]);

        assertEquals(4,result.get(2)[0]);
        assertEquals(5,result.get(2)[1]);
    }
    @Test
    public void testGetNewUpdateRegions3() {

        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);

        List<int[]> result = GameBatchUpdator.getNewUpdateRegions(1,2,set);
        assertEquals(0,result.size());
    }
    @Test
    public void testGetNewUpdateRegions4() {

        Set<Integer> set = new HashSet<>();
        set.add(1);
        set.add(3);
        set.add(5);

        List<int[]> result = GameBatchUpdator.getNewUpdateRegions(0,5,set);
        assertEquals(3,result.size());


        assertEquals(0,result.get(0)[0]);
        assertEquals(0,result.get(0)[1]);

        assertEquals(2,result.get(1)[0]);
        assertEquals(2,result.get(1)[1]);

        assertEquals(4,result.get(2)[0]);
        assertEquals(4,result.get(2)[1]);
    }
    @Test
    public void testGetNewUpdateRegions5() {

        Set<Integer> set = new HashSet<>();
        set.add(0);
        set.add(1);
        set.add(3);
        set.add(5);
        set.add(6);

        List<int[]> result = GameBatchUpdator.getNewUpdateRegions(0,5,set);
        assertEquals(2,result.size());

        assertEquals(2,result.get(0)[0]);
        assertEquals(2,result.get(0)[1]);

        assertEquals(4,result.get(1)[0]);
        assertEquals(4,result.get(1)[1]);
    }
    @Test
    public void testGetNewUpdateRegions6() {

        Set<Integer> set = new HashSet<>();
        set.add(100);

        List<int[]> result = GameBatchUpdator.getNewUpdateRegions(0,5,set);
        assertEquals(1,result.size());

        assertEquals(0,result.get(0)[0]);
        assertEquals(5,result.get(0)[1]);
    }
    @Test
    public void testGetNewUpdateRegions7() {

        Set<Integer> set = new HashSet<>();
        set.add(1);

        List<int[]> result = GameBatchUpdator.getNewUpdateRegions(2,5,set);
        assertEquals(1,result.size());

        assertEquals(2,result.get(0)[0]);
        assertEquals(5,result.get(0)[1]);
    }
    @Test
    public void testGetNewUpdateRegions8() {

        Set<Integer> set = new HashSet<>();
        set.add(10);
        set.add(11);

        List<int[]> result = GameBatchUpdator.getNewUpdateRegions(1,20,set);
        assertEquals(2,result.size());

        assertEquals(1,result.get(0)[0]);
        assertEquals(9,result.get(0)[1]);

        assertEquals(12,result.get(1)[0]);
        assertEquals(20,result.get(1)[1]);
    }
}
