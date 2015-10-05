package mc.hipotecaproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Hipoteca extends AppCompatActivity {

    private EditText preuInmoble;
    private EditText estalvis;
    private EditText plaz;
    private EditText euribor;
    private EditText diferencial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hipoteca);

        preuInmoble = (EditText)findViewById(R.id.preuInmoble);
        estalvis = (EditText)findViewById(R.id.estalvis);
        plaz = (EditText)findViewById(R.id.plaz);
        euribor = (EditText)findViewById(R.id.euribor);
        diferencial = (EditText)findViewById(R.id.diferencial);

        preuInmoble.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calcula();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hipoteca, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        calcula();
    }

    private void calcula() {
        CalculaValors calculaValors = new CalculaValors().invoke();
        double total = calculaValors.getTotal();
        double totalMes = calculaValors.getTotalMes();

        actualitzaTextViews(total, totalMes);
    }

    private void actualitzaTextViews(double total, double totalMes) {
        TextView textTotal = (TextView)findViewById(R.id.calcTotal);
        TextView textMes = (TextView)findViewById(R.id.calcMes);

        //Toast.makeText(this, "resultado " + total, Toast.LENGTH_SHORT).show();

        String totalHipoteca = "Cuota Anual:\n" + String.valueOf(total);
        textTotal.setText(totalHipoteca);
        String totalHipoMes = "Cuota Mes:\n" + String.valueOf(totalMes);
        textMes.setText(totalHipoMes);
    }

    private class CalculaValors {
        private double totalMes;
        private double total;

        public double getTotalMes() {
            return totalMes;
        }

        public double getTotal() {
            return total;
        }

        public CalculaValors invoke() {
            double dbPreuInmoble = Double.valueOf(preuInmoble.getText().toString());
            double dbEstalvis = Double.valueOf(estalvis.getText().toString());
            double dbPlaz = Double.valueOf(plaz.getText().toString());
            double dbEuribor = Double.valueOf(euribor.getText().toString());
            double dbDiferencial = Double.valueOf(diferencial.getText().toString());

            //-----------------------------------------------------------------------------------------------

            double capital = dbPreuInmoble-dbEstalvis;
            double dbIntereses = dbEuribor + dbDiferencial;
            double potencia = 1+(dbIntereses/12)/100;
            double meses = dbPlaz*12;
            double meses2 = meses*(-1);

            totalMes = (capital * (dbIntereses / 12)) / (100 * (1 - Math.pow(potencia, meses2)));
            //double cuotaMensual = (dbPreuInmoble-dbEstalvis)*(dbEuribor+dbDiferencial) / (100*(1- (1/Math.pow((1+((dbEuribor+dbDiferencial/100))), (dbPlaz/12) ) )));
            total = totalMes * 12 * dbPlaz;
            return this;
        }
    }
}