package core_refactoring.impl.easy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import core_refactoring.*;

public class SimpleSingleStepCardGameAnalyzerImpl implements CardGameAnalyzer{

	private static Map<Integer,Components> map=new HashMap<>();
	
	private static Map<Integer,Components> boxmap=new HashMap<>();
	
	static{
		map.put(1, Components.CARD_HEAP_1);
		map.put(2, Components.CARD_HEAP_2);
		map.put(3, Components.CARD_HEAP_3);
		map.put(4, Components.CARD_HEAP_4);
		map.put(5, Components.CARD_HEAP_5);
		map.put(6, Components.CARD_HEAP_6);
		map.put(7, Components.CARD_HEAP_7);
		
		boxmap.put(1, Components.BOX_1);
		boxmap.put(2, Components.BOX_2);
		boxmap.put(3, Components.BOX_3);
		boxmap.put(4, Components.BOX_4);
	}
	
	private ArrayList<String> tips;
	private boolean finishedGame;
	
	private boolean fastMode;

	public SimpleSingleStepCardGameAnalyzerImpl(boolean fastMode) {
		super();
		this.fastMode = fastMode;
	}
	
	public boolean isFastMode() {
		return fastMode;
	}

	public void setFastMode(boolean fastMode) {
		this.fastMode = fastMode;
	}

	@Override
	public void analyzerGame(CardManagement cardGame){
		tips=new ArrayList<>();
		
		if(cardGame.getAllCard(Components.DEALER).size()==0){
			boolean flag=true;
			for(int i=1;i<7;i++){
				if(cardGame.getAllCard(map.get(i)).contains(null)){
					flag=false;
					break;
				}
			}
			if(flag){
				finishedGame=true;
				return;
			}
		}
		
	//	System.out.println("#1");
		
		for(int i=1;i<=7;i++){
			for(int j=1;j<=4;j++){
				if(cardGame.moveCards(map.get(i), boxmap.get(j), 1)==MoveState.SUCCESS){
					tips.add(map.get(i)+" "+boxmap.get(j)+" "+1);
					
				//	System.out.println("Analyzer:"+map.get(i)+" "+boxmap.get(j)+" "+1);
					
					cardGame.undo();
					if(fastMode){
						return;
					}
				}
			}
		}
		
	//	System.out.println("#2");
		
		String[] sp=cardGame.lastMove().split(" ");
		
		boolean move=sp.length!=1;
		
		Components from = null;
		Components to = null;
		int numx = 0;
		
		if(move){
			from=Components.valueOf(sp[0]);
			to=Components.valueOf(sp[1]);
			numx=Integer.parseInt(sp[2]);
		}
		
		for(int num=1;num<13;num++){
			for(int i=1;i<=7;i++){
				for(int j=1;j<=7;j++){
					if(i==j)
						continue;
					if(move)
						if(map.get(i)==to&&map.get(j)==from&&num==numx)
							continue;
					if(cardGame.moveCards(map.get(i), map.get(j), num)==MoveState.SUCCESS){
						tips.add(map.get(i)+" "+map.get(j)+" "+num);
						
					//	System.out.println("Analyzer:"+map.get(i)+" "+map.get(j)+" "+num);
						
						cardGame.undo();
						if(fastMode){
							return;
						}
					}
				}
			}
		}
		
		for(int j=1;j<=7;j++){
			if(cardGame.moveCards(Components.DEALER, map.get(j), 1)==MoveState.SUCCESS){
				tips.add(Components.DEALER+" "+map.get(j)+" "+1+" "+0);
				
			//	System.out.println("Analyzer:"+Components.DEALER+" "+map.get(j)+" "+1);
				
				cardGame.undo();
			}
		}
		for(int j=1;j<=4;j++){
			if(cardGame.moveCards(Components.DEALER, boxmap.get(j), 1)==MoveState.SUCCESS){
				tips.add(Components.DEALER+" "+boxmap.get(j)+" "+1+" "+0);
				
			//	System.out.println("Analyzer:"+Components.DEALER+" "+boxmap.get(j)+" "+1);
				
				cardGame.undo();
			}
		}
		
	//	System.out.println("#3");
		
		
	}

	@Override
	public ArrayList<String> getTips() {
		return tips;
	}

	@Override
	public String getBestTips() {
		if(!tips.isEmpty())
			return tips.get(0);
		throw new NoSuchElementException("No tips available");
	}

	@Override
	public boolean isGameFinish() {
		return finishedGame;
	}

	@Override
	public boolean isGameOver() {
		return !finishedGame&&tips.isEmpty();
	}
	
}
