package pukiwikiCommunicator.language;
import java.awt.Color;
import javax.swing.JTextArea;
import pukiwikiCommunicator.controlledparts.FrameWithLanguageProcessor;
public class ABasic extends ALisp implements Runnable
{
 
    public LispObject evalWhile(LispObject form, LispObject env)
    throws java.lang.Exception
    {
        LispObject rtn=null;

        LispObject relop;
        LispObject slist;
        relop   =second(form);
        slist   =third(form);

           while(!Null(eval(relop,env))){
                  rtn=eval(slist,env);
           }
        return rtn;
    }



    public Color colors[]=new Color[8];

    public int previousY;
    public int previousX;

    public void clearEnvironment()
    {
        super.clearEnvironment();
        arrays.init();
    }
    public LispObject printLine(LispObject x)
    {
    	String line="";
        LispObject s=x;
        String o;
        while(!atom(s)){
            o=print.print(car(s));
            line=line+o;
            printArea.append(o);
            s=((ListCell)s).d;
            if(!atom(s)) printArea.append(",");
        }
        if(!Null(s)){
            printArea.append(".");
            o=print.print(s);
            printArea.append(o);
        }
        this.setResult(line);
        printArea.append("\n");
        printArea.repaint();
        return x;
    }
    public LispObject evalFor(LispObject form, LispObject env)
    throws Exception
    {
        LispObject rtn;

        LispObject variable;
        LispObject initial;
        LispObject end;
        LispObject step;
        LispObject slist;
        MyNumber vx;
        variable=second(form);
        initial =third(form);
        end     =fourth(form);
        step    =car(cdr(cdr(cdr(cdr(form)))));
        slist   =car(cdr(cdr(cdr(cdr(cdr(form))))));

        // set initial value
        rtn=setf(variable,eval(initial,env),env);

        LispObject stepx=eval(step,env);
        if(!numberp(stepx)) return nilSymbol;

        LispObject endx=eval(end,env);
        if(!numberp(endx)) return nilSymbol;

        if(((MyNumber)stepx).gt(new MyInt(0))){
            while(true){
              vx=((MyNumber)(eval(variable,env)));
              if(vx.gt((MyNumber)endx)) return tSymbol;
              rtn=eval(slist,env);
              vx=((MyNumber)(eval(variable,env)));
              rtn=setf(variable,vx.add((MyNumber)stepx));
            }
        }
        else{
            while(true){
              vx=((MyNumber)(eval(variable,env)));
              if(vx.lt((MyNumber)endx)) return tSymbol;
              rtn=eval(slist,env);
              vx=((MyNumber)(eval(variable,env)));
              rtn=setf(variable,vx.add((MyNumber)stepx));
            }
        }

    }
    public LispObject applyUserDefined(LispObject proc,
                            LispObject argl,
                            LispObject env)
    throws Exception
    {
               LispObject f;
               f=get(proc, recSymbol("lambda"));
               if(Null(f)) {
                   // if proc is not a function, ...
                       f=assoc(proc,((ListCell)env).a);
                       if(Null(f)) {
                        // if proc is not associated with any name ...
                           plist("can not find out ",proc);
                           return nilSymbol;
                       }
                       else if(eq(recSymbol("dimension"),
                                   car(second(f))))
                           {
                                // if proc is dimension name, ...
                                LispObject getdimarg=
                                     cons(proc,
                                     cons(argl,nilSymbol));
                              return apply(recSymbol("aget"),getdimarg,env);
                           }
                       else f=second(f);
//                    }
               }
               return apply( f,argl,env);
    }
    public LispObject caseOfDefDim(LispObject f, LispObject env)
    throws Exception
    {
        LispObject x=setf(car(f),cons(recSymbol("dimension"),
                                 cons(car(f),nilSymbol)));
        return environment;
    }
     public boolean isDefDim(LispObject form)
    {
        if(atom(form)) return false;
        if(eq(car(form),recSymbol("defdim"))) return true;
        return false;
    }
    public LispObject defExt(LispObject s, LispObject env)
    throws Exception
    {
        if(isDefDim(s)){
            environment=caseOfDefDim(cdr(s),env);
            return second(s);
        }
        return nilSymbol;
    }
    public synchronized void evalList(LispObject x)
    throws Exception
    {
        while(!Null(x)){
            LispObject s=car(x);
            LispObject r=preEval(s,environment); //eval the S expression
            x=cdr(x);
        }
        printArea.append("OK\n");
        printArea.setCaretPosition(printArea.getText().length());
        printArea.repaint();
    }
    public BasicParser basicparser;
    public void parseCommands(LispObject x)
    {
        String str;
//        str=print.print(x);
//        printArea.appendText(str);
        if(x==null) return;
        if(Null(x)) return;
        if(numberp(car(x))){
            if(Null(cdr(x))) sourceManager.deleteLine(car(x));
            else sourceManager.addLine(x);
            return;
        }
        if(eq(car(x),recSymbol("list"))||
           eq(car(x),recSymbol("LIST"))){
              str=sourceManager.printTheSource();
              printArea.append(str);
              return;
        }
        if(eq(car(x),recSymbol("run"))||
           eq(car(x),recSymbol("RUN"))){
              LispObject o=sourceManager.getTheProgram();
//              str=print.print(o);
//              printArea.appendText(str);
              LispObject p=basicparser.parseBasic(o);
              str=print.print(p);
              printArea.append(str);
              try{
              evalList(p);
              }
              catch(Exception e){}
              printArea.repaint();
              return;
        }
        else {
//            LispObject t=parser.parseLine(x);
//            LispObject r=preEval(t,environment); //eval the S expression
            LispObject t=basicparser.parseBasic(x);
//            str=print.print(t);
//            printArea.appendText(str);
            try{
              evalList(t);
            }
            catch(Exception e){}
            printArea.repaint();
        }
    }
    SourceManager sourceManager;
    LispObject sourceProgram;
    public void comment()
    {
        /*
        ABasic class

        commands:
            '?'<expression>
           | 'print' <expression>
           | 'PRINT' <expression>    ... print

            <var>'='<expression>
           |<var>'('<expressionlist>')''='<expression> ... assign

            <linenumber> <statement> ... store program
            <linenumber>             ... delete the statement

            <def>   ... define the function

            'list'  ... listing the program
            'run'   ... compiling and run the program


        no goto statement.
        ignore line numbers while compiling and running.

        <def>::= 'def' <fun>'('<argl>')'=<block>

        this is translated into

             (defun <fun> (<argl>) <block>)

        <dim>::= 'dim' <name>['('<numlist>')']

        this is translated into

             (defdim <name>)

        <if>::= 'if' <loe> 'then' <block> 'else' <block> 'endif'

        this is translated into

              (if <loe> <block> <block>)


        <if>::= 'if' <loe> 'then' <block> 'endif'

        this is translated into

              (if <loe> <blocl> t)


        <for>::= 'for' <var>'='<expression> 'to' <expression>
                          <block>
                 'next' <var>

        this is translated into

              (for  <var> <expression> <expression> 1 <block>)

        <for>::= 'for' <var>'='<expression> 'to' <expression> 'step'
                                  <expression>
                          <block>
                  'next' <var>

        this is translated into

              (for <var> <expression> <expression> <expression>
                          <block>)

        <while>::='while' <relationalexpression> <statement>
        
        this is translated into
              (while <relationalexpression> <statement>)

        <block>::=<statement>|<statementList>
        <statementList>::='['<statement>{(';'|<lf>)<statement>}']'

        this is translated into

              (progn <statement> ... <statement>)

        <statement>=<if>|<for>|<print>|<return>|<assign>

        <return>::='return' <expression>

        this is translated into
              (return <expression>)

        <return>::='return'

        this is translated into
              (return t)

        <gosub>::='gosub' <fname>'('<expressionlist>')'

        this is translated into
              (<fname> <expressionlist>)

        sourceprogram=
        (
         (<lineNumber0> <list of tokens>)
         (<lineNumber1> <list of tokens>)
            ...
         (<lineNumbern-1> <list of tokens>)
        )

        programInS=

        environment (in super)
        = association list

        */
    }
    public LispObject evalMiscForm(LispObject form, LispObject env)
    throws Exception
    {
            LispObject fform=car(form);
            if(eq(fform,recSymbol("for"))){
                return evalFor(form,env);
            }
            if(eq(fform,recSymbol("while"))){
                return evalWhile(form,env);
            }
            return null;
    }
    public LispObject evalAtomForm(LispObject form, LispObject env){
    	LispObject rtn=null;
        if(numberp(form)) rtn= form;
        else
        if(eq(tSymbol,form)) rtn= tSymbol;
        else
        if(eq(nilSymbol,form)) rtn= nilSymbol;
        else{
           LispObject w=assoc(form,((ListCell)env).a);
           if(Null(w)){
             plist("can not find out ",form);
             return nilSymbol;
           }
           //
           if(!atom(second(w))){
              rtn=nilSymbol;
              if(eq(recSymbol("dimension"),car(second(w)))){
                 rtn= form;
              }
           }
           else
           //
           rtn= second(w);
        }    	
        return rtn;
    }
    public LispObject applyMiscOperation(LispObject proc,LispObject argl)
    {

        /*
          array access operations

          assign a <value> to the element of the <array>, indexed by <index>:
          (aput <array> <index> <value>)
          <array>: symbol
          <index>: (i1 i2 ... in)
          <value>: LispObject

        */

            if(eq(proc,recSymbol("aput"))){
                String aname=print.print(car(argl));
                String index=print.print(second(argl));
                LispObject val=third(argl);
                arrays.put(aname,index,val);
                return val;
            }
        /*
          array access operations

          get a <value> from the element of the <array>, indexed by <index>:
          (aget <array> <index>)
          <array>: symbol
          <index>: (i1 i2 ... in)

        */
             if(eq(proc,recSymbol("aget"))){
                String aname=print.print(car(argl));
                String index=print.print(second(argl));
                LispObject val= arrays.get(aname,index);
                if(val==null) return nilSymbol;
                return val;
            }

        /*
        */
             if(eq(proc,recSymbol("printl"))){
                return printLine(argl);
             }
             
      return null;
    }
    public ArrayManager arrays;
    public String reservedWords[]={"print", "PRINT",
                                    "list",  "LIST",
                                    "run",   "RUN",
                                    "if",    "IF",
                                    "then",  "THEN",
                                    "else",  "ELSE",
                                    "for",   "FOR",
                                    "to",    "TO",
                                    "step",  "STEP",
                                    "next",  "NEXT",
                                    "def",   "DEF",
                                    "return","RETURN",
                                    "rtn",   "RTN",
                                    "gosub", "GOSUB",
                                    "input", "INPUT",
                                    "dim",   "DIM",
                                    "line",  "LINE",
                                    "pset",  "PSET",
                                    "circle","CIRCLE",
                                    "true",  "TRUE",
                                    "nil",   "NIL",
                                    "pset",  "PSET",
                                    "call",  "CALL",
                                    "gosub", "GOSUB",
                                    "while", "WHILE"};
    public Parser parser;
    public String breakSymbols[]=
          {" ", "+", "-", "*", "/", "=", "(", ")",
           ",", ".", ":", "?", "^", ";", "$", "#",
           "%", "&", "\"","<", ">", "'", "!", "@",
           "|", "[", "]", "{", "}"};
    public ABasic()
    {
    }
    public ABasic(JTextArea in, JTextArea out,CQueue iq,FrameWithLanguageProcessor g)
    {
        init(in,out,iq,g);
    }


