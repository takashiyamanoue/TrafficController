package pukiwikiCommunicator.language;
public class MyString extends MyNumber
{
    public boolean eq(MyNumber y)
    {/*
        if(y.getClass().getName().equals("MyInt")){
            return false;
        }
        if(y.getClass().getName().equals("MyDouble")){
            return false;
        }
        if(y.getClass().getName().equals("MyString")){
        */
        if(y.ntype==1){
            return val.equals(((MyString)y).val);
        }
       return false;
    }
    public MyNumber add(MyNumber y)
    {
        int nt=y.ntype;
        /*
            if(y.getClass().getName().equals("MyInt")){
            */
        if(nt==2){  // MyInt
            String r=val+((MyInt)y).val;
            return new MyString(r);
        }
        /*
        if(y.getClass().getName().equals("MyDouble")){
        */
        if(nt==3){ // MyDouble
            return new MyString(val+((MyDouble)y).val);
        }
        /*
        if(y.getClass().getName().equals("MyString")){
        */
        if(nt==1){ // MyString
            return new MyString(val+((MyString)y).val);
        }
        return null;
}
    public MyString(String x)
    {
        val=x;
        ntype=1;
    }
    public String toString(){
    	return val;
    }
    public String val;
}

