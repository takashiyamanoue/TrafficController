package pukiwikiCommunicator.language;
import java.util.Hashtable;

import javax.swing.JTextArea;
import pukiwikiCommunicator.controlledparts.FrameWithLanguageProcessor;
public class ALisp extends java.lang.Object implements Runnable
{
    public LispObject applyGraphicsOperation(LispObject proc, LispObject argl)
    {
        return null;
    }

    public Symbol sym_m_neg;
    public Symbol sym_setq;
    public void initFunctionDispatcher()
    {
         functionDispatcher=new Hashtable();

         Fun_car fcar=new Fun_car(this);
         functionDispatcher.put(sym_car,fcar);
         functionDispatcher.put(sym_first,fcar);
         Fun_cdr fcdr=new Fun_cdr(this);
         functionDispatcher.put(sym_cdr,fcdr);
         functionDispatcher.put(sym_rest,fcdr);
         functionDispatcher.put(sym_cons,new Fun_cons(this));
         functionDispatcher.put(sym_atom,new Fun_atom(this));
         functionDispatcher.put(sym_null,new Fun_null(this));
         functionDispatcher.put(sym_eq,  new Fun_eq(this));
         functionDispatcher.put(sym_equal,new Fun_equal(this));
         functionDispatcher.put(sym_append,new Fun_append(this));
         functionDispatcher.put(sym_reverse,new Fun_reverse(this));
         functionDispatcher.put(sym_list,  new Fun_list(this));

         functionDispatcher.put(sym_m_add, new Fun_m_add(this));
         functionDispatcher.put(sym_m_sub, new Fun_m_sub(this));
         functionDispatcher.put(sym_m_mul, new Fun_m_mul(this));
         functionDispatcher.put(sym_m_div, new Fun_m_div(this));
         functionDispatcher.put(sym_m_exp2, new Fun_m_exp2(this));
         functionDispatcher.put(sym_m_eq,  new Fun_m_eq(this));
         functionDispatcher.put(sym_m_gt,  new Fun_m_gt(this));
         functionDispatcher.put(sym_m_lt,  new Fun_m_lt(this));
         functionDispatcher.put(sym_m_ge,  new Fun_m_ge(this));
         functionDispatcher.put(sym_m_le,  new Fun_m_le(this));
         functionDispatcher.put(sym_m_ne,  new Fun_m_ne(this));
         functionDispatcher.put(sym_m_sin, new Fun_m_sin(this));
         functionDispatcher.put(sym_m_cos, new Fun_m_cos(this));
         functionDispatcher.put(sym_m_tan, new Fun_m_tan(this));
         functionDispatcher.put(sym_m_atan,new Fun_m_atan(this));
         functionDispatcher.put(sym_m_sqrt,new Fun_m_sqrt(this));
         functionDispatcher.put(sym_m_log, new Fun_m_log(this));
         functionDispatcher.put(sym_m_exp, new Fun_m_exp(this));
         functionDispatcher.put(sym_m_mod, new Fun_m_mod(this));
         functionDispatcher.put(sym_m_neg, new Fun_m_neg(this));
    }
    public Hashtable functionDispatcher;
    public void initSymbols()
    {
        sym_append=recSymbol("append");
        sym_atom=  recSymbol("atom");
        sym_car   =recSymbol("car");
        sym_cdr    =recSymbol("cdr");
        sym_cons   =recSymbol("cons");
        sym_eq     =recSymbol("eq");
        sym_equal  =recSymbol("equal");
        sym_first  =recSymbol("first");
        sym_list   =recSymbol("list");
        sym_setq   =recSymbol("setq");
        sym_m_add  =recSymbol("+");
        sym_m_atan =recSymbol("atan");
        sym_m_cos  =recSymbol("cos");
        sym_m_div  =recSymbol("/");
        sym_m_eq   =recSymbol("=");
        sym_m_exp  =recSymbol("exp");
        sym_m_exp2 =recSymbol("exp2");
        sym_m_ge   =recSymbol(">=");
        sym_m_gt   =recSymbol(">");
        sym_m_lambda=recSymbol("lambda");
        sym_m_le   =recSymbol("<=");
        sym_m_log  =recSymbol("log");
        sym_m_lt   =recSymbol("<");
        sym_m_mod  =recSymbol("mod");
        sym_m_mul  =recSymbol("*");
        sym_m_ne   =recSymbol("<>");
        sym_m_sin  =recSymbol("sin");
        sym_m_sqrt =recSymbol("sqrt");
        sym_m_sub  =recSymbol("-");
        sym_m_tan  =recSymbol("tan");
        sym_m_neg  =recSymbol("neg");
        sym_null   =recSymbol("null");
        sym_print  =recSymbol("print");
        sym_read   =recSymbol("read");
        sym_rest   =recSymbol("rest");
        sym_reverse=recSymbol("reverse");
    }
    public Symbol sym_m_lambda;
    public Symbol sym_m_mod;
    public Symbol sym_m_log;
    public Symbol sym_m_sqrt;
    public Symbol sym_m_exp;
    public Symbol sym_m_atan;
    public Symbol sym_m_tan;
    public Symbol sym_m_cos;
    public Symbol sym_m_sin;
    public Symbol sym_m_ne;
    public Symbol sym_m_ge;
    public Symbol sym_m_le;
    public Symbol sym_m_lt;
    public Symbol sym_m_gt;
    public Symbol sym_m_eq;
    public Symbol sym_m_exp2;
    public Symbol sym_m_div;
    public Symbol sym_m_mul;
    public Symbol sym_m_sub;
    public Symbol sym_m_add;
    public Symbol sym_list;
    public Symbol sym_reverse;
    public Symbol sym_append;
    public Symbol sym_equal;
    public Symbol sym_eq;
    public Symbol sym_null;
    public Symbol sym_atom;
    public Symbol sym_cons;
    public Symbol sym_rest;
    public Symbol sym_cdr;
    public Symbol sym_first;
    public Symbol sym_car;
    public Symbol sym_read;
    public Symbol sym_print;
    public static LispObject fifth(LispObject x)
    {
        return car(cdr(cdr(cdr(cdr(x)))));
    }
    public void clearEnvironment()
    {
         environment=cons(nilSymbol,nilSymbol);
   }
    public LispObject applyUserDefined(
                            LispObject proc,
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
                        // if proc is not associated with any name
                           plist("can not find out ",proc);
                           return nilSymbol;
                       }
                       else f=second(f);
               }
               return apply( f,argl,env);
   }

    public LispObject defExt(LispObject s, LispObject env)
    throws Exception
    {
        if(s==null) {
            throw new Exception("null");
        }
        return nilSymbol;
    }
    public LispObject applyMiscOperation(LispObject proc,
                                  LispObject argl, LispObject env)
    {
        return null;
    }
    public LispObject progn(LispObject proc, LispObject env)
    throws Exception
    {
        LispObject rtn=nilSymbol;
//        LispObject thisEnv=cons(env,nilSymol);
        ((ListCell)env).d=cons(nilSymbol,nilSymbol);
        LispObject ps=proc;
        while(!Null(ps)){
            try{
                Thread.sleep(1);
            }
            catch(InterruptedException e){
            }
            rtn=eval(car(ps),env);
            if(eq(second((ListCell)env),tSymbol)) return rtn;
            ps=cdr(ps);
        }
        return rtn;
    }
    public LispObject nconc(LispObject x, LispObject y)
    {
        LispObject w=x;
        if(w==null) return nilSymbol;
        if(Null(w)) return y;
        while(!atom(((ListCell)w).d)){
            w=((ListCell)w).d;
        }
        rplcd(w,y);
        return x;
    }
    public LispObject evalMiscForm(LispObject form, LispObject env)
    throws Exception
    {
             if(form==null){
                throw new Exception("no form");
             }
             LispObject fform=car(form);
             return null;
    }
    public LispObject applyMiscOperation(LispObject proc, LispObject argl)
    {
        return null;
    }
    public LispObject applyNumericalOperation(LispObject proc, LispObject argl)
    /*
       This method is not used now.
    */
    {
        /*
            if(eq(proc,sym_m_add)){
                MyNumber x=(MyNumber)(car(argl));
                MyNumber y;
                LispObject p=cdr(argl);
                while(!Null(p)){
                   y=(MyNumber)(car(p));
                   p=cdr(p);
                   x=x.add(y);
                }
                return x;
            }

            if(eq(proc,sym_m_sub)){
                MyNumber x=(MyNumber)(car(argl));
                LispObject p=cdr(argl);
                if(Null(p)) return (new MyNumber(0)).sub(x);
                MyNumber y=(MyNumber)(car(p));
                return x.sub(y);
            }
            if(eq(proc,sym_m_mul)){
                MyNumber x=(MyNumber)(car(argl));
                MyNumber y;
                LispObject p=cdr(argl);
                while(!Null(p)){
                   y=(MyNumber)(car(p));
                   p=cdr(p);
                   x=x.mul(y);
                }
                return x;
            }
            if(eq(proc,sym_m_div)){
                MyNumber x=(MyNumber)(car(argl));
                MyNumber y=(MyNumber)(second(argl));
                return x.div(y);
            }
            if(eq(proc,sym_m_exp2)){
                MyNumber x=(MyNumber)(car(argl));
                MyNumber y=(MyNumber)(second(argl));
                return x.exp(y);
            }

            if(eq(proc,sym_m_eq)){
                MyNumber x=(MyNumber)(car(argl));
                MyNumber y=(MyNumber)(second(argl));
                if(x.eq(y)) return tSymbol;
                else     return nilSymbol;
            }
            if(eq(proc,sym_m_gt)){
                MyNumber x=(MyNumber)(car(argl));
                MyNumber y=(MyNumber)(second(argl));
                if(x.gt(y))  return tSymbol;
                else     return nilSymbol;
            }
            if(eq(proc,sym_m_lt)){
                MyNumber x=(MyNumber)(car(argl));
                MyNumber y=(MyNumber)(second(argl));
                if(x.lt(y))  return tSymbol;
                else     return nilSymbol;
            }
            if(eq(proc,sym_m_ge)){
                MyNumber x=(MyNumber)(car(argl));
                MyNumber y=(MyNumber)(second(argl));
                if(x.ge(y))  return tSymbol;
                else     return nilSymbol;
            }
            if(eq(proc,sym_m_le)){
                MyNumber x=(MyNumber)(car(argl));
                MyNumber y=(MyNumber)(second(argl));
                if(x.le(y))  return tSymbol;
                else     return nilSymbol;
            }
            if(eq(proc,sym_m_ne)){
                MyNumber x=(MyNumber)(car(argl));
                MyNumber y=(MyNumber)(second(argl));
                if(x.ne(y))  return tSymbol;
                else     return nilSymbol;
            }

            if(eq(proc,sym_m_sin)){
                MyNumber x=(MyNumber)(car(argl));
                return x.sin();
            }
            if(eq(proc,sym_m_cos)){
                MyNumber x=(MyNumber)(car(argl));
                return x.cos();
            }
             if(eq(proc,sym_m_tan)){
                MyNumber x=(MyNumber)(car(argl));
                return x.tan();
            }
             if(eq(proc,sym_m_atan)){
                MyNumber x=(MyNumber)(car(argl));
                return x.sin();
            }
            if(eq(proc,sym_m_sqrt)){
                MyNumber x=(MyNumber)(car(argl));
                return x.sin();
            }
            if(eq(proc,sym_m_log)){
                MyNumber x=(MyNumber)(car(argl));
                return x.sin();
            }
             if(eq(proc,sym_m_exp)){
                MyNumber x=(MyNumber)(car(argl));
                return x.sin();
            }
            if(eq(proc,sym_m_mod)){
                MyNumber x=(MyNumber)(car(argl));
                MyNumber y=(MyNumber)(second(argl));
                return x.mod(y);
            }
            */
           return null;
    }
    public LispObject applyListOperation(LispObject proc,
                                         LispObject argl)
    /*
       This method is not used now.
    */
    {
        /*
            if(eq(proc,sym_car)||
               eq(proc,sym_first))
                return car(car(argl));
            if(eq(proc,sym_cdr)||
               eq(proc,sym_rest))
                return cdr(car(argl));
            if(eq(proc,sym_cons))
                return cons(car(argl),second(argl));
            if(eq(proc,sym_atom))
                return atom2(car(argl));
            if(eq(proc,sym_null))
                return Null2(car(argl));
            if(eq(proc,sym_eq))
                return eq2(car(argl),second(argl));
                */
            PrimitiveFunction f=(PrimitiveFunction)(functionDispatcher.get(proc));
            if(f!=null) return f.fun(proc,argl);
            if(eq(proc,sym_equal))
                return equal2(car(argl),second(argl));
/*
   The following append and reverse is
   added by Tomoki Katayama, Kyushu Institute of Technology,
   27 Feb.1998
*/
            if(eq(proc,sym_append))
                return append(car(argl),second(argl));
            if(eq(proc,sym_reverse))
                return reverse(car(argl));
//
            if(eq(proc,sym_list))
                return argl;

            return null;

    }
    
    public LispObject equal2(LispObject x, LispObject y)
    {
        if(equal(x,y)) return tSymbol;
        else        return nilSymbol;
    }
    public void stop()
    {
        if(me!=null){ 
            // me.stop(); 
            me=null;
        }
    }
    public void start()
    {
        if(me==null){me=new Thread(this,"language"); me.start();}
    }
    public void run()
    {
        while(me!=null){
            if(inqueue!=null){
              while(!inqueue.isEmpty()){
               LispObject s=read.read(inqueue);
               if(s!=null){
                try{
                 LispObject r=preEval(s,environment);
          //   LispObject r=eval(s,environment);
                 String o=print.print(r);
                 printArea.append(o+"\n");
                }
                catch(Exception e){
                    printArea.append("exception "+e);
                    return;
                }
               }
               printArea.repaint();
             }
            }
            try{ Thread.sleep(100);}
            catch(InterruptedException e){System.out.println(e);}
        }
//        stop();
    }
    public void rplcd(LispObject x, LispObject y)
    {
        if(atom(x)) { printArea.append("rplcd failed\n"); return;}
        ((ListCell)x).d=y;
        return;

   }
    public void rplca(LispObject x, LispObject y)
    {
        if(atom(x)) { printArea.append("rplca failed\n"); return;}
        ((ListCell)x).a=y;
        return;

    }
    public LispObject setf(LispObject form, LispObject val)
    throws Exception
    {
        return setf(form,val,environment);
    }
    public LispObject setf(LispObject form, LispObject val, LispObject env)
    throws Exception
    {
        ((ListCell)env).a=setfx(form,val,((ListCell)env).a);
        return form;
    }
