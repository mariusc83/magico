package org.mariusconstantin.magico;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import org.mariusconstantin.annotations.OnClickHandler;
import org.mariusconstantin.magicoapi.Magico;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Magico.registerForUIDelegate(this, findViewById(R.id.mainContainer));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Magico.unregisterForUIDelegate(this, findViewById(R.id.mainContainer));
    }

    @OnClickHandler(elementId = R.id.button1)
    public void onSubmit(View view) {
        Toast.makeText(this, R.string.submit_event_label, Toast.LENGTH_LONG).show();
    }

    @OnClickHandler(elementId = R.id.button2)
    public void onCancel(View view) {
        Toast.makeText(this, R.string.cancel_event_label, Toast.LENGTH_LONG).show();
    }
}
