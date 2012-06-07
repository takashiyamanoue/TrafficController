package pukiwikiCommunicator.language;
public class MyNumber extends Atom
{
    public static int StringTYPE=1;

    public static int DoubleTYPE=3;

    public static int IntTYPE=2;

    public int ntype=0;

    public MyNumber neg()
    {
        return null;
    }
    public boolean ne(MyNumber y)
    {
        return val!=y.val;
    }
    public MyNumber mod(MyNumber y)
    {
        return null;
    }
    public MyNumber log()
    {
        return null;
    }
    public MyNumber exp()
    {
        return null;
    }
    public MyNumber sqrt()
    {
        return null;
    }
    public MyNumber atan()
    {
        return null;
    }
    public MyNumber tan()
    {
        return null;
    }
    public MyNumber cos()
    {
        return null;
    }
    public MyNumber sin()
    {
        return null;
    }
    public int getInt()
    {
        return val;
    }
    public MyNumber exp(MyNumber y)
    {
        return null;
    }
    public boolean le(MyNumber y)
    {
        return val<=y.val;
    }
    public boolean lt(MyNumber y)
    {
        return false;
    }
    public boolean ge(MyNumber y)
    {
        return val>=y.val;
    }
    public boolean gt(MyNumber y)
    {
        return val>y.val;
    }
    public boolean eq(MyNumber y)
    {
        return val==y.val;
    }
    public MyNumber div(MyNumber y)
    {
        return null;
    }
    public MyNumber mul(MyNumber y)
    {
        return null;
    }
    public MyNumber sub(MyNumber y)
    {
        return null;
    }
    public MyNumber add(MyNumber y)
    {
        return null;
    }
    public MyNumber()
    {
        atype=1;
        ntype=0;
    }
    public MyNumber(int x)
    {
        val=x;
        atype=1;
        ntype=0;
    }
    public int val;
}

