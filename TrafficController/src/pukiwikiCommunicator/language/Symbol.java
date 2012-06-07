package pukiwikiCommunicator.language;
public class Symbol extends Atom
{
    public String name;
    public Symbol(String s)
    {
        name=s;
        hc=s.hashCode();
        atype=0; //a type: atom type, 0:symbol
    }
    public Symbol(int x)
    {
        hc=x;
        atype=0;
    }
    public int hc;
    public String toString(){
    	return name;
    }

}

