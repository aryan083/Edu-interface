 
import java.lang.String;
import java.util.Scanner;
public class String_funcs
{
    public static void main(String args[])
    {
        char c;

        Scanner sc = new Scanner(System.in);
        System.out.println("Welcome to the string functions.");
        System.out.println("A. Find the length of a string");
        System.out.println("B. Find the character at an index");
        System.out.println("C. Find the substring of your string");
        System.out.println("D. Find the equality of you rstrings");
        System.out.println("E. Compare your strings");
        System.out.println("F. Merge your strings");
        System.out.println("G. Find the uppercase of your string");
        System.out.println("H. Find the lowercase of your string");
        System.out.println("I. Find the index of your character");
        System.out.println("J. Trim your string");
        System.out.println("K. Replace characters in your strings");
        c=sc.nextLine().charAt(0);
        switch(c)
        {
            case 'A':
                {
                    length_string();     

                    break;
                }
            case 'B':
                {
                    char_index();
                    break;
                }
            case 'C':
                {
                    sub_string();
                    break;
                }
            case 'D':
                {
                    equality_check();
                    break;
                }
            case 'E':
                {
                    comp_strings();
                    break;
                }
            case 'F':
                {
                    merge_string();
                    break;
                }
            case 'G':
                {
                    to_uppercase();
                    break;
                }
            case 'H':
                {
                    to_lowercase();
                    break;
                }
            case 'I':
                {
                    indexof_char();
                    break;
                }
            case 'J':
                {
                    trim_string();
                    break;
                }
            case 'K':
                {
                    replace_string();
                    break;
                }
            default:
                {
                    break;
                }
        }

    }

    static void length_string()
    {
        Scanner sc = new Scanner(System.in);
        String string1;
        System.out.println("Please enter your desired string");
        string1 = sc.nextLine();
        int x = string1.length();
        System.out.println("Length of your string is:"+x);

    }

    static void char_index()
    {
        Scanner sc = new Scanner(System.in);
        String string1;
        System.out.println("Please enter your desired string");
        string1 = sc.nextLine();
        System.out.println("Please enter the position number");
        int i = sc.nextInt();
        char q = string1.charAt(i);
        System.out.println("Character at "+i+"in your string is :"+q);
    }

    static void sub_string()
    {
        Scanner sc = new Scanner(System.in);
        String string1;
        System.out.println("Please enter your desired string");
        string1 = sc.nextLine();
        System.out.println("Please enter the position number from where you want to substring");
        int i = sc.nextInt();
        System.out.println("Please enter the ending position number if you want, else put in 0");
        int j = sc.nextInt();
        if(j==0)
        {
            String q = string1.substring(i);
            System.out.println("Your substring is:"+q);
        }
        else
        {
            String q = string1.substring(i,j);
            System.out.println("Your substring is:"+q);

        }
    }

    static void equality_check()
    {
        Scanner sc = new Scanner(System.in);
        String string1,string2;
        System.out.println("Please enter your desired string");
        string1 = sc.nextLine();
        System.out.println("Please enter your other desired string");
        string2 = sc.nextLine();
        System.out.println("Do you want to check equality irrespective of case? Say true or false in smal case only.");
        boolean c = sc.nextBoolean();
        if(c==false)
        {
            boolean b = string1.equals(string2);
            if(b== false)
            {
                System.out.println("The given strings are not the same");
            }
            else{
                System.out.println("The given strings are the same"); 
            }
        }
        else
        {
            boolean b = string1.equalsIgnoreCase(string2);

            if(b== false)
            {
                System.out.println("The given strings are not the same");
            }
            else{
                System.out.println("The given strings are the same"); 
            }   
        }
    }

    static void comp_strings()
    {
        Scanner sc = new Scanner(System.in);
        String string1,string2;
        System.out.println("Please enter your desired string");
        string1 = sc.nextLine();
        System.out.println("Please enter your other desired string");
        string2 = sc.nextLine();
        int x = string1.compareTo(string2);
        if(x>0)
        {
            System.out.println("The first string is greater than second one");
        }
        else if(x==0){
            System.out.println("The given strings are the same"); 
        }
        else{
            System.out.println("The first string is smaller than the froemr one");
        }
    }

    static void merge_string()
    {
        Scanner sc = new Scanner(System.in);
        String string1,string2,string3,string4;
        System.out.println("Please enter your desired string");
        string1 = sc.nextLine();
        System.out.println("Please enter your other desired string");
        string2 = sc.nextLine();
        string3 = string1+string2;
        System.out.println(string3);
        string4 = string1.concat(string2);
        System.out.println(string4);
        string4 += string1;
        System.out.println(string4);
    }

    static void to_uppercase()
    {
        Scanner sc = new Scanner(System.in);
        String string1,string2;
        System.out.println("Please enter your desired string");
        string1 = sc.nextLine();
        string2 = string1.toUpperCase();
        System.out.println(string2);
    }

    static void to_lowercase()
    {
        Scanner sc = new Scanner(System.in);
        String string1,string2;
        System.out.println("Please enter your desired string");
        string1 = sc.nextLine();
        string2 = string1.toLowerCase();
        System.out.println(string2);
    }

    static void indexof_char()
    {
        Scanner sc = new Scanner(System.in);
        String string1,string2;
        System.out.println("Please enter your desired string");
        string1 = sc.nextLine();
        System.out.println("Please enter your desired word");
        char c0 = sc.nextLine().charAt(0);
        System.out.println("Please enter the number ofrepeated time if the desired word is repetative.");
        int u = sc.nextInt();
        System.out.println("Would you like to search from last character, i.e. in reverse.Answer in True or False in small case only");
        boolean q = sc.nextBoolean();
        if(q==true)
        {
            u=2;
        }
        if(u==0)
        {
            int x =string1.indexOf(c0);
            System.out.println(x);
        }
        else if(u==2){
            int x =string1.lastIndexOf(c0);
            System.out.println(x);
        }
        else{

            int x =string1.indexOf(c0,u);
            System.out.println(x);

        }
    }

    static void trim_string()
    {
        Scanner sc = new Scanner(System.in);
        String string1,string2;
        System.out.println("Please enter your desired string");
        string1 = sc.nextLine();
        string2 = string1.trim();
        System.out.println(string2);
    }

    static void replace_string()
    {
        Scanner sc = new Scanner(System.in);
        String string1,string2;
        char c,d;
        System.out.println("Please enter your desired string");
        string1 = sc.nextLine();
        System.out.println("Please enter your character to be changed");
        c = sc.nextLine().charAt(0);
        System.out.println("Please enter your desired character");
        d = sc.nextLine().charAt(0);
        string2 = string1.replace(c,d);
        System.out.println(string2);
    }
}


