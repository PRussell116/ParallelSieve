package com.company;

import java.util.concurrent.CyclicBarrier;
public class threadSieve extends Thread {

    static long n = 100000; // primes will be found from below N
    static boolean[] primeTable = new boolean[(int) n + 1];
    static int p = 4;
    static long currentPrimeTableNum = 2;
    static CyclicBarrier barrier = new CyclicBarrier(p);

    public static void main(String[] args) throws Exception {

        // setup prime table

        for (int i = 0; i < n; i++) {
            primeTable[i] = true;
        }


        //setup threads
        threadSieve[] threads = new threadSieve[p];
        for (int me = 0; me < p; me++) {
            threads[me] = new threadSieve(me);
            threads[me].start();
        }

        long startTime = System.currentTimeMillis();

        for (int me = 0; me < p; me++) {
            threads[me].join();
        }





        long endTime = System.currentTimeMillis();
        //print primes
        for (long i = 2; i < n; i++) {
            if (primeTable[(int) i]) {
                System.out.println(i + " ");
            }
        }
        System.out.println("Calculation completed in : " + (endTime - startTime) + " ms");

    }

    int me;

    public threadSieve(int me) {
        this.me = me;
    }

    long region = n / p; // range of n that each thread is responsible for e.g. for n = 100 thread 0  controls 0 - 25

    public void run() {

        while (currentPrimeTableNum < n) {
         //   System.out.println(currentPrimeTableNum + ": current Prime");
            long block;
            long begin = 0;

            // find how to split the loads
            long remainder = region % currentPrimeTableNum;

            if(remainder == 0){
                // all block sizes are same if divides evenly
                block = region/ (currentPrimeTableNum);
            }else{
                // first blocks up to the remainder are larger if does not divide evenly
                if(me < remainder){
                    block =(long) Math.ceil((double)region /currentPrimeTableNum);
                    begin = (me * block) * currentPrimeTableNum;
                }else{
                    block = (long) Math.floor((double)region /currentPrimeTableNum);
                    // adjust begin for due to the larger blocks
                    begin = (me * block) * currentPrimeTableNum + (remainder * currentPrimeTableNum);
                }

            }

            // find how many iterations are needed to be per thread

            long end = begin + (block) * currentPrimeTableNum;

            if(end > n){ end = n;} // prevent out of bounds error
            if (begin == 0) {
                begin = currentPrimeTableNum;
            }

            // set multiples to false
            for (long j = begin; j <= (end); j += currentPrimeTableNum) {
                // ignore the actual prime and only get the multiples
                if (j != currentPrimeTableNum) {
                    primeTable[(int) j] = false;
                }

            }
            // update the table for all threads
            sync();
            // find the next prime
            if (me == 0) {
                for (long i = currentPrimeTableNum + 1; i < n + 1; i++) {
                    if (primeTable[(int) i] == true) {
                        currentPrimeTableNum = i;
                        break;
                    } else {
                        currentPrimeTableNum = i + 1;
                    }
                }
            }
            //update the current prime for all other threads
            sync();

        }
    }
    static void sync() {
        try {
            barrier.await();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
