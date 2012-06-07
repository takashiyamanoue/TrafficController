package pukiwikiCommunicator.language;
import javax.swing.JTextArea;
public class Parser extends java.lang.Object
{
    public JTextArea message;
    public Parser()
    {
    }
    public boolean parseExpressionList(LispObject x, LispObject type)
    {
        ListCell w=new ListCell();
        w.d=ALisp.nilSymbol;
        if(!parseExpression(w,type)) return false;
        while(is(",")){
            ListCell y=new ListCell();
            if(!parseExpression(y,type)) return false;
            y.d=ALisp.nilSymbol;
//            ALisp.nconc(w,y);
            lisp.nconc(w,y);
        }
        ((ListCell)x).a=ALisp.cons(lisp.sym_list,w);
        return true;
    }
    public boolean parseParenList(LispObject x,LispObject type)
    {
        if(!is("(")) return false;
        if(!parseExpressionList(x,type)) return false;
        if(!is(")")) return false;
        return true;

    }
    public boolean leftHandSide(LispObject x, LispObject type, LispObject lt)
    {
        ListCell y=new ListCell();
        ListCell w=new ListCell();
        y.d=ALisp.nilSymbol;
        if(!isName(w,type)) return false;
        if(parseParenList(y,type)){
            ((ListCell)lt).a=lisp.recSymbol("dimension");
            w.d=ALisp.nilSymbol;
//            LispObject u=lisp.cons(lisp.recSymbol("quote"),w);
//            ((ListCell)x).a=lisp.cons(u,y);
            ((ListCell)x).a=ALisp.cons(w.a,y);
//            ((ListCell)x).a=w.a;
            return true;
        }
        else{
            ((ListCell)x).a=w.a;
            ((ListCell)lt).a=lisp.recSymbol("var");
            return true;
        }
    }
    public boolean parseAssign(LispObject x)
    {
        boolean dmy;
        ListCell lt=new ListCell();
        ListCell left=new ListCell();
        ListCell right=new ListCell();
        ListCell type=new ListCell();
        if(!leftHandSide(left,type,lt)) return false;
        dmy=rB();
        if(!is("=")) return false;
        if(!parseExpression(right,type)) return false;
        if(ALisp.eq(lt.a,lisp.recSymbol("dimension"))){
            right.d=ALisp.nilSymbol;
//            ALisp.nconc(((ListCell)left).a,right);
            lisp.nconc(((ListCell)left).a,right);
            ((ListCell)x).a=ALisp.cons(lisp.recSymbol("aput"),
                                       ((ListCell)left).a);
            return true;
        }
        if(ALisp.eq(lt.a,lisp.recSymbol("var"))){
//            LispObject quoted=consUnaryOpr("quote",left.a);
            ((ListCell)x).a=consBinaryOpr(lisp.sym_setq,left.a,right.a);
            return true;
        }
        return false;
    }
    public LispObject parseLine()
    {
        ListCell x=new ListCell();
        if(parsePrint(x)) return x.a;
        if(parseAssign(x)) return x.a;
        return null;
    }
    public LispObject parseLine(LispObject l)
    {
        line=l;
        pointer=line;
        return parseLine();
    }
    public Parser(ALisp l,JTextArea m)
    {
        line=null;
        pointer=null;
        lisp=l;
        message=m;
    }
    public void dmy()
    {
    }
    public boolean rB()
    {
        if(!is(" ")) return false;
        while(is(" ")){
            dmy();
        }
        return true;
    }
    public LispObject consUnaryOpr(String opr, LispObject x)
    {
        LispObject u,v;
        v=ALisp.cons(x,ALisp.nilSymbol);
        u=ALisp.cons(lisp.recSymbol(opr),v);
        return u;
    }
    public boolean parsePrint(LispObject x)
    {
        ListCell type=new ListCell();
        if(is("?")||is("print")||is("PRINT")){
            if(!parseExpression(x,type)) return false;
            ((ListCell)x).a=consUnaryOpr("print",((ListCell)x).a);
            return true;
        }
        return false;
    }
    public LispObject consBinaryOpr(LispObject opr,LispObject x, LispObject y)
    {
        LispObject u,v,w;
        w=ALisp.cons(y,ALisp.nilSymbol);
        v=ALisp.cons(x,w);
        u=ALisp.cons(opr,v);
        return u;
    }
    public boolean isNumber(LispObject x,LispObject type)
    {
        if(ALisp.Null(pointer)) return false;
        LispObject s=((ListCell)pointer).a;
        if(s.ltype==0) return false; // this is not atom
        int at=((Atom)s).atype;
        if(at==0) return false; // this atom is symbol, is not number.
        int nt=((MyNumber)s).ntype;
        
        /*
        if(s.getClass().getName().equals("MyInt")){
        */
        if(nt==2){
            ((ListCell)x).a=s; ((ListCell)type).a=lisp.recSymbol("number");
            pointer=((ListCell)pointer).d;
            return true;
        }
        /*
        if(s.getClass().getName().equals("MyDouble")){
        */
        if(nt==3){
            ((ListCell)x).a=s; ((ListCell)type).a=lisp.recSymbol("number");
            pointer=((ListCell)pointer).d;
            return true;
        }
        /*
        if(s.getClass().getName().equals("MyString")){
        */
        if(nt==1){
            ((ListCell)x).a=s; ((ListCell)type).a=lisp.recSymbol("string");
            pointer=((ListCell)pointer).d;
            return true;
        }
        /*
        if(s.getClass().getName().equals("MyNumber")){
        */
        if(nt==0){
            ((ListCell)x).a=s; ((ListCell)type).a=lisp.recSymbol("number");
            pointer=((ListCell)pointer).d;
            return true;
        }
        return false;
    }
    public boolean isName(LispObject x, LispObject type)
    {
        if(ALisp.Null(pointer)) return false;
        LispObject s=((ListCell)pointer).a;
        /*
        if(!s.getClass().getName().equals("ListCell")) return false;
        */
        if(s.ltype!=0) return false;
//        if(isReservedNR()) return false;
        if(!ALisp.eq(ALisp.car(s),lisp.recSymbol("name"))) return false;
        ((ListCell)x).a=ALisp.car(ALisp.cdr(s));
        ((ListCell)type).a=lisp.recSymbol("number");
        pointer=((ListCell)pointer).d;
        return true;
    }
    public boolean isReservedNR()
    {
        if(ALisp.Null(pointer)) return false;
        Symbol sym=(Symbol)(((ListCell)pointer).a);
        String str=(String)(lisp.symbolTable.get(sym));
        if(isReserved(str)) return true;
        return false;
    }
    public String symbol2str(Symbol x)
    {

        String y;
        y=((Symbol)(lisp.symbolTable.get(
             new Integer(x.hc)))).name;
        return y;
    }
    public ALisp lisp;
    public boolean is(String x)
    {
        String y;
        if(ALisp.Null(pointer)) return false;
        LispObject o=((ListCell)pointer).a;
        /*
        if(!(o.getClass().getName().equals("Symbol"))) return false;
        */
        if(o.ltype==0) return false; // if o is list then return false;
        int at=((Atom)o).atype;      // o is atom
        if(at!=0) return false;      // however, o is not symbol, return false;
        
        y=symbol2str((Symbol)o);
        if(!y.equals(x)) return false;
        pointer=((ListCell)pointer).d;
        return true;
    }
    public boolean parseElement(LispObject x,LispObject type)
    {
        boolean dmy;
        boolean sign=false;
        LispObject w,v;
        dmy=rB();
        if(is("(")){
            if(!parseExpression(x,type)) return false;
            if(is(")")) return true;
            return false;
        }
        if(is("-")) {sign=true; dmy=rB();}
        if(isNumber(x,type)) {
            if(sign){
                w=ALisp.cons(ALisp.car(x),ALisp.nilSymbol);
                w=ALisp.cons(lisp.recSymbol("neg"),w);
                ((ListCell)x).a=w;
            }
            return true;
        }
//        if(isReservedNR()) return false;
        if(isName(x,type)){
            ListCell y=new ListCell();
            y.d=ALisp.nilSymbol;
            if(parseParenList(y,type)){
                ListCell u=new ListCell();
                u.d=ALisp.nilSymbol;
                u.a=((ListCell)x).a;
//                LispObject f=lisp.get(((ListCell)x).a,
//                                      lisp.recSymbol("lambda"));
                /*
                if(lisp.Null(f)){ // case of array reference
//                   w=lisp.cons(lisp.recSymbol("quote"), u);
//                   v=lisp.cons(w,y)
                     v=lisp.cons(((ListCell)x).a,y);
                   ((ListCell)x).a=lisp.cons(lisp.recSymbol("aget"),v);
                }
                else{ // case of function call
                   LispObject argl=lisp.cdr(lisp.car(y));
                   ((ListCell)x).a=lisp.cons(((ListCell)x).a,argl);
                }
                */
                LispObject argl=lisp.cdr(lisp.car(y));
                ((ListCell)x).a=lisp.cons(((ListCell)x).a,argl);


            }
            if(sign){
              w=lisp.cons(lisp.car(x),lisp.nilSymbol);
              w=lisp.cons(lisp.recSymbol("neg"),w);
              ((ListCell)x).a=w;
            }
            return true;
        }
        return false;
    }
    public boolean parseFactor(LispObject x, LispObject type)
    {
        ListCell y=new ListCell();
        if(!parseElement(x,type)) return false;
        do{
            if(is("^")){
                if(parseElement(y,type)){
                   ((ListCell)x).a=consBinaryOpr(lisp.sym_m_exp2,
                       ((ListCell)x).a,((ListCell)y).a);
                }
            }
            else return true;
        } while(true);
    }
    public boolean parseTerm(LispObject x, LispObject type)
    {
        ListCell y=new ListCell();
        if(!parseFactor(x,type)) return false;
        do{
            if(is("*")){
                if(parseFactor(y,type)){
                  ((ListCell)x).a=consBinaryOpr(lisp.sym_m_mul,
                     ((ListCell)x).a,((ListCell)y).a);
                }
            }
            else if(is("/")){
                if(parseFactor(y,type)){
                    ((ListCell)x).a=consBinaryOpr(lisp.sym_m_div,
                       ((ListCell)x).a,((ListCell)y).a);
                }
            }
            else return true;
        } while(true);
    }
    public LispObject x;
    public boolean parseExpression(LispObject x,LispObject type)
    {
        ListCell y=new ListCell();
        boolean dmy;
        if(!parseTerm(x,type)) return false;
        dmy=rB();
        do{
          //  dmy=rB();
            if(is("+")){
                if(parseTerm(y,type)){
                   ((ListCell)x).a=consBinaryOpr(lisp.sym_m_add,
                       ((ListCell)x).a,((ListCell)y).a);
                }
                dmy=rB();
            }
            else if(is("-")){
                if(parseTerm(y,type)){
                    ((ListCell)x).a=consBinaryOpr(lisp.sym_m_sub,
                       ((ListCell)x).a,((ListCell)y).a);
                }
                dmy=rB();
            }
            else return true;
        } while(true);
    }
    public int sizeOfReserveSymbols;
    public boolean isReserved(String s)
    {
        int i;
        for(i=0;i<sizeOfReserveSymbols;i++){
            if(s.equals(reserveSymbols[i])) return true;
        }
        return false;
    }
    public String reserveSymbols[];
    public void setReserveSymbols(String[] s, int l)
    {
        reserveSymbols=s;
        sizeOfReserveSymbols=l;
    }
    public void comment()
    {
        /*
        This Class is for parse a program.

        line is the input.
        pointer is the pointer to the line.
        */
    }
    public LispObject pointer;
    public LispObject line;
    public Parser(LispObject s,ALisp l)
    {
        line=s;
        pointer=line;
        lisp=l;
    }
}

