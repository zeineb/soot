/* Soot - a J*va Optimization Framework
 * Copyright (C) 1999-2000 Patrice Pominville, Raja Vallee-Rai
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

/*
 * Modified by the Sable Research Group and others 1997-1999.  
 * See the 'credits' file distributed with Soot for the complete list of
 * contributors.  (Soot is distributed at http://www.sable.mcgill.ca/soot)
 */


package soot.toolkits.graph;

import soot.util.*;
import java.util.*;
import soot.*;
import soot.baf.*;




/**
 *    Represents BasicBlocks that partition
 *    a method body.  It is implemented as view on an
 *    underlying Body instance; as a consequence, changes made on a Block 
 *    will be automatically reflected in its enclosing method body. Blocks
 *    also exist in the context of a BlockGraph, a CFG for a method where 
 *    Block instances are the nodes of the graph. Hence, a Block can be queried
 *    for its successors and predecessors Blocks, as found in this graph.
 */
public class Block
{
    private Unit mHead, mTail;
    private Body mBody;
    private List mPreds, mSuccessors;
    private int mPredCount = 0, mBlockLength = 0, mIndexInMethod = 0;
    private BlockGraph mBlockGraph;

    /**
     *   Constructs a Block in the context of a BlockGraph, and enclosing Body instances.
     *  
     *
     *   @param aHead            The first unit ir this Block.
     *   @param aTail            The last unit  in this Block.   
     *   @param aBody            The Block's enclosing Body instance.
     *   @param aIndexInMethod   The index of this Block in the list of
     *                           Blocks that partition it's enclosing Body instance.
     *   @param aBlockLength     The number of units that makeup this block.
     *   @param aBlockGraph      The Graph of Blocks in which this block lives.
     *
     *   @see Body
     *   @see Chain
     *   @see BlockGraph
     *   @see Unit
     *   @see SootMethod
     */
    public Block(Unit aHead, Unit aTail, Body aBody, int aIndexInMethod, int aBlockLength, BlockGraph aBlockGraph)
    {
        mHead = aHead;        
        mTail = aTail;
        mBody = aBody;
        mIndexInMethod = aIndexInMethod;
        mBlockLength = aBlockLength;
        mBlockGraph = aBlockGraph;
    }



    /** 
     *  Returns the Block's enclosing Body instance.
     *
     *  @return      The block's chain of instructions.
     *  @see         soot.jimple.JimpleBody
     *  @see         BafBody 
     *  @see         Body
     */
    public Body getBody() 
    {
        return mBody;
    }
       
    
    /**
     *  Returns an iterator for the linear chain of Units that make up the block.
     *
     *  @return      An iterator that iterates over the block's units.
     *  @see Chain 
     *  @see Unit
     */
    public Iterator iterator() 
    {
        if(mBody != null) 
        {
            Chain units = mBody.getUnits();
            return units.iterator(mHead, mTail);
        } else {
            return null;
        }
    }
    
    /**
     *  Inserts a Unit before some other Unit in this block.
     *
     *
     *  @param toInsert  A Unit to be inserted.
     *  @param point     A Unit in the Block's body
     *                   before which we wish to insert the Unit.           
     *  @see Unit
     *  @see Chain
     */         
    public void insertBefore(Unit toInsert, Unit point)
    {
        if(point == mHead) 
            mHead = toInsert;

        Chain methodBody = mBody.getUnits();
        methodBody.insertBefore(toInsert, point);
    }


     /**
     *  Inserts a Unit after some other Unit in the Block.
     *
     *  @param toInsert  A Unit to be inserted.
     *  @param point     A Unit in the Block  after which we wish to 
     *                   insert the Unit.           
     *  @see Unit
     */         
    public void insertAfter(Unit toInsert, Unit point)
    {
        if(point == mTail) 
            mTail = toInsert;

        Chain methodBody = mBody.getUnits();
        methodBody.insertAfter(toInsert, point);
    }



    /**
     *  Removes a Unit occuring before some other Unit in the Block.
     *
     *  @param item       A Unit to be remove from the Block's Unit Chain.         
     *  @return           True if the item could be found and removed.
     *
     */         
    public boolean remove(Unit item) 
    {
        Chain methodBody = mBody.getUnits();
        
        if(item == mHead)
            mHead = (Unit)methodBody.getSuccOf(item);
        else if(item == mTail)
            mTail = (Unit) methodBody.getPredOf(item);
        
        return methodBody.remove(item);
    }
    
    /**
     *  Returns the  Unit occuring immediatly after some other Unit in the block.
     *
     *  @param aItem      The Unit from which we wish to get it's successor.
     *  @return           The successor or null if <code>aItem</code> is the tail
     *                    for this Block.     
     *
     */           
    public Unit getSuccOf(Unit aItem) 
    {        
        Chain methodBody = mBody.getUnits();
        if(aItem != mTail)
            return  (Unit) methodBody.getSuccOf(aItem);
        else
            return null;
    }
    
