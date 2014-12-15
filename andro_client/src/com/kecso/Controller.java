package com.kecso;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

import org.apache.commons.lang3.SerializationUtils;

import com.kecso.computer.CalibratedGyroscopeProvider;
import com.kecso.computer.OrientationProvider;
import com.kecso.computer.util.Quaternion;
import com.kecso.socket.UdpMessage;
import com.kecso.socket.UdpResponse;
import com.kecso.R;

public class Controller extends Activity {

	public int PORT = 8888;
	private Button connectPhones;
	private String serverIpAddress = "192.168.0.102";
	private boolean connected = false;
	private EditText port;
	private EditText ipAdr;
	private SeekBar throttle;
	private TextView speed;
	private TextView alt;
	private TextView rpm;
	private TextView vertSpeed;
	private Switch yawSwitch;

	private SensorManager sensorManager;
	boolean acc_disp = false;
	boolean isStreaming = false;
	private OrientationProvider orientationProvider;

	boolean isStartingValueInitialized = false;
	protected PowerManager.WakeLock mWakeLock;

	float headingAngle;
	float pitchAngle;
	float rollAngle;
	private Button reset;
	private Boolean yawChecked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		connectPhones = (Button) findViewById(R.id.send);
		reset = (Button) findViewById(R.id.reset);
		reset.setOnClickListener(calibrateListener);
		connectPhones.setOnClickListener(connectListener);
		port = (EditText) findViewById(R.id.port);
		ipAdr = (EditText) findViewById(R.id.ipadr);
		throttle = (SeekBar) findViewById(R.id.throttle);
		speed = (TextView) findViewById(R.id.speedText);
		vertSpeed = (TextView) findViewById(R.id.textSpeedVert);
		rpm = (TextView) findViewById(R.id.textRpm);
		alt = (TextView) findViewById(R.id.textAlt);
		yawSwitch = (Switch) findViewById(R.id.yawSwitch);
		yawSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				yawChecked = isChecked;
				Toast.makeText(getApplicationContext(), "Sendig yaw data is "+(isChecked?"enabled":"disabled")+"!",
						Toast.LENGTH_SHORT).show();

			}
		});

		throttle.setMax(100);
		sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		orientationProvider = new CalibratedGyroscopeProvider(sensorManager);
		port.setText("8888");
		ipAdr.setText(serverIpAddress);
		acc_disp = false;
		final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		this.mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
				"My Tag");
		this.mWakeLock.acquire();
	}

	private Button.OnClickListener connectListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			if (!connected) {
				if (!serverIpAddress.equals("")) {
					connectPhones.setText("Stop Streaming");
					ipAdr.setEnabled(false);
					port.setEnabled(false);
					orientationProvider.reinitialize();
					Thread cThread = new Thread(new SocketClient());
					cThread.start();
				}
			} else {
				connectPhones.setText("Start Streaming");
				connected = false;
				acc_disp = false;
				ipAdr.setEnabled(true);
				port.setEnabled(true);
			}
		}
	};

	private Button.OnClickListener calibrateListener = new Button.OnClickListener() {
		@Override
		public void onClick(View v) {
			orientationProvider.reinitialize();

			Toast.makeText(getApplicationContext(), "Reinitialised",
					Toast.LENGTH_SHORT).show();
		}
	};

	public class SocketClient implements Runnable {

		@Override
		public void run() {
			DatagramSocket sock = null;
			try {
				sock = new DatagramSocket();

				acc_disp = true;
				PORT = Integer.parseInt(port.getText().toString());
				serverIpAddress = ipAdr.getText().toString();
				InetAddress host = InetAddress.getByName(serverIpAddress);
				connected = true;
				sock.setSoTimeout(10);

				while (connected) {

					Quaternion quat = orientationProvider.getQuaternion();
					UdpMessage message = new UdpMessage(quat.getX(),
							quat.getY(), quat.getZ(), quat.getW(),
							(float) throttle.getProgress(),yawChecked==null?yawSwitch.isChecked():yawChecked);
					byte[] b = SerializationUtils.serialize(message);
					DatagramPacket dp = new DatagramPacket(b, b.length, host,
							PORT);
					sock.send(dp);

					byte[] buffer = new byte[65536];
					DatagramPacket reply = new DatagramPacket(buffer,
							buffer.length);
					try {
						sock.receive(reply);

						byte[] data = reply.getData();
						UdpResponse response = SerializationUtils
								.deserialize(data);
						if (response != null) {
							setText(speed,
									String.format("Speed: %3.1f",
											response.getSpeed()));
							setText(alt,
									String.format("Alt: %3.1f",
											response.getAltitude()));
							setText(vertSpeed, String.format(
									"Vertical speed: %3.1f",
									response.getVerticalSpeed()));
							setText(rpm,
									String.format("RPM: %3.1f",
											response.getRpm()));
						}

					} catch (SocketTimeoutException e) {
						Log.w("Timeout reached!!! ", e);
					}
				}
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "Connection Error",
						Toast.LENGTH_SHORT).show();
			} finally {
				try {
					sock.close();
				} catch (Exception e) {
					Log.d("UDP Error: ", e.getMessage(), e);
					Toast.makeText(getApplicationContext(), "Connection Error",
							Toast.LENGTH_SHORT).show();
				}
				acc_disp = false;
				connected = false;
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		orientationProvider.start();
	}

	@Override
	protected void onStop() {
		orientationProvider.stop();
		super.onStop();
	}

	private void setText(final TextView text, final String value) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				text.setText(value);
			}
		});
	}
}