/*
   The following "append" and "reverse" is
   added by Tomoki Katayama, Kyushu Institute of Technology,
   27 Feb.1998
*/
    public LispObject append(LispObject x,LispObject y){
        if (Null(x))
                return y;
        else{
                return cons(car(x), append(cdr(x),y));
        }
    }
    public LispObject reverse(LispObject x)
        {
        if(Null(x)){
                return nilSymbol;
                }
        else if(Null(cdr(x))){
                return cons(car(x),nilSymbol);
        }
        else {
                return append(reverse(cdr(x)),cons(car(x),nilSymbol));
        }
    }

    public FrameWithLanguageProcessor gui;
    public LispObject atom2(LispObject s)
    {
        if(atom(s)) return tSymbol;
        else        return nilSymbol;
    }
    public LispObject evalcond(LispObject cond, LispObject env)
    throws Exception
    {
        while(true){
            if(Null(cond)) return nilSymbol;
            LispObject pair=car(cond);
            LispObject px=eval(car(pair),env);
           if(!Null(px)) return eval(second(pair),env);
           cond=cdr(cond);
        }
    }
    public boolean isDefun(LispObject form)
    {
        if(atom(form)) return false;
        if(eq(car(form),recSymbol("defun"))) return true;
        return false;
    }

    public LispObject caseOfDefun(LispObject f, LispObject env)
    throws Exception
    {

        LispObject fn=cons(recSymbol("get"),
                      cons(
                        cons(recSymbol("quote"),
                        cons(car(f),nilSymbol)),
                      cons(
                        cons(recSymbol("quote"),
                        cons(recSymbol("lambda"),nilSymbol)),
                      nilSymbol)));
        LispObject val=cons(recSymbol("lambda"),
                       cdr(f));

        LispObject x=setf(fn,val);
        return environment;
    }
