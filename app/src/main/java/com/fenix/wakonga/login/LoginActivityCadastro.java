package com.fenix.wakonga.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.multidex.MultiDex;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fenix.wakonga.MainActivity;
import com.fenix.wakonga.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivityCadastro extends AppCompatActivity implements LoaderCallbacks<Cursor> {
    private String TAG = LoginActivityCadastro.class.getSimpleName();


    //autenticacao de usuario
    private FirebaseAuth mAuth;
    //faz analise do fluxo do programa
    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView, nomeuser, sobrenomeuser, verificar_palavra_passe;
    private View mProgressView;
    private View mLoginFormView;
    Button criaconta;
    SignInButton loginGoogle;
    private final static int RC_SIGN_IN=2;
    GoogleApiClient mGoogleApiClient;
    UserProfileChangeRequest profileUpdates;
    ImageView image_login;
    TextView texto_desc, ja_tem_uma_conta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_cadastro);
        loginGoogle=findViewById(R.id.sign_in_button_google);
        nomeuser=findViewById(R.id.nomeuser);
        sobrenomeuser=findViewById(R.id.sobrenomeuser);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        verificar_palavra_passe=findViewById(R.id.verificarpassword);


        ja_tem_uma_conta=findViewById(R.id.ja_tem_uma_conta);


        criaconta=findViewById(R.id.criar_conta);


         image_login=findViewById(R.id.img_login);
         texto_desc=findViewById(R.id.texto_desc);




        loginGoogle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (haveNetworkConnection()){
                    signIn();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Sem conexão a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Metodo google login
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.id_client))
                .requestEmail()
                .build();


        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivityCadastro.this, "Ligacao falhou", Toast.LENGTH_LONG).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()!=null){
                    startActivity(new Intent(LoginActivityCadastro.this, MainActivity.class));
                }
            }
        };
        // Set up the login form.
        populateAutoComplete();
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.password || id == EditorInfo.IME_NULL) {
                    attemptLogin(false);
                    return true;
                }
                return false;
            }
        });
        mAuth=FirebaseAuth.getInstance();
        mFirebaseAnalytics=FirebaseAnalytics.getInstance(this);


        criaconta.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (haveNetworkConnection()){
                    attemptLogin(true);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Sem conexão a internet", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ja_tem_uma_conta.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivityCadastro.this, LoginActivity.class));
                finish();


            }
        });
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin(boolean isNewUser) {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        nomeuser.setError(null);
        sobrenomeuser.setError(null);
        verificar_palavra_passe.setError(null);


        // Store values at the time of the login attempt.
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();
        final String verificasenha=verificar_palavra_passe.getText().toString();

        final String nomeusuario=nomeuser.getText().toString();
        final String sobrenomeusuario=sobrenomeuser.getText().toString();

        boolean cancel = false;
        View focusView = null;
        Pattern pat = Pattern.compile ("^[A-Za-z\\s]{1,}[A-Za-z\\p{L}][\\.]{0,1}[A-Za-z\\s]{0,}$");


        // Check for a valid email address.
        if (TextUtils.isEmpty(nomeusuario)) {
            nomeuser.setError(getString(R.string.error_field_required));
            focusView = nomeuser;
            cancel = true;
        }
        else if(!nomeuser.getText().toString().matches(pat.toString()))
        {
            focusView = nomeuser;
            nomeuser.setError("Nome invalido");
            cancel = true;
        }
        else
        if (TextUtils.isEmpty(sobrenomeusuario)) {
            sobrenomeuser.setError(getString(R.string.error_field_required));
            focusView = sobrenomeuser;
            cancel = true;
        }
        else if(!sobrenomeuser.getText().toString().matches(pat.toString()))
        {
            focusView = sobrenomeuser;
            sobrenomeuser.setError("Sobrenome invalido");
            cancel = true;
        }

        // Check for a valid email address.
        else
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        else
        if (TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        else
        if (TextUtils.isEmpty(verificasenha) && !isPasswordValid(verificasenha)) {
            verificar_palavra_passe.setError(getString(R.string.error_invalid_password));
            focusView = verificar_palavra_passe;
            cancel = true;
        }else if (!verificasenha.equals(password)){
            verificar_palavra_passe.setError("As senhas devem ser iguais");
            focusView = verificar_palavra_passe;
            //focusView = mPasswordView;
            cancel = true;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            if (isNewUser) {

                try {
                showProgress(true);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivityCadastro.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                try {
                                    FirebaseUser user = task.getResult().getUser();

                                    Bundle bundle = new Bundle();
                                    if (task.isSuccessful()) {
                                        bundle.putString("email", email);
                                        mFirebaseAnalytics.logEvent("RegistarSucesso", bundle);
                                        //  Toast.makeText(getApplicationContext(), "Benvindo(a): "+user.getEmail()+"\nJá pode usufruir da nossa aplicacao", Toast.LENGTH_LONG).show();
                                        //actualizar perfil do usuario
                                        profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(nomeuser.getText().toString() + " " + sobrenomeuser.getText().toString())
                                                //.setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                                .build();
                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            //Verificação de email
                                                            try {
                                                            mAuth.getCurrentUser().sendEmailVerification()
                                                                    .addOnCompleteListener(new OnCompleteListener() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task task) {
                                                                            // Re-enable button
                                                                            showProgress(false);

                                                                            if (task.isSuccessful()) {
                                                                                showProgress(false);
                                                                                Toast.makeText(getApplicationContext(), "Verifique a sua caixa de email, para activar sua conta! ", Toast.LENGTH_LONG).show();

                                                                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                                                                if (user.isEmailVerified()) {
                                                                                    showProgress(false);
                                                                                    //Toast.makeText(getApplicationContext(), "Benvindo(a) "+user.getDisplayName(), Toast.LENGTH_LONG).show();
                                                                                    startActivity(new Intent(LoginActivityCadastro.this, MainActivity.class));
                                                                                    finish();
                                                                                } else {
                                                                                    showProgress(false);
                                                                                    //Toast.makeText(getApplicationContext(), "A sua conta não foi activada! ", Toast.LENGTH_LONG).show();
                                                                                    FirebaseAuth.getInstance().signOut();
                                                                                    Intent intent = new Intent(LoginActivityCadastro.this, LoginActivity.class);
                                                                                    startActivity(intent);
                                                                                    //Toast.makeText(getApplicationContext(), "A sua conta foi criaca com sucesso "+user.getDisplayName(), Toast.LENGTH_LONG).show();

                                                                                }


                                                                            } else {
                                                                                showProgress(false);

                                                                                Log.e(TAG, "sendEmailVerification", task.getException());
                                                                                Toast.makeText(getApplicationContext(),
                                                                                        "Email de verificação não enviado",
                                                                                        Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    showProgress(false);

                                                                    Toast.makeText(getApplicationContext(),
                                                                            "Email de verificação não enviado",
                                                                            Toast.LENGTH_SHORT).show();

                                                                }
                                                            });//Log.d(TAG, "User profile updated.");
                                                        }catch (Exception e){
                                                                showProgress(false);
                                                                Toast.makeText(getApplicationContext(), "Email de verificação não enviado!", Toast.LENGTH_LONG).show();

                                                            }
                                                        } else {
                                                            showProgress(false);
                                                            Toast.makeText(getApplicationContext(), "Email de verificação não enviado!", Toast.LENGTH_LONG).show();
                                                        }

                                                    }
                                                });

                                    } else {
                                        bundle.putString("email", user.getEmail());
                                        bundle.putString("senha", password);
                                        mFirebaseAnalytics.logEvent("RegistarFalha", bundle);
                                        Toast.makeText(getApplicationContext(), "Houve um erro no seu cadastro, tente novamente mais tarde!", Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    Bundle bundle = new Bundle();
                                    Toast.makeText(getApplicationContext(), "Houve um erro no seu cadastro, tente novamente mais tarde! ", Toast.LENGTH_LONG).show();
                                    bundle.putString("Contaexistente", e.getMessage());
                                }
                            }
                        }).addOnFailureListener(LoginActivityCadastro.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Bundle bundle = new Bundle();
                        showProgress(false);
                        mFirebaseAnalytics.logEvent("RegistarFalha", bundle);
                        //Toast.makeText(getApplicationContext(), "Conta não criada, tente mais tarde!", Toast.LENGTH_LONG).show();
                    }
                }).addOnCanceledListener(LoginActivityCadastro.this, new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        showProgress(false);

                        Toast.makeText(getApplicationContext(), "Operação cancelada! ", Toast.LENGTH_LONG).show();

                    }
                });
            }catch (Exception e){
                    showProgress(false);
                    Toast.makeText(getApplicationContext(), "Conta não criada, tente mais tarde!", Toast.LENGTH_LONG).show();

                }
            }
             /* else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivityCadastro.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()){
                           showProgress(false);
                           startActivity(new Intent(LoginActivityCadastro.this, LoginActivity.class));
                           finish();
                       }else {
                           Toast.makeText(LoginActivityCadastro.this, "Email e/ou Senha incorreta", Toast.LENGTH_LONG).show();
                       }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivityCadastro.this, "Email e/ou Senha incorreta", Toast.LENGTH_LONG).show();
                    }
                });
              mAuthTask = new UserLoginTask(email, password);
                mAuthTask.execute((Void) null);
            }*/
        }
    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >=6;
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show){
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},
                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }
    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivityCadastro.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }
    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }
        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }
        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    //verifica se tem conexao a internet
    private boolean haveNetworkConnection(){
        boolean haveConnectionWifi=false;
        boolean haveConnectionMobile=false;
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo[] networkInfos=cm.getAllNetworkInfo();
        for (NetworkInfo ni:networkInfos){
            if (ni.getTypeName().equalsIgnoreCase("WIFI")){
                if (ni.isConnected())
                    haveConnectionWifi=true;}

            if (ni.getTypeName().equalsIgnoreCase("MOBILE")){
                if (ni.isConnected())
                    haveConnectionMobile=true;}
        }
        return haveConnectionWifi || haveConnectionMobile;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        showProgress(true);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                showProgress(true);
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
                //startActivity(new Intent(LoginActivity.this, MainActivity.class));
               // finish();
                //Toast.makeText(getApplicationContext(), "Benvindo(a) "+account.getDisplayName()+" Pode usufruir da aplicação!", Toast.LENGTH_LONG).show();
            } catch (ApiException e){
                showProgress(false);
               Toast.makeText(LoginActivityCadastro.this, "Não foi possivel iniciar sessão", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            showProgress(true);
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user!=null){
                                Toast.makeText(getApplicationContext(), "Benvindo(a): "+user.getDisplayName(), Toast.LENGTH_LONG).show();
                            }
                            startActivity(new Intent(LoginActivityCadastro.this, MainActivity.class));
                             finish();
                          //  updateUI(user);
                        } else {
                            showProgress(false);

                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivityCadastro.this, "Falha na Autenticação", Toast.LENGTH_SHORT).show();
                          //  updateUI(null);
                        }
                        // ...
                    }
                });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

}