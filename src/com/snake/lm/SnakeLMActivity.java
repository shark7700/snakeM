package com.snake.lm;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class SnakeLMActivity extends Activity implements OnClickListener {

	Button butt; 
	TextView tv;
	
	// ����� ������� �������� - 0 ������ ������
	// 1 - ������ �������� ����� ���������
	public static int GAME_MODE=0;
	
	public static int GAME_SCORE=0;
	

	public void PauseGame()
	{
		SnakeGame sg = new SnakeGame();
		sg.mScore = 12;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onStart(){
		super.onStart();
		// ��� ��� �������� ������ �
		// ��������� ������ ��������
		if (GAME_MODE==0){
		setContentView(R.layout.main);
		butt = (Button) this.findViewById(R.id.button1);
		butt.setOnClickListener(this);
		}
		else
		{
			setContentView(R.layout.lose);
			butt = (Button) this.findViewById(R.id.button2);
			tv = (TextView) this.findViewById(R.id.textView2);
			tv.setText("Your score: "+GAME_SCORE);
			butt.setOnClickListener(this);
		}
	}

	public void onClick(View v) {
		// ��� ����� �������� ���� �� ������� �� ������, �� ���� �����������
		Intent i = new Intent(this, com.snake.lm.GameActivity.class);
		GAME_MODE=0;
		GAME_SCORE=0;
		this.startActivity(i);
	}

}