package Asg7;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Asg7 {

    public static void main(String[] args){

        //create a new txt file
        try {
            File output = new File("out.txt");
            if (output.createNewFile()) {
                System.out.println("File created: " + output.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // Initialize output string & buddy buffer manager
        String out = "";
        BuddyBufferManager bbm = new BuddyBufferManager();

        // Test getting buffers of different sizes
        int address1 = bbm.allocateBuffer(7);
        out = out + "Address of buffer size 7: " + address1 + "\n";
        out = out + bbm.debug() + "\n";
        out = out + bbm.getManagerStatus() + "\n";

        int address2 = bbm.allocateBuffer(15);
        out = out + "Address of buffer size 15: " + address2 + "\n";
        out = out + bbm.debug() + "\n";
        out = out + bbm.getManagerStatus() + "\n";

        int address3 = bbm.allocateBuffer(83);
        out = out + "Address of buffer size 83: " + address3 + "\n";
        out = out + bbm.debug() + "\n";
        out = out + bbm.getManagerStatus() + "\n";

        //Test incorrect values
        int address4 = bbm.allocateBuffer(700);
        out = out + "Address of buffer size 700: " + address4 + "\n";
        int address5 = bbm.allocateBuffer(2);
        out = out + "Address of buffer size 2: " + address5 + "\n";
        out = out + bbm.debug() + "\n";
        out = out + bbm.getManagerStatus() + "\n";

        int address6 = bbm.allocateBuffer(214);
        out = out + "Address of buffer size 214: " + address6 + "\n";
        out = out + bbm.debug() + "\n";
        out = out + bbm.getManagerStatus() + "\n";

        int address7 = bbm.allocateBuffer(23);
        out = out + "Address of buffer size 23: " + address7 + "\n";
        out = out + bbm.debug() + "\n";
        out = out + bbm.getManagerStatus() + "\n";

        int address8 = bbm.allocateBuffer(45);
        out = out + "Address of buffer size 8: " + address8 + "\n";
        out = out + bbm.debug() + "\n";
        out = out + bbm.getManagerStatus() + "\n";


        // Test releasing buffers
        bbm.deallocateBuffer(address6);
        out = out + "Deallocated buffer at address: " + address6 + "\n";
        out = out + bbm.debug() + "\n";

        bbm.deallocateBuffer(address1);
        out = out + "Deallocated buffer at address: " + address1 + "\n";
        out = out + bbm.debug() + "\n";

        bbm.deallocateBuffer(address7);
        out = out + "Deallocated buffer at address: " + address7 + "\n";
        out = out + bbm.debug() + "\n";

        //Write to the output file
        try {
            FileWriter writer = new FileWriter("out.txt");
            writer.write("Andy Phan, May 9 2023, OS Assignment 7\n\n");
            writer.write(out);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occured.");;
            e.printStackTrace();
        }
        System.out.println("Finished");

    }//end main

}//end class
