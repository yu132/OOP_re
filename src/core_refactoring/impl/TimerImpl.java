package core_refactoring.impl;

import core_refactoring.Timer;

/**
 * 计时器的实现类 
 * @author 87663
 */
public class TimerImpl implements Timer{

	/**
	 * 记录开始的时间
	 */
	private long startedTime;
	
	/**
	 * 使用参数来构造一个计时器
	 * @param startedTime
	 */
	public TimerImpl(long startedTime) {
		this.startedTime = startedTime;
	}

	/**
	 * 使用系统时间来构造一个计时器
	 */
	public TimerImpl() {
		this.startedTime = System.currentTimeMillis();
	}

	@Override
	public long getTime() {
		return (System.currentTimeMillis()-startedTime)/1000;
	}

}
