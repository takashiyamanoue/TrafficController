package pukiwikiCommunicator.language;
public class MyInt extends MyNumber
{
    public MyNumber neg()
    {
        return new MyInt(-val);
    }
    public boolean ne(MyNumber y)
    {
        int nt=y.ntype;
        /*
        if(y.getClass().getName().equals("MyInt")){
        */
        if(nt==2){
            return val!=((MyInt)y).val;
        }
        /*
        if(y.getClass().getName().equals("MyDouble")){
        */
        if(nt==3){
            return (double)val!=((MyDouble)y).val;
        }
        return false;
    }
    public MyNumber mod(MyNumber y)
    {
        int nt=y.ntype;
        /*
        if(y.getClass().getName().equals("MyInt")){
        */
        if(nt==2){
            return new MyInt(val%((MyInt)y).val);
        }
        /*
        if(y.getClass().getName().equals("MyDouble")){
        */
        if(nt==3){
            return new MyDouble(val%((MyDouble)y).val);
        }
        return null;
    }
    public MyNumber tan()
    {
        return new MyDouble(Math.tan((double)val));
    }
    public MyNumber sqrt()
    {
        return new MyDouble(Math.sqrt((double)val));
    }
    public MyNumber sin()
    {
        return new MyDouble(Math.sin((double)val));
    }
    public MyNumber log()
    {
        return new MyDouble(Math.log((double)val));
    }
    public MyNumber exp()
    {
        return new MyDouble(Math.exp((double)val));
    }
    public MyNumber cos()
    {
        return new MyDouble(Math.cos((double)val));
    }
    public MyNumber atan()
    {
        return new MyDouble(Math.atan((double)val));
    }
    public int getInt()
    {
        return val;
    }
    public boolean ge(MyNumber y)
    {
        int nt=y.ntype;
        /*
         if(y.getClass().getName().equals("MyInt")){
         */
         if(nt==2){// MyInt
            return val>=((MyInt)y).val;
        }
        /*
        if(y.getClass().getName().equals("MyDouble")){
        */
        if(nt==3){// MyDouble
            return (double)val>=((MyDouble)y).val;
        }
        return false;
   }
    public boolean le(MyNumber y)
    {
        int nt=y.ntype;
        /*
        if(y.getClass().getName().equals("MyInt")){
        */
        if(nt==2){
            return val<=((MyInt)y).val;
        }
        /*
        if(y.getClass().getName().equals("MyDouble")){
        */
        if(nt==3){
            return (double)val<=((MyDouble)y).val;
        }
        return false;
    }
    public boolean eq(MyNumber y)
    {
        int nt=y.ntype;
        /*
        if(y.getClass().getName().equals("MyInt")){
        */
        if(nt==2){// MyInt
            return val==((MyInt)y).val;
        }
        /*
        if(y.getClass().getName().equals("MyDouble")){
        */
        if(nt==3){// MyDouble
            return (double)val==((MyDouble)y).val;
        }
        return false;
    }
    public boolean gt(MyNumber y)
    {
        int nt=y.ntype;
        /*
        if(y.getClass().getName().equals("MyInt")){
        */
        if(nt==2){ //MyInt
            return val>((MyInt)y).val;
        }
        /*
        if(y.getClass().getName().equals("MyDouble")){
        */
        if(nt==3){ //MyDouble
            return (double)val>((MyDouble)y).val;
        }
        return false;
    }
    public boolean lt(MyNumber y)
    {
        int nt=y.ntype;
        /*
        if(y.getClass().getName().equals("MyInt")){
        */
        if(nt==2){
            return val<((MyInt)y).val;
        }
        /*
        if(y.getClass().getName().equals("MyDouble")){
        */
        if(nt==3){
            return (double)val<((MyDouble)y).val;
        }
        return false;
    }
    public double exp(double x, double y)
    {
        return Math.exp(y*(Math.log(x)));
    }
    public MyNumber exp(MyNumber y)
    {
        int i;
        int xi;
        int nt=y.ntype;
        double xd,yd;
        /*
         if(y.getClass().getName().equals("MyInt")){
         */
         if(nt==2){ // MyInt
            int yy=((MyInt)y).val;
            if(yy==0) return new MyInt(1);
            if(yy>0){
                xi=1;
                for(i=0;i<yy;i++) xi=xi*val;
                return new MyInt(xi);
            }
            if(yy<0){
                xd=1.0;
                for(i=0;i<-yy;i++) xd=xd/val;
                return new MyDouble(xd);
            }
            return null;
        }
        /*
        if(y.getClass().getName().equals("MyDouble")){
        */
        if(nt==3){ //MyDouble
            yd=((MyDouble)y).val;
            if(yd==0.0) new MyInt(1);
            if(val<=0) return null;
            double r=exp((double)val,yd);
            return new MyDouble(r);
        }
        return null;
   }
    public MyNumber div(MyNumber y)
    {
        int nt=y.ntype;
        /*
        if(y.getClass().getName().equals("MyInt")){
        */
        if(nt==2){ // MyInt
            return new MyInt(val/((MyInt)y).val);
        }
        /*
        if(y.getClass().getName().equals("MyDouble")){
        */
        if(nt==3){ //MyDouble
            return new MyDouble(((double)val)/((MyDouble)y).val);
        }
        return null;
    }
    public MyInt()
    {
        ntype=2;
    }
    public MyNumber mul(MyNumber y)
    {
        int nt=y.ntype;
        /*
        if(y.getClass().getName().equals("MyInt")){
        */
        if(nt==2){
            return new MyInt(val*((MyInt)y).val);
        }
        /*
        if(y.getClass().getName().equals("MyDouble")){
        */
        if(nt==3){
            return new MyDouble((double)val*((MyDouble)y).val);
        }
        return null;
    }
    public MyNumber sub(MyNumber y)
    {
        int nt=y.ntype;
        /*
        if(y.getClass().getName().equals("MyInt")){
        */
        if(nt==2){
            return new MyInt(val-((MyInt)y).val);
        }
        /*
        if(y.getClass().getName().equals("MyDouble")){
        */
        if(nt==3){
            return new MyDouble((double)val-((MyDouble)y).val);
        }
        return null;
    }
    public MyNumber add(MyNumber y)
    {
        int nt=y.ntype;
        /*
        if(y.getClass().getName().equals("MyInt")){
        */
        if(nt==1){ // MyString
             String x=((MyString)y).val;
             int r=0;
             try{
                r=(new Integer(x)).intValue();
             }
             catch(Exception e){
                return null;
             }
                return new MyInt(val+r);
        }
        if(nt==2){ // MyInt
            int r=val+((MyInt)y).val;
            return new MyInt(r);
        }
        /*
        if(y.getClass().getName().equals("MyDouble")){
        */
        if(nt==3){// MyDouble
            return new MyDouble((double)val+((MyDouble)y).val);
        }
        return null;
    }
    public int val;
    public MyInt(int x)
    {
        val=x;
        ntype=2;
    }
}

