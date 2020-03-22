package id.rizmaulana.covid19.ui.percountry.indonesia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import id.rizmaulana.covid19.databinding.ActivityCountryIndonesiaBinding
import id.rizmaulana.covid19.ui.base.BaseActivity
import kotlinx.android.synthetic.main.layout_toolbar.view.*

class CountryIndonesiaActivity : BaseActivity() {

    private lateinit var binding: ActivityCountryIndonesiaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCountryIndonesiaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBarWithBackButton(binding.root.toolbar)
        initView()

    }

    private fun initView() {

    }

    override fun observeChange() {
    }

    companion object {
        @JvmStatic
        fun startActivity(context: Context?) =
            context?.startActivity(Intent(context, CountryIndonesiaActivity::class.java))
    }
}