    /**
     *  Returns the  Unit occuring immediatly before some other Unit in the block.
     *
     *  @param aItem      The Unit from which we wish to get it's predecessor.
     *  @return           The predecessor or null if <code>aItem</code> is the head
     *                    for this Block.     
     */      
    public Unit getPredOf(Unit aItem) 
    {
        Chain methodBody = mBody.getUnits();
        if(aItem != mHead)
            return  (Unit) methodBody.getPredOf(aItem);
        else
            return null;        
    }

    /**
     *  Set the index of this Block in the list of Blocks that partition
     *  its enclosing Body instance.
     *
     *   @param aIndexInMethod The index of this Block in the list of
     *                         Blocks that partition it's enclosing
     *                         Body instance.
     **/
    public void setIndexInMethod(int aIndexInMethod)
    {
        mIndexInMethod = aIndexInMethod;
    }

    /**
     *  Returns the index of this Block in the list of Blocks that partition it's
     *  enclosing Body instance.
     *   @return         The index of the block in it's enclosing Body instance.
     */    
    public int getIndexInMethod()
    {
        return mIndexInMethod;
    }

    /**
     * Returns the first unit in this block.
     * @return The first unit in this block. 
     */
    public Unit getHead() 
    {
        return mHead;
    }
    
    /**
     * Returns the last unit in this block.
     * @return The last unit in this block.
     */
    public Unit getTail()
    {
        return mTail;
    }

    /** 
     *   Sets the list of Blocks that are predecessors of this block in it's enclosing
     *   BlockGraph instance.
     *   @param preds       The a List of Blocks that precede this block.
     *
     *   @see BlockGraph
     */ 
    public void setPreds(List preds)
    {
        mPreds = preds;
        return;
    }

    /** 
     *   Returns the List of Block that are predecessors to this block, 
     *   @return            A list of predecessor blocks.
     *   @see BlockGraph
     */     
    public List getPreds()
    {
        return mPreds;
    }



    /**
     *   Sets the list of Blocks that are successors of this block in it's enclosing
     *   BlockGraph instance.
     *   @param succs      The a List of Blocks that succede this block.
     *
     *   @see BlockGraph
     */
    public void setSuccs(List succs)
    {
        mSuccessors = succs;
    }



    /**
     *   Returns the List of Blocks that are successors to this block,
     *   @return            A list of successorblocks.
     *   @see BlockGraph
     */
    public List getSuccs()
    {
        return mSuccessors;
    }



    private Map buildMapForBlock() 
    {
        Map m = new HashMap();
        List basicBlocks = mBlockGraph.getBlocks();
        Iterator it = basicBlocks.iterator();
        while(it.hasNext()) {
            Block currentBlock = (Block) it.next();
            m.put(currentBlock.getHead(),  "block" + (new Integer(currentBlock.getIndexInMethod()).toString()));
        }        
        return m;
    }


    Map allMapToUnnamed = new AllMapTo("???");

    class AllMapTo extends AbstractMap
    {
        Object dest;
        
        public AllMapTo(Object dest)
        {
            this.dest = dest;
        }
        
        public Object get(Object key)
        {
            return dest;
        }
        
        public Set entrySet()
        {
            throw new UnsupportedOperationException();
        }
    }


    
    public String toShortString() {return "Block #" + mIndexInMethod; }

    public String toString()
    {
        StringBuffer strBuf = new StringBuffer();

                

        // print out predecessors.

        strBuf.append("Block " + mIndexInMethod + ":" + System.getProperty("line.separator"));
        strBuf.append("[preds: ");
        int count = 0;
        if(mPreds != null) {
            Iterator it = mPreds.iterator();
            while(it.hasNext()) {
                
                strBuf.append(((Block) it.next()).getIndexInMethod()+ " ");
            }
        }
        strBuf.append("] [succs: ");
        if(mSuccessors != null) {
            Iterator it = mSuccessors.iterator();
            while(it.hasNext()) {
                
                strBuf.append(((Block) it.next()).getIndexInMethod() + " ");
            }
            
        }
            
        strBuf.append("]" + System.getProperty("line.separator"));
        

        
        //strBuf.append("     block" + mIndexInMethod + ":" + System.getProperty("line.separator"));

        Chain methodUnits = mBody.getUnits();
        Iterator basicBlockIt = methodUnits.iterator(mHead, mTail);
        
        if(basicBlockIt.hasNext()) {
            Unit someUnit = (Unit) basicBlockIt.next();
            strBuf.append(someUnit.toString() + ";" + System.getProperty("line.separator"));
            while(basicBlockIt.hasNext()){
                someUnit = (Unit) basicBlockIt.next();
                if(someUnit == mTail)
                    break;
                strBuf.append(someUnit.toString() + ";" + System.getProperty("line.separator"));        
            }
            someUnit = mTail;
            if(mTail == null) 
                strBuf.append("error: null tail found; block length: " + mBlockLength +"" + System.getProperty("line.separator"));
            else if(mHead != mTail)
                strBuf.append(someUnit.toString() + ";" + System.getProperty("line.separator"));        
        

        }  else 
            G.v().out.println("No basic blocks found; must be interface class.");

        return strBuf.toString();
    }

}
