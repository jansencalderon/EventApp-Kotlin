package tip.dgts.eventapp.ui.profile.update

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import android.widget.Toast

import com.hannesdorfmann.mosby.mvp.MvpActivity

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

import tip.dgts.eventapp.R
import tip.dgts.eventapp.app.App
import tip.dgts.eventapp.databinding.ActivityUpdateProfileBinding

class UpdateProfileActivity : MvpActivity<UpdateProfileView, UpdateProfilePresenter>(), UpdateProfileView {
    internal lateinit var binding: ActivityUpdateProfileBinding
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_profile)
        binding.view = mvpView

        setSupportActionBar(binding.toolbar)
        if (supportActionBar != null)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.user = App.user
    }

    override fun createPresenter(): UpdateProfilePresenter {
        return UpdateProfilePresenter()
    }

    override fun onSubmit() {
        presenter.update(
                binding.etEmail.text.toString(),
                binding.etFName.text.toString(),
                binding.etLName.text.toString(),
                binding.etBday.text.toString(),
                binding.etContact.text.toString(),
                binding.etAddress.text.toString()
        )
    }

    override fun showAlert(message: String) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun startLoading() {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this@UpdateProfileActivity)
            progressDialog!!.setCancelable(false)
            progressDialog!!.setMessage("Please wait...")
        }
        progressDialog!!.show()
    }

    override fun stopLoading() {
        if (progressDialog != null) progressDialog!!.dismiss()
    }

    override fun onUpdateSuccess() {
        showAlert("Profile Updated Successfully")
        finish()
    }

    override fun onBirthdayClicked() {
        val newCalendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
            internal var dateFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)

            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                val newDate = Calendar.getInstance()
                newDate.set(year, monthOfYear, dayOfMonth)
                binding.etBday.setText(dateFormatter.format(newDate.time))
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
