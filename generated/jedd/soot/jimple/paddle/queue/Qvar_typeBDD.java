package soot.jimple.paddle.queue;

import soot.util.*;
import soot.jimple.paddle.bdddomains.*;
import soot.jimple.paddle.*;
import soot.jimple.toolkits.callgraph.*;
import soot.*;
import soot.util.queue.*;
import jedd.*;
import java.util.*;

public final class Qvar_typeBDD extends Qvar_type {
    public Qvar_typeBDD(String name) { super(name); }
    
    private LinkedList readers = new LinkedList();
    
    public void add(VarNode _var, Type _type) {
        add(new jedd.internal.RelationContainer(new Attribute[] { var.v(), type.v() },
                                                new PhysicalDomain[] { V1.v(), T1.v() },
                                                ("add(jedd.internal.Jedd.v().literal(new java.lang.Object[...]" +
                                                 ", new jedd.Attribute[...], new jedd.PhysicalDomain[...])) at" +
                                                 " /home/research/ccl/olhota/olhotak/soot-trunk/src/soot/jimpl" +
                                                 "e/paddle/queue/Qvar_typeBDD.jedd:34,8-11"),
                                                jedd.internal.Jedd.v().literal(new Object[] { _var, _type },
                                                                               new Attribute[] { var.v(), type.v() },
                                                                               new PhysicalDomain[] { V1.v(), T1.v() })));
    }
    
    public void add(final jedd.internal.RelationContainer in) {
        if (!jedd.internal.Jedd.v().equals(jedd.internal.Jedd.v().read(in), jedd.internal.Jedd.v().falseBDD()))
            invalidate();
        for (Iterator it = readers.iterator(); it.hasNext(); ) {
            Rvar_typeBDD reader = (Rvar_typeBDD) it.next();
            reader.add(new jedd.internal.RelationContainer(new Attribute[] { var.v(), type.v() },
                                                           new PhysicalDomain[] { V1.v(), T1.v() },
                                                           ("reader.add(in) at /home/research/ccl/olhota/olhotak/soot-tru" +
                                                            "nk/src/soot/jimple/paddle/queue/Qvar_typeBDD.jedd:40,12-18"),
                                                           in));
        }
    }
    
    public Rvar_type reader(String rname) {
        Rvar_type ret = new Rvar_typeBDD(name + ":" + rname);
        readers.add(ret);
        return ret;
    }
}
