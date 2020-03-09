package id.rizmaulana.covid19.ui.base

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import id.rizmaulana.covid19.R
import id.rizmaulana.covid19.util.Constant
import id.rizmaulana.covid19.widget.TopSnackbar
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

/**
 * rizmaulana 2020-02-24.
 */
abstract class BaseActivity : AppCompatActivity() {

    private val progressDialog by lazy {
        ProgressDialog(this)
    }

    private val permissionsList = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarWhite()
        observeChange()
    }

    fun loading(loaded: Boolean) {
        if (loaded) showProgress() else hideProgress()
    }

    fun showProgress() {
        with(progressDialog) {
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.layout_progress_dialog)
            isIndeterminate = true
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            show()
        }
    }

    fun hideProgress() {
        progressDialog?.dismiss()
    }

    fun showSnackbarMessage(message: String?) {
            message?.let {
                val container = try {
                    findViewById<CoordinatorLayout>(R.id.layout_content)
                } catch (e: Exception){
                    window.decorView.rootView
                }
                showTopSnackbar(container, it, R.color.prince_ton_orange)
            }
    }

    fun showSnackbarError(message: String?) {
        message?.let {
            val container = findViewById<CoordinatorLayout>(R.id.layout_content)
            if (container != null) {
                showTopSnackbar(container, it, R.color.red)
            } else {
                showTopSnackbar(window.decorView.rootView, it, R.color.red)
            }
        }
    }

    fun onUnexpectedError() {
        hideProgress()
        showSnackbarError(getString(R.string.msg_unexpected_error))
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun showTopSnackbar(root: View, message: String, @ColorRes color: Int) {
        val topSnackbar = TopSnackbar.make(root, message, TopSnackbar.LENGTH_LONG)
        val snackbarView = topSnackbar.getView()
        snackbarView.setBackgroundColor(ContextCompat.getColor(this, color))
        val textView = snackbarView.findViewById(R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        topSnackbar.show()
    }

    fun hideSoftKeyboard() {
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        val view = this.currentFocus
        if (view != null) {
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    open fun changeStatusBarWhite() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        }
    }

    protected open fun permission(invoke: () -> Unit) {
        runtimePermission(permissionsList, invoke)
    }

    private fun runtimePermission(permissions: List<String>, action: () -> Unit) {
        Dexter.withActivity(this).withPermissions(permissions)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    report?.let {
                        if (it.deniedPermissionResponses.size > 0) {
                            runtimePermission(
                                it.deniedPermissionResponses.map { p -> p.permissionName },
                                action
                            )
                        } else {
                            action.invoke()
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }
            })
            .withErrorListener { showSnackbarError(Constant.ERROR_MESSAGE) }
            .check()
    }

    abstract fun observeChange()

}