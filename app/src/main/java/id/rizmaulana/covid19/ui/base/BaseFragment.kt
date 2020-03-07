package id.rizmaulana.covid19.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment

/**
 * rizmaulana 2020-02-24.
 */
abstract class BaseFragment : Fragment() {
    private fun getBaseActivity() = activity as BaseActivity

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeChange()
    }

    abstract fun observeChange()

    fun showProgress() {
        getBaseActivity().showProgress()
    }

    fun hideProgress() {
        getBaseActivity().hideProgress()
    }

    fun showSnackbarMessage(message: String?) {
        getBaseActivity().showSnackbarMessage(message)
    }

    fun showSnackbarError(message: String?) {
        getBaseActivity().showSnackbarError(message)
    }

    fun onUnexpectedError() {
        getBaseActivity().onUnexpectedError()
    }
}