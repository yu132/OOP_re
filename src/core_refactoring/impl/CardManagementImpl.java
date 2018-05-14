package core_refactoring.impl;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

import core_refactoring.*;

public class CardManagementImpl implements CardManagement{
	
	private enum Operation{
		move,next;
	}
	
	private class Operation_pair{
		private Operation o;
		private Components from;
		private Components to;
		public Operation_pair(Operation o, Components from, Components to) {
			super();
			this.o = o;
			this.from = from;
			this.to = to;
		}
	}
	
	private boolean needAnalyze=true;
	
	private Deque<Operation_pair> snapshot=new ArrayDeque<>();

	private Dealer dealer;
	
	private Box box_1;
	private Box box_2;
	private Box box_3;
	private Box box_4;
	
	private CardHeap cardHeap_1;
	private CardHeap cardHeap_2;
	private CardHeap cardHeap_3;
	private CardHeap cardHeap_4;
	private CardHeap cardHeap_5;
	private CardHeap cardHeap_6;
	private CardHeap cardHeap_7;
	
	private PointCounter pointCounter;
	
	private CardGameAnalyzer tipsGetter;
	
	private String lastMove="";
	
	private Map<Components,Component> map=new HashMap<>();
	
	public CardManagementImpl(Dealer dealer, Box box_1, Box box_2, Box box_3, Box box_4, CardHeap cardHeap_1,
			CardHeap cardHeap_2, CardHeap cardHeap_3, CardHeap cardHeap_4, CardHeap cardHeap_5, CardHeap cardHeap_6,
			CardHeap cardHeap_7, PointCounter pointCounter, CardGameAnalyzer tipsGetter) {
		super();
		this.dealer = dealer;
		this.box_1 = box_1;
		this.box_2 = box_2;
		this.box_3 = box_3;
		this.box_4 = box_4;
		this.cardHeap_1 = cardHeap_1;
		this.cardHeap_2 = cardHeap_2;
		this.cardHeap_3 = cardHeap_3;
		this.cardHeap_4 = cardHeap_4;
		this.cardHeap_5 = cardHeap_5;
		this.cardHeap_6 = cardHeap_6;
		this.cardHeap_7 = cardHeap_7;
		this.pointCounter = pointCounter;
		this.tipsGetter = tipsGetter;
		map.put(Components.DEALER, this.dealer);
		map.put(Components.BOX_1, this.box_1);
		map.put(Components.BOX_2, this.box_2);
		map.put(Components.BOX_3, this.box_3);
		map.put(Components.BOX_4, this.box_4);
		map.put(Components.CARD_HEAP_1, this.cardHeap_1);
		map.put(Components.CARD_HEAP_2, this.cardHeap_2);
		map.put(Components.CARD_HEAP_3, this.cardHeap_3);
		map.put(Components.CARD_HEAP_4, this.cardHeap_4);
		map.put(Components.CARD_HEAP_5, this.cardHeap_5);
		map.put(Components.CARD_HEAP_6, this.cardHeap_6);
		map.put(Components.CARD_HEAP_7, this.cardHeap_7);
	}
	
	public void init(){
		if(needAnalyze){
			needAnalyze=false;
			tipsGetter.analyzerGame(this);
			needAnalyze=true;
		}
	}

	private Operation_pair getSnapshot(Components from, Components to){
		return new Operation_pair(Operation.move, from, to);
	}
	
	public MoveState moveSingleCard(Components from, Components to) {
		if(from==to)
			return MoveState.ILLEGAL_MOVE;
		
		String templm=lastMove;
		lastMove=from+" "+to+" "+1;
		MoveState ms=map.get(from).sentSingleCard(map.get(to));
		lastMove=templm;
		
		if(ms==MoveState.SUCCESS){
			
			
			snapshot.push(getSnapshot(from, to));
		
			pointCounter.addPoint(map.get(from), map.get(to), ms);
			
			if(needAnalyze){
				needAnalyze=false;
				lastMove=from+" "+to+" "+1;
				tipsGetter.analyzerGame(this);
				needAnalyze=true;
			}
		
		}
		return ms;
	}

	@Override
	public MoveState moveCards(Components from, Components to, int number) {
		if(from==to)
			return MoveState.ILLEGAL_MOVE;
		
		if(number==1){
			return moveSingleCard(from,to);
		}
		
		String templm=lastMove;
		lastMove=from+" "+to+" "+number;
		MoveState ms=map.get(from).sentCards(map.get(to),number);
		lastMove=templm;
		
		if(ms==MoveState.SUCCESS){	
			
			snapshot.push(getSnapshot(from, to));
		
			pointCounter.addPoint(map.get(from), map.get(to), ms);
			
			if(needAnalyze){
				needAnalyze=false;
				lastMove=from+" "+to+" "+number;
				tipsGetter.analyzerGame(this);
				needAnalyze=true;
			}
		}
		
		return ms;
	}

	@Override
	public void nextCard() {
		snapshot.push(new Operation_pair(Operation.next,null,null));
		dealer.nextCards();
		if(needAnalyze){
			needAnalyze=false;
			lastMove="next";
			tipsGetter.analyzerGame(this);
			needAnalyze=true;
		}
	}
	
	@Override
	public ArrayList<String> getTopCard(Components c) {
		return map.get(c).getTopCard();
	}

	@Override
	public ArrayList<String> getAllCard(Components c) {
		return map.get(c).getAllCard();
	}

	@Override
	public boolean undo() {
		if(snapshot.isEmpty())
			return false;
		
		Operation_pair op=snapshot.pop();
		
		if(op.o==Operation.next){
			dealer.undo();
		}else{
			map.get(op.from).undo();
			map.get(op.to).undo();
			pointCounter.undo();
		}
		
		return true;
	}

	@Override
	public boolean undoAll() {
		if(snapshot.isEmpty())
			return false;
		
		snapshot.clear();
		
		for(Component c:map.values()){
			c.undoAll();
		}
		
		pointCounter.undoAll();
		
		return true;
	}

	@Override
	public boolean undoable() {
		return !snapshot.isEmpty();
	}
	
	@Override
	public String lastMove() {
		return lastMove;
	}


}
