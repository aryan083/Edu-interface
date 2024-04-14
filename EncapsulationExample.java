public class EncapsulationExample
{
    public static int Balance=100;
    public static int debited=40;
   
}
class Receipt{
    public static void main(String args[]){
     EncapsulationExample X = new EncapsulationExample();
     int temp_d = X.debited;
     try{
     
         int temp_b = X.Balance;
     }
     catch(Exception e)
     {
       System.out.println("Access denied"); 
     }
     
    }
    }
