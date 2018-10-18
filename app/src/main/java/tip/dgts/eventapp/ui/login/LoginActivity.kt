package tip.dgts.eventapp.ui.login

import android.app.ProgressDialog
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast

import com.hannesdorfmann.mosby.mvp.viewstate.MvpViewStateActivity
import com.hannesdorfmann.mosby.mvp.viewstate.ViewState

import io.realm.Realm
import tip.dgts.eventapp.R
import tip.dgts.eventapp.databinding.ActivityLoginBinding
import tip.dgts.eventapp.model.data.User
import tip.dgts.eventapp.ui.main.MainActivity
import tip.dgts.eventapp.ui.register.RegisterActivity

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : MvpViewStateActivity<LoginView, LoginPresenter>(), LoginView, TextWatcher {


    private var binding: ActivityLoginBinding? = null
    private var progressDialog: ProgressDialog? = null
    private var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        isRetainInstance = true
        realm = Realm.getDefaultInstance()

        val user = realm!!.where(User::class.java).findFirst()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding!!.view = mvpView
        binding!!.etEmail.addTextChangedListener(this)
        binding!!.etPassword.addTextChangedListener(this)


        progressDialog = ProgressDialog(this)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setMessage("Logging in...")


        if (user != null) {
            onLoginSuccess()
        }
    }


    /***
     * Start of LoginView
     */

    override fun onDestroy() {
        realm!!.close()
        super.onDestroy()
    }

    /***
     * Start of MvpViewStateActivity
     */

    override fun createPresenter(): LoginPresenter {
        return LoginPresenter()
    }

    override fun createViewState(): ViewState<LoginView> {
        isRetainInstance = true
        return LoginViewState()
    }

    /***
     * End of MvpViewStateActivity
     */

    override fun onNewViewStateInstance() {
        saveValues()
    }

    override fun onLoginButtonClicked() {
        presenter.login(
                binding!!.etEmail.text.toString(),
                binding!!.etPassword.text.toString()
        )
    }

    override fun onRegisterButtonClicked() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    override fun showAlert(message: String?) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
    }

    override fun setEditTextValue(username: String?, password: String?) {
        binding!!.etEmail.setText(username)
        binding!!.etPassword.setText(password)
    }

    override fun startLoading() {
        progressDialog!!.show()
    }

    override fun stopLoading() {
        progressDialog!!.dismiss()
    }

    override fun onLoginSuccess() {
        if(progressDialog!!.isShowing) progressDialog!!.dismiss()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }


    override fun onForgotPasswordButtonClicked() {
        // startActivity(new Intent(this, ForgotPasswordActivity.class));
    }

    /***
     * End of LoginView
     */

    /***
     * Start of TextWatcher
     */

    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

    }

    override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
        saveValues()
    }

    override fun afterTextChanged(editable: Editable) {

    }

    /***
     * End of TextWatcher
     */

    private fun saveValues() {
        val loginViewState:LoginViewState ?= getViewState() as LoginViewState
        loginViewState?.setUsername(binding!!.etEmail.text.toString())
        loginViewState?.setPassword(binding!!.etPassword.text.toString())
    }
}