//    public JTerm jterm;
    public LispObject bindVars(LispObject vars, LispObject vals)
    {
        return bindVars(vars,vals,nilSymbol);
    }
    public LispObject bindVars(LispObject vars, LispObject vals, LispObject env)
    {
        LispObject alist=((ListCell)env).a;
        while(true){
            if(Null(vars)) return alist;
            if(Null(vals)) return alist;
            alist=cons(cons(car(vars),
                       cons(car(vals),
                            nilSymbol)),alist);
            vars=cdr(vars); vals=cdr(vals);
        }
    }
    public LispObject get(LispObject sym, LispObject attr, LispObject env)
    {
         LispObject getf=cons(recSymbol("get"),
                        cons(sym,
                        cons(attr, nilSymbol)));
         LispObject w=assoc(getf,env);
         if(Null(w)) return nilSymbol;
         else return second(w);
    }
    public LispObject get(LispObject sym, LispObject attr)
    {
        return get(sym,attr,((ListCell)environment).a);
    }
    public boolean isSetf(LispObject form)
    {
        if(atom(form)) return false;
        if(eq(car(form),recSymbol("setf"))) return true;
        return false;
    }
    public synchronized LispObject preEval(LispObject s, LispObject env)
    throws Exception
    {
    	try{
        LispObject rtn;
        if(isSetf(s)) {
            environment=caseOfSetf(cdr(s),env);
            return second(car(environment));
        }
        else
        if(isDefun(s)){
            environment=caseOfDefun(cdr(s),env);
            return second(s);
        }
        else
        {
            rtn=defExt(s,env);
            if(!Null(rtn)) return rtn;
            else  return eval(s,env);
        }
    	}
    	catch(Exception e){
    		System.out.println(e.toString());
    		Thread.dumpStack();
    		throw e;
    	}
    }
    public LispObject caseOfSetf(LispObject form, LispObject env)
    throws Exception
    {
        while(true){
          if(Null(form)) return env;
          else {
            env =setfx(car(form),
                       eval(second(form),env),env);
            form= cdr(cdr(form));
          }
        }
    }
    public boolean equal(LispObject x, LispObject y)
    {
        if(atom(x)) return eq(x,y);
        if(atom(y)) return false;
        if(equal(car(x),car(y)))
           return equal(cdr(x),cdr(y));
        else return false;
    }
    public LispObject setfx(LispObject form, LispObject value, LispObject env)
    throws Exception
    {
        LispObject key=null;
        if(symbolp(form)){
//            return cons(cons(form,cons(value,nilSymbol)),env);
              key=form;
        }
        else
        if(eq(car(form),recSymbol("get"))){
            LispObject fa=eval(second(form),env);
            LispObject sa=eval(third(form),env);
            key=cons(car(form),cons(fa,cons(sa,nilSymbol)));
//            return cons(cons(key,cons(value,nilSymbol)),env);
        }
        LispObject obj=assoc(key,env);
        if(!Null(obj)) {rplca(cdr(obj),value); return env;}
        else return cons(cons(key,cons(value,nilSymbol)),env);
    }
    public LispObject eq2(LispObject x, LispObject y)
    {
        if(eq(x,y)) return tSymbol;
        else        return nilSymbol;
    }
    public void plist(String s, LispObject x)
    {

          String a=s;
          String o=print.print(x);
          if(printArea==null) return;
          printArea.append(a+o+"\n");
          printArea.repaint();

   }
    public void plist2(String s, LispObject x)
    {

          String a=s;
          String o=print.print(x);
          System.out.println(a+" "+o+"\n");

   }
    public boolean symbolp(LispObject s)
    {
        /*
        if(s.getClass().getName().equals("ListCell"))
        */
        if(s.ltype==0) 
             return false;
        /*
        if(s.getClass().getName().equals("Symbol"))
        */
        int at=((Atom)s).atype;
        if(at==0)
             return true;
        else return false;
   }
    public static LispObject Null2(LispObject s)
    {
        if(Null(s)) return tSymbol;
        else return nilSymbol;
    }
    
    public void setResult(String x){
    	
    }
    public LispObject apply(LispObject proc,
                            LispObject argl,
                            LispObject env)
    throws Exception
    {
        LispObject f=null;
        LispObject rtn=null;
        if(gui.traceFlagIsOn()){ //gui.traceFlag.isSelected()
           plist("apply-",proc);
           plist("argl-",argl);
        }
        if(symbolp(proc)){

            // apply premitive functions
              PrimitiveFunction fx=(PrimitiveFunction)(functionDispatcher.get(proc));
              if(fx!=null) return fx.fun(proc,argl);
            // apply list operation
            // apply numerical operation

            // apply misc operation
            rtn=applyMiscOperation(proc,argl);
            if(rtn!=null) return rtn;

            // apply graphics operation
            rtn=applyGraphicsOperation(proc,argl);
            if(rtn!=null) return rtn;

            if(eq(proc,recSymbol("print"))){
                LispObject p=argl;
                String o="";
                while(!Null(p)){
                    o=print.print(car(p));
                    printArea.append(o+"\n");
                    printArea.repaint();
                    p=cdr(p);
                }
                this.setResult(o);
                return car(argl);
            }
            if(eq(proc,recSymbol("read"))){
                return read.read(inqueue);
            }
            else{
     //
     //       if proc is user defined name, ...
     //
               return applyUserDefined(proc,argl,env);
           }
        }
        else{
            LispObject newEnv=bindVars(second(proc),
                                       argl,env);
            rtn=nilSymbol;
            LispObject ps=cdr(cdr(proc));
            rtn=progn(ps,cons(newEnv,nilSymbol));
            return rtn;
        }
    }

    public LispObject evalArgl(LispObject argl, LispObject env)
    throws Exception
    {
        if(Null(argl)) return nilSymbol;
        LispObject y=evalArgl(cdr(argl),env);
        LispObject x=eval(car(argl),env);
        return cons(x,y);
    }
    public static LispObject fourth(LispObject x)
    {
        return car(cdr(cdr(cdr(x))));
    }
    public static LispObject third(LispObject x)
    {
        return car(cdr(cdr(x)));
    }
    public LispObject assoc(LispObject key, LispObject alist)
    {
        LispObject l=alist;
        while(true){
          if(Null(l)) return nilSymbol;
          if(equal(key,car(car(l)))) return car(l);
          l=cdr(l);
        }
    }
    public static LispObject second(LispObject x)
    {
        return car(cdr(x));
    }
    public LispObject eval(LispObject form, LispObject env)
    throws java.lang.Exception 
    {
        LispObject rtn;
        
        if(gui!=null){
            /*
            try{
                Thread.sleep(1);
            }
            catch(InterruptedException e){
            }
            */
            if(gui.traceFlagIsOn())
                     plist("eval..",form);
            if(gui.stopFlagIsOn()){
                  plist("stopped at evaluating ",form);
                  throw new java.lang.Exception("stop") ;
            }
//            plist("env...",env);
        }
        if(atom(form)){
        	/*
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
            */
        	rtn=this.evalAtomForm(form,env);
        }
        else{
            LispObject fform=car(form);
            if(eq(fform,recSymbol("quote")))
                 rtn= second(form);
             else
            if(eq(fform,recSymbol("if"))) {
               if(!Null( eval(second(form),env)))
                     rtn=  eval(third(form), env);
               else  rtn=  eval(fourth(form),env);
            }
            else
            if(eq(fform,recSymbol("cond")))
               rtn= evalcond(cdr(form),env);
            else
            if(eq(fform,recSymbol("get")))
               rtn= get(eval(second(form),env),
                          eval(third(form), env), env);
            else
            if(eq(fform,recSymbol("apply")))
               rtn= apply( eval(second(form),env),
                           eval(third(form),env),env);
            else
            if(eq(fform,recSymbol("setq"))){
               rtn= setf( second(form),
                             eval(third(form),env),env);
            }
            else
            if(eq(fform,recSymbol("progn"))){
                rtn= progn(cdr(form),env);
            }
            else
            if(eq(fform,recSymbol("return"))){
                rtn=eval(second(form),env);
                ((ListCell)(((ListCell)env).d)).a=tSymbol;
            }
            else{
                rtn=evalMiscForm(form,env);
                if(rtn!=null) return rtn;
                else rtn= apply(fform,
                            evalArgl(cdr(form),env),env);
            }
        }
        if(gui.traceFlagIsOn()) //gui.traceFlag.isSelected()
           plist("eval return ...",rtn);
        return rtn;

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
           /*
           if(!atom(second(w))){
              rtn=nilSymbol;
              if(eq(recSymbol("dimension"),car(second(w)))){
                 rtn= form;
              }
           }
           else
           */
           rtn= second(w);
        }    	
        return rtn;
    }
    public void evals(CQueue iq)
    {
        inqueue=iq;
        /*
        if(me==null){
          inqueue=iq;
          me=new Thread(this);
          me.start();
        }
        else{

        }
        */
        /*
        while(!inqueue.isEmpty()){
          LispObject s=read.read(inqueue);
          LispObject r=preEval(s,environment);
       //   LispObject r=eval(s,environment);
          String o=print.print(r);
          printArea.appendText(o+"\n");
          printArea.repaint();
        }
        */
   }
