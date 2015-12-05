package edu.stonybrook.utils;


/**
 * Created by praveenkumaralam on 10/26/15.
 */
public class TestCountSynchronized {
    int count;
    public TestCountSynchronized(int n){
        count = n;
    }
    synchronized public void decremetCount(){
        count--;
    }
    synchronized public int getCount(){
        return count;
    }
    synchronized public void setCount(int n){
        count = n;
    }
}
