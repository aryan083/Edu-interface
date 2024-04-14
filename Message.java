 

 class overriding
{

   public  void dis()
   {
    System.out.println("Welcome to the parent class");
   }
}
    public class Message extends overriding
    {
         public void dis(){
            System.out.println("Welcome to child class.");
            super.dis();
        }
        public static void main(String args[])
        {
            Message m = new Message();
            m.dis();
        }

        
    }