//    public JTextArea printArea;
    public JTextArea printArea;
    public JTextArea readArea;
    public void init(JTextArea rarea, JTextArea parea,CQueue iq, FrameWithLanguageProcessor g)
    {
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

        read=new ReadS(inqueue,this);
        print=new PrintS(this);
        gui=g;
    }
    public ALisp()
    {
    }
    public static boolean eq(LispObject x, LispObject y)
    {
    	
        if(x==y) return true;
        if(!atom(x)) return false;
        if(!atom(y)) return false;
        if(numberp(x)){
            if(numberp(y))
               return ((MyNumber)x).eq((MyNumber)y);}
        if(numberp(y)) return false;
        if(x.getClass().equals(y.getClass())){
           return((Symbol)x).hc == ((Symbol)y).hc;
        }
        else return false;
    }
    public Thread me;
    public static Symbol tSymbol;
    public static Symbol nilSymbol;
    public PrintS print;
    public ReadS read;
    public CQueue inqueue;
    public ALisp(JTextArea in, JTextArea out,CQueue iq, FrameWithLanguageProcessor g)
    {
        init(in,out,iq,g);
    }
    public static boolean numberp(LispObject s)
    {
        if(s.ltype==0) return false;
        int at=((Atom)s).atype;
        if(at==0) return false;
        /*
        if(s.getClass().getName().equals("ListCell"))
             return false;
        if(s.getClass().getName().equals("MyNumber"))
             return true;
        if(s.getClass().getName().equals("MyInt"))
            return true;
        if(s.getClass().getName().equals("MyDouble"))
             return true;
        if(s.getClass().getName().equals("MyString"))
             return true;
        else return false;
        */
        return true;
    }
    public static LispObject cons(LispObject x, LispObject y)
    {
        ListCell z=new ListCell();
        z.a=x; z.d=y;
        return z;
    }
    public static boolean Null(LispObject s)
    {
        if(!atom(s)) return false;
        if(s==nilSymbol) return true;
        try{
          if(eq(nilSymbol,s)) return true;
        }
        catch(java.lang.ClassCastException e){
            System.out.println("exception:"+e);
        }
        return false;
    }
    public static boolean atom(LispObject s)
    {
        /*
        if(s.getClass().getName().equals("ListCell"))
           return false;
        else return true;
        */
        return s.ltype==1;
    }
    public static LispObject cdr(LispObject s)
    {
        /*
        if(s.getClass().getName().equals("ListCell"))
        */
        if(s.ltype==0)
            return ((ListCell)s).d;
        else{  System.out.println("error");}
        return null;
    }
    public static LispObject car(LispObject s)
    {
        /*
        if(s.getClass().getName().equals("ListCell"))
        */
        if(s.ltype==0)
            return ((ListCell)s).a;
        else{  System.out.println("error");}
        return null;
   }
    public Symbol recSymbol(String s)
    {
        int hc=s.hashCode();
        Integer key=new Integer(hc);
        Symbol rtn=(Symbol)(symbolTable.get(key));
        if(rtn==null){
            rtn=new Symbol(s); symbolTable.put(key,rtn);
        }
        return rtn;
    }
    public LispObject environment;
    public Hashtable symbolTable;
}

