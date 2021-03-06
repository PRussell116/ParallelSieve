package com.company;

import java.time.Instant;

public class Main {

    public static void main(String[] args) {
	long n =100000000;// primes will be found from below N
    boolean[] primeTable = new boolean[(int)n+1]; // table of true representing a prime number


    for(int i = 0; i<n; i++){
        primeTable[i] = true;
        }

   // long startTime = System.nanoTime();
        long startTime = System.currentTimeMillis();
    for(long i = 2; i<=n; i++){
        //if not a multiple, find multiples
       if(primeTable[(int) i]){
           // mark each multiple of i as false
           for(long j = i*i; j <= n; j+=i ){
               primeTable[(int)j] = false;
           }
       }
    }
    long endTime =System.currentTimeMillis();
    //print primes
//    for(int i = 2; i<n;i++){
//        if(primeTable[i] == true){
//            System.out.println(i + " ");
//        }
//    }
    System.out.println("Calculation completed in : " + (endTime-startTime) + " ms");

        
    }
}
