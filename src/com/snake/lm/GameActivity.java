package com.snake.lm;


import java.util.List;
import java.util.Timer;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

public class GameActivity extends Activity implements SensorEventListener {

	GameSurface surf;
	Timer t;
	int width, height;
	boolean pause;

	StepUpdater task = new StepUpdater(this);
	
	SensorManager mSensorManager;
	Sensor mAccelerometerSensor;


	float SSX = 0, SSY = 0;
	float SX = 0, SY = 0;
	boolean firstTime;

	// Ќу тут конструктор - ничего особо интересного
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		surf = new GameSurface(this);
		this.setContentView(surf);
		t = new Timer();
		height = this.getWindowManager().getDefaultDisplay().getHeight();
		width = this.getWindowManager().getDefaultDisplay().getWidth();

		// »нициализируем акселерометр
		mSensorManager = (SensorManager) getSystemService(Activity.SENSOR_SERVICE);
		List<Sensor> sensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
		if (sensors.size() > 0) {
			for (Sensor sensor : sensors) {
				if (sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
					if (mAccelerometerSensor == null)
						mAccelerometerSensor = sensor;
				}
			}
		}
	}
	
	
	// «апуск активити
	@Override
	public void onStart() {
		super.onStart();
		pause=true;
		
		// «апускаем таймер обновлени€ картинки на экране
		t.scheduleAtFixedRate(new GraphUpdater(surf), 0, 100);
		// «апускаем таймер обновлени€ положени€ змейки
		t.scheduleAtFixedRate(task, 0, 500);//changed
		// регистрируем нашу форму как объект слушающий 
		// изменени€ датчика - акселерометра
		mSensorManager.registerListener(this, mAccelerometerSensor,
				SensorManager.SENSOR_DELAY_GAME);
		this.firstTime = true;
		// заставл€ем подсветку гореть
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	
	public boolean onTouchEvent(MotionEvent event) {// Pause
		//SnakeLMActivity snake = new SnakeLMActivity(); // Changed
		//GameSurface surf = new GameSurface(snake);
		StepUpdater new_task = new StepUpdater(this);
	    switch (event.getAction()) {
	   	    case MotionEvent.ACTION_UP: // отпускание
	    	if (pause==true){
				pause=false;
				
				new_task.act = task.act;
				task.cancel();
				t.cancel();
				t.purge();
				return true;
			}
			else {
				t = new Timer();
				t.scheduleAtFixedRate(new GraphUpdater(surf), 0, 100);
				t.scheduleAtFixedRate(new_task, 0, 500);
				task.act = new_task.act;
				task.run();
				/*
				
				new_task.act.surf.mField.mScore = 32;
				pause=true;
				surf = new GameSurface(this);
				this.setContentView(surf);
				t = new Timer();
				height = this.getWindowManager().getDefaultDisplay().getHeight();
				width = this.getWindowManager().getDefaultDisplay().getWidth();
				t.scheduleAtFixedRate(new GraphUpdater(surf), 0, 100);
				// «апускаем таймер обновлени€ положени€ змейки
				t.scheduleAtFixedRate(new_task, 0, 500);
				
				mSensorManager.registerListener(this, mAccelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
				this.firstTime = true;
				// заставл€ем подсветку гореть
				getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
				*/
				Toast.makeText(getApplicationContext(), "Resume", Toast.LENGTH_LONG).show();
				Log.i("else wo", "else work");	
			}
	    	break;
	    }
	    return true;
	}


	// ќбрабатываем остановку активити
	@Override
	public void onStop() {
		super.onStop();
		// ќстанавливаем таймеры
		t.cancel();
		t.purge();
		// ќтписываемс€ от получени€ сообщений об изменении
		// от датчика
		mSensorManager.unregisterListener(this);
	}

//	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do nothing! 
	}

	// метод, который определ€ет по показани€м акселерометра
	// передаваемым ему как параметры (х и у)
	// в каком направлении должна двигатьс€ зме€
	private int getDirection(float x, float y) {
		if (Math.abs(x) > Math.abs(y)) {
			if (x > 0) {
				return SnakeGame.DIR_WEST;
			} else {
				return SnakeGame.DIR_EAST;
			}
		} else {
			if (y > 0) {
				return SnakeGame.DIR_SOUTH;
			} else {
				return SnakeGame.DIR_NORTH;
			}
		}
	}

	
	// ј вот так мы обрабатываем изменение ориентации
	// телефона в пространстве
//	@Override
	public void onSensorChanged(SensorEvent event) {
		surf.setSomeText("Your score is: "+SnakeLMActivity.GAME_SCORE);
		
		// получаем показани€ датчика
		SX = event.values[0];
		SY = event.values[1];
		
		// ≈сли игра уже идет, то
		if (!this.firstTime) {
			// получаем положение телефона в пространстве 
			// с поправкой на его начальное положение
			float dirX = SX - SSX;
			float dirY = SY - SSY;
			// ”станавливаем дл€ змеи новое направление
			surf.mField.setDirection(this.getDirection(dirX, dirY));
			// передаем в нашу повержность координаты телефона в пространстве
			surf.setXY(dirX, dirY);
		} else {
			// ≈сли игра только началась делаем поправку на начальное
			// положение телефона
			this.firstTime = false;
			SSX = SX;
			SSY = SY;
		}
	}

	// Ётот метод вызываетс€ из потока одного из таймеров
	// именно в этом методе происходит движение змейки в 
	// зависимости от ее направлени€ установленного в предидущем 
	// методе
	public void Step() {
		// ≈сли ход не удалс€ то закрываем текущую активити
		if (!surf.mField.nextMove()) {
			SnakeLMActivity.GAME_MODE=1;
			this.finish();
		}
		// ≈сли все впор€дке то обновл€ем очки
		// в стартовой активити
		else{
			SnakeLMActivity.GAME_SCORE=this.surf.mField.mScore;
		}
	}
}
