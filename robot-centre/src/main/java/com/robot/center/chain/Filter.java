package com.robot.center.chain;


public abstract class Filter <T>{
  public abstract boolean dofilter(T invocation) throws Exception;

  public abstract int order();

  boolean doFilterFinal(Invoker invoker, T invocation) throws Exception {
    return dofilter(invocation) ? (null != invoker ? invoker.invoke(invocation) : true) : false;
  }
}
