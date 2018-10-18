package tip.dgts.eventapp.ui.register

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.DialogInterface
import android.content.Intent
import android.databinding.DataBindingUtil
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.DatePicker
import android.widget.Toast

import com.hannesdorfmann.mosby.mvp.MvpActivity

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

import tip.dgts.eventapp.R
import tip.dgts.eventapp.databinding.ActivityRegisterBinding

class RegisterActivity : MvpActivity<RegisterView, RegisterPresenter>(), RegisterView {

    private var binding: ActivityRegisterBinding? = null
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        binding!!.view = mvpView

        setSupportActionBar(binding!!.toolbar)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun createPresenter(): RegisterPresenter {
        return RegisterPresenter()
    }

    override fun onSubmit() {
        presenter.register(
                binding!!.etEmail.text.toString(),
                binding!!.etPassword.text.toString(),
                binding!!.etConfirmPass.text.toString(),
                binding!!.etFName.text.toString(),
                binding!!.etLName.text.toString(),
                binding!!.etBday.text.toString(),
                binding!!.etContact.text.toString(),
                binding!!.etAddress.text.toString()
        )
    }

    override fun showAlert(message: String) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun setEditTextValue(email: String, password: String, confirmPassword: String, firstName: String, lastName: String, birthday: String, contact: String, address: String) {

    }


    override fun startLoading() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this@RegisterActivity)
            progressDialog!!.setCancelable(false)
            progressDialog!!.setMessage("Signing up...")
        }
        progressDialog!!.show()
    }

    override fun stopLoading() {
        if (progressDialog != null) progressDialog!!.dismiss()
    }

    override fun onRegistrationSuccess() {
        AlertDialog.Builder(this)
                .setTitle("Register Successful")
                .setMessage("Go Back to Login Activity")
                .setCancelable(false)
                .setPositiveButton("Close") { dialogInterface, i -> this@RegisterActivity.finish() }
                .show()
    }

    override fun onBirthdayClicked() {
        val newCalendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
            internal var dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)

            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                val newDate = Calendar.getInstance()
                newDate.set(year, monthOfYear, dayOfMonth)
                binding!!.etBday.setText(dateFormatter.format(newDate.time))
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }
    }
}
