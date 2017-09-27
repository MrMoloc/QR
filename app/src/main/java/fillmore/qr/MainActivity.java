package fillmore.qr;

import android.app.Activity;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import static fillmore.qr.R.id.parent;

public class MainActivity extends Activity implements OnItemSelectedListener {

    JSONObject json = new JSONObject();
    TextView textView;
    Spinner spinner;
    Button button;
    Button button2;
    String qrstring = null;
    EditText editTextdf;
    String dfstring = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.app, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setOnItemSelectedListener(this);
        spinner.setAdapter(adapter);
        button = (Button) findViewById(R.id.button);
        button2 = (Button) findViewById(R.id.button2);
        editTextdf = (EditText) findViewById(R.id.editTextdf);

        try{
            qrstring = savedInstanceState.getString("qrstring");
            textView.setText(qrstring);
        }catch (Exception e){
            textView.setText(e.toString());
            qrstring = null;
        }

        if(textView.getText() != null) {
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
        button.setVisibility(View.GONE);
        button2.setVisibility(View.GONE);
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(qrstring != null) {
            button.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else{
            textView.setText("Resumed aber qrstring = null");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("qrstring", qrstring);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onCreate(savedInstanceState);
        onResume();
    }

    private static final int SCAN_QR_CODE_REQUEST_CODE = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.add("Log");
        menuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, SCAN_QR_CODE_REQUEST_CODE);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SCAN_QR_CODE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                qrstring = intent.getStringExtra("SCAN_RESULT");
                textView.setText(qrstring);
            }
        }
    }

    private void log(JSONObject json) {
        Intent intent = new Intent("ch.appquest.intent.LOG");

        if (getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isEmpty()) {
            Toast.makeText(this, "Logbook App not Installed", Toast.LENGTH_LONG).show();
            return;
        }

        // Achtung, je nach App wird etwas anderes eingetragen

        intent.putExtra("ch.appquest.logmessage", json.toString());

        startActivity(intent);
    }

    public void onClickbutton(View view){
        dfstring = editTextdf.getText().toString();
        try {
            switch (spinner.getSelectedItem().toString()) {
                case "Metalldetektor":
                    json.put("task", "Metalldetektor");
                    json.put("solution", qrstring);
                    break;
                case "Dechiffrierer":
                    json.put("task", "Dechiffrierer");
                    json.put("solution", dfstring);
                    break;
                case "Memory":
                    json.put("task", "Memory");
                    json.put("solution", "");
                    break;
                case "Schatzkarte":
                    json.put("task", "Schatzkarte");
                    json.put("points", "");
                    break;
                case "Pixelmaler":
                    json.put("task", "Pixelmaler");
                    json.put("pixels", "");
                    break;
            }
        }catch (JSONException e){
            e.printStackTrace();
        }

        log(json);
    }

    public void onClickbutton2(View vie){
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, SCAN_QR_CODE_REQUEST_CODE);
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (spinner.getSelectedItem().toString()) {
            case "Metalldetektor":
                button2.setVisibility(View.VISIBLE);
                editTextdf.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
                break;
            case "Dechiffrierer":
                button2.setVisibility(View.GONE);
                editTextdf.setVisibility(View.VISIBLE);
                button.setVisibility(View.VISIBLE);
                break;
            case "Memory":
                button2.setVisibility(View.GONE);
                editTextdf.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
                break;
            case "Schatzkarte":
                button2.setVisibility(View.GONE);
                editTextdf.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
                break;
            case "Pixelmaler":
                button2.setVisibility(View.GONE);
                editTextdf.setVisibility(View.GONE);
                button.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
