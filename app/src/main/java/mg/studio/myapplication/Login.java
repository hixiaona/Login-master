package mg.studio.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


public class Login extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private ProgressDialog progressDialog;
    private SessionManager session;
    private Feedback feedback;
    private Button loginButton;
    private final String mFileName="test.txt";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        /**
         * If the user just registered an account from Register.class,
         * the parcelable should be retrieved
         */
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            // Retrieve the parcelable
            Feedback feedback = bundle.getParcelable("feedback");
            // Get the from the object
            String userName = feedback.getName();
            TextView display = findViewById(R.id.display);
            display.setVisibility(View.VISIBLE);
            String prompt = userName.substring(0, 1).toUpperCase() + userName.substring(1) + " " + getString(R.string.account_created);
            display.setText(prompt);

        }

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        loginButton = findViewById(R.id.btnLogin);


        /**
         * Prepare the dialog to display when the login button is pressed
         */
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);


        /**
         * Use the SessionManager class to check whether
         * the user already logged in, is yest  then go to the MainActivity
         */
        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }


    }

    /**
     *  Process the user input and log in if credentials are correct
     *  Disable the button while login is processing
     *  @param view from activity_login.xml
     */
    public void btnLogin(View view) {


        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();


        // Check for empty data in the form
        if (!email.isEmpty() && !password.isEmpty()) {

            // Avoid multiple clicks on the button
            loginButton.setClickable(false);

             String allinfo=read();
             int kUser=allinfo.indexOf(email);
             if(kUser>=0){
                 String mpassword=allinfo.substring(kUser+email.length(),kUser+email.length()+password.length());
                 String outchar=allinfo.substring(kUser+email.length()+password.length());
                 if(outchar.equals("\t")){
                     if(mpassword.equals(password)){
                         setContentView(R.layout.activity_main);
                     }else
                     {
                         Toast.makeText(getApplicationContext(),
                                 "Email or password incorrect!", Toast.LENGTH_LONG)
                                 .show();
                     }
                 }else {
                     Toast.makeText(getApplicationContext(),
                             "Email or password incorrect!", Toast.LENGTH_LONG)
                             .show();
                 }
             }else {
                 Toast.makeText(getApplicationContext(),
                         "Email not exist !", Toast.LENGTH_LONG)
                         .show();
             }


            //Todo : ensure the user has Internet connection

            // Display the progress Dialog
            progressDialog.setMessage("Logging in ...");
           // if (!progressDialog.isShowing())
             //   progressDialog.show();

            //Todo: need to check weather the user has Internet before attempting checking the data
            // Start fetching the data from the Internet
            //new OnlineCredentialValidation().execute(email,password);


        } else {
            // Prompt user to enter credentials
            Toast.makeText(getApplicationContext(),
                    R.string.enter_credentials, Toast.LENGTH_LONG)
                    .show();
        }
    }
    private String  read(){
        FileInputStream fileInputStream = null;
        try {
            fileInputStream=openFileInput(mFileName);
            byte[]buff=new byte[1024];
            StringBuilder sb=new StringBuilder("");
            int len=0;
            while ((len=fileInputStream.read(buff))>0){
                sb.append(new String(buff,0,len));
            }
            return sb.toString();
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * Press the button register, go to Registration form
     *
     * @param view from the activity_login.xml
     */
    public void btnRegister(View view) {
        startActivity(new Intent(getApplicationContext(), Register.class));
        finish();
    }





}