    public void init(JTextArea rarea, JTextArea parea,CQueue iq,FrameWithLanguageProcessor g)
    {
        /*
         me=null;
         inqueue=iq;
        symbolTable=new Hashtable();
        nilSymbol  = recSymbol("nil");
        environment=cons(nilSymbol,nilSymbol);
        tSymbol    = recSymbol("true");
        initSymbols();
        initFunctionDispatcher();
    //    inqueue=iq;
    //    outqueue=oq;
        readArea=rarea;
        printArea=parea;
        */
        super.init(rarea,parea,iq,g);

        read=(ReadS)(new ReadLine(inqueue,this));
        ((ReadLine)read).setBreak(breakSymbols,29);
        ((ReadLine)read).setReserve(reservedWords,50);
        print=new PrintS(this);
        gui=g;
        parser=new Parser(this,parea);
//        parser.setReserveSymbols(reserveSymbols,2);
        arrays=new ArrayManager();
        sourceManager=new SourceManager(this);
        basicparser=new BasicParser(this,parea);
        colors[0]=Color.black;
        colors[1]=Color.blue;
        colors[2]=Color.red;
        colors[3]=Color.magenta;
        colors[4]=Color.green;
        colors[5]=Color.cyan;
        colors[6]=Color.yellow;
        colors[7]=Color.white;
    }
    public void run()
    {
        String o;
        while(me!=null){

            if(inqueue!=null){
//                printArea.appendText("OK\n");
              while(!inqueue.isEmpty()){
//               while(true){
//                  printArea.appendText("OK\n");
//                  if(inqueue.isEmpty()) break;
//                printArea.appendText("OK\n");
               LispObject s=((ReadLine)read).readLine(inqueue); //input the line
//               LispObject s=((ReadS)read).read(inqueue);
               if(s!=null){
//                  LispObject r=preEval(s,environment);
//                  LispObject r=eval(s,environment);
//                 String o=print.print(s);
//                 printArea.appendText(o+"\n");
//                 LispObject t=parser.parseLine(s);  // translate the line into S expression.
//                 o=print.print(t);
//                 printArea.appendText(o+"\n");
//                 LispObject r=eval(t,environment);
//                 LispObject r=preEval(t,environment); //eval the S expression
                   parseCommands(s);
//                 o=print.print(r);
//                 printArea.appendText(o+'\n');
               }
               printArea.repaint();
//               printArea.appendText("OK\n");

             }

            }

            try{ Thread.sleep(100);}
            catch(InterruptedException e){System.out.println(e);}
        }

//        stop();
    }
}

