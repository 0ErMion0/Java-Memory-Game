import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.Image.*;
//import java.io.*;
import java.util.*;
//btn1.setBackground(colors[index]
import java.awt.Component;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class GameM implements ActionListener, ChangeListener {
	JFrame frame = new JFrame("메모리 게임"); // 프레임
	
	//JPanel field = new JPanel(); // (삭제)게임 띄울 필드
	JPanel field; // (추가)게임 띄울 필드
	JPanel menu = new JPanel(); // 게임 시작 때 필요한 메뉴 버튼들 담을 패널
	JPanel menu2 = new JPanel();
	JPanel menu3 = new JPanel();
	JPanel mini = new JPanel(); // 솔직히 왜 있는지 모름. menu3 안에 있는 패널
	
	JPanel showLevel = new JPanel(); // (추가)설정한 레벨 보여줄 패널
	JPanel setLevel = new JPanel(); // (추가)레벨 설정 슬라이드바 넣을 패널
	JPanel redoPanel = new JPanel(); // (추가) 게임 진행 중 redo 버튼 넣을 패널

	JPanel start_screen = new JPanel();
	JPanel end_screen = new JPanel();
	JPanel instruct_screen = new JPanel();
	
	JButton btn[] = new JButton[20]; // 카드용 버튼
	JButton start = new JButton("Start"); // 게임 시작 버튼
    JButton over = new JButton("Exit"); // 게임 끝내기 버튼
    JButton easy = new JButton("Easy"); // 게임 모드: 쉬움
    JButton hard = new JButton("Hard"); // 게임 모드: 어려움
    JButton inst = new JButton("Instructions"); // 게임 설명
    JButton redo = new JButton("Play Again"); // 다시 시작
    JButton goBack = new JButton("Main Menu"); // 메인 메뉴로 돌아가기
    
    // 슬라이더 관련 (추가)
    static final int INIT_VALUE = 5; // 슬라이더의 초기값
    JSlider slider; // 슬라이더
    
    Random randomGenerator = new Random(); // 난수 생성
    private boolean purgatory = false; // 일치하지 않는 버튼 클릭 o/x
	JLabel winner; // 게임에서 승리한 경우 표시되는 라벨
	Boolean game_over = false; // 게임 종료 여부
	int level=0; // 게임 레벨(1~10)
	int currentScore=0; // 게임 점수
	private static int highestScore = 0; // 최고 점수
	
	String[] board; // 버튼에 표시되는 문자열 저장하는 배열
	String[] boardSave; //(추가) redo 때를 위한 board 요소 저장하는 배열
	int[] boardQ=new int[20]; // 버튼 오픈/클로즈 상태값 저장 배열
	Boolean shown = true; // 버튼 보여진 상태인지 여부
	int temp=30; // 버튼 클릭 시 임시로 값 저장 변수
	int temp2=30; 
	////String a[]=new String[10]; // 게임의 Easy, Hard 모드에 사용되는 문자열 배열
	String gameMode[] = new String[10];
	boolean eh=true;

	
	private JLabel label = new JLabel("Enter level from 1 to 10");
	private JTextField text = new JTextField(10);
	private JTextArea instructM = new JTextArea(
			"When the game begins, the screen will be filled\nwith pairs of buttons.\n Memorize their placement.\nOnce you press any button, they will all clear. \n Your goal is to click the matching buttons in a row.\nWhen you finish that, you win.\nEvery incorrect click gives you a point (those are bad).\n GOOD LUCK! \n"
	+ "for a single level: enter a level between 1 and 10,\nselect easy or hard, then press start."
			);
	//instructM.setEditable(false);
	//instructW.setEditable(false);
	//instructM.setLineWrap(true);
	//instructW.setWrapStyleWord(true);
	
	// (추가) 이미지 파일 등록용
	ImageIcon hideIcon = new ImageIcon("hideIcon.png"); // 이미지 아이콘
	ImageIcon[] boardImageIcons; // 보드에 표시할 이미지 아이콘
	ImageIcon[] imageIcons = new ImageIcon[10]; // 이미지 아이콘
	ImageIcon[] imageIconsEasy = new ImageIcon[10]; // 이미지 아이콘 어려움 모드

	
	public GameM(){
		// 프레임의 사이즈와 위치 설정
		frame.setSize(800,500); // (추가)사이즈와 위치 수정함
		frame.setLocation(350,150);
		
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		field = new JPanel(); //(추가)
		
		
		start_screen.setLayout(new BorderLayout());
		menu.setLayout(new BorderLayout());
		menu2.setLayout(new FlowLayout(FlowLayout.CENTER));
		menu3.setLayout(new FlowLayout(FlowLayout.CENTER));
		mini.setLayout(new FlowLayout(FlowLayout.CENTER));

		showLevel.setLayout(new FlowLayout(FlowLayout.CENTER));
		setLevel.setLayout(new FlowLayout(FlowLayout.CENTER));
		
		// 시작 화면에 메뉴, 메뉴3, 메뉴2 배치
		start_screen.add(menu, BorderLayout.NORTH);
		start_screen.add(menu3, BorderLayout.CENTER);
		start_screen.add(menu2, BorderLayout.SOUTH);
		// mini를 menu3에 배치
		menu3.add(mini, BorderLayout.CENTER);
		// 메뉴 패널에 레벨 관련 패널 추가
		menu.add(showLevel, BorderLayout.NORTH);
		menu.add(setLevel, BorderLayout.SOUTH);
		
		text.setText("5"); text.setEditable(false); text.setBackground(Color.white);
		// 슬라이더
		slider = new JSlider(1, 10, INIT_VALUE); // 슬라이더(하한값, 상한값, 초기값)
		//slider.setMajorTickSpacing(4); // 큰 눈금 간격
		slider.setMinorTickSpacing(1);	// 작은 눈금 간격
        slider.setPaintTicks(true);		// 눈금을 표시
        slider.setPaintLabels(true);	// 값을 레이블로 표시
        slider.addChangeListener(this);	// 이벤트 리스너 등록
		
        showLevel.add(label);
        showLevel.add(text);
        setLevel.add(slider);
		
//		// (삭제)굳이 배치를 이렇게 해야되나
//		mini.add(easy, BorderLayout.NORTH);
//		mini.add(hard, BorderLayout.NORTH);
//		mini.add(inst, BorderLayout.SOUTH);
		mini.add(easy); mini.add(hard); mini.add(inst); //(수정)
		
        
        // 각 버튼에 리스너 등록
		start.addActionListener(this);
		start.setEnabled(true); // 필요없는 듯. 원래 자바 버튼 비활성화여 두번 클릭 방지하도록 하는 애인데 true라서. -> 아님. 다음 게임 할 때 메뉴 버튼 클릭이 안됨 없으면.
		menu2.add(start);
		over.addActionListener(this);
		over.setEnabled(true);
		menu2.add(over);
		easy.addActionListener(this);
		easy.setEnabled(true);
		hard.addActionListener(this);
		hard.setEnabled(true);
		inst.addActionListener(this);
		inst.setEnabled(true);
		
		start_screen.setBackground(Color.RED); // (나중에 지우기)
		
		// (추가) 이미지 등록용 - 이미지 크기 조절도 함
		for(int i = 0; i<10; i++) { // 쉬움 모드
			Image image = new ImageIcon((i + 1) + ".png").getImage(); // 이미지 로드
		    Image scaledImage = image.getScaledInstance(80, 80, java.awt.Image.SCALE_SMOOTH); // 이미지 크기 조정
		    imageIconsEasy[i] = new ImageIcon(scaledImage); // 크기가 조정된 이미지를 ImageIcon으로 변환
		}
		
		frame.add(start_screen, BorderLayout.CENTER);
		frame.setVisible(true);
	}	
	
	// 게임 설정하고 필드 구성
	public void setUpGame(int x,Boolean what){
		level=x; // 레벨 초기화
		clearMain(); // clearMain() 메소드를 호출하여 이전에 표시된 화면 지움
		
		board = new String[2*x]; // 레벨x2의 크기만큼 문자열 배열 생성
		boardSave = new String[2*x]; //(추가) 레벨x2의 크기만큼 board 저장용 문자열 배열 생성
		boardImageIcons = new ImageIcon[2*x]; // (추가)레벨x2의 크기만큼 보드에 표시할 이미지 아이콘 배열 생성
		for(int i=0;i<(x*2);i++){ // 레벨x2의 크기만큼 반복
			btn[i] = new JButton(""); // 버튼 배열의 각 요소에 ""을 넣어줌
			//btn[i].setBackground(new Color(220, 220, 220)); // (삭제)배경색을 회색으로 설정
			btn[i].setBackground(Color.white); // (추가)배경색을 흰색으로 설정
			btn[i].addActionListener(this); // 이벤트 등록
			btn[i].setEnabled(true); // 버튼을 활성화 상태로 설정
			field.add(btn[i]); // 필드에 버튼 추가
		}
		
		String[] gameModeHard = {":-D","X","O","-(*.*)-","<>","<(^-^)>","=>",";^P","ABC","123"};//harder version
		String[] gameModeEasy = {"   Python","   Coltline","   C","   C++","   C#","   Java","   Linux","   JavaScript","   SQL","   Php"};//easier version
		if(what) {
			gameMode=gameModeEasy;//if what is true, make the game easy and use c
			//for(int i = 0; i<10; i++) {
				imageIcons = imageIconsEasy; //(추가)
			//}
		}
		else {
			gameMode=gameModeHard;//otherwise make it hard and use b
			//imageIcons = imageIconsHard; //(추가)
		}
		
		// 주어진 레벨에 따라 게임 필드에 버튼을 랜덤으로 배치하는 과정(패 섞기 개념)
		for(int i=0;i<x;i++){ // 레벨 크기만큼
				for(int z=0;z<2;z++){ // 0, 1 (모드 의미)
					while(true){	
						int y = randomGenerator.nextInt(x*2); // 배열 인덱스 숫자 중 하나 랜덤으로(0~레벨x2-1)
						if(board[y]==null){ // 해당 위치에 이미 버튼이 배치되지 않았는지 확인
							btn[y].setText(gameMode[i]); // 버튼의 텍스트를 해당 모드용 텍스트로 넣어줌(배열 순서대로)(ex. 레벨1이고 쉬움 모드면 square로만 구성됨)
							btn[y].setIcon(imageIcons[i]); // (추가)
							board[y]=gameMode[i]; // 해당 게임 모드를 저장하여 해당 위치에 버튼이 배치되었음을 표시
							boardSave[y]=gameMode[i]; //(추가)
							boardImageIcons[y] = imageIcons[i]; //(추가)
							break;
						}
					}
				}
		}
		
		redo.addActionListener(this); //(추가)
		createBoard();
	}
//	public void setUpGame2() { //(추가)
//		for(int i=0;i<(level*2);i++){ // 레벨x2의 크기만큼 반복
//			//btn[i] = new JButton(""); // 버튼 배열의 각 요소에 ""을 넣어줌
//			btn[i].setText(boardSave[i]);
//			btn[i].setIcon(imageIcons[i]);
//			//btn[i].setBackground(new Color(220, 220, 220)); // (삭제)배경색을 회색으로 설정
//			btn[i].setBackground(Color.white); // (추가)배경색을 흰색으로 설정
//			btn[i].addActionListener(this); // 이벤트 등록
//			btn[i].setEnabled(true); // 버튼을 활성화 상태로 설정
//			field.add(btn[i]); // 필드에 버튼 추가
//		}
//		redo.addActionListener(this);
//		createBoard();
//	}
	public void hideField(int x){//this sets all the boxes blank
		for(int i=0;i<(x*2);i++){
			/*if(boardQ[i]==0)*/ btn[i].setText("");
			btn[i].setIcon(hideIcon); //(추가) 버튼이 클로우즈 상태일 때 보여줄 기본 이미지
		}
		shown=false;
	}
	public void switchSpot(int i){//this will switch the current spot to either blank or what it should have
		if(board[i]!="done"){//when a match is correctly chosen, it will no longer switch when pressed
			if(btn[i].getText()==""){
				btn[i].setText(board[i]);
				btn[i].setIcon(boardImageIcons[i]); //(추가)
				//shown=true; // 전체가 다 shown 상태가 아니라서 바꿔주지 않음
			} else {
				btn[i].setText("");
				btn[i].setIcon(hideIcon); //(추가)
				//shown=false;
			}
		}
	}
	public void showSpot(int i){
		btn[i].setText(board[i]);
		btn[i].setIcon(boardImageIcons[i]); //(추가)
	}
	public void showField(int x, String a[]){//this shows all the symbols on the field
		for(int i=0;i<(x*2);i++){
			btn[i].setText(a[i]);
			btn[i].setIcon(boardImageIcons[i]); //(추가)
		}
		shown=true;
	}
	void waitABit(){//this was an attempt at fixing the glitch i told you about
		
		try{
			Thread.sleep(5);
		} catch(Exception e){
			
		}
	}
	public boolean checkWin(){//checks if every spot is labeled as done
		for(int i=0;i<(level*2);i++){
			if (board[i]!="done") {
				return false;
			}
		}
		winner();
		return true;
	}
	public void winner(){
			// 현재 점수를 최고점수와 비교하여 갱신
        	updateHighestScore();
        	
			start_screen.remove(field);
        	start_screen.remove(redoPanel); //(추가)
			start_screen.add(end_screen, BorderLayout.CENTER);
			
			// 7, 14. 최고 점수 구현 및 디자인 수정(추가)
			end_screen.setLayout(new BorderLayout()); // end_screen 패널을 BorderLayout()으로 변경
			JPanel winPanel = new JPanel(); // "You Win" 라벨 담기 위한 패널
			winPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			JPanel scorePanel = new JPanel(); // 현재 점수와 최고 점수를 담기 위한 패널
			scorePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			JPanel menuPanel = new JPanel(); // 메인 메뉴 버튼 담기 위한 패널
			menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
			
			end_screen.add(winPanel, BorderLayout.NORTH);
			end_screen.add(scorePanel, BorderLayout.CENTER);
			end_screen.add(menuPanel, BorderLayout.SOUTH);
			
			JLabel winPrint = new JLabel("You Win");
			winPrint.setFont(new Font("Serif", Font.BOLD, 18)); // 라벨의 글씨체 변경
			winPrint.setForeground(Color.blue); // 라벨의 글씨 색상 변경
			JTextField scorePrint = new JTextField(8);
			scorePrint.setText("Score: " + currentScore);
			scorePrint.setEditable(false); // 텍스트 수정 불가하도록
			scorePrint.setBackground(Color.white);
			scorePrint.setFont(new Font("맑은 고딕", Font.BOLD, 12));
			JTextField bestScorePrint = new JTextField(8);
			bestScorePrint.setText("Best Score: "+getHighestScore());
			bestScorePrint.setEditable(false); // 텍스트 수정 불가하도록
			bestScorePrint.setBackground(Color.white);
			bestScorePrint.setFont(new Font("맑은 고딕", Font.BOLD, 12));
			
			winPanel.add(winPrint);
			scorePanel.add(scorePrint);
			scorePanel.add(bestScorePrint);
			menuPanel.add(goBack);
			
			goBack.setEnabled(true);
			goBack.addActionListener(this);		
	}
	public void goToMainScreen(){
		new GameM();
	}
	public void createBoard(){//(삭제)this is just gui stuff to show the board
		//field.setLayout(new BorderLayout()); // (삭제)필요없어보임 아래에서 레이아웃 바꿔서
		redoPanel.add(redo); //(추가) 패널에 재시작 버튼 추가
		start_screen.add(redoPanel, BorderLayout.SOUTH); //(추가) 패널을 start_screen 아래에 추가
		start_screen.add(field, BorderLayout.CENTER);
		
		field.setLayout(new GridLayout(5,4,2,2));
		field.setBackground(Color.black);
		field.requestFocus();
		
	}
	public void clearMain(){//clears the main menu so i can add the board or instructions
		start_screen.remove(menu);
		start_screen.remove(menu2);
		start_screen.remove(menu3);

       start_screen.revalidate();
       start_screen.repaint();
	}
//	public void clearGame() {//(추가) 게임 화면 지움
////		start_screen.remove(field);
////		start_screen.remove(redoPanel);
//		
//		start_screen.revalidate();
//		start_screen.removeAll(); // 잘 지워짐
//		start_screen.repaint();
//		
////		field.revalidate();
////		field.removeAll();
//		
//		currentScore =0;
//		shown = true;
//		eh = true;
//		purgatory = false;
//		shown = false;
//		game_over = false;
//		
////		for(int i = 0; i < level*2; i++) {
////			// 짝 맞추어진 패는 board의 텍스트가 done이 되기 때문에 오잉 이건 상관없나
////			board[i] = boardSave[i]; // "done"으로 표시되었던거 없애줌
////			
////			//btn[i].setText(board[i]);
////		}
//		temp =30;
//		temp2 = 30;		
//		//start_screen.revalidate();
//		//start_screen.repaint();
//	}
    // 최고점수 갱신
    private void updateHighestScore() {
        if (currentScore > highestScore) {
            highestScore = currentScore;
        }
    }
    // 최고점수 반환
    public static int getHighestScore() {
        return highestScore;
    }
    // 버튼 클릭 및 이벤트 처리	
    public void actionPerformed(ActionEvent click){
		Object source = click.getSource(); // 이벤트가 발생한 소스(버튼) 가져옴
		// 이전에 클릭된 버튼과 현재 클릭된 버튼이 매치되지 않았을 때
		if(purgatory){
			// temp2와 temp에 저장된 버튼의 상태 변경 => 매치되지 않은 버튼이 다시 보이거나 숨겨짐
			switchSpot(temp2);
			switchSpot(temp);
			
			currentScore++; // 매치되지 않은 클릭이 발생할 때마다 점수가 증가
			
			// 변수 초기화(temp: 게임 상태 완료값 갖도록 설정, temp2: 임시 값 30 할당)
			temp=(level*2);
			temp2=30;
			
			// false로 설정하여 다시 switchSpot()메소드가 실행되지 않도록 함
			// -> 잘못된 클릭이 발생했을 때 임시로 표시된 버튼 상태 초기화됨(이 if문 다시 작동 안되도록)
			purgatory=false;
		}
		
		// "Start"버튼이 클릭되었을 때
		if(source==start){ //start sets level and difficulty and calls method to set up game
			// 
			try{
			level = Integer.parseInt(text.getText()); // text에서 입력된 레벨값으로 level 초기화
			} catch (Exception e){ // text에 입력된 텍스트가 정수로 변환될 수 없다면 level을 1로 초기화
				level=1;
			}
			
			// setUpGame() 호출하여 게임 시작함, 매개변수로 레벨(1/2)과 난이도(t/f) 전달
			setUpGame(level, eh);//level between 1 and 2, eh is true or false
		}
		if(source==over){//quits
			System.exit(0);
		}
		if(source==inst){//this just sets the instruction screen
			clearMain();
	        
			start_screen.add(instruct_screen, BorderLayout.NORTH);
			
			JPanel one = new JPanel();
			one.setLayout(new FlowLayout(FlowLayout.CENTER));
			JPanel two = new JPanel();
			two.setLayout(new FlowLayout(FlowLayout.CENTER));
			instruct_screen.setLayout(new BorderLayout());
			instruct_screen.add(one, BorderLayout.NORTH);
			instruct_screen.add(two, BorderLayout.SOUTH);
			
			one.add(instructM);
			two.add(goBack);
			goBack.addActionListener(this);
			goBack.setEnabled(true);
		}
		if(source==redo) { // 패 다시 로드(추가)
			// 지금 화면 지우고
			// 버튼 화면 재로드
			//clearGame();
		
			//System.out.println("redo 눌림"); // 이건 잘 눌리는데 뭐지
			//setUpGame2();
			
			// clearGame 내용, 화면 지워져야 해
//			start_screen.revalidate();
//			start_screen.removeAll(); // 잘 지워짐.. 왜 안되냐 이젠
			
			// 문자, 아이콘들 다 뒤집기
			for(int i =0; i<level*2; i++) {
				btn[i].setText(boardSave[i]);
				btn[i].setIcon(boardImageIcons[i]);
				board[i]=boardSave[i]; // 기존의 상태가 남아있지 않도록 기존 값 저장해놓은 배열로 덮어씌움(처음부터 다시 할 수 있도록)
				// 이거 안하면 한번 뒤집기 성공한 카드를 redo하여 뒤집어진 카드 뒤집어서 패 확인하려고 하면 안 뒤집어짐 -> "done" 상태라서
			}
			
			currentScore =0;
			shown = true;
			eh = true;
			purgatory = false;
			shown = true;
			game_over = false;
			temp =30;
			temp2 = 30;
			
			//start_screen.remove(field);
		    //start_screen.revalidate();
		    //start_screen.repaint();
			//createBoard(); // 문제점.. 예전 그대로의 상태대로 나타난다..
		}
		if(source==goBack){//back to main screen
		    frame.dispose();  
		    goToMainScreen();
		}
		if(source==easy){//sets the type. ex. if easy is clicked it turns blue and hard remains black
			eh=true;
			easy.setForeground(Color.BLUE);
			hard.setForeground(Color.BLACK);
		} else if(source==hard){
			eh=false;
			hard.setForeground(Color.BLUE);
			easy.setForeground(Color.BLACK);
		}
		
		for(int i =0;i<(level*2);i++){//gameplay when a button is pressed
			if(source==btn[i]){ // 눌린 버튼 찾음
				if(shown){ // open되어있다면
					hideField(level);//if first time, hides field
				}else{//otherwise play
					switchSpot(i);
					if(temp>=(level*2)){ // 처음 플레이할 때 처음으로 패 뒤집으면
						temp=i; // temp = 눌린 버튼의 인덱스 번호
					} else {
						if((board[temp]!=board[i])||(temp==i)){
							temp2=i;
							purgatory=true;
							
							
						} else {
							board[i]="done";
							board[temp]="done";
							checkWin();
							temp=(level*2);
						}
						
					}
				}			
			}	
		}
	}

	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		int value = (int) source.getValue(); // 입력된 값 받음
		text.setText(""+value); // 입력된 값을 text 텍스트 라벨에 표시
	}

	public static void main(String[] args) {
		new GameM();// Calling the class construtor.
	}
	
}
