package com.robot.center.chain;


public abstract class Filter <T>{
  protected abstract boolean dofilter(T invocation) throws Exception;
  
  boolean doFilterFinal(Invoker invoker, T invocation) throws Exception {
    return dofilter(invocation) ? (null != invoker ? invoker.invoke(invocation) : true) : false;
  }
}
