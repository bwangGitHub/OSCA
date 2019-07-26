package org.bupt.osca;
public class ClassDemo {
 
       private  String text = "Hello World!";
       private int temp = 123;
       public  void print(int value) {
              System.out.println(value);
              System.out.println(temp);
              add(1+1);
 
       }
       public int add(int value) {
    	   value =value+1;
    	   while(value==1) 
    	   {
    	   continue;
    	   }
    	   return value;
       }
       
       
       public static void main(String args[])
       {

       }
}


abstract class AbstractClass
{
	public abstract void Print();
	public int i;
}



