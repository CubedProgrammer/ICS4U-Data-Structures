import structs.HashMap_03_29;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * HashAssign1.java
 * @author Kevin Zhang
 */
public class HashAssign1 {

    /**
     * Gets all anagrams of a string
     * @param s The string in question
     * @return An array list of anagrams
     */
    public static final ArrayList<String> anagram(String s) {
        //an arraylist
        ArrayList<String>arr=new ArrayList<String>();
        //base case
        if(s.length()==1) {
            arr.add(s);
            return arr;
        }
        //answer and boolean array to not have duplicate anagrams
        ArrayList<String>ans=new ArrayList<String>();
        boolean[]used=new boolean[127];
        //loops through every letter in the string
        for(int i=0;i<s.length();i++) {
            //checks if the character hasn't been used
            if(!used[s.charAt(i)]) {
                //removes the character and call recursively
                arr=anagram(s.substring(0,i)+s.substring(i+1));
                //adds the character back onto the front
                for(int j=0;j<arr.size();j++) {
                    ans.add(s.substring(i,i+1)+arr.get(j));
                }
                //mark the character as used
                used[s.charAt(i)]=true;

            }

        }

        return ans;

    }

    /**
     * Main method
     * @param args Args
     */
    public static final void main(String[]args)throws IOException {
        //reader and map
        BufferedReader reader=new BufferedReader(new InputStreamReader(HashAssign1.class.getResourceAsStream("/dictionary.txt")));
        String s=reader.readLine();
        HashMap_03_29<String,String>map=new HashMap_03_29<String,String>();
        //read in all the words and add them to the map
        while(s!=null) {
            map.add(s);
            s=reader.readLine();
        }
        //close the reader
        reader.close();
        //read in a word and get its anagrams
        Scanner scanner=new Scanner(System.in);
        ArrayList<String>perms=anagram(scanner.next());
        //if its anagrams are in the dictionary, print it out
        for(int i=0;i<perms.size();i++) {
            if(map.contains(perms.get(i)))
                System.out.println(perms.get(i));
        }

    }

